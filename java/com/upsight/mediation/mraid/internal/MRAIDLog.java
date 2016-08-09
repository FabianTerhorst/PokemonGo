package com.upsight.mediation.mraid.internal;

import com.upsight.mediation.log.FuseLog;

public class MRAIDLog {
    private static final String TAG = "MRAID";

    public static void d(String msg) {
        FuseLog.d(TAG, msg);
    }

    public static void e(String msg) {
        FuseLog.e(TAG, msg);
    }

    public static void i(String msg) {
        FuseLog.i(TAG, msg);
    }

    public static void v(String msg) {
        FuseLog.v(TAG, msg);
    }

    public static void w(String msg) {
        FuseLog.w(TAG, msg);
    }

    public static void d(String subTag, String msg) {
        FuseLog.d(TAG, "[" + subTag + "] " + msg);
    }

    public static void e(String subTag, String msg) {
        FuseLog.e(TAG, "[" + subTag + "] " + msg);
    }

    public static void i(String subTag, String msg) {
        FuseLog.i(TAG, "[" + subTag + "] " + msg);
    }

    public static void v(String subTag, String msg) {
        FuseLog.v(TAG, "[" + subTag + "] " + msg);
    }

    public static void w(String subTag, String msg) {
        FuseLog.w(TAG, "[" + subTag + "] " + msg);
    }
}
