package com.nianticlabs.pokemongoplus;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.nianticlabs.pokemongoplus.ble.Peripheral;
import com.nianticlabs.pokemongoplus.ble.Service;
import com.nianticlabs.pokemongoplus.ble.SfidaConstant.BluetoothError;
import com.nianticlabs.pokemongoplus.ble.SfidaConstant.PeripheralState;
import com.nianticlabs.pokemongoplus.ble.callback.CompletionCallback;
import com.nianticlabs.pokemongoplus.ble.callback.ConnectCallback;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import spacemadness.com.lunarconsole.R;

public class SfidaPeripheral extends Peripheral {
    private static final String TAG = SfidaPeripheral.class.getSimpleName();
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(SfidaPeripheral.TAG, "onConnectionStateChange");
            SfidaPeripheral.this.onConnectionStateChange(gatt, status, newState);
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(SfidaPeripheral.TAG, "onServicesDiscovered");
            SfidaPeripheral.this.onServicesDiscovered(gatt, status);
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(SfidaPeripheral.TAG, "onCharacteristicRead");
            synchronized (SfidaPeripheral.this.serviceRef) {
                Iterator it = SfidaPeripheral.this.serviceRef.iterator();
                while (it.hasNext()) {
                    ((SfidaService) it.next()).onCharacteristicRead(gatt, characteristic, status);
                }
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(SfidaPeripheral.TAG, "onCharacteristicWrite");
            synchronized (SfidaPeripheral.this.serviceRef) {
                Iterator it = SfidaPeripheral.this.serviceRef.iterator();
                while (it.hasNext()) {
                    ((SfidaService) it.next()).onCharacteristicWrite(gatt, characteristic, status);
                }
            }
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(SfidaPeripheral.TAG, "onCharacteristicChanged");
            synchronized (SfidaPeripheral.this.serviceRef) {
                Iterator it = SfidaPeripheral.this.serviceRef.iterator();
                while (it.hasNext()) {
                    ((SfidaService) it.next()).onCharacteristicChanged(gatt, characteristic);
                }
            }
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(SfidaPeripheral.TAG, "onDescriptorWrite");
            synchronized (SfidaPeripheral.this.serviceRef) {
                Iterator it = SfidaPeripheral.this.serviceRef.iterator();
                while (it.hasNext()) {
                    ((SfidaService) it.next()).onDescriptorWrite(gatt, descriptor, status);
                }
            }
        }
    };
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            onHandleBluetoothIntent(intent);
            Log.d(SfidaPeripheral.TAG, "onReceive() : " + intent.getAction());
        }

        private void onHandleBluetoothIntent(Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                Log.d(SfidaPeripheral.TAG, "onReceived() action was null");
                return;
            }
            Object obj = -1;
            switch (action.hashCode()) {
                case -223687943:
                    if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
                        obj = 1;
                        break;
                    }
                    break;
                case 2116862345:
                    if (action.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
                        obj = null;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case R.styleable.AdsAttrs_adSize /*0*/:
                    return;
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    SfidaPeripheral.this.onPairingRequest((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"));
                    return;
                default:
                    Log.d(SfidaPeripheral.TAG, "onReceive() : " + action);
                    return;
            }
        }
    };
    private ConnectCallback connectCallback;
    private Context context;
    private ConnectCallback disconnectCallback;
    private CompletionCallback discoverServicesCallback;
    private BluetoothGatt gatt;
    private long nativeHandle;
    private byte[] scanRecord;
    private final ArrayList<SfidaService> serviceRef = new ArrayList();
    private PeripheralState state;

    private native void nativeConnectCallback(boolean z, int i);

    private native void nativeDisconnectCallback(boolean z, int i);

    private native void nativeDiscoverService(SfidaService sfidaService);

    private native void nativeDiscoverServicesCallback(boolean z, int i);

    public SfidaPeripheral(Context context, BluetoothDevice bluetoothDevice, byte[] scanRecord) {
        this.context = context;
        this.bluetoothDevice = bluetoothDevice;
        this.state = PeripheralState.Disconnected;
        this.bluetoothAdapter = SfidaUtils.getBluetoothManager(context).getAdapter();
        this.scanRecord = scanRecord;
    }

    public void onCreate() {
        IntentFilter bluetoothIntentFilter = new IntentFilter();
        bluetoothIntentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        bluetoothIntentFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
        this.context.registerReceiver(this.bluetoothReceiver, bluetoothIntentFilter);
    }

    public void onDestroy() {
        this.context.unregisterReceiver(this.bluetoothReceiver);
    }

    public String getIdentifier() {
        return this.bluetoothDevice.getAddress();
    }

    public String getName() {
        return this.bluetoothDevice.getName();
    }

    public PeripheralState getState() {
        return this.state;
    }

    public int getStateInt() {
        return getState().getInt();
    }

    private byte[] byteArrayFromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public void setScanRecord(byte[] record) {
        synchronized (this) {
            this.scanRecord = record;
        }
    }

    public long getAdvertisingServiceDataLongValue(String uuid) {
        long j;
        synchronized (this) {
            byte[] uuidBytes = byteArrayFromHexString(uuid);
            int length = uuidBytes.length;
            for (int i = 0; i < this.scanRecord.length - length; i++) {
                boolean found = true;
                for (int j2 = 0; j2 < length; j2++) {
                    if (this.scanRecord[i + j2] != uuidBytes[(length - 1) - j2]) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    j = (long) this.scanRecord[i + length];
                    break;
                }
            }
            j = 0;
        }
        return j;
    }

    public void discoverServices(CompletionCallback callback) {
        Log.d(TAG, "discoverServices(" + callback.toString() + ")");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.gatt != null) {
            this.discoverServicesCallback = callback;
            Log.d(TAG, "discoverSerivice:" + this.gatt.discoverServices());
            return;
        }
        Log.e(TAG, "gatt is null");
        callback.onCompletion(false, BluetoothError.NotConnected);
    }

    public void discoverServices() {
        discoverServices(new CompletionCallback() {
            public void onCompletion(boolean success, BluetoothError error) {
                Log.d(SfidaPeripheral.TAG, "discoverServices success:" + success + " error:" + error);
                SfidaPeripheral.this.nativeDiscoverServicesCallback(success, error.getInt());
            }
        });
    }

    public void connect() {
        connect(new ConnectCallback() {
            public void onConnectionStateChanged(boolean success, BluetoothError error) {
                SfidaPeripheral.this.nativeConnectCallback(success, error.getInt());
            }
        });
    }

    public void disconnect() {
        discoverServices(new CompletionCallback() {
            public void onCompletion(boolean success, BluetoothError error) {
                SfidaPeripheral.this.nativeDisconnectCallback(success, error.getInt());
            }
        });
    }

    public int getServiceCount() {
        return this.serviceRef.size();
    }

    public Service getService(int index) {
        if (index > getServiceCount() - 1) {
            return null;
        }
        return (Service) this.serviceRef.get(index);
    }

    public Service getService(String uuid) {
        if (uuid != null) {
            int count = getServiceCount();
            for (int i = 0; i < count; i++) {
                Service service = getService(i);
                if (uuid.equals(service.getUuid())) {
                    return service;
                }
            }
        }
        return null;
    }

    private void retryConnect() {
        String address = this.bluetoothDevice.getAddress();
        if (this.bluetoothAdapter == null || address == null) {
            Log.w(TAG, "[BLE] BluetoothAdapter not initialized or unspecified address.");
            this.state = PeripheralState.Disconnected;
        } else if (!address.equals(this.bluetoothDevice.getAddress()) || this.gatt == null) {
            this.gatt = this.bluetoothDevice.connectGatt(this.context, false, this.bluetoothGattCallback);
            Log.d(TAG, "Trying to create a new connection.");
        } else {
            Log.d(TAG, "[BLE] Trying to use an existing bluetoothGatt for connection.");
            this.gatt.connect();
        }
    }

    public void connect(ConnectCallback callback) {
        this.connectCallback = callback;
        this.state = PeripheralState.Connecting;
        retryConnect();
    }

    public void disconnect(ConnectCallback callback) {
        this.disconnectCallback = callback;
        if (this.bluetoothAdapter == null || this.gatt == null) {
            Log.w(TAG, "[BLE] BluetoothAdapter not initialized");
        } else {
            this.gatt.disconnect();
        }
    }

    public void closeBluetoothGatt() {
        if (this.gatt != null) {
            this.gatt.close();
            this.gatt = null;
        }
    }

    private void releaseServices() {
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

    private void reconnnectFromBonding(BluetoothDevice device) {
        Log.d(TAG, "reconnnectFromBonding()");
        retryConnect();
    }

    private void unpairDevice() {
        Log.d(TAG, "unpairDevice()");
        try {
            this.bluetoothDevice.getClass().getMethod("removeBond", (Class[]) null).invoke(this.bluetoothDevice, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void disconnectFromBonding(BluetoothDevice device) {
        Log.d(TAG, "disconnectFromBonding()");
        unpairDevice();
    }

    private void bondingCanceled(BluetoothDevice device) {
        Log.d(TAG, "bondingCanceled()");
        SfidaUtils.createBond(device);
    }

    private void onPairingRequest(BluetoothDevice device) {
        Log.d(TAG, "onPairingRequest()");
        try {
            device.getClass().getMethod("setPairingConfirmation", new Class[]{Boolean.TYPE}).invoke(device, new Object[]{Boolean.valueOf(true)});
            device.getClass().getMethod("cancelPairingUserInput", new Class[0]).invoke(device, new Object[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }
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
                        disconnectFromBonding(device);
                        return;
                    } else if (oldState == 11) {
                        bondingCanceled(device);
                        return;
                    } else {
                        Log.d(TAG, "Unhandled oldState : " + oldState);
                        return;
                    }
                case 12:
                    if (!tryCompleteConnect()) {
                        reconnnectFromBonding(device);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    boolean tryCompleteConnect() {
        if (this.connectCallback == null) {
            return false;
        }
        Log.d(TAG, "calling onConnectionStateChanged");
        this.connectCallback.onConnectionStateChanged(true, BluetoothError.Unknown);
        return true;
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        boolean z = false;
        switch (newState) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                Log.d(TAG, "Disconnected from GATT server., state = " + this.state.toString());
                SfidaUtils.refreshDeviceCache(gatt);
                closeBluetoothGatt();
                releaseServices();
                if (this.state == PeripheralState.Connecting || this.state == PeripheralState.Connected) {
                    this.state = PeripheralState.Connecting;
                    retryConnect();
                    Log.d(TAG, "Reconnecting., state now " + this.state.toString());
                    return;
                }
                this.state = PeripheralState.Disconnected;
                if (this.disconnectCallback != null) {
                    ConnectCallback connectCallback = this.disconnectCallback;
                    if (status == 0) {
                        z = true;
                    }
                    connectCallback.onConnectionStateChanged(z, BluetoothError.Unknown);
                }
                Log.d(TAG, "Disconnected., state now " + this.state.toString());
                return;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                Log.d(TAG, "Connected with GATT server.");
                if (status == 0) {
                    this.state = PeripheralState.Connected;
                    tryCompleteConnect();
                    return;
                } else if (this.connectCallback != null) {
                    this.connectCallback.onConnectionStateChanged(false, BluetoothError.Unknown);
                    return;
                } else {
                    return;
                }
            default:
                Log.e(TAG, "onConnectionStateChange() UnhandledState status : " + status + " " + "newState : " + newState);
                return;
        }
    }

    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        switch (status) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                List<BluetoothGattService> services = gatt.getServices();
                synchronized (this.serviceRef) {
                    Log.e(TAG, "onServicesDiscovered thread:" + Thread.currentThread().getId());
                    this.serviceRef.clear();
                    for (BluetoothGattService service : services) {
                        SfidaService sfidaService = new SfidaService(service, gatt);
                        nativeDiscoverService(sfidaService);
                        this.serviceRef.add(sfidaService);
                    }
                }
                this.state = PeripheralState.Disconnected;
                if (this.discoverServicesCallback != null) {
                    this.discoverServicesCallback.onCompletion(true, BluetoothError.Unknown);
                    return;
                }
                Log.e(TAG, String.format("onServicesDiscovered() no callback when discover %d service on device %s", new Object[]{Integer.valueOf(services.size()), this.bluetoothDevice.getAddress()}));
                return;
            default:
                this.state = PeripheralState.Disconnected;
                this.discoverServicesCallback.onCompletion(false, BluetoothError.Unknown);
                Log.e(TAG, "[BLE] onServicesDiscovered received error: " + status);
                return;
        }
    }
}
