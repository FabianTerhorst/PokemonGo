package com.nianticlabs.pokemongoplus.bridge;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.nianticlabs.pokemongoplus.service.BackgroundService;

public class ClientBridge {
    public static final String CLIENT_BROADCAST_DOMAIN = "com.nianticlabs.pokemongoplus.service.ToClient";
    private static final String TAG = ClientBridge.class.getSimpleName();
    public static Context currentContext;
    LoginDelegate loginDelegate;
    private long nativeHandle;
    SfidaRegisterDelegate sfidaRegisterDelegate;

    public interface LoginDelegate {
        void onLogin(boolean z);
    }

    public interface SfidaRegisterDelegate {
        void onSfidaRegistered(boolean z, String str);
    }

    private native void initialize();

    public static native void nativeInit();

    public native void connectDevice(String str);

    public native void disconnectDevice();

    public native void dispose();

    public native void login();

    public native void logout();

    public native void pausePlugin();

    public native void registerDevice(String str);

    public native void resumePlugin();

    public native void sendBatteryLevel(double d);

    public native void sendCentralState(int i);

    public native void sendEncounterId(long j);

    public native void sendPluginState(int i);

    public native void sendPokestopId(String str);

    public native void sendScannedSfida(String str, int i);

    public native void sendSfidaState(int i);

    public native void sendUpdateTimestamp(long j);

    public native void standaloneInit(long j);

    public native void standaloneUpdate();

    public native void startPlugin();

    public native void startScanning();

    public native void stopPlugin();

    public native void stopScanning();

    protected ClientBridge() {
        initialize();
        Log.w(TAG, "Initialize();");
    }

    public static ClientBridge createBridge(Context context) {
        currentContext = context;
        Log.w(TAG, ClientBridge.class.toString());
        nativeInit();
        ClientBridge clientBridge = new ClientBridge();
        Log.w(TAG, "new ClientBridge");
        return clientBridge;
    }

    private static Intent createIntentWithAction(String action) {
        Intent i = new Intent(currentContext, BackgroundService.class);
        i.setPackage("com.nianticlabs.holoholo.dev");
        i.putExtra("action", action);
        return i;
    }

    public static void initBackgroundBridge() {
        Log.i(TAG, "Init background bridge PROCESS_LOCAL_VALUE = " + BackgroundService.PROCESS_LOCAL_VALUE);
        Intent i = new Intent(currentContext, BackgroundService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", "init");
        currentContext.startService(i);
    }

    public static void shutdownBackgroundBridge() {
        Log.i(TAG, "shutdown background bridge PROCESS_LOCAL_VALUE = " + BackgroundService.PROCESS_LOCAL_VALUE);
        Intent i = new Intent(currentContext, BackgroundService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", "shutdown");
        currentContext.startService(i);
    }

    public static void sendStart() {
        Log.i(TAG, "sendStart PROCESS_LOCAL_VALUE = " + BackgroundService.PROCESS_LOCAL_VALUE);
        Intent i = new Intent(currentContext, BackgroundService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", "start");
        currentContext.startService(i);
    }

    public static void sendResume() {
        Intent i = createIntentWithAction("resume");
        Log.i(TAG, "send resume intent");
        currentContext.startService(i);
    }

    public static void sendPause() {
        Intent i = createIntentWithAction("pause");
        Log.i(TAG, "send pause intent");
        currentContext.startService(i);
    }

    public static void sendStop() {
        Intent i = createIntentWithAction("stop");
        Log.i(TAG, "send stop intent");
        currentContext.startService(i);
    }

    public static void sendStartScanning() {
        Intent i = createIntentWithAction("startScanning");
        Log.i(TAG, "send startScanning intent");
        currentContext.startService(i);
    }

    public static void sendStopScanning() {
        Intent i = createIntentWithAction("stopScanning");
        Log.i(TAG, "send stopScanning intent");
        currentContext.startService(i);
    }

    public static void sendStartSession(String hostPort, String device, byte[] authToken, long pokemonId) {
        Intent i = createIntentWithAction("startSession");
        i.putExtra("hostPort", hostPort);
        i.putExtra("device", device);
        i.putExtra("authToken", authToken);
        i.putExtra("pokemonId", pokemonId);
        Log.i(TAG, String.format("send startSession intent %s %s %s %d", new Object[]{hostPort, device, authToken, Long.valueOf(pokemonId)}));
        currentContext.startService(i);
    }

    public static void sendStopSession() {
        Intent i = createIntentWithAction("stopSession");
        Log.i(TAG, "send stopSession intent");
        currentContext.startService(i);
    }

    public void standaloneLogin(LoginDelegate delegate) {
        this.loginDelegate = delegate;
        login();
    }

    public void standaloneSfidaRegister(String device, SfidaRegisterDelegate delegate) {
        this.sfidaRegisterDelegate = delegate;
        registerDevice(device);
    }

    public void onLogin(boolean success) {
        if (this.loginDelegate != null) {
            this.loginDelegate.onLogin(success);
            this.loginDelegate = null;
        }
    }

    public void onSfidaRegistered(boolean success, String device) {
        if (this.sfidaRegisterDelegate != null) {
            this.sfidaRegisterDelegate.onSfidaRegistered(success, device);
            this.sfidaRegisterDelegate = null;
        }
    }
}
