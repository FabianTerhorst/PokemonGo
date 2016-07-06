package com.upsight.android.internal.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public final class NetworkHelper {
    public static final String NETWORK_OPERATOR_NONE = "none";
    public static final String NETWORK_TYPE_NONE = "no_network";

    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
            if (cm == null) {
                return false;
            }
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        } catch (SecurityException e) {
            return false;
        }
    }

    public static String getActiveNetworkType(Context context) {
        String type = NETWORK_TYPE_NONE;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
            if (cm == null) {
                return type;
            }
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo == null || !netInfo.isConnected()) {
                return type;
            }
            String typeName = netInfo.getTypeName();
            if (TextUtils.isEmpty(typeName)) {
                return type;
            }
            return typeName;
        } catch (SecurityException e) {
            return type;
        }
    }

    public static String getNetworkOperatorName(Context context) {
        String networkOperatorName = NETWORK_OPERATOR_NONE;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            if (tm != null) {
                networkOperatorName = tm.getNetworkOperatorName();
            }
        } catch (SecurityException e) {
        }
        return networkOperatorName;
    }
}
