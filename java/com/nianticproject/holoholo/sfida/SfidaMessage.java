package com.nianticproject.holoholo.sfida;

import java.util.UUID;

public class SfidaMessage {
    public static final int ACTIVITY_BYTE_LENGTH = 3;
    public static final String SFIDA_RESPONSE_CERTIFICATION_CHALLENGE_1 = "4010";
    public static final String SFIDA_RESPONSE_CERTIFICATION_CHALLENGE_2 = "5000";
    public static final String SFIDA_RESPONSE_CERTIFICATION_COMPLETE = "4020";
    public static final String SFIDA_RESPONSE_CERTIFICATION_NOTIFY = "3000";
    private static final String TAG = SfidaMessage.class.getSimpleName();
    public static final UUID UUID_BATTERY_LEVEL_CHAR = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_BATTERY_SERVICE = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_BUTTON_NOTIF_CHAR = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939aed");
    public static final UUID UUID_CENTRAL_TO_SFIDA_CHAR = UUID.fromString("bbe87709-5b89-4433-ab7f-8b8eef0d8e38");
    public static final UUID UUID_CERTIFICATE_SERVICE = UUID.fromString("bbe87709-5b89-4433-ab7f-8b8eef0d8e37");
    public static final UUID UUID_DEVICE_CONTROL_SERVICE = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939aeb");
    public static final UUID UUID_FW_UPDATE_REQUEST_CHAR = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939aef");
    public static final UUID UUID_FW_UPDATE_SERVICE = UUID.fromString("0000fef5-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_FW_VERSION_CHAR = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939af0");
    public static final UUID UUID_LED_VIBRATE_CTRL_CHAR = UUID.fromString("21c50462-67cb-63a3-5c4c-82b5b9939aec");
    public static final UUID UUID_SFIDA_COMMANDS_CHAR = UUID.fromString("bbe87709-5b89-4433-ab7f-8b8eef0d8e39");
    public static final UUID UUID_SFIDA_TO_CENTRAL_CHAR = UUID.fromString("bbe87709-5b89-4433-ab7f-8b8eef0d8e3a");

    public static byte[] getDonePattern() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) 3, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 112, (byte) 1, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 112};
    }

    public static byte[] getCancelPattern() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
    }

    public static byte[] getReachedPokestop() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 4, (byte) 0, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 8, (byte) 0, (byte) -1, (byte) 10, (byte) 0, Byte.MIN_VALUE, (byte) 4, (byte) 0, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 8, (byte) 0, (byte) -1, (byte) 10, (byte) 0, Byte.MIN_VALUE, (byte) 4, (byte) 0, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 8, (byte) 0, (byte) -1, (byte) 10, (byte) 0, Byte.MIN_VALUE, (byte) 4, (byte) 0, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 8, (byte) 0, (byte) -1, (byte) 10, (byte) 0, Byte.MIN_VALUE, (byte) 4, (byte) 0, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 8, (byte) 0, (byte) -1, (byte) 10, (byte) 0, Byte.MIN_VALUE, (byte) 4, (byte) 0, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 8, (byte) 0, (byte) -1, (byte) 10, (byte) 0, Byte.MIN_VALUE, (byte) 2, (byte) 0, (byte) -113, (byte) -1, (byte) 0, (byte) -113, (byte) -21, (byte) 0, (byte) -113};
    }

    public static byte[] getRewardItems(int itemCount) {
        byte[] afterActivity = new byte[]{(byte) 0, (byte) 0, (byte) 0};
        byte[] activities = getRewardItemActivity(itemCount);
        byte[] activityCount = new byte[]{(byte) (activities.length / ACTIVITY_BYTE_LENGTH)};
        byte[] result = new byte[((afterActivity.length + 1) + activities.length)];
        System.arraycopy(afterActivity, 0, result, 0, afterActivity.length);
        System.arraycopy(activityCount, 0, result, afterActivity.length, activityCount.length);
        System.arraycopy(activities, 0, result, afterActivity.length + activityCount.length, activities.length);
        return result;
    }

    private static byte[] getRewardItemActivity(int vibrateCount) {
        if (vibrateCount == 1) {
            return new byte[]{(byte) 2, (byte) -16, Byte.MIN_VALUE, (byte) 2, (byte) 15, (byte) -16, (byte) 2, (byte) 0, (byte) -113};
        }
        if (vibrateCount == 2) {
            return new byte[]{(byte) 2, (byte) -16, (byte) -16, (byte) 1, (byte) -16, Byte.MIN_VALUE, (byte) 2, (byte) 15, (byte) -16, (byte) 1, (byte) 15, Byte.MIN_VALUE, (byte) 3, (byte) 0, (byte) -113};
        }
        if (vibrateCount >= ACTIVITY_BYTE_LENGTH) {
            byte[] result = new byte[(vibrateCount * 6)];
            for (int count = 0; count < vibrateCount; count++) {
                byte[] activity;
                if (count % ACTIVITY_BYTE_LENGTH == 0) {
                    activity = new byte[]{(byte) 2, (byte) 0, (byte) -8, (byte) 2, (byte) 0, (byte) -113};
                } else if (count % ACTIVITY_BYTE_LENGTH == 1) {
                    activity = new byte[]{(byte) 2, Byte.MIN_VALUE, (byte) -16, (byte) 2, (byte) -16, Byte.MIN_VALUE};
                } else {
                    activity = new byte[]{(byte) 2, (byte) 8, (byte) -16, (byte) 2, (byte) 15, Byte.MIN_VALUE};
                }
                System.arraycopy(activity, 0, result, 6 * count, 6);
            }
            return result;
        }
        throw new IllegalArgumentException();
    }

    public static byte[] getSpawnedPokemon() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 15, (byte) 16, (byte) -16, (byte) -16, (byte) 8, (byte) 0, (byte) 0, (byte) 16, (byte) -16, (byte) -16, (byte) 8, (byte) 0, (byte) 0, (byte) 16, (byte) -16, (byte) -16, (byte) 8, (byte) 0, (byte) 0, (byte) 16, (byte) -16, (byte) -16, (byte) 8, (byte) 0, (byte) 0, (byte) 16, (byte) -16, (byte) -16, (byte) 8, (byte) 0, (byte) 0, (byte) 16, (byte) -16, (byte) -16, (byte) 8, (byte) 0, (byte) 0, (byte) 16, (byte) -16, Byte.MIN_VALUE, (byte) -1, (byte) -16, Byte.MIN_VALUE, (byte) -81, (byte) -16, Byte.MIN_VALUE};
    }

    public static byte[] getSpawnedUncaughtPokemon() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 15, (byte) 14, (byte) -1, (byte) -16, (byte) 2, (byte) 0, (byte) 0, (byte) 14, (byte) -1, (byte) -16, (byte) 2, (byte) 0, (byte) 0, (byte) 14, (byte) -1, (byte) -16, (byte) 2, (byte) 0, (byte) 0, (byte) 14, (byte) -1, (byte) -16, (byte) 2, (byte) 0, (byte) 0, (byte) 14, (byte) -1, (byte) -16, (byte) 2, (byte) 0, (byte) 0, (byte) 14, (byte) -1, (byte) -16, (byte) 2, (byte) 0, (byte) 0, (byte) 64, (byte) -1, Byte.MIN_VALUE, (byte) -1, (byte) -1, Byte.MIN_VALUE, (byte) -81, (byte) -1, Byte.MIN_VALUE};
    }

    public static byte[] getSpawnedLegendaryPokemon() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 30, (byte) 16, (byte) 15, (byte) -1, (byte) 8, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 8, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 6, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 3, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE, (byte) 16, (byte) 15, (byte) -1, (byte) 1, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getReadyForThrowPokeball() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 18, (byte) 2, (byte) -16, (byte) -16, (byte) 11, (byte) -16, Byte.MIN_VALUE, (byte) 13, (byte) 0, Byte.MIN_VALUE, (byte) 2, (byte) -16, (byte) -16, (byte) 11, (byte) -16, Byte.MIN_VALUE, (byte) 13, (byte) 0, Byte.MIN_VALUE, (byte) 2, (byte) -16, (byte) -16, (byte) 11, (byte) -16, Byte.MIN_VALUE, (byte) 13, (byte) 0, Byte.MIN_VALUE, (byte) 2, (byte) -16, (byte) -16, (byte) 11, (byte) -16, Byte.MIN_VALUE, (byte) 13, (byte) 0, Byte.MIN_VALUE, (byte) 2, (byte) -16, (byte) -16, (byte) 11, (byte) -16, Byte.MIN_VALUE, (byte) 13, (byte) 0, Byte.MIN_VALUE, (byte) 2, (byte) -16, Byte.MIN_VALUE, (byte) -1, (byte) -16, Byte.MIN_VALUE, (byte) -43, (byte) -16, Byte.MIN_VALUE};
    }

    public static byte[] getThrewPokeball() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 20, (byte) 0, (byte) -16, (byte) 4, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getPokeballShakeThree() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 12, (byte) 3, (byte) -1, (byte) -1, (byte) 25, (byte) 0, Byte.MIN_VALUE, (byte) 3, (byte) -1, (byte) -1, (byte) 24, (byte) 0, Byte.MIN_VALUE, (byte) 3, (byte) -1, (byte) -1, (byte) 24, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getPokeballShakeTwice() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 10, (byte) 3, (byte) -1, (byte) -1, (byte) 25, (byte) 0, Byte.MIN_VALUE, (byte) 3, (byte) -1, (byte) -1, (byte) 24, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getPokeballShakeOnce() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 8, (byte) 3, (byte) -1, (byte) -1, (byte) 25, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getPokeballNoShake() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) 15, (byte) -16, (byte) 2, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getPokemonCaught() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 21, (byte) 3, (byte) -1, (byte) -1, (byte) 25, (byte) 0, Byte.MIN_VALUE, (byte) 3, (byte) -1, (byte) -1, (byte) 24, (byte) 0, Byte.MIN_VALUE, (byte) 3, (byte) -1, (byte) -1, (byte) 24, (byte) 0, Byte.MIN_VALUE, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 3, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 3, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 3, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 3, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 3, (byte) 0, (byte) -113};
    }

    public static byte[] getNoPokeball() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 2, (byte) 15, (byte) -16, (byte) 10, (byte) 0, Byte.MIN_VALUE, (byte) 2, (byte) 15, (byte) -16, (byte) 10, (byte) 0, Byte.MIN_VALUE, (byte) 2, (byte) 15, (byte) -16, (byte) 10, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getDowserVisible() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 10, (byte) 6, (byte) -1, (byte) -1, (byte) 3, (byte) 0, Byte.MIN_VALUE, (byte) 6, (byte) -1, (byte) -1, (byte) 3, (byte) 0, Byte.MIN_VALUE, (byte) 6, (byte) -1, (byte) -1, (byte) 3, (byte) 0, Byte.MIN_VALUE, (byte) 6, (byte) -1, (byte) -1, (byte) 3, (byte) 0, Byte.MIN_VALUE, (byte) 6, (byte) -1, (byte) -1, (byte) 3, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getDowserProximity1() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) 10, (byte) -1, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 10, (byte) -1, (byte) -1, (byte) 2, (byte) 0, Byte.MIN_VALUE, (byte) 4, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getDowserProximity2() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 10, (byte) 2, (byte) -1, (byte) -113, (byte) 1, (byte) -1, (byte) -1, (byte) 3, (byte) -1, (byte) -113, (byte) 1, (byte) -1, (byte) -1, (byte) 3, (byte) -1, (byte) -113, (byte) 1, (byte) -1, (byte) -1, (byte) 3, (byte) -1, (byte) -113, (byte) 1, (byte) -1, (byte) -1, (byte) 3, (byte) -1, (byte) -113, (byte) 4, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getDowserProximity3() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 2, (byte) -1, (byte) -113, (byte) 1, (byte) -1, (byte) -1, (byte) 3, (byte) -1, (byte) -113, (byte) 1, (byte) -1, (byte) -1, (byte) 3, (byte) -1, (byte) -113, (byte) 1, (byte) -1, (byte) -1, (byte) 4, (byte) -1, (byte) -113};
    }

    public static byte[] getDowserProximity4() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) 2, (byte) -1, (byte) -113, (byte) 1, (byte) -1, (byte) -1, (byte) 3, (byte) 0, Byte.MIN_VALUE, (byte) 1, (byte) -1, (byte) -1, (byte) 4, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getDowserProximity5() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 1, (byte) -1, (byte) -1, (byte) 17, (byte) 0, Byte.MIN_VALUE};
    }

    public static byte[] getDowserCancel() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 24, (byte) -1, (byte) -1};
    }

    public static byte[] getError() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 24, (byte) 15, (byte) -16};
    }

    public static byte[] getManualPattern(int playtime, int r, int g, int b, int vibrationStrength, int repeat, int interval) {
        byte[] activity = new byte[ACTIVITY_BYTE_LENGTH];
        activity[0] = (byte) playtime;
        activity[1] = (byte) 0;
        activity[2] = Byte.MIN_VALUE;
        byte[] intervalActivity = new byte[ACTIVITY_BYTE_LENGTH];
        intervalActivity[0] = (byte) interval;
        intervalActivity[1] = (byte) 0;
        intervalActivity[2] = (byte) 0;
        activity[0] = (byte) (activity[0] | playtime);
        activity[1] = (byte) (activity[1] | (g << 4));
        activity[1] = (byte) (activity[1] | r);
        activity[2] = (byte) (activity[2] | b);
        activity[2] = (byte) (activity[2] | (vibrationStrength << 4));
        byte[] pattern = new byte[(((repeat + 1) * 6) + 4)];
        int i = 0 + 1;
        pattern[0] = (byte) (pattern[0] | 0);
        int i2 = i + 1;
        pattern[i] = (byte) (pattern[i] | 0);
        i = i2 + 1;
        pattern[i2] = (byte) (pattern[i2] | 0);
        i2 = i + 1;
        pattern[i] = (byte) (pattern[i] | (((byte) (repeat + 1)) * 2));
        int count = 0;
        while (count < repeat + 1) {
            int activityByteCount = 0;
            i = i2;
            while (activityByteCount < ACTIVITY_BYTE_LENGTH) {
                i2 = i + 1;
                pattern[i] = activity[activityByteCount];
                activityByteCount++;
                i = i2;
            }
            int intervalByteCount = 0;
            while (intervalByteCount < ACTIVITY_BYTE_LENGTH) {
                i2 = i + 1;
                pattern[i] = intervalActivity[intervalByteCount];
                intervalByteCount++;
                i = i2;
            }
            count++;
            i2 = i;
        }
        return pattern;
    }

    public static byte[] getMorseGame1() {
        return new byte[]{(byte) -56, (byte) -2, (byte) -2, (byte) 11, (byte) 6, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 2, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 2, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 6, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 2, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 2, (byte) 15, (byte) 112};
    }

    public static byte[] getMorseGame2() {
        return new byte[]{(byte) -56, (byte) -2, (byte) -2, (byte) 13, (byte) 6, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 6, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 2, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 2, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 6, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 2, (byte) 15, (byte) 112, (byte) 7, (byte) 0, (byte) 0, (byte) 2, (byte) 15, (byte) 112};
    }

    public static byte[] getCaptureSucceed() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 24, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 2, (byte) 0, (byte) -1, (byte) 1, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 2, (byte) 0, (byte) -1, (byte) 1, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 2, (byte) 0, (byte) -1, (byte) 1, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 2, (byte) 0, (byte) -1, (byte) 1, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 2, (byte) 0, (byte) -1, (byte) 1, (byte) 0, (byte) -113, (byte) 3, (byte) 8, (byte) -16, (byte) 3, (byte) -16, (byte) -16, (byte) 2, (byte) 0, (byte) -1, (byte) 1, (byte) 0, (byte) -113};
    }

    public static byte[] getIncorrectMorseGameEffect() {
        return new byte[]{(byte) 0, (byte) 2, (byte) 0, (byte) 3, (byte) 2, (byte) 8, (byte) -16, (byte) 4, (byte) 0, (byte) 0, (byte) 10, (byte) 8, (byte) -16};
    }

    public static byte[] getNotify() {
        return new byte[]{(byte) -56, (byte) -2, (byte) -2, (byte) 1, (byte) 2, (byte) 8, (byte) -16};
    }

    public static byte[] getBlinkRed() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 13, (byte) 1, (byte) 15, (byte) 112, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 15, (byte) 112, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 15, (byte) 112, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 15, (byte) 112, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 15, (byte) 112, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 15, (byte) 112, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 15, (byte) 112};
    }

    public static byte[] getUpdateRequest() {
        return new byte[]{(byte) 1};
    }

    public static byte[] getSecurityResponseForDebug() {
        return SfidaUtils.hexStringToByteArray("0400000000000000000000000000000000000000");
    }

    public static byte[] getSecurityResponseForDebug2() {
        return SfidaUtils.hexStringToByteArray("050000000000000000000000000000000000000000000000000000000000000000000000");
    }

    public static byte[] getSecurityResponseForDebug3() {
        return SfidaUtils.hexStringToByteArray("0300000001");
    }
}
