package com.nianticlabs.nia.unity;

import android.app.Activity;
import android.util.Log;

public class UnityUtil {
    private static final String TAG = UnityUtil.class.getSimpleName();
    private static volatile Activity activity;

    private static native void nativeInit();

    public static Activity getActivity() {
        if (activity != null) {
            return activity;
        }
        try {
            activity = (Activity) Class.forName("com.unity3d.player.UnityPlayer").getField("currentActivity").get(null);
            return activity;
        } catch (Exception e) {
            Log.e(TAG, "Unable to get currentActivity", e);
            return null;
        }
    }

    public static void init() {
        nativeInit();
    }

    private UnityUtil() {
    }
}
