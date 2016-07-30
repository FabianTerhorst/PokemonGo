package com.nianticlabs.pokemongoplus.bridge;

public class BridgeConstants {

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
