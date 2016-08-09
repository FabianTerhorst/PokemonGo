package com.nianticlabs.pokemongoplus;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import com.nianticlabs.pokemongoplus.ble.BluetoothDriver;
import com.nianticlabs.pokemongoplus.ble.Peripheral;
import com.nianticlabs.pokemongoplus.ble.SfidaConstant.CentralState;
import com.nianticlabs.pokemongoplus.ble.callback.CentralStateCallback;
import com.nianticlabs.pokemongoplus.ble.callback.ScanCallback;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class SfidaBluetoothDriver extends BluetoothDriver {
    private static final String TAG = SfidaBluetoothDriver.class.getSimpleName();
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private boolean isScanning;
    private long nativeHandle;
    private Peripheral peripheral;
    private Map<String, SfidaPeripheral> peripheralMap = new HashMap();
    private ScanCallback scanCallback;
    private HandlerExecutor serialExecutor = new HandlerExecutor("SfidaBluetoothDriver");
    private SfidaScanCallback sfidaScanCallback;

    private static class HandlerExecutor implements Executor {
        private final Handler handler = new Handler(this.handlerThread.getLooper());
        private final HandlerThread handlerThread;

        HandlerExecutor(String name) {
            this.handlerThread = new HandlerThread(name);
            this.handlerThread.start();
        }

        public void execute(Runnable command) {
            this.handler.post(command);
        }

        public boolean onThread() {
            return Looper.myLooper() == this.handlerThread.getLooper();
        }

        public void assertOnThread() {
            if (!onThread()) {
                throw new RuntimeException("Must be on the worker thread");
            }
        }

        public void stop() {
            this.handlerThread.quitSafely();
        }
    }

    private class SfidaScanCallback implements LeScanCallback {
        private String peripheralName;

        public SfidaScanCallback(String peripheralName) {
            this.peripheralName = peripheralName;
        }

        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            final String address = device.getAddress();
            SfidaBluetoothDriver.this.serialExecutor.execute(new Runnable() {
                public void run() {
                    if (SfidaBluetoothDriver.this.IsScanning()) {
                        String deviceName = device.getName();
                        if (deviceName != null && deviceName.contains(SfidaScanCallback.this.peripheralName) && SfidaBluetoothDriver.this.scanCallback != null) {
                            SfidaPeripheral foundPeripheral;
                            if (SfidaBluetoothDriver.this.peripheralMap.containsKey(address)) {
                                foundPeripheral = (SfidaPeripheral) SfidaBluetoothDriver.this.peripheralMap.get(address);
                                foundPeripheral.setScanRecord(scanRecord);
                            } else {
                                foundPeripheral = new SfidaPeripheral(SfidaBluetoothDriver.this.context, device, scanRecord);
                                SfidaBluetoothDriver.this.peripheralMap.put(address, foundPeripheral);
                            }
                            SfidaBluetoothDriver.this.scanCallback.onScan(foundPeripheral);
                        }
                    }
                }
            });
        }
    }

    private native void nativeScanCallback(Peripheral peripheral);

    private native void nativeStartCallback(int i);

    public SfidaBluetoothDriver(Context context) {
        this.context = context;
    }

    public boolean IsScanning() {
        return this.isScanning;
    }

    public int start(final CentralStateCallback callback) {
        this.serialExecutor.execute(new Runnable() {
            public void run() {
                BluetoothManager bluetoothManager = SfidaUtils.getBluetoothManager(SfidaBluetoothDriver.this.context);
                if (bluetoothManager != null) {
                    SfidaBluetoothDriver.this.bluetoothAdapter = bluetoothManager.getAdapter();
                    callback.OnStateChanged(SfidaBluetoothDriver.this.bluetoothAdapter.isEnabled() ? CentralState.PoweredOn : CentralState.PoweredOff);
                    return;
                }
                Log.e(SfidaBluetoothDriver.TAG, "start(CentralStateCallback): Could not find bluetooth manager.");
                callback.OnStateChanged(CentralState.Unknown);
            }
        });
        return 0;
    }

    public void stop(int unusedTag) {
        Log.e(TAG, "stop()");
        this.serialExecutor.execute(new Runnable() {
            public void run() {
                SfidaBluetoothDriver.this.releasePeripherals();
            }
        });
        this.serialExecutor.stop();
    }

    public void stopDriver() {
        stop(0);
    }

    public void startDriver() {
        start(new CentralStateCallback() {
            public void OnStateChanged(CentralState state) {
                SfidaBluetoothDriver.this.serialExecutor.assertOnThread();
                SfidaBluetoothDriver.this.nativeStartCallback(state.getInt());
            }
        });
    }

    public void releasePeripherals() {
        for (SfidaPeripheral peripheral : this.peripheralMap.values()) {
            peripheral.onDestroy();
        }
        this.peripheralMap.clear();
    }

    public void startScanning(final String peripheralName, final ScanCallback callback) {
        this.serialExecutor.execute(new Runnable() {
            public void run() {
                SfidaBluetoothDriver.this.releasePeripherals();
                SfidaBluetoothDriver.this.scanCallback = callback;
                if (SfidaBluetoothDriver.this.bluetoothAdapter.isEnabled()) {
                    SfidaBluetoothDriver.this.sfidaScanCallback = new SfidaScanCallback(peripheralName);
                    SfidaBluetoothDriver.this.bluetoothAdapter.startLeScan(SfidaBluetoothDriver.this.sfidaScanCallback);
                    SfidaBluetoothDriver.this.isScanning = true;
                }
            }
        });
    }

    public void startScanning(String peripheralName) {
        Log.d(TAG, String.format("startScanning(%s)", new Object[]{peripheralName}));
        startScanning(peripheralName, new ScanCallback() {
            public void onScan(Peripheral peripheral) {
                SfidaBluetoothDriver.this.nativeScanCallback(peripheral);
            }
        });
    }

    public void stopScanning(String peripheralName) {
        Log.d(TAG, String.format("stopScanning(%s)", new Object[]{peripheralName}));
        if (IsScanning()) {
            try {
                this.bluetoothAdapter.stopLeScan(this.sfidaScanCallback);
            } catch (IllegalArgumentException e) {
            }
            this.isScanning = false;
        }
    }

    public boolean isEnabledBluetoothLe() {
        if (this.bluetoothAdapter == null || !this.bluetoothAdapter.isEnabled()) {
            return false;
        }
        return true;
    }
}
