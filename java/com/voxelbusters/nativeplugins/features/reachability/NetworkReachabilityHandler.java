package com.voxelbusters.nativeplugins.features.reachability;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import com.voxelbusters.NativeBinding;
import com.voxelbusters.nativeplugins.NativePluginHelper;
import com.voxelbusters.nativeplugins.base.interfaces.IAppLifeCycleListener;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import com.voxelbusters.nativeplugins.defines.UnityDefines.Reachability;
import com.voxelbusters.nativeplugins.utilities.Debug;

public class NetworkReachabilityHandler implements IAppLifeCycleListener {
    private static NetworkReachabilityHandler INSTANCE;
    static boolean isSocketConnected = false;
    static boolean isWifiReachable = false;
    ConnectivityListener connectivityListener;
    Context context;
    HostConnectionPoller socketPoller = new HostConnectionPoller();

    public static NetworkReachabilityHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkReachabilityHandler();
        }
        return INSTANCE;
    }

    private NetworkReachabilityHandler() {
    }

    public void initialize(String ipAddress, int port, float timeGapBetweenPolls, int connectionTimeOutPeriod, int maxRetryCount) {
        this.context = NativePluginHelper.getCurrentContext();
        StartTestingNetworkHardware();
        StartSocketPoller(ipAddress, port, timeGapBetweenPolls, connectionTimeOutPeriod, maxRetryCount);
        NativeBinding.addAppLifeCycleListener(this);
    }

    void StartSocketPoller(String ipAddress, int port, float timeGapBetweenPolls, int connectionTimeOutPeriod, int maxRetryCount) {
        this.socketPoller.setIp(ipAddress);
        this.socketPoller.setPort(port);
        this.socketPoller.setConnectionTimeOutPeriod(connectionTimeOutPeriod);
        this.socketPoller.setMaxRetryCount(maxRetryCount);
        this.socketPoller.setTimeGapBetweenPolls(timeGapBetweenPolls);
        this.socketPoller.Start();
    }

    void StartTestingNetworkHardware() {
        pauseReachability();
        this.connectivityListener = new ConnectivityListener();
        registerBroadcastReceiver(this.connectivityListener);
        this.connectivityListener.updateConnectionStatus(this.context);
    }

    void registerBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter connectivityChangeIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        IntentFilter wifiStateChangeIntentFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        IntentFilter networkStateChangeIntentFilter = new IntentFilter("android.net.wifi.STATE_CHANGE");
        this.context.registerReceiver(receiver, connectivityChangeIntentFilter);
        this.context.registerReceiver(receiver, wifiStateChangeIntentFilter);
        this.context.registerReceiver(receiver, networkStateChangeIntentFilter);
    }

    public void resumeReachability() {
        try {
            registerBroadcastReceiver(this.connectivityListener);
            this.connectivityListener.updateConnectionStatus(this.context);
        } catch (IllegalArgumentException e) {
            Debug.warning(CommonDefines.NETWORK_CONNECTIVITY_TAG, "Already registered! " + e.getMessage());
        }
    }

    public void pauseReachability() {
        try {
            this.context.unregisterReceiver(this.connectivityListener);
        } catch (IllegalArgumentException e) {
            Debug.warning(CommonDefines.NETWORK_CONNECTIVITY_TAG, "Already unregistered!" + e.getMessage());
        }
    }

    public static void sendWifiReachabilityStatus(boolean newWifiStatus) {
        if (isWifiReachable != newWifiStatus) {
            isWifiReachable = newWifiStatus;
            NativePluginHelper.sendMessage(Reachability.NETWORK_CONNECTIVITY_HARDWARE_STATUS_CHANGE, isWifiReachable ? "true" : "false");
        }
    }

    public static void sendSocketConnectionStatus(boolean newSocketStatus) {
        if (isSocketConnected != newSocketStatus) {
            isSocketConnected = newSocketStatus;
            NativePluginHelper.sendMessage(Reachability.NETWORK_CONNECTIVITY_SOCKET_STATUS_CHANGE, isSocketConnected ? "true" : "false");
        }
    }

    public void onApplicationPause() {
    }

    public void onApplicationResume() {
    }

    public void onApplicationQuit() {
        pauseReachability();
        NativeBinding.removeAppLifeCycleListener(this);
    }
}
