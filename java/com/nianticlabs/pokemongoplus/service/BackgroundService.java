package com.nianticlabs.pokemongoplus.service;

import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.nianticlabs.pokemongoplus.bridge.BackgroundBridge;
import com.nianticlabs.pokemongoplus.bridge.ClientBridge;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.Random;
import spacemadness.com.lunarconsole.R;

public class BackgroundService extends Service {
    public static int PROCESS_LOCAL_VALUE = new Random().nextInt();
    private static final String TAG = BackgroundService.class.getSimpleName();
    private Handler handler;
    private HandlerThread handlerThread;
    private BackgroundBridge pgpBackgroundBridge = null;
    private BroadcastReceiver sfidaReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, final Intent intent) {
            BackgroundService.this.handler.post(new Runnable() {
                public void run() {
                    AnonymousClass3.this.onHandleBroadcastIntent(intent);
                }
            });
        }

        void onHandleBroadcastIntent(Intent intent) {
            String action = intent.getStringExtra("action");
            Object obj = -1;
            switch (action.hashCode()) {
                case -2118782451:
                    if (action.equals("stopNotification")) {
                        obj = 1;
                        break;
                    }
                    break;
                case -1708606089:
                    if (action.equals("batteryLevel")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 769171603:
                    if (action.equals("sendNotification")) {
                        obj = null;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case R.styleable.AdsAttrs_adSize /*0*/:
                    String message = intent.getStringExtra(Keys.MESSAGE);
                    if (message != null) {
                        BackgroundService.this.createPlayerNotification(message);
                        return;
                    }
                    return;
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    BackgroundService.this.stopPlayerNotification();
                    return;
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                    BackgroundService.this.updateBatteryLevel(intent.getDoubleExtra("level", 0.0d));
                    return;
                default:
                    return;
            }
        }
    };

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "BackgroundService onCreate() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        this.handlerThread = new HandlerThread("BackgroundService");
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper());
        IntentFilter filter = new IntentFilter();
        filter.addAction(ClientBridge.CLIENT_BROADCAST_DOMAIN);
        registerReceiver(this.sfidaReceiver, filter);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "BackgroundService onDestroy IF NOT REALLY DONE WITH SERVICE, THIS IS A BIG PROBLEM! PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        unregisterReceiver(this.sfidaReceiver);
        this.handlerThread.quitSafely();
        this.handler = null;
        this.handlerThread = null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i(TAG, "BackgroundService onStartCommand() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        this.handler.post(new Runnable() {
            public void run() {
                BackgroundService.this.onHandleIntent(intent);
            }
        });
        return 1;
    }

    public void initBackgroundBridge() {
        Log.i(TAG, "BackgroundService onCreate PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        this.pgpBackgroundBridge = BackgroundBridge.createBridge(this);
        createPlayerNotification("Background bridge initialized");
    }

    private static void sendClientIntent(Context context, String serviceAction) {
        Intent i = new Intent(context, ClientService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", serviceAction);
        context.startService(i);
    }

    public void shutdownBackgroundBridge() {
        Log.i(TAG, "BackgroundService shutdownBackgroundBridge() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        createPlayerNotification("Background bridge shutting down");
        sendClientIntent(this, "confirmBridgeShutdown");
        if (this.pgpBackgroundBridge != null) {
            Log.i(TAG, "BackgroundService destroy the bridge ");
            this.pgpBackgroundBridge.destroyBridge();
            this.pgpBackgroundBridge = null;
        }
        Intent i = new Intent(this, BackgroundService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", "finishShutdown");
        startService(i);
    }

    public void finishShutdownBackgroundBridge() {
        this.handler.post(new Runnable() {
            public void run() {
                Log.i(BackgroundService.TAG, "BackgroundService stopSelf ");
                BackgroundService.this.stopSelf();
            }
        });
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            Log.i(TAG, "BackgroundService onHandleIntent (intent == null)");
            return;
        }
        String action = intent.getStringExtra("action");
        if (action == null) {
            Log.i(TAG, "BackgroundService onHandleIntent (action == null)");
            Log.e(TAG, "Missing action  PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
            return;
        }
        Object obj = -1;
        switch (action.hashCode()) {
            case 617329673:
                if (action.equals("finishShutdown")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                finishShutdownBackgroundBridge();
                return;
            default:
                onHandleBridgedIntent(action, intent);
                return;
        }
    }

    protected void onHandleBridgedIntent(String action, Intent intent) {
        Log.i(TAG, action + " :: " + PROCESS_LOCAL_VALUE);
        if (this.pgpBackgroundBridge == null && !action.equals("stop")) {
            Log.i(TAG, "BackgroundService onHandleBridgedIntent (pgpBackgroundBridge == null)");
            initBackgroundBridge();
            if (!action.equals("start")) {
                Log.e(TAG, "Background servic iunintialized when received \"" + action + "\", PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
                action = "start";
            }
        }
        Log.i(TAG, "BackgroundService onHandleBridgedIntent action = " + action);
        Object obj = -1;
        switch (action.hashCode()) {
            case -2062998829:
                if (action.equals("stopScanning")) {
                    obj = 5;
                    break;
                }
                break;
            case -934426579:
                if (action.equals("resume")) {
                    obj = 1;
                    break;
                }
                break;
            case -762609869:
                if (action.equals("startScanning")) {
                    obj = 4;
                    break;
                }
                break;
            case 3540994:
                if (action.equals("stop")) {
                    obj = 3;
                    break;
                }
                break;
            case 106440182:
                if (action.equals("pause")) {
                    obj = 2;
                    break;
                }
                break;
            case 109757538:
                if (action.equals("start")) {
                    obj = null;
                    break;
                }
                break;
            case 700214324:
                if (action.equals("stopSession")) {
                    obj = 7;
                    break;
                }
                break;
            case 1850541012:
                if (action.equals("startSession")) {
                    obj = 6;
                    break;
                }
                break;
        }
        switch (obj) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                this.pgpBackgroundBridge.start();
                return;
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                this.pgpBackgroundBridge.resume();
                return;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                this.pgpBackgroundBridge.pause();
                return;
            case 3:
                if (this.pgpBackgroundBridge != null) {
                    this.pgpBackgroundBridge.stop();
                }
                shutdownBackgroundBridge();
                return;
            case 4:
                this.pgpBackgroundBridge.startScanning();
                return;
            case 5:
                this.pgpBackgroundBridge.stopScanning();
                return;
            case 6:
                handleStartSession(intent);
                return;
            case 7:
                this.pgpBackgroundBridge.stopSession();
                return;
            default:
                Log.e(TAG, "Can't handle intent message: " + action);
                return;
        }
    }

    private void handleStartSession(Intent intent) {
        String hostPort = intent.getStringExtra("hostPort");
        String device = intent.getStringExtra("device");
        byte[] authToken = intent.getByteArrayExtra("authToken");
        long pokemonId = intent.getLongExtra("pokemonId", 0);
        Log.i(TAG, String.format("Start session: %s %s %d", new Object[]{hostPort, device, Long.valueOf(pokemonId)}));
        this.pgpBackgroundBridge.startSession(hostPort, device, authToken, pokemonId);
    }

    private void stopPlayerNotification() {
        stopForeground(true);
    }

    private void updateBatteryLevel(double batteryLevel) {
    }

    private void createPlayerNotification(String message) {
        Log.i(TAG, "BackgroundService createPlayerNotification message = " + message);
        Class<?> mainContextClass = GetLauncherActivity(this);
        Intent notificationIntent = new Intent(this, mainContextClass);
        notificationIntent.setFlags(67108864);
        Builder builder = new Builder(this).setSmallIcon(com.nianticlabs.pokemongoplus.R.drawable.ic_swap_horiz_white_24dp).setLargeIcon(BitmapFactory.decodeResource(getResources(), com.nianticlabs.pokemongoplus.R.drawable.sfida_icon)).setContentTitle("Pok\u00e9mon GO Plus").setContentText(message);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(mainContextClass);
        stackBuilder.addNextIntent(notificationIntent);
        builder.setContentIntent(stackBuilder.getPendingIntent(1516, 134217728));
        startForeground(1515, builder.build());
    }

    public static Class<?> GetLauncherActivity(Context context) {
        String className = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Launcher class not found: " + className);
            return null;
        }
    }
}
