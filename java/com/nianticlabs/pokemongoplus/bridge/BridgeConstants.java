package com.nianticlabs.pokemongoplus.bridge;

public class BridgeConstants {
    public static final String BATTERY_LEVEL_ACTION = "batteryLevel";
    public static final String CENTRAL_STATE_ACTION = "centralState";
    public static final String CONFIRM_BRIDGE_SHUTDOWN_ACTION = "confirmBridgeShutdown";
    public static final String ENCOUNTER_ID_ACTION = "encounterId";
    public static final String FINISH_SHUTDOWN_ACTION = "finishShutdown";
    public static final String IS_SCANNING_ACTION = "isScanning";
    public static final String PAUSE_ACTION = "pause";
    public static final String PLUGIN_STATE_ACTION = "pluginState";
    public static final String POKESTOP_ACTION = "pokestop";
    public static final String RESUME_ACTION = "resume";
    public static final String SCANNED_SFIDA_ACTION = "scannedSfida";
    public static final String SEND_NOTIFICATION_ACTION = "sendNotification";
    public static final String SFIDA_STATE_ACTION = "sfidaState";
    public static final String START_ACTION = "start";
    public static final String START_SCANNING_ACTION = "startScanning";
    public static final String START_SERVICE_ACTION = "startService";
    public static final String START_SESSION_ACTION = "startSession";
    public static final String STOP_ACTION = "stop";
    public static final String STOP_NOTIFICATION_ACTION = "stopNotification";
    public static final String STOP_SCANNING_ACTION = "stopScanning";
    public static final String STOP_SERVICE_ACTION = "stopService";
    public static final String STOP_SESSION_ACTION = "stopSession";
    public static final String UPDATE_TIMESTAMP_ACTION = "updateTimestamp";

    public enum PgpState {
        Unknown,
        Initialized,
        Started,
        Resumed,
        Paused,
        Stopped,
        BadValue;

        public static PgpState fromInt(int i) {
            try {
                return values()[i];
            } catch (Exception e) {
                return BadValue;
            }
        }
    }

    public enum SfidaState {
        Disconnected,
        Disconnecting,
        Connected,
        Discovered,
        Certified,
        SoftwareUpdate,
        Failed,
        BadValue;

        public static SfidaState fromInt(int i) {
            try {
                return values()[i];
            } catch (Exception e) {
                return BadValue;
            }
        }
    }
}
