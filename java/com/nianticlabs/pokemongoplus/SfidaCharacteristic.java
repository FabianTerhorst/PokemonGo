package com.nianticlabs.pokemongoplus;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.AsyncTask;
import android.util.Log;
import com.nianticlabs.pokemongoplus.ble.Characteristic;
import com.nianticlabs.pokemongoplus.ble.SfidaConstant;
import com.nianticlabs.pokemongoplus.ble.SfidaConstant.BluetoothError;
import com.nianticlabs.pokemongoplus.ble.callback.CompletionCallback;
import com.nianticlabs.pokemongoplus.ble.callback.ValueChangeCallback;
import java.util.ArrayDeque;

public class SfidaCharacteristic extends Characteristic {
    private static final String TAG = SfidaCharacteristic.class.getSimpleName();
    private final int RETRIES = 7;
    private final long SLEEP_DELAY_MS = 250;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGatt gatt;
    private long nativeHandle;
    private CompletionCallback onDisableNotifyCallback;
    private CompletionCallback onEnableNotifyCallback;
    private CompletionCallback onReadCallback;
    private ValueChangeCallback onValueChangedCallback;
    private CompletionCallback onWriteCallback;
    private volatile ArrayDeque<byte[]> queue = new ArrayDeque();

    private native void nativeDisableNotifyCallback(boolean z, int i);

    private native void nativeEnableNotifyCallback(boolean z, int i);

    private native void nativeReadCompleteCallback(boolean z, int i);

    private native void nativeSaveValueChangedCallback(byte[] bArr);

    private native void nativeValueChangedCallback(boolean z, boolean z2, int i);

    private native void nativeWriteCompleteCallback(boolean z, int i);

    public SfidaCharacteristic(BluetoothGattCharacteristic characteristic, BluetoothGatt gatt) {
        this.gatt = gatt;
        this.characteristic = characteristic;
    }

    public void onDestroy() {
    }

    public String getUuid() {
        return this.characteristic.getUuid().toString();
    }

    public long getLongValue() {
        return 0;
    }

    public byte[] getValue() {
        return (byte[]) this.queue.pollFirst();
    }

    public void cancelNotify() {
    }

    public void notifyValueChanged() {
        this.onValueChangedCallback = new ValueChangeCallback() {
            public void OnValueChange(boolean success, boolean valueChanged, BluetoothError error) {
                SfidaCharacteristic.this.nativeValueChangedCallback(success, valueChanged, error.getInt());
            }
        };
    }

    public void writeByteArray(byte[] byteArray, CompletionCallback callback) {
        this.onWriteCallback = callback;
        this.characteristic.setValue(byteArray);
        boolean result = false;
        for (int i = 0; i < 7; i++) {
            result = this.gatt.writeCharacteristic(this.characteristic);
            if (result) {
                break;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }
        }
        if (!result) {
            this.onWriteCallback.onCompletion(false, BluetoothError.Unknown);
            this.onWriteCallback = null;
        }
    }

    public void writeByteArray(byte[] byteArray) {
        writeByteArray(byteArray, new CompletionCallback() {
            public void onCompletion(boolean success, BluetoothError error) {
                SfidaCharacteristic.this.nativeWriteCompleteCallback(success, error.getInt());
            }
        });
    }

    public void readValue(CompletionCallback callback) {
        this.onReadCallback = callback;
        if (!this.gatt.readCharacteristic(this.characteristic)) {
            this.onReadCallback.onCompletion(false, BluetoothError.Unknown);
        }
    }

    public void readValue() {
        readValue(new CompletionCallback() {
            public void onCompletion(boolean success, BluetoothError error) {
                SfidaCharacteristic.this.nativeReadCompleteCallback(success, error.getInt());
            }
        });
    }

    public void enableNotify(CompletionCallback callback) {
        int i;
        this.onEnableNotifyCallback = callback;
        for (i = 0; i < 7; i++) {
            boolean success = this.gatt.setCharacteristicNotification(this.characteristic, true);
            Log.d(TAG, String.format("setCharacteristicNotification success: %b", new Object[]{Boolean.valueOf(success)}));
            if (success) {
                break;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }
        }
        if ((this.characteristic.getProperties() & 16) == 0) {
            Log.w(TAG, "Enable Notify not supported");
        }
        byte[] previousValue = this.characteristic.getValue();
        BluetoothGattDescriptor descriptor = this.characteristic.getDescriptor(SfidaConstant.UUID_CLIENT_CHARACTERISTIC_CONFIG);
        Log.d(TAG, String.format("Config characteristic:%s descriptor:%s", new Object[]{getUuid(), descriptor}));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            boolean result = false;
            for (i = 0; i < 7; i++) {
                result = this.gatt.writeDescriptor(descriptor);
                Log.d(TAG, String.format("Write descriptor success: %b", new Object[]{Boolean.valueOf(result)}));
                if (result) {
                    break;
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e2) {
                }
            }
            if (!result) {
                this.onEnableNotifyCallback.onCompletion(false, BluetoothError.Unknown);
            }
        }
    }

    public void enableNotify() {
        enableNotify(new CompletionCallback() {
            public void onCompletion(boolean success, BluetoothError error) {
                Log.d(SfidaCharacteristic.TAG, String.format("enableNotify callback success: %b error[%d]:%s UUID:%s", new Object[]{Boolean.valueOf(success), Integer.valueOf(error.getInt()), error.toString(), SfidaCharacteristic.this.getUuid()}));
                SfidaCharacteristic.this.nativeEnableNotifyCallback(success, error.getInt());
            }
        });
    }

    public void disableNotify(CompletionCallback callback) {
        this.onDisableNotifyCallback = callback;
        boolean success = this.gatt.setCharacteristicNotification(this.characteristic, false);
        byte[] currentValue = this.characteristic.getValue();
        BluetoothGattDescriptor descriptor = this.characteristic.getDescriptor(SfidaConstant.UUID_CLIENT_CHARACTERISTIC_CONFIG);
        Log.d(TAG, String.format("Config characteristic:%s descriptor:%s", new Object[]{getUuid(), descriptor}));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            boolean result = false;
            for (int i = 0; i < 7; i++) {
                result = this.gatt.writeDescriptor(descriptor);
                Log.d(TAG, String.format("Write descriptor success: %b", new Object[]{Boolean.valueOf(result)}));
                if (result) {
                    break;
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                }
            }
            if (!result) {
                this.onDisableNotifyCallback.onCompletion(false, BluetoothError.Unknown);
            }
        }
    }

    public void disableNotify() {
        disableNotify(new CompletionCallback() {
            public void onCompletion(boolean success, BluetoothError error) {
                Log.d(SfidaCharacteristic.TAG, String.format("disableNotify callback success: %b error[%d]:%s UUID:%s", new Object[]{Boolean.valueOf(success), Integer.valueOf(error.getInt()), error.toString(), SfidaCharacteristic.this.getUuid()}));
                SfidaCharacteristic.this.nativeDisableNotifyCallback(success, error.getInt());
            }
        });
    }

    public void onCharacteristicChanged() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                Log.d(SfidaCharacteristic.TAG, String.format("onCharacteristicChanged: %s", new Object[]{SfidaCharacteristic.this.characteristic.getUuid().toString()}));
                byte[] receivedValue = SfidaCharacteristic.this.characteristic.getValue();
                if (SfidaCharacteristic.this.onValueChangedCallback != null) {
                    SfidaCharacteristic.this.nativeSaveValueChangedCallback(receivedValue);
                    SfidaCharacteristic.this.queue.add(receivedValue);
                    SfidaCharacteristic.this.onValueChangedCallback.OnValueChange(true, true, BluetoothError.Unknown);
                }
            }
        });
    }

    public void onCharacteristicWrite(final int status) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                if (SfidaCharacteristic.this.onWriteCallback == null) {
                    return;
                }
                if (status == 0) {
                    SfidaCharacteristic.this.onWriteCallback.onCompletion(true, BluetoothError.Unknown);
                } else {
                    SfidaCharacteristic.this.onWriteCallback.onCompletion(false, BluetoothError.Unknown);
                }
            }
        });
    }

    public void onCharacteristicRead(final int status) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                if (status == 0) {
                    Log.d(SfidaCharacteristic.TAG, String.format("onCharacteristicRead: %s", new Object[]{SfidaCharacteristic.this.characteristic.getUuid().toString()}));
                    SfidaCharacteristic.this.nativeSaveValueChangedCallback(SfidaCharacteristic.this.characteristic.getValue());
                    SfidaCharacteristic.this.onReadCallback.onCompletion(true, BluetoothError.Unknown);
                    return;
                }
                SfidaCharacteristic.this.onReadCallback.onCompletion(false, BluetoothError.Unknown);
            }
        });
    }

    public void onDescriptorWrite(BluetoothGattDescriptor descriptor, final int status) {
        Log.d(TAG, String.format("onDescriptorWrite status:%d", new Object[]{Integer.valueOf(status)}));
        AsyncTask.execute(new Runnable() {
            public void run() {
                if (SfidaCharacteristic.this.onEnableNotifyCallback != null) {
                    if (status == 0 || status == 8) {
                        SfidaCharacteristic.this.onEnableNotifyCallback.onCompletion(true, BluetoothError.Unknown);
                    } else {
                        SfidaCharacteristic.this.onEnableNotifyCallback.onCompletion(false, BluetoothError.Unknown);
                    }
                    SfidaCharacteristic.this.onEnableNotifyCallback = null;
                } else if (SfidaCharacteristic.this.onDisableNotifyCallback != null) {
                    if (status == 0 || status == 8) {
                        SfidaCharacteristic.this.onDisableNotifyCallback.onCompletion(true, BluetoothError.Unknown);
                    } else {
                        SfidaCharacteristic.this.onDisableNotifyCallback.onCompletion(false, BluetoothError.Unknown);
                    }
                    SfidaCharacteristic.this.onDisableNotifyCallback = null;
                }
            }
        });
    }
}
