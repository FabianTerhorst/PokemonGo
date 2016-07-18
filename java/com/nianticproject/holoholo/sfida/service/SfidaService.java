package com.nianticproject.holoholo.sfida.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.SfidaNotification;
import com.nianticproject.holoholo.sfida.SfidaUtils;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants.CertificationState;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants.ConnectionState;
import com.nianticproject.holoholo.sfida.service.SfidaButtonDetector.OnClickListener;
import com.nianticproject.holoholo.sfida.service.SfidaWatchDog.OnTimeoutListener;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import spacemadness.com.lunarconsole.R;

@TargetApi(18)
public class SfidaService extends Service {
    private static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static final String EXTRA_DATA_CHARACTERISTIC = "com.nianticproject.holoholo.sfida..EXTRA_DATA_TYPE";
    public static final String EXTRA_DATA_RAW = "com.nianticproject.holoholo.sfida.EXTRA_DATA_RAW";
    private static final String TAG = SfidaService.class.getSimpleName();
    private static final boolean USE_AUTO_CONNECT = false;
    private final IBinder binder = new LocalBinder();
    private BluetoothAdapter bluetoothAdapter;
    private String bluetoothDeviceAddress;
    private BluetoothGatt bluetoothGatt;
    private BluetoothManager bluetoothManager;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
       public void onReceive(final Context context, final Intent intent) {
            int n = 0;
            Log.d(SfidaService.TAG, "onReceive() : " + intent.getAction());
            final String action = intent.getAction();
            if (action == null) {
                Log.d(SfidaService.TAG, "onReceived() action was null");
            } else {
                switch (action) {
                    case "android.bluetooth.device.action.BOND_STATE_CHANGED":
                        SfidaService.this.onBondStateChanged(intent);
                        break;
                    case "android.bluetooth.device.action.ACL_DISCONNECTED":
                        if (SfidaService.this.connectionState == SfidaConstants.ConnectionState.RE_BOND) {
                            SfidaService.this.setConnectionState(SfidaConstants.ConnectionState.NO_CONNECTION);
                            SfidaUtils.createBond(SfidaService.this.getDevice(SfidaService.this.bluetoothDeviceAddress));
                            SfidaService.this.sendBroadcast("com.nianticproject.holoholo.sfida.ACTION_CREATE_BOND");
                        }
                        break;
                    case "com.nianticproject.holoholo.sfida.dismiss":
                        SfidaNotification.dissmiss(SfidaService.this.getApplicationContext());
                        SfidaService.this.disconnectBluetooth();
                        break;
                    case "com.nianticproject.holoholo.sfida.vibrate":
                        SfidaService.this.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getBlinkRed());
                        break;
                    case "android.bluetooth.device.action.PAIRING_REQUEST":
                        final BluetoothDevice bluetoothDevice = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                        try {
                            bluetoothDevice.getClass().getMethod("setPairingConfirmation", Boolean.TYPE).invoke(bluetoothDevice, true);
                            bluetoothDevice.getClass().getMethod("cancelPairingUserInput").invoke(bluetoothDevice);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        } catch (InvocationTargetException ex2) {
                            ex2.printStackTrace();
                        } catch (NoSuchMethodException ex3) {
                            ex3.printStackTrace();
                        }
                        break;
                    default:
                        Log.d(SfidaService.TAG, "onReceive() : " + action);
                }
            }
        }
    };
    private Certificator certificator = new Certificator(this);
    private volatile ConnectionState connectionState = ConnectionState.NO_CONNECTION;
    private volatile boolean isReceivedNotifyCallback = false;
    private volatile boolean isReceivedWriteCallback = false;
    private SfidaButtonDetector sfidaButtonDetector = new SfidaButtonDetector();

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$ConnectionState = new int[ConnectionState.values().length];

        static {
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$ConnectionState[ConnectionState.DISCOVERING_SERVICE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$ConnectionState[ConnectionState.RE_BOND.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public class LocalBinder extends Binder {
        public SfidaService getService() {
            return SfidaService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        intentFilter.addAction(SfidaNotification.ACTION_NOTIFICATION_DISMISS);
        intentFilter.addAction(SfidaNotification.ACTION_NOTIFICATION_VIBRATE);
        registerReceiver(this.broadcastReceiver, intentFilter);
        return this.binder;
    }

    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        closeBluetoothGatt();
        unregisterReceiver(this.broadcastReceiver);
        if (this.sfidaButtonDetector != null) {
            this.sfidaButtonDetector.release();
        }
        SfidaNotification.dissmiss(getApplicationContext());
    }

    public String getBluetoothAddress() {
        return this.bluetoothDeviceAddress;
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.bluetoothGatt == null) {
            return null;
        }
        return this.bluetoothGatt.getServices();
    }

    public boolean getIsReceivedNotifyCallback() {
        return this.isReceivedNotifyCallback;
    }

    public void setIsReceivedNotifyCallback(boolean isReceivedNotifyCallback) {
        this.isReceivedNotifyCallback = isReceivedNotifyCallback;
    }

    public boolean getIsReceivedWriteCallback() {
        return this.isReceivedWriteCallback;
    }

    public void setIsReceivedWriteCallback(boolean isReceivedWriteCallback) {
        this.isReceivedWriteCallback = isReceivedWriteCallback;
    }

    public void setOnClickSfidaListener(@Nullable OnClickListener listener) {
        if (this.sfidaButtonDetector != null) {
            this.sfidaButtonDetector.setOnclickListener(listener);
        }
    }

    public void setConnectionState(ConnectionState connectionState) {
        Log.d(TAG, "ConnectionState [" + this.connectionState + "] \u2192 [" + connectionState + "]");
        this.connectionState = connectionState;
    }

    public BluetoothDevice getDevice(String address) {
        BluetoothDevice device = this.bluetoothAdapter.getRemoteDevice(address);
        if (device != null) {
            return device;
        }
        Log.w(TAG, "[BLE] Device not found.  Unable to connect.");
        return null;
    }

    public boolean initialize() {
        if (this.bluetoothManager == null) {
            this.bluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        this.bluetoothAdapter = this.bluetoothManager.getAdapter();
        if (this.bluetoothAdapter != null) {
            return true;
        }
        Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        return false;
    }

    public boolean connect(BluetoothDevice device) {
        if (isBoundDevice(device).booleanValue()) {
            setConnectionState(ConnectionState.CONNECT_GATT);
            String address = device.getAddress();
            if (this.bluetoothAdapter == null || address == null) {
                Log.w(TAG, "[BLE] BluetoothAdapter not initialized or unspecified address.");
                return false;
            } else if (this.bluetoothDeviceAddress == null || !address.equals(this.bluetoothDeviceAddress) || this.bluetoothGatt == null) {
                this.bluetoothGatt = device.connectGatt(this, false, new SfidaGattCallback(this));
                Log.d(TAG, "Trying to create a new connection.");
                this.bluetoothDeviceAddress = address;
            } else {
                Log.d(TAG, "[BLE] Trying to use an existing bluetoothGatt for connection.");
                return this.bluetoothGatt.connect();
            }
        }
        setConnectionState(ConnectionState.BONDING);
        SfidaUtils.createBond(device);
        return true;
    }

    public boolean disconnectBluetooth() {
        if (this.bluetoothAdapter == null || this.bluetoothGatt == null) {
            Log.w(TAG, "[BLE] BluetoothAdapter not initialized");
            return false;
        }
        this.bluetoothGatt.disconnect();
        return true;
    }

    public void closeBluetoothGatt() {
        if (this.bluetoothGatt != null) {
            this.bluetoothGatt.close();
            this.bluetoothGatt = null;
        }
    }

    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.bluetoothAdapter == null || this.bluetoothGatt == null) {
            Log.w(TAG, "[BLE] BluetoothAdapter not initialized");
            return false;
        }
        this.bluetoothGatt.setCharacteristicNotification(characteristic, true);
        return this.bluetoothGatt.readCharacteristic(characteristic);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, OnTimeoutListener listener) {
        Log.d(TAG, "writeCharacteristic()");
        boolean succeed = this.bluetoothGatt.writeCharacteristic(characteristic);
        if (succeed) {
            this.isReceivedWriteCallback = false;
            if (listener != null) {
                SfidaWatchDog.getInstance().startWatch(characteristic.getUuid(), listener);
            }
        }
        return succeed;
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, OnTimeoutListener listener, int timeout) {
        Log.d(TAG, "writeCharacteristic()");
        boolean succeed = this.bluetoothGatt.writeCharacteristic(characteristic);
        if (succeed) {
            this.isReceivedWriteCallback = false;
            if (listener != null) {
                SfidaWatchDog.getInstance().startWatch(characteristic.getUuid(), listener, timeout);
            }
        }
        return succeed;
    }

    public BluetoothGattCharacteristic findCharacteristic(UUID serviceUuid, UUID characteristicUuid) {
        if (this.bluetoothGatt == null) {
            return null;
        }
        BluetoothGattService service = this.bluetoothGatt.getService(serviceUuid);
        if (service != null) {
            return service.getCharacteristic(characteristicUuid);
        }
        Log.e(TAG, "findCharacteristic() characteristic was not found.");
        return null;
    }

    public boolean sendToEnableSfidaNotification(BluetoothGattCharacteristic characteristic, boolean enabled, UUID notificationUUID, OnTimeoutListener listener) {
        if (this.bluetoothAdapter == null || this.bluetoothGatt == null) {
            Log.w(TAG, "[BLE] BluetoothAdapter not initialized");
            return false;
        }
        boolean succeed = this.bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (succeed && notificationUUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            Log.d(TAG, " permission : " + descriptor.getPermissions());
            succeed = this.bluetoothGatt.writeDescriptor(descriptor);
            if (succeed && listener != null) {
                this.isReceivedNotifyCallback = false;
                SfidaWatchDog.getInstance().startWatch(characteristic.getUuid(), listener);
                return succeed;
            } else if (succeed) {
                return succeed;
            } else {
                Log.e(TAG, "failed writeDescriptor()");
                return succeed;
            }
        } else if (succeed) {
            return succeed;
        } else {
            Log.e(TAG, "failed setCharacteristicNotification()");
            return succeed;
        }
    }

    public boolean sendToEnableSfidaNotification(BluetoothGattCharacteristic characteristic, boolean enabled, UUID notificationUuid) {
        return sendToEnableSfidaNotification(characteristic, enabled, notificationUuid, null);
    }

    public boolean sendDeviceControlMessage(UUID characteristic, byte[] value, OnTimeoutListener timeoutListener) {
        Log.d(TAG, "[BLE] sendDeviceControlMessage() \n  value : " + SfidaUtils.byteArrayToString(value) + "\n" + "  UUID  : " + characteristic);
        BluetoothGattCharacteristic bleGattChar = findCharacteristic(SfidaMessage.UUID_DEVICE_CONTROL_SERVICE, characteristic);
        if (bleGattChar != null) {
            bleGattChar.setValue(value);
            boolean succeed = writeCharacteristic(bleGattChar, timeoutListener);
            Log.d(TAG, "sendDeviceControlMessage() result : " + succeed);
            return succeed;
        }
        Log.e(TAG, "sendDeviceControlMessage() BluetoothGattCharacteristic not found.");
        return false;
    }

    public boolean sendDeviceControlMessage(UUID characteristic, byte[] value, OnTimeoutListener timeoutListener, int timeout) {
        Log.d(TAG, "[BLE] sendDeviceControlMessage() \n  value : " + SfidaUtils.byteArrayToString(value) + "\n" + "  UUID  : " + characteristic + " Timeout : " + timeout);
        BluetoothGattCharacteristic bleGattChar = findCharacteristic(SfidaMessage.UUID_DEVICE_CONTROL_SERVICE, characteristic);
        if (bleGattChar != null) {
            bleGattChar.setValue(value);
            boolean succeed = writeCharacteristic(bleGattChar, timeoutListener, timeout);
            Log.d(TAG, "sendDeviceControlMessage() result : " + succeed);
            return succeed;
        }
        Log.e(TAG, "sendDeviceControlMessage() BluetoothGattCharacteristic not found.");
        return false;
    }

    public boolean sendDeviceControlMessage(UUID characteristic, byte[] value) {
        return sendDeviceControlMessage(characteristic, value, null);
    }

    public boolean sendCertificateMessage(byte[] value, OnTimeoutListener timeoutListener) {
        Log.d(TAG, "sendCertificateMessage()");
        if (value == null) {
            throw new NullPointerException();
        }
        Log.d(TAG, "sendCertificateMessage() \n  value : " + SfidaUtils.byteArrayToString(value) + "\n" + "  UUID  : " + SfidaMessage.UUID_CENTRAL_TO_SFIDA_CHAR);
        BluetoothGattCharacteristic bleGattChar = findCharacteristic(SfidaMessage.UUID_CERTIFICATE_SERVICE, SfidaMessage.UUID_CENTRAL_TO_SFIDA_CHAR);
        if (bleGattChar != null) {
            bleGattChar.setValue(value);
            boolean succeed = writeCharacteristic(bleGattChar, timeoutListener);
            Log.d(TAG, "sendCertificateMessage() result : " + succeed);
            return succeed;
        }
        Log.e(TAG, "sendCertificateMessage() BluetoothGattCharacteristic not found.");
        return false;
    }

    public boolean sendCertificateMessage(byte[] value) {
        return sendCertificateMessage(value, null);
    }

    public boolean readCertificateMessage(UUID characteristic) {
        Log.d(TAG, "readCertificateMessage()");
        if (characteristic == null) {
            throw new NullPointerException();
        }
        BluetoothGattCharacteristic bleGattChar = findCharacteristic(SfidaMessage.UUID_CERTIFICATE_SERVICE, characteristic);
        if (bleGattChar != null) {
            boolean result = readCharacteristic(bleGattChar);
            Log.d(TAG, "readCertificateMessage() : " + result);
            return result;
        }
        Log.e(TAG, "readCertificateMessage() BluetoothGattCharacteristic not found.");
        return false;
    }

    public boolean enableDeviceControlServiceNotify() {
        Log.d(TAG, "enableDeviceControlServiceNotify()");
        BluetoothGattCharacteristic bleGattChar = findCharacteristic(SfidaMessage.UUID_DEVICE_CONTROL_SERVICE, SfidaMessage.UUID_BUTTON_NOTIF_CHAR);
        if (bleGattChar != null) {
            boolean succeed = sendToEnableSfidaNotification(bleGattChar, true, SfidaMessage.UUID_BUTTON_NOTIF_CHAR);
            Log.d(TAG, "enableDeviceControlServiceNotify() result : " + succeed);
            return succeed;
        }
        Log.e(TAG, "enableDeviceControlServiceNotify() BluetoothGattCharacteristic not found.");
        return false;
    }

    public boolean enableSecurityServiceNotify(OnTimeoutListener timeoutListener) {
        Log.d(TAG, "enableSecurityServiceNotify()");
        BluetoothGattCharacteristic bleGattChar = findCharacteristic(SfidaMessage.UUID_CERTIFICATE_SERVICE, SfidaMessage.UUID_SFIDA_COMMANDS_CHAR);
        if (bleGattChar != null) {
            boolean succeed = sendToEnableSfidaNotification(bleGattChar, true, SfidaMessage.UUID_SFIDA_COMMANDS_CHAR, timeoutListener);
            Log.d(TAG, "enableSecurityServiceNotify() result : " + succeed);
            return succeed;
        }
        Log.e(TAG, "enableSecurityServiceNotify() BluetoothGattCharacteristic not found.");
        return false;
    }

    public boolean enableSecurityServiceNotify() {
        return enableSecurityServiceNotify(null);
    }

    public void readFwVersionMessage() {
        Log.d(TAG, "readFwVersionMessage()");
        BluetoothGattCharacteristic bleGattChar = findCharacteristic(SfidaMessage.UUID_DEVICE_CONTROL_SERVICE, SfidaMessage.UUID_FW_VERSION_CHAR);
        if (bleGattChar != null) {
            readCharacteristic(bleGattChar);
        } else {
            Log.e(TAG, "readFwVersionMessage() BluetoothGattCharacteristic not found.");
        }
    }

    public void readBatteryLevel() {
        Log.d(TAG, "readBatteryLevel()");
        BluetoothGattCharacteristic bleGattChar = findCharacteristic(SfidaMessage.UUID_BATTERY_SERVICE, SfidaMessage.UUID_BATTERY_LEVEL_CHAR);
        if (bleGattChar != null) {
            readCharacteristic(bleGattChar);
        } else {
            Log.e(TAG, "readBatteryLevel() BluetoothGattCharacteristic not found.");
        }
    }

    public void onSfidaUpdated(BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(SfidaConstants.ACTION_DATA_AVAILABLE);
        byte[] receivedValue = characteristic.getValue();
        UUID receivedUuid = characteristic.getUuid();
        String receivedValueString = SfidaUtils.byteArrayToString(receivedValue);
        String bits = SfidaUtils.byteArrayToBitString(receivedValue);
        Log.d(TAG, "[BLE] onSfidaUpdated()");
        Log.d(TAG, "  RawData : " + receivedValueString);
        Log.d(TAG, "  Bit : " + bits);
        Log.d(TAG, "  UUID    : " + receivedUuid.toString());
        try {
            Log.d(TAG, "  type    : " + characteristic.getIntValue(18, 0).intValue());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        SfidaWatchDog.getInstance().stopWatch();
        if (SfidaMessage.UUID_SFIDA_COMMANDS_CHAR.equals(receivedUuid)) {
            if (this.certificator == null) {
                this.certificator = new Certificator(this);
            }
            this.certificator.onSfidaUpdated(receivedValueString);
        } else if (SfidaMessage.UUID_FW_VERSION_CHAR.equals(receivedUuid)) {
            if (receivedValue != null && receivedValue.length > 0) {
                try {
                    intent.putExtra(EXTRA_DATA_RAW, new String(receivedValue, "US-ASCII"));
                    intent.putExtra(EXTRA_DATA_CHARACTERISTIC, receivedUuid);
                    sendBroadcast(intent);
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
            }
        } else if (SfidaMessage.UUID_BATTERY_LEVEL_CHAR.equals(receivedUuid)) {
            if (receivedValue != null && receivedValue.length > 0) {
                Log.d(TAG, "BatteryLevel received. " + characteristic.getValue()[0] + "%");
            }
        } else if (!SfidaMessage.UUID_BUTTON_NOTIF_CHAR.equals(receivedUuid)) {
            sendBroadcast(intent);
        } else if (receivedValue != null) {
            this.sfidaButtonDetector.setButtonStatus(receivedValue, false);
            intent.putExtra(EXTRA_DATA_RAW, receivedValue);
            intent.putExtra(EXTRA_DATA_CHARACTERISTIC, receivedUuid);
            sendBroadcast(intent);
        }
    }

    public void onConnectedWithGattServer(BluetoothGatt gatt) {
        gatt.discoverServices();
        setConnectionState(ConnectionState.DISCOVERING_SERVICE);
        sendBroadcast(SfidaConstants.ACTION_GATT_CONNECTED);
    }

    public void onDisconnectedWithGattServer() {
        if (this.connectionState != ConnectionState.RE_BOND) {
            closeBluetoothGatt();
            sendBroadcast(SfidaConstants.ACTION_GATT_DISCONNECTED);
            setConnectionState(ConnectionState.NO_CONNECTION);
            SfidaNotification.dissmiss(getApplicationContext());
        }
    }

    public void onServiceDiscovered() {
        switch (AnonymousClass2.$SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$ConnectionState[this.connectionState.ordinal()]) {
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                setConnectionState(ConnectionState.CERTIFICATION);
                startCertificateSequence();
                sendBroadcast(SfidaConstants.ACTION_GATT_SERVICES_DISCOVERED);
                return;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                setConnectionState(ConnectionState.NO_CONNECTION);
                disconnectBluetooth();
                return;
            default:
                return;
        }
    }

    public void onCertificationComplete() {
        setConnectionState(ConnectionState.CONNECTED);
        enableDeviceControlServiceNotify();
        sendBroadcast(SfidaConstants.ACTION_CERTIFICATE_COMPLETE);
        SfidaNotification.showSfidaNotification(getApplicationContext());
    }

    private Boolean isBoundDevice(BluetoothDevice device) {
        Set<BluetoothDevice> bondedDevices = this.bluetoothAdapter.getBondedDevices();
        if (!(bondedDevices == null || bondedDevices.size() == 0)) {
            for (BluetoothDevice bondedDevice : bondedDevices) {
                if (bondedDevice.getAddress().equals(device.getAddress())) {
                    return Boolean.valueOf(true);
                }
            }
        }
        return Boolean.valueOf(false);
    }

    private void onBondStateChanged(Intent intent) {
        int newState = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
        int oldState = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", Integer.MIN_VALUE);
        Log.d(TAG, "[BLE] ACTION_BOND_STATE_CHANGED oldState : " + SfidaUtils.getBondStateName(oldState) + " \u2192 newState : " + SfidaUtils.getBondStateName(newState));
        BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        if (device != null) {
            switch (newState) {
                case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                    if (oldState == 12) {
                        setConnectionState(ConnectionState.RE_BOND);
                        this.certificator.setCertificationState(CertificationState.NO_CERTIFICATION);
                        disconnectBluetooth();
                        return;
                    } else if (oldState == 11) {
                        setConnectionState(ConnectionState.NO_CONNECTION);
                        sendBroadcast(SfidaConstants.ACTION_BOND_CANCELED);
                        return;
                    } else {
                        Log.d(TAG, "Unhandled oldState : " + oldState);
                        return;
                    }
                case 12:
                    try {
                        device.getClass().getMethod("cancelPairingUserInput", new Class[0]).invoke(device, new Object[0]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e2) {
                        e2.printStackTrace();
                    } catch (NoSuchMethodException e3) {
                        e3.printStackTrace();
                    }
                    connect(device);
                    return;
                default:
                    return;
            }
        }
    }

    private void startCertificateSequence() {
        this.certificator.startCertification();
    }

    private void sendBroadcast(String action) {
        sendBroadcast(new Intent(action));
    }
}
