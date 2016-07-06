package com.voxelbusters.nativeplugins.features.reachability;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityListener extends BroadcastReceiver {
    boolean isConnected;

    public void onReceive(Context context, Intent arg1) {
        updateConnectionStatus(context);
    }

    public void updateConnectionStatus(Context context) {
        boolean connectionStatus;
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            connectionStatus = false;
        } else {
            connectionStatus = true;
        }
        NetworkReachabilityHandler.sendWifiReachabilityStatus(connectionStatus);
    }
}
