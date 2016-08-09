package com.nianticlabs.pokemongoplus.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.nianticlabs.pokemongoplus.ble.BluetoothGattSupport;
import com.nianticlabs.pokemongoplus.bridge.BridgeConstants;
import com.nianticlabs.pokemongoplus.bridge.ClientBridge;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import spacemadness.com.lunarconsole.R;

public class ClientService extends Service {
    private static final String TAG = ClientService.class.getSimpleName();
    static ClientBridge pgpClientBridge = null;
    private BroadcastReceiver sfidaReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ClientService.this.onHandleIntent(intent);
        }
    };

    private static void sendClientServiceIntent(Context context, String serviceAction) {
        Intent i = new Intent(context, ClientService.class);
        i.putExtra("action", serviceAction);
        context.startService(i);
    }

    public static void startClientService(Context context, ClientBridge bridge) {
        pgpClientBridge = bridge;
        sendClientServiceIntent(context, BridgeConstants.START_SERVICE_ACTION);
    }

    public static void stopClientService(Context context) {
        context.stopService(new Intent(context, ClientService.class));
        pgpClientBridge = null;
    }

    private static void sendBackgroundServiceIntent(Context context, String serviceAction) {
        Intent i = new Intent(context, BackgroundService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", serviceAction);
        context.startService(i);
    }

    protected void confirmBridgeShutdown() {
    }

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "ClientService onCreate PROCESS_LOCAL_VALUE = " + BackgroundService.PROCESS_LOCAL_VALUE);
        registerReceiver(this.sfidaReceiver, new IntentFilter(ClientBridge.CLIENT_BROADCAST_DOMAIN));
    }

    public void onDestroy() {
        unregisterReceiver(this.sfidaReceiver);
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return 1;
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null && pgpClientBridge != null) {
            String action = intent.getStringExtra("action");
            if (action != null) {
                Object obj = -1;
                switch (action.hashCode()) {
                    case -1708606089:
                        if (action.equals(BridgeConstants.BATTERY_LEVEL_ACTION)) {
                            obj = 8;
                            break;
                        }
                        break;
                    case -1682997253:
                        if (action.equals(BridgeConstants.IS_SCANNING_ACTION)) {
                            obj = 7;
                            break;
                        }
                        break;
                    case -1076199202:
                        if (action.equals(BridgeConstants.SFIDA_STATE_ACTION)) {
                            obj = 1;
                            break;
                        }
                        break;
                    case -384563172:
                        if (action.equals(BridgeConstants.CENTRAL_STATE_ACTION)) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 107147694:
                        if (action.equals(BridgeConstants.ENCOUNTER_ID_ACTION)) {
                            obj = 2;
                            break;
                        }
                        break;
                    case 515058459:
                        if (action.equals(BridgeConstants.POKESTOP_ACTION)) {
                            obj = 3;
                            break;
                        }
                        break;
                    case 886369566:
                        if (action.equals(BridgeConstants.PLUGIN_STATE_ACTION)) {
                            obj = 6;
                            break;
                        }
                        break;
                    case 1260642893:
                        if (action.equals(BridgeConstants.UPDATE_TIMESTAMP_ACTION)) {
                            obj = null;
                            break;
                        }
                        break;
                    case 1337874399:
                        if (action.equals(BridgeConstants.CONFIRM_BRIDGE_SHUTDOWN_ACTION)) {
                            obj = 10;
                            break;
                        }
                        break;
                    case 1849706483:
                        if (action.equals(BridgeConstants.START_SERVICE_ACTION)) {
                            obj = 9;
                            break;
                        }
                        break;
                    case 2138493795:
                        if (action.equals(BridgeConstants.SCANNED_SFIDA_ACTION)) {
                            obj = 5;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case R.styleable.AdsAttrs_adSize /*0*/:
                        pgpClientBridge.sendUpdateTimestamp(intent.getLongExtra(GameServices.USER_TIME_STAMP, 0));
                        return;
                    case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                        pgpClientBridge.sendSfidaState(intent.getIntExtra(GameServices.STATE, 0));
                        return;
                    case R.styleable.LoadingImageView_circleCrop /*2*/:
                        pgpClientBridge.sendEncounterId(intent.getLongExtra(TriggerIfContentAvailable.ID, 0));
                        return;
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                        pgpClientBridge.sendPokestopId(intent.getStringExtra(TriggerIfContentAvailable.ID));
                        return;
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                        pgpClientBridge.sendCentralState(intent.getIntExtra(GameServices.STATE, 0));
                        return;
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                        pgpClientBridge.sendScannedSfida(intent.getStringExtra("device"), intent.getIntExtra("button", 0));
                        return;
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                        pgpClientBridge.sendPluginState(intent.getIntExtra(GameServices.STATE, 0));
                        return;
                    case 7:
                        pgpClientBridge.sendIsScanning(intent.getIntExtra(BridgeConstants.IS_SCANNING_ACTION, 0));
                        return;
                    case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                        pgpClientBridge.sendBatteryLevel(intent.getDoubleExtra("level", 0.0d));
                        return;
                    case 9:
                        return;
                    case 10:
                        confirmBridgeShutdown();
                        return;
                    default:
                        Log.e("ClientService", "Can't handle intent message: " + action);
                        return;
                }
            }
        }
    }
}
