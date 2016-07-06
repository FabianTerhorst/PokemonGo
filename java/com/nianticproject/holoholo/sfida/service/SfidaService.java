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
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
            r8 = this;
            r5 = 1;
            r3 = 0;
            r4 = com.nianticproject.holoholo.sfida.service.SfidaService.TAG;
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "onReceive() : ";
            r6 = r6.append(r7);
            r7 = r10.getAction();
            r6 = r6.append(r7);
            r6 = r6.toString();
            android.util.Log.d(r4, r6);
            r0 = r10.getAction();
            if (r0 != 0) goto L_0x0030;
        L_0x0026:
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.TAG;
            r4 = "onReceived() action was null";
            android.util.Log.d(r3, r4);
        L_0x002f:
            return;
        L_0x0030:
            r4 = -1;
            r6 = r0.hashCode();
            switch(r6) {
                case -223687943: goto L_0x007e;
                case -141093944: goto L_0x0074;
                case 1079267555: goto L_0x006a;
                case 1821585647: goto L_0x0060;
                case 2116862345: goto L_0x0057;
                default: goto L_0x0038;
            };
        L_0x0038:
            r3 = r4;
        L_0x0039:
            switch(r3) {
                case 0: goto L_0x0088;
                case 1: goto L_0x008e;
                case 2: goto L_0x00b7;
                case 3: goto L_0x00c7;
                case 4: goto L_0x00d4;
                default: goto L_0x003c;
            };
        L_0x003c:
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.TAG;
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r5 = "onReceive() : ";
            r4 = r4.append(r5);
            r4 = r4.append(r0);
            r4 = r4.toString();
            android.util.Log.d(r3, r4);
            goto L_0x002f;
        L_0x0057:
            r5 = "android.bluetooth.device.action.BOND_STATE_CHANGED";
            r5 = r0.equals(r5);
            if (r5 == 0) goto L_0x0038;
        L_0x005f:
            goto L_0x0039;
        L_0x0060:
            r3 = "android.bluetooth.device.action.ACL_DISCONNECTED";
            r3 = r0.equals(r3);
            if (r3 == 0) goto L_0x0038;
        L_0x0068:
            r3 = r5;
            goto L_0x0039;
        L_0x006a:
            r3 = "com.nianticproject.holoholo.sfida.dismiss";
            r3 = r0.equals(r3);
            if (r3 == 0) goto L_0x0038;
        L_0x0072:
            r3 = 2;
            goto L_0x0039;
        L_0x0074:
            r3 = "com.nianticproject.holoholo.sfida.vibrate";
            r3 = r0.equals(r3);
            if (r3 == 0) goto L_0x0038;
        L_0x007c:
            r3 = 3;
            goto L_0x0039;
        L_0x007e:
            r3 = "android.bluetooth.device.action.PAIRING_REQUEST";
            r3 = r0.equals(r3);
            if (r3 == 0) goto L_0x0038;
        L_0x0086:
            r3 = 4;
            goto L_0x0039;
        L_0x0088:
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r3.onBondStateChanged(r10);
            goto L_0x002f;
        L_0x008e:
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r3 = r3.connectionState;
            r4 = com.nianticproject.holoholo.sfida.constants.SfidaConstants.ConnectionState.RE_BOND;
            if (r3 != r4) goto L_0x002f;
        L_0x0098:
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r4 = com.nianticproject.holoholo.sfida.constants.SfidaConstants.ConnectionState.NO_CONNECTION;
            r3.setConnectionState(r4);
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r4 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r4 = r4.bluetoothDeviceAddress;
            r3 = r3.getDevice(r4);
            com.nianticproject.holoholo.sfida.SfidaUtils.createBond(r3);
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r4 = "com.nianticproject.holoholo.sfida.ACTION_CREATE_BOND";
            r3.sendBroadcast(r4);
            goto L_0x002f;
        L_0x00b7:
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r3 = r3.getApplicationContext();
            com.nianticproject.holoholo.sfida.SfidaNotification.dissmiss(r3);
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r3.disconnectBluetooth();
            goto L_0x002f;
        L_0x00c7:
            r3 = com.nianticproject.holoholo.sfida.service.SfidaService.this;
            r4 = com.nianticproject.holoholo.sfida.SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR;
            r5 = com.nianticproject.holoholo.sfida.SfidaMessage.getBlinkRed();
            r3.sendDeviceControlMessage(r4, r5);
            goto L_0x002f;
        L_0x00d4:
            r3 = "android.bluetooth.device.extra.DEVICE";
            r1 = r10.getParcelableExtra(r3);
            r1 = (android.bluetooth.BluetoothDevice) r1;
            r3 = r1.getClass();	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r4 = "setPairingConfirmation";
            r5 = 1;
            r5 = new java.lang.Class[r5];	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r6 = 0;
            r7 = java.lang.Boolean.TYPE;	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r5[r6] = r7;	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r3 = r3.getMethod(r4, r5);	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r4 = 1;
            r4 = new java.lang.Object[r4];	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r5 = 0;
            r6 = 1;
            r6 = java.lang.Boolean.valueOf(r6);	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r4[r5] = r6;	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r3.invoke(r1, r4);	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r3 = r1.getClass();	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r4 = "cancelPairingUserInput";
            r5 = 0;
            r5 = new java.lang.Class[r5];	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r3 = r3.getMethod(r4, r5);	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r4 = 0;
            r4 = new java.lang.Object[r4];	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            r3.invoke(r1, r4);	 Catch:{ IllegalAccessException -> 0x0111, InvocationTargetException -> 0x0117, NoSuchMethodException -> 0x011d }
            goto L_0x002f;
        L_0x0111:
            r2 = move-exception;
            r2.printStackTrace();
            goto L_0x002f;
        L_0x0117:
            r2 = move-exception;
            r2.printStackTrace();
            goto L_0x002f;
        L_0x011d:
            r2 = move-exception;
            r2.printStackTrace();
            goto L_0x002f;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nianticproject.holoholo.sfida.service.SfidaService.1.onReceive(android.content.Context, android.content.Intent):void");
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
