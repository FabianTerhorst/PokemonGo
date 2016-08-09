package com.nianticlabs.pokemongoplus.service;

import android.app.PendingIntent;
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
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.util.Log;
import com.nianticlabs.pokemongoplus.R;
import com.nianticlabs.pokemongoplus.bridge.BackgroundBridge;
import com.nianticlabs.pokemongoplus.bridge.BridgeConstants;
import com.nianticlabs.pokemongoplus.bridge.BridgeConstants.PgpState;
import com.nianticlabs.pokemongoplus.bridge.BridgeConstants.SfidaState;
import com.nianticlabs.pokemongoplus.bridge.ClientBridge;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import spacemadness.com.lunarconsole.BuildConfig;

public class BackgroundService extends Service {
    public static int PROCESS_LOCAL_VALUE = new Random().nextInt();
    private static final String TAG = BackgroundService.class.getSimpleName();
    private static final int kCapturedPokemon = 1;
    private static final int kEmptyMessage = 0;
    private static final int kItemInventoryFull = 9;
    private static final int kOutOfPokeballs = 7;
    private static final int kPokemonEscaped = 2;
    private static final int kPokemonInventoryFull = 8;
    private static final int kPokestopCooldown = 6;
    private static final int kPokestopOutOfRange = 5;
    private static final int kRetrievedItems = 4;
    private static final int kRetrievedOneItem = 3;
    private static final int kSessionEnded = 12;
    private static final int kTrackedPokemonFound = 10;
    private static final int kTrackedPokemonLost = 11;
    private static final Map<Integer, Integer> notificationMap = new HashMap();
    private static boolean serviceStopped = false;
    private double batteryLevel;
    private Handler handler;
    private HandlerThread handlerThread;
    private boolean isScanning;
    private BackgroundBridge pgpBackgroundBridge = null;
    private PgpState pluginState;
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
            int i = -1;
            switch (action.hashCode()) {
                case -2118782451:
                    if (action.equals(BridgeConstants.STOP_NOTIFICATION_ACTION)) {
                        i = BackgroundService.kCapturedPokemon;
                        break;
                    }
                    break;
                case -1708606089:
                    if (action.equals(BridgeConstants.BATTERY_LEVEL_ACTION)) {
                        i = BackgroundService.kPokemonEscaped;
                        break;
                    }
                    break;
                case -1076199202:
                    if (action.equals(BridgeConstants.SFIDA_STATE_ACTION)) {
                        i = BackgroundService.kRetrievedOneItem;
                        break;
                    }
                    break;
                case 769171603:
                    if (action.equals(BridgeConstants.SEND_NOTIFICATION_ACTION)) {
                        i = BackgroundService.kEmptyMessage;
                        break;
                    }
                    break;
                case 886369566:
                    if (action.equals(BridgeConstants.PLUGIN_STATE_ACTION)) {
                        i = BackgroundService.kRetrievedItems;
                        break;
                    }
                    break;
            }
            switch (i) {
                case BackgroundService.kEmptyMessage /*0*/:
                    BackgroundService.this.createPlayerNotification(BackgroundService.this.formatNotification(intent.getIntExtra(Keys.MESSAGE, BackgroundService.kEmptyMessage), intent.getStringExtra("arg")));
                    return;
                case BackgroundService.kCapturedPokemon /*1*/:
                    BackgroundService.this.stopPlayerNotification();
                    return;
                case BackgroundService.kPokemonEscaped /*2*/:
                    BackgroundService.this.updateBatteryLevel(intent.getDoubleExtra("level", 0.0d));
                    return;
                case BackgroundService.kRetrievedOneItem /*3*/:
                    SfidaState newSfidaState = SfidaState.fromInt(intent.getIntExtra(GameServices.STATE, BackgroundService.kEmptyMessage));
                    BackgroundService.this.updateNotificationForSfidaState(newSfidaState, BackgroundService.this.sfidaState);
                    BackgroundService.this.sfidaState = newSfidaState;
                    if (BackgroundService.this.shuttingDown) {
                        BackgroundService.this.continueStopService();
                        return;
                    }
                    return;
                case BackgroundService.kRetrievedItems /*4*/:
                    BackgroundService.this.pluginState = PgpState.fromInt(intent.getIntExtra(GameServices.STATE, BackgroundService.kEmptyMessage));
                    if (BackgroundService.this.shuttingDown) {
                        BackgroundService.this.continueStopService();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private SfidaState sfidaState;
    private boolean shuttingDown;

    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState = new int[PgpState.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$SfidaState = new int[SfidaState.values().length];

        static {
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$SfidaState[SfidaState.Disconnecting.ordinal()] = BackgroundService.kCapturedPokemon;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$SfidaState[SfidaState.Disconnected.ordinal()] = BackgroundService.kPokemonEscaped;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$SfidaState[SfidaState.Connected.ordinal()] = BackgroundService.kRetrievedOneItem;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$SfidaState[SfidaState.Certified.ordinal()] = BackgroundService.kRetrievedItems;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState[PgpState.Started.ordinal()] = BackgroundService.kCapturedPokemon;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState[PgpState.Resumed.ordinal()] = BackgroundService.kPokemonEscaped;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState[PgpState.Initialized.ordinal()] = BackgroundService.kRetrievedOneItem;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState[PgpState.Paused.ordinal()] = BackgroundService.kRetrievedItems;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState[PgpState.Stopped.ordinal()] = BackgroundService.kPokestopOutOfRange;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState[PgpState.BadValue.ordinal()] = BackgroundService.kPokestopCooldown;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState[PgpState.Unknown.ordinal()] = BackgroundService.kOutOfPokeballs;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    static {
        notificationMap.put(Integer.valueOf(kCapturedPokemon), Integer.valueOf(R.string.Captured_Pokemon));
        notificationMap.put(Integer.valueOf(kPokemonEscaped), Integer.valueOf(R.string.Pokemon_Escaped));
        notificationMap.put(Integer.valueOf(kRetrievedOneItem), Integer.valueOf(R.string.Retrieved_an_Item));
        notificationMap.put(Integer.valueOf(kRetrievedItems), Integer.valueOf(R.string.Retrieved_Items));
        notificationMap.put(Integer.valueOf(kPokestopOutOfRange), Integer.valueOf(R.string.Pokestop_Out_Of_Range));
        notificationMap.put(Integer.valueOf(kPokestopCooldown), Integer.valueOf(R.string.Pokestop_Cooldown));
        notificationMap.put(Integer.valueOf(kOutOfPokeballs), Integer.valueOf(R.string.Out_Of_Pokeballs));
        notificationMap.put(Integer.valueOf(kPokemonInventoryFull), Integer.valueOf(R.string.Pokemon_Inventory_Full));
        notificationMap.put(Integer.valueOf(kItemInventoryFull), Integer.valueOf(R.string.Item_Inventory_Full));
        notificationMap.put(Integer.valueOf(kTrackedPokemonFound), Integer.valueOf(R.string.Tracked_Pokemon_Found));
        notificationMap.put(Integer.valueOf(kTrackedPokemonLost), Integer.valueOf(R.string.Tracked_Pokemon_Lost));
        notificationMap.put(Integer.valueOf(kSessionEnded), Integer.valueOf(R.string.Session_Ended));
    }

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
        Log.i(TAG, "BackgroundService onDestroy PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        shutdownBackgroundBridge();
        unregisterReceiver(this.sfidaReceiver);
        this.handlerThread.quitSafely();
        this.handler = null;
        this.handlerThread = null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i(TAG, "BackgroundService onStartCommand() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        String str;
        Object[] objArr;
        if (intent != null) {
            String action = intent.getStringExtra("action");
            if (serviceStopped) {
                if (action == null || !action.equals(BridgeConstants.START_ACTION)) {
                    String str2 = TAG;
                    Object[] objArr2 = new Object[kCapturedPokemon];
                    objArr2[kEmptyMessage] = action;
                    Log.w(str2, String.format("Ignoring \"%s\" intent because stopped", objArr2));
                    return kPokemonEscaped;
                }
                serviceStopped = false;
            }
            if (!this.shuttingDown || BridgeConstants.FINISH_SHUTDOWN_ACTION.equals(action)) {
                this.handler.post(new Runnable() {
                    public void run() {
                        BackgroundService.this.onHandleIntent(intent);
                    }
                });
                return kCapturedPokemon;
            }
            str = TAG;
            objArr = new Object[kCapturedPokemon];
            objArr[kEmptyMessage] = action;
            Log.w(str, String.format("Ignoring \"%s\" intent because shutting down", objArr));
            return kCapturedPokemon;
        } else if (this.pluginState == null || this.pluginState == PgpState.Unknown) {
            stopPlayerNotification();
            return kPokemonEscaped;
        } else {
            str = TAG;
            objArr = new Object[kCapturedPokemon];
            objArr[kEmptyMessage] = this.pluginState.toString();
            Log.e(str, String.format("Null intent but valid state: %s", objArr));
            return kCapturedPokemon;
        }
    }

    public void initBackgroundBridge() {
        Log.i(TAG, "BackgroundService onCreate PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        this.pgpBackgroundBridge = BackgroundBridge.createBridge(this);
    }

    private static void sendClientIntent(Context context, String serviceAction) {
        Intent i = new Intent(context, ClientService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", serviceAction);
        context.startService(i);
    }

    public void shutdownBackgroundBridge() {
        Log.i(TAG, "BackgroundService shutdownBackgroundBridge() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        sendClientIntent(this, BridgeConstants.CONFIRM_BRIDGE_SHUTDOWN_ACTION);
        if (this.pgpBackgroundBridge != null) {
            Log.i(TAG, "BackgroundService destroy the bridge ");
            this.pgpBackgroundBridge.destroyBridge();
            this.pgpBackgroundBridge = null;
        }
        Log.i(TAG, "DONE BackgroundService shutdownBackgroundBridge() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        Intent i = new Intent(this, BackgroundService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", BridgeConstants.FINISH_SHUTDOWN_ACTION);
        startService(i);
    }

    public void finishShutdownBackgroundBridge() {
        this.handler.post(new Runnable() {
            public void run() {
                Log.i(BackgroundService.TAG, "BackgroundService stopSelf ");
                BackgroundService.this.shuttingDown = false;
                BackgroundService.serviceStopped = true;
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
                if (action.equals(BridgeConstants.FINISH_SHUTDOWN_ACTION)) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case kEmptyMessage /*0*/:
                finishShutdownBackgroundBridge();
                return;
            default:
                onHandleBridgedIntent(action, intent);
                return;
        }
    }

    protected void onHandleBridgedIntent(String action, Intent intent) {
        Log.i(TAG, action + " :: " + PROCESS_LOCAL_VALUE);
        if (this.pgpBackgroundBridge == null && !action.equals(BridgeConstants.STOP_ACTION)) {
            Log.i(TAG, "BackgroundService onHandleBridgedIntent (pgpBackgroundBridge == null)");
            initBackgroundBridge();
            if (!action.equals(BridgeConstants.START_ACTION)) {
                Log.e(TAG, "Background servic iunintialized when received \"" + action + "\", PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
                action = BridgeConstants.START_ACTION;
            }
        }
        Log.i(TAG, "BackgroundService onHandleBridgedIntent action = " + action);
        boolean z = true;
        switch (action.hashCode()) {
            case -2062998829:
                if (action.equals(BridgeConstants.STOP_SCANNING_ACTION)) {
                    z = true;
                    break;
                }
                break;
            case -934426579:
                if (action.equals(BridgeConstants.RESUME_ACTION)) {
                    z = true;
                    break;
                }
                break;
            case -762609869:
                if (action.equals(BridgeConstants.START_SCANNING_ACTION)) {
                    z = true;
                    break;
                }
                break;
            case 3540994:
                if (action.equals(BridgeConstants.STOP_ACTION)) {
                    z = true;
                    break;
                }
                break;
            case 106440182:
                if (action.equals(BridgeConstants.PAUSE_ACTION)) {
                    z = true;
                    break;
                }
                break;
            case 109757538:
                if (action.equals(BridgeConstants.START_ACTION)) {
                    z = false;
                    break;
                }
                break;
            case 699379795:
                if (action.equals(BridgeConstants.STOP_SERVICE_ACTION)) {
                    z = true;
                    break;
                }
                break;
            case 700214324:
                if (action.equals(BridgeConstants.STOP_SESSION_ACTION)) {
                    z = true;
                    break;
                }
                break;
            case 1850541012:
                if (action.equals(BridgeConstants.START_SESSION_ACTION)) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case kEmptyMessage /*0*/:
                this.pgpBackgroundBridge.start();
                return;
            case kCapturedPokemon /*1*/:
                this.pgpBackgroundBridge.resume();
                return;
            case kPokemonEscaped /*2*/:
                this.pgpBackgroundBridge.pause();
                return;
            case kRetrievedOneItem /*3*/:
                if (this.pgpBackgroundBridge != null) {
                    this.pgpBackgroundBridge.stop();
                }
                shutdownBackgroundBridge();
                return;
            case kRetrievedItems /*4*/:
                this.isScanning = true;
                this.pgpBackgroundBridge.startScanning();
                return;
            case kPokestopOutOfRange /*5*/:
                this.isScanning = false;
                this.pgpBackgroundBridge.stopScanning();
                return;
            case kPokestopCooldown /*6*/:
                handleStartSession(intent);
                return;
            case kOutOfPokeballs /*7*/:
                this.pgpBackgroundBridge.stopSession();
                return;
            case kPokemonInventoryFull /*8*/:
                stopService();
                return;
            default:
                Log.e(TAG, "Can't handle intent message: " + action);
                return;
        }
    }

    private void stopService() {
        this.shuttingDown = true;
        continueStopService();
    }

    private void finishStopService() {
        shutdownBackgroundBridge();
    }

    private void forceStopService() {
        stopPlayerNotification();
        stopSelf();
    }

    private void continueStopService() {
        if (this.sfidaState != SfidaState.Disconnecting) {
            if (this.sfidaState == SfidaState.Connected || this.sfidaState == SfidaState.Certified) {
                this.pgpBackgroundBridge.stopSession();
            } else if (this.pgpBackgroundBridge != null) {
                switch (AnonymousClass4.$SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$PgpState[this.pluginState.ordinal()]) {
                    case kCapturedPokemon /*1*/:
                    case kPokemonEscaped /*2*/:
                        this.pgpBackgroundBridge.pause();
                        return;
                    case kRetrievedOneItem /*3*/:
                    case kRetrievedItems /*4*/:
                        this.shuttingDown = false;
                        return;
                    case kPokestopOutOfRange /*5*/:
                        finishStopService();
                        return;
                    case kPokestopCooldown /*6*/:
                    case kOutOfPokeballs /*7*/:
                        forceStopService();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private void handleStartSession(Intent intent) {
        String hostPort = intent.getStringExtra("hostPort");
        String device = intent.getStringExtra("device");
        byte[] authToken = intent.getByteArrayExtra("authToken");
        long pokemonId = intent.getLongExtra("pokemonId", 0);
        String message = getResources().getString(R.string.Connecting_GO_Plus);
        String str = TAG;
        Object[] objArr = new Object[kRetrievedItems];
        objArr[kEmptyMessage] = hostPort;
        objArr[kCapturedPokemon] = device;
        objArr[kPokemonEscaped] = Long.valueOf(pokemonId);
        objArr[kRetrievedOneItem] = message;
        Log.i(str, String.format("Start session: %s %s %d \"%s\"", objArr));
        createPlayerNotification(message);
        this.pgpBackgroundBridge.startSession(hostPort, device, authToken, pokemonId);
    }

    private void stopPlayerNotification() {
        Log.i(TAG, String.format("stopping notification", new Object[kEmptyMessage]));
        stopForeground(true);
    }

    private void updateBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    private String formatNotification(int message, String arg) {
        Integer tag = (Integer) notificationMap.get(Integer.valueOf(message));
        if (tag == null) {
            return BuildConfig.FLAVOR;
        }
        String format = getResources().getString(tag.intValue());
        Object[] objArr = new Object[kCapturedPokemon];
        objArr[kEmptyMessage] = arg;
        return String.format(format, objArr);
    }

    private void createPlayerNotification(String message) {
        Log.i(TAG, "BackgroundService createPlayerNotification message = " + message);
        Class<?> mainContextClass = GetLauncherActivity(this);
        Intent stopSelf = new Intent(this, BackgroundService.class);
        stopSelf.setAction(BridgeConstants.STOP_SERVICE_ACTION);
        stopSelf.putExtra("action", BridgeConstants.STOP_SERVICE_ACTION);
        PendingIntent pendingStopSelf = PendingIntent.getService(this, kEmptyMessage, stopSelf, 268435456);
        Intent notificationIntent = new Intent(this, mainContextClass);
        notificationIntent.setFlags(67108864);
        Builder builder = new Builder(this).setSmallIcon(R.drawable.ic_swap_horiz_white_24dp).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sfida_icon)).setContentTitle(getResources().getString(R.string.Pokemon_Go_Plus)).setContentText(message).setVisibility(kCapturedPokemon).addAction(R.drawable.ic_media_pause, "Stop", pendingStopSelf);
        InboxStyle inboxStyle = new InboxStyle();
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

    private void updateNotificationForSfidaState(SfidaState newSfidaState, SfidaState sfidaState) {
        Log.e(TAG, "New state: " + newSfidaState.toString());
        if (newSfidaState != sfidaState) {
            switch (AnonymousClass4.$SwitchMap$com$nianticlabs$pokemongoplus$bridge$BridgeConstants$SfidaState[newSfidaState.ordinal()]) {
                case kCapturedPokemon /*1*/:
                    createPlayerNotification(getResources().getString(R.string.Disconnecting_GO_Plus));
                    return;
                case kPokemonEscaped /*2*/:
                    if (sfidaState == SfidaState.Disconnecting) {
                        stopPlayerNotification();
                        return;
                    }
                    return;
                case kRetrievedOneItem /*3*/:
                case kRetrievedItems /*4*/:
                    createPlayerNotification(BuildConfig.FLAVOR);
                    return;
                default:
                    return;
            }
        }
    }
}
