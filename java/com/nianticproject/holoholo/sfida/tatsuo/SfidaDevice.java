package com.nianticproject.holoholo.sfida.tatsuo;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TargetApi(18)
public class SfidaDevice {
    private static final String TAG = SfidaDevice.class.getSimpleName();
    private BluetoothGatt bluetoothGatt;
    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(SfidaDevice.TAG, String.format("onConnectionStateChange. status: %d", new Object[]{Integer.valueOf(status)}));
            if (newState == 2) {
                SfidaDevice.this.connectionState = 2;
                Log.d(SfidaDevice.TAG, "Connected to GATT server.");
                Log.d(SfidaDevice.TAG, String.format("Attempting to start service discovery: %s", new Object[]{Boolean.valueOf(SfidaDevice.this.bluetoothGatt.discoverServices())}));
            } else if (newState == 0) {
                SfidaDevice.this.connectionState = 0;
                SfidaDevice.this.bluetoothGatt = null;
                Log.d(SfidaDevice.TAG, "Disconnected from GATT server.");
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status != 0) {
                Log.e(SfidaDevice.TAG, String.format("GATT Failed to discover service. status: %d", new Object[]{Integer.valueOf(status)}));
                return;
            }
            SfidaDevice.this.service = gatt.getService(SfidaUUID.SFIDA_DEV_CTRL_SVC_UUID);
            if (SfidaDevice.this.service == null) {
                Log.e(SfidaDevice.TAG, "GATT Service not found: SFIDA_DEV_CTRL_SVC");
                return;
            }
            Log.d(SfidaDevice.TAG, "GATT onServicesDiscovered service is set");
            BluetoothGattCharacteristic characteristic = SfidaDevice.this.service.getCharacteristic(SfidaUUID.SFIDA_BUTTON_NOTIF_CHAR_UUID);
            if (characteristic != null) {
                gatt.setCharacteristicNotification(characteristic, true);
                List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                for (int i = 0; i < descriptors.size(); i++) {
                    BluetoothGattDescriptor descriptor = (BluetoothGattDescriptor) descriptors.get(i);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(descriptor);
                }
            }
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] value = characteristic.getValue();
            onButtonUpdated((value[0] * 256) + value[1]);
        }

        public void onButtonUpdated(int value) {
            if (SfidaDevice.this.buttonPresses == null) {
                SfidaDevice.this.buttonPresses = new int[]{value};
            } else {
                int[] destination = new int[(SfidaDevice.this.buttonPresses.length + 1)];
                System.arraycopy(SfidaDevice.this.buttonPresses, 0, destination, 0, SfidaDevice.this.buttonPresses.length);
                destination[SfidaDevice.this.buttonPresses.length] = value;
                SfidaDevice.this.buttonPresses = destination;
            }
            Log.d(SfidaDevice.TAG, String.format("SFIDA button press returns %d", new Object[]{Integer.valueOf(value)}));
        }
    };
    private int[] buttonPresses;
    private int connectionState = 0;
    private Context context;
    private BluetoothDevice device;
    private BluetoothGattService service;

    private static class SfidaFlashPatternBuilder {
        private int inputReadTimeMs;
        private List<SfidaFlash> patterns;
        private int priority;

        private static class SfidaFlash {
            public int color;
            public int flashTimeMs;
            public boolean interpolationEnabled;
            public int vibrationLevel;

            public SfidaFlash(int flashTimeMs, int color, int vibrationLevel, boolean interpolationEnabled) {
                this.flashTimeMs = flashTimeMs;
                this.color = color;
                this.vibrationLevel = vibrationLevel;
                this.interpolationEnabled = interpolationEnabled;
            }
        }

        private SfidaFlashPatternBuilder() {
            this.patterns = new ArrayList();
        }

        public SfidaFlashPatternBuilder setInputReadTimeMs(int inputReadTimeMs) {
            this.inputReadTimeMs = inputReadTimeMs;
            return this;
        }

        public SfidaFlashPatternBuilder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public SfidaFlashPatternBuilder addFlash(int flashTimeMs, int color, int vibrationLevel, boolean interpolationEnabled) {
            if (this.patterns.size() < 30) {
                this.patterns.add(new SfidaFlash(flashTimeMs, color, vibrationLevel, interpolationEnabled));
                return this;
            }
            throw new InvalidParameterException("Number of flash patterns exceeded limit.");
        }

        public byte[] build() {
            byte[] data = new byte[((this.patterns.size() * 3) + 4)];
            int offset = 0 + 1;
            data[0] = (byte) (this.inputReadTimeMs / 50);
            int offset2 = offset + 1;
            data[offset] = (byte) 0;
            offset = offset2 + 1;
            data[offset2] = (byte) 0;
            offset2 = offset + 1;
            data[offset] = (byte) (((this.priority & 7) << 5) | (this.patterns.size() & 31));
            int i = 0;
            while (i < this.patterns.size()) {
                int i2;
                SfidaFlash flash = (SfidaFlash) this.patterns.get(i);
                offset = offset2 + 1;
                data[offset2] = (byte) (flash.flashTimeMs / 50);
                offset2 = offset + 1;
                data[offset] = (byte) (((Color.green(flash.color) >> 4) << 4) | (Color.red(flash.color >> 4) & 15));
                offset = offset2 + 1;
                if (flash.interpolationEnabled) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                data[offset2] = (byte) (((i2 << 7) | ((flash.vibrationLevel & 7) << 4)) | ((Color.blue(flash.color) >> 4) & 15));
                i++;
                offset2 = offset;
            }
            return data;
        }
    }

    public SfidaDevice(Context context, BluetoothDevice device) {
        this.context = context;
        this.device = device;
    }

    public void connect() {
        Log.d(TAG, "Connect to the GATT server");
        if (this.bluetoothGatt == null) {
            this.bluetoothGatt = this.device.connectGatt(this.context, true, this.bluetoothGattCallback);
        }
    }

    public boolean hasGattService() {
        if (this.service != null) {
            Log.d(TAG, "GATT hasGattService returns true");
        }
        return this.service != null;
    }

    public int[] readButtonPresses() {
        if (this.buttonPresses == null || this.buttonPresses.length <= 0) {
            return new int[0];
        }
        Log.d(TAG, String.format("SFIDA readButtonPresses %s", new Object[]{this.buttonPresses.toString()}));
        int[] values = this.buttonPresses;
        this.buttonPresses = null;
        return values;
    }

    @Deprecated
    public void play(int pattern) {
        this.buttonPresses = null;
        playFlash(16711680, 0, 2, 10000, 1000, 10000);
    }

    public void play(int rgb, int level, int timeMs, int listenMS) {
        this.buttonPresses = null;
        write(SfidaUUID.SFIDA_LED_VIBR_CTRL_CHAR_UUID, new SfidaFlashPatternBuilder().setPriority(1).setInputReadTimeMs(listenMS).addFlash(timeMs, rgb, level, false).build());
    }

    public void playFlash(int rgb1, int rgb2, int level, int timeMs, int timeFlashMs, int listenMS) {
        int count = timeMs / timeFlashMs;
        if (count > 30) {
            timeFlashMs = timeMs / 30;
            count = 30;
        }
        SfidaFlashPatternBuilder builder = new SfidaFlashPatternBuilder();
        builder.setPriority(1);
        builder.setInputReadTimeMs(listenMS);
        for (int i = 0; i < count / 2; i++) {
            builder.addFlash(timeFlashMs, rgb1, level, false);
            builder.addFlash(timeFlashMs, rgb2, 0, false);
        }
        write(SfidaUUID.SFIDA_LED_VIBR_CTRL_CHAR_UUID, builder.build());
    }

    public void playPattern(int rgb, int level, int[] patternMs, int patternCnt, int offTimeMs, int listenMS) {
        SfidaFlashPatternBuilder builder = new SfidaFlashPatternBuilder();
        builder.setPriority(1);
        builder.setInputReadTimeMs(listenMS);
        for (int i = 0; i < patternCnt; i++) {
            builder.addFlash(patternMs[i], rgb, level, false);
            builder.addFlash(offTimeMs, 0, 0, false);
        }
        write(SfidaUUID.SFIDA_LED_VIBR_CTRL_CHAR_UUID, builder.build());
    }

    public void stop() {
        write(SfidaUUID.SFIDA_LED_VIBR_CTRL_CHAR_UUID, new SfidaFlashPatternBuilder().setPriority(7).addFlash(100, 255, 0, false).build());
    }

    public String getVersion() {
        byte[] data = read(SfidaUUID.SFIDA_FW_VERSION);
        if (data != null) {
            return new String(data);
        }
        return "Unknown";
    }

    private byte[] read(UUID charUuid) {
        if (this.service == null) {
            Log.e(TAG, "No SFIDA Service.");
            return null;
        }
        BluetoothGattCharacteristic characteristic = this.service.getCharacteristic(charUuid);
        if (characteristic == null) {
            Log.e(TAG, "Characteristic not found: SFIDA_LED_VIBR_CTRL_CHAR");
            return null;
        }
        this.bluetoothGatt.readCharacteristic(characteristic);
        return characteristic.getValue();
    }

    private void write(UUID charUuid, byte[] data) {
        if (this.service == null) {
            Log.e(TAG, "No SFIDA Service.");
            return;
        }
        BluetoothGattCharacteristic characteristic = this.service.getCharacteristic(charUuid);
        if (characteristic == null) {
            Log.e(TAG, "Characteristic not found: SFIDA_LED_VIBR_CTRL_CHAR");
            return;
        }
        characteristic.setValue(data);
        this.bluetoothGatt.writeCharacteristic(characteristic);
    }
}
