package com.nianticlabs.pokemongoplus.ble;

import java.util.UUID;

public class SfidaConstant {
    public static final String PERIPHERAL_NAME = "Pokemon GO Plus";
    public static final String SFIDA_RESPONSE_CERTIFICATION_CHALLENGE_1 = "4010";
    public static final String SFIDA_RESPONSE_CERTIFICATION_CHALLENGE_2 = "5000";
    public static final String SFIDA_RESPONSE_CERTIFICATION_COMPLETE = "4020";
    public static final String SFIDA_RESPONSE_CERTIFICATION_NOTIFY = "3000";
    public static final UUID UUID_BATTERY_LEVEL_CHAR = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_BATTERY_SERVICE = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_BUTTON_NOTIF_CHAR = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939aed");
    public static final UUID UUID_CENTRAL_TO_SFIDA_CHAR = UUID.fromString("bbe87709-5b89-4433-ab7f-8b8eef0d8e38");
    public static final UUID UUID_CERTIFICATE_SERVICE = UUID.fromString("bbe87709-5b89-4433-ab7f-8b8eef0d8e37");
    public static final UUID UUID_CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_DEVICE_CONTROL_SERVICE = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939aeb");
    public static final UUID UUID_FW_UPDATE_REQUEST_CHAR = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939aef");
    public static final UUID UUID_FW_UPDATE_SERVICE = UUID.fromString("0000fef5-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_FW_VERSION_CHAR = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939af0");
    public static final UUID UUID_LED_VIBRATE_CTRL_CHAR = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939aec");
    public static final UUID UUID_SFIDA_COMMANDS_CHAR = UUID.fromString("bbe87709-5b89-4433-ab7f-8b8eef0d8e39");
    public static final UUID UUID_SFIDA_TO_CENTRAL_CHAR = UUID.fromString("bbe87709-5b89-4433-ab7f-8b8eef0d8e3a");

    public enum BluetoothError {
        Unknown(1),
        InvalidParameters(2),
        InvalidHandle(3),
        NotConnected(4),
        OutOfSpace(5),
        OperationCancelled(6),
        ConnectionTimeout(7),
        PeripheralDisconnected(8),
        UUIDNotAllowed(9),
        AlreadyAdvertising(10),
        ConnectionFailed(11);
        
        private final int id;

        private BluetoothError(int id) {
            this.id = id;
        }

        public int getInt() {
            return this.id;
        }
    }

    public enum CentralState {
        Unknown(0),
        Resetting(1),
        Unsupported(2),
        Unauthorized(3),
        PoweredOff(4),
        PoweredOn(5);
        
        private final int id;

        private CentralState(int id) {
            this.id = id;
        }

        public int getInt() {
            return this.id;
        }
    }

    public enum PeripheralState {
        Disconnected(0),
        Connecting(1),
        Connected(2);
        
        private final int id;

        private PeripheralState(int id) {
            this.id = id;
        }

        public int getInt() {
            return this.id;
        }
    }
}
