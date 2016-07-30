package com.nianticlabs.pokemongoplus.bridge;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;

public class BackgroundBridge {
    public static final String BACKGROUND_BROADCAST_DOMAIN = "com.nianticlabs.pokemongoplus.service.ToServer";
    private static final String TAG = BackgroundBridge.class.getSimpleName();
    public static Context currentContext;
    private long nativeHandle;

    private native void initialize();

    public static native void nativeInit();

    public native void dispose();

    public native void pause();

    public native void resume();

    public native void start();

    public native void startScanning();

    public native void startSession(String str, String str2, byte[] bArr, long j);

    public native void stop();

    public native void stopScanning();

    public native void stopSession();

    static {
        System.loadLibrary("pgpplugin");
    }

    protected BackgroundBridge() {
        initialize();
        Log.w(TAG, "Initialize();");
    }

    public static BackgroundBridge createBridge(Context context) {
        currentContext = context;
        Log.w(TAG, BackgroundBridge.class.toString());
        nativeInit();
        Log.w(TAG, "BackgroundBridge createBridge");
        BackgroundBridge pgpwrap = new BackgroundBridge();
        Log.w(TAG, "new BackgroundBridge");
        return pgpwrap;
    }

    public void destroyBridge() {
        dispose();
    }

    private static Intent createIntentWithAction(String action) {
        Intent i = new Intent(ClientBridge.CLIENT_BROADCAST_DOMAIN);
        i.putExtra("action", action);
        Log.i(TAG, "createIntentWithAction: " + action);
        return i;
    }

    public static void sendUpdateTimestamp(long timestampMs) {
        Intent i = createIntentWithAction("updateTimestamp");
        i.putExtra(GameServices.USER_TIME_STAMP, timestampMs);
        currentContext.sendBroadcast(i);
    }

    public static void sendSfidaState(int state) {
        Intent i = createIntentWithAction("sfidaState");
        i.putExtra(GameServices.STATE, state);
        Log.i(TAG, "sfidaState: " + state);
        currentContext.sendBroadcast(i);
    }

    public static void sendEncounterId(long encounterId) {
        Intent i = createIntentWithAction("encounterId");
        i.putExtra(TriggerIfContentAvailable.ID, encounterId);
        Log.i(TAG, "sendEncounterId: " + encounterId);
        currentContext.sendBroadcast(i);
    }

    public static void sendPokestopId(String pokestop) {
        Intent i = createIntentWithAction("pokestop");
        i.putExtra(TriggerIfContentAvailable.ID, pokestop);
        Log.i(TAG, "sendPokestopId: " + pokestop);
        currentContext.sendBroadcast(i);
    }

    public static void sendCentralState(int state) {
        Intent i = createIntentWithAction("centralState");
        i.putExtra(GameServices.STATE, state);
        Log.i(TAG, "centralState: " + state);
        currentContext.sendBroadcast(i);
    }

    public static void sendScannedSfida(String deviceId, int buttonValue) {
        Intent i = createIntentWithAction("scannedSfida");
        i.putExtra("device", deviceId);
        i.putExtra("button", buttonValue);
        Log.i(TAG, "sendScannedSfida: " + deviceId);
        currentContext.sendBroadcast(i);
    }

    public static void sendPluginState(int state) {
        Intent i = createIntentWithAction("pluginState");
        i.putExtra(GameServices.STATE, state);
        Log.i(TAG, "sendPluginState: " + state);
        currentContext.sendBroadcast(i);
    }

    public static void sendBatteryLevel(double batteryLevel) {
        Intent i = createIntentWithAction("batteryLevel");
        i.putExtra("level", batteryLevel);
        Log.i(TAG, "sendBatteryLevel: " + batteryLevel);
        currentContext.sendBroadcast(i);
    }

    public static void sendNotification(String message) {
        Intent i = createIntentWithAction("sendNotification");
        i.putExtra(Keys.MESSAGE, message);
        Log.i(TAG, "sendNotification: " + message);
        currentContext.sendBroadcast(i);
    }

    public static void stopNotification() {
        Intent i = createIntentWithAction("stopNotification");
        Log.i(TAG, "stopNotification");
        currentContext.sendBroadcast(i);
    }
}
