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
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGatt gatt;
    private long nativeHandle;
    private CompletionCallback onEnableNotifyCallback;
    private CompletionCallback onReadCallback;
    private ValueChangeCallback onValueChangedCallback;
    private CompletionCallback onWriteCallback;
    private volatile ArrayDeque<byte[]> queue = new ArrayDeque();

    private native void nativeEnableNotifyCallback(boolean z, int i);

    private native void nativeReadCompleteCallback(boolean z, int i);

    private native void nativeSaveValueChangedCallback(byte[] bArr);

    private native void nativeValueChangedCallback(boolean z, boolean z2, int i);

    private native void nativeWriteCompleteCallback(boolean z, int i);

    public SfidaCharacteristic(BluetoothGattCharacteristic characteristic, BluetoothGatt gatt) {
        this.gatt = gatt;
        this.characteristic = characteristic;
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

    public void notifyValueChanged() {
        this.onValueChangedCallback = new ValueChangeCallback() {
            public void OnValueChange(boolean success, boolean valueChanged, BluetoothError error) {
                SfidaCharacteristic.this.nativeValueChangedCallback(success, valueChanged, error.getInt());
            }
        };
        boolean success = this.gatt.setCharacteristicNotification(this.characteristic, true);
    }

    public void writeByteArray(byte[] byteArray, CompletionCallback callback) {
        this.onWriteCallback = callback;
        this.characteristic.setValue(byteArray);
        if (!this.gatt.writeCharacteristic(this.characteristic)) {
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
        this.onEnableNotifyCallback = callback;
        BluetoothGattDescriptor descriptor = this.characteristic.getDescriptor(SfidaConstant.UUID_CLIENT_CHARACTERISTIC_CONFIG);
        Log.d(TAG, String.format("Config characteristic:%s descriptor:%s", new Object[]{getUuid(), descriptor}));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            boolean result = this.gatt.writeDescriptor(descriptor);
            Log.d(TAG, String.format("Write description success:%b", new Object[]{Boolean.valueOf(result)}));
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

    public void onCharacteristicChanged() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                Log.d(SfidaCharacteristic.TAG, String.format("onCharacteristicChanged: %s", new Object[]{SfidaCharacteristic.this.characteristic.getUuid().toString()}));
                byte[] receivedValue = SfidaCharacteristic.this.characteristic.getValue();
                SfidaCharacteristic.this.nativeSaveValueChangedCallback(receivedValue);
                if (SfidaCharacteristic.this.onValueChangedCallback != null) {
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
                if (SfidaCharacteristic.this.onEnableNotifyCallback == null) {
                    return;
                }
                if (status == 0) {
                    SfidaCharacteristic.this.onEnableNotifyCallback.onCompletion(true, BluetoothError.Unknown);
                } else {
                    SfidaCharacteristic.this.onEnableNotifyCallback.onCompletion(false, BluetoothError.Unknown);
                }
            }
        });
    }
}
