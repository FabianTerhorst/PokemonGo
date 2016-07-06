package com.voxelbusters.nativeplugins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import com.unity3d.player.UnityPlayer;
import com.voxelbusters.nativeplugins.defines.UnityDefines;
import com.voxelbusters.nativeplugins.utilities.Debug;
import com.voxelbusters.nativeplugins.utilities.StringUtility;
import java.util.ArrayList;
import java.util.HashMap;
import spacemadness.com.lunarconsole.BuildConfig;

public class NativePluginHelper {

    class AnonymousClass1 implements Runnable {
        private final /* synthetic */ Intent val$intent;

        AnonymousClass1(Intent intent) {
            this.val$intent = intent;
        }

        public void run() {
            NativePluginHelper.getCurrentContext().startActivity(this.val$intent);
        }
    }

    public static void sendMessage(String methodName) {
        sendMessage(methodName, BuildConfig.FLAVOR);
    }

    public static void sendMessage(String methodName, String message) {
        if (!StringUtility.isNullOrEmpty(methodName)) {
            Debug.log("UnitySendMessage", "Method Name : " + methodName + " " + "Message : " + message);
            if (getCurrentContext() != null) {
                UnityPlayer.UnitySendMessage(UnityDefines.NATIVE_BINDING_EVENT_LISTENER, methodName, message);
            }
        }
    }

    public static void sendMessage(String methodName, ArrayList dataList) {
        String message = BuildConfig.FLAVOR;
        if (dataList != null) {
            message = new Gson().toJson(dataList);
        }
        sendMessage(methodName, message);
    }

    public static void sendMessage(String methodName, HashMap dataMap) {
        String message = BuildConfig.FLAVOR;
        if (dataMap != null) {
            message = new Gson().toJson(dataMap);
        }
        sendMessage(methodName, message);
    }

    public static Context getCurrentContext() {
        return UnityPlayer.currentActivity;
    }

    public static Activity getCurrentActivity() {
        return (Activity) getCurrentContext();
    }

    public static void executeOnUIThread(Runnable runnableThread) {
        Activity currentActivity = (Activity) getCurrentContext();
        if (currentActivity != null) {
            currentActivity.runOnUiThread(runnableThread);
        }
    }

    public static void startActivityOnUiThread(Intent intent) {
        executeOnUIThread(new AnonymousClass1(intent));
    }

    public static boolean isApplicationRunning() {
        return getCurrentContext() != null;
    }
}
