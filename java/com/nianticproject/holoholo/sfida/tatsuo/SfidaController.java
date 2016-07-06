package com.nianticproject.holoholo.sfida.tatsuo;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

@TargetApi(18)
public class SfidaController {
    private static final String TAG = SfidaController.class.getSimpleName();
    private static SfidaController instance_;
    private BluetoothAdapter bluetoothAdapter;
    private final Context context;
    private LeScanCallback leScanCallback = new LeScanCallback() {
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(SfidaController.TAG, String.format("Found device: %s address: %s", new Object[]{device.getName(), device.getAddress()}));
            if ("EBISU".equals(device.getName())) {
                Log.d(SfidaController.TAG, String.format("Found SFIDA device: %s", new Object[]{device.getAddress()}));
                SfidaController.this.sfidaDevice = new SfidaDevice(SfidaController.this.context, device);
                SfidaController.this.sfidaDevice.connect();
                SfidaController.this.stopScan();
            }
        }
    };
    private SfidaDevice sfidaDevice;

    public SfidaController(Context context) {
        this.context = context;
    }

    public boolean init() {
        Log.d(TAG, "Initialize SfidaController..");
        this.bluetoothAdapter = ((BluetoothManager) this.context.getSystemService("bluetooth")).getAdapter();
        if (this.bluetoothAdapter == null) {
            Log.d(TAG, "No Bluetooth available.");
            return false;
        } else if (this.bluetoothAdapter.isEnabled()) {
            return true;
        } else {
            Log.d(TAG, "Bluetooth disabled.");
            return false;
        }
    }

    public void startScan() {
        Log.d(TAG, "Start bluetooth device scan");
        this.bluetoothAdapter.startLeScan(this.leScanCallback);
    }

    public void stopScan() {
        Log.d(TAG, "Stop bluetooth device scan");
        this.bluetoothAdapter.stopLeScan(this.leScanCallback);
    }

    @Nullable
    public SfidaDevice getSfidaDevice() {
        return this.sfidaDevice;
    }

    public static SfidaController get(Context context) {
        if (instance_ == null) {
            instance_ = new SfidaController(context);
        }
        return instance_;
    }
}
