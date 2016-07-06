package com.voxelbusters;

import android.util.Log;
import com.voxelbusters.nativeplugins.base.interfaces.IAppLifeCycleListener;
import com.voxelbusters.nativeplugins.utilities.Debug;
import com.voxelbusters.nativeplugins.utilities.StringUtility;
import java.util.ArrayList;
import java.util.Iterator;

public class NativeBinding {
    public static ArrayList<IAppLifeCycleListener> appLifeCycleListeners = new ArrayList();
    public static boolean isAppForeground = true;

    public static void onApplicationQuit() {
        isAppForeground = false;
        Iterator it = appLifeCycleListeners.iterator();
        while (it.hasNext()) {
            ((IAppLifeCycleListener) it.next()).onApplicationQuit();
        }
    }

    public static boolean isApplicationForeground() {
        return isAppForeground;
    }

    public static void onApplicationResume() {
        isAppForeground = true;
        Iterator it = appLifeCycleListeners.iterator();
        while (it.hasNext()) {
            ((IAppLifeCycleListener) it.next()).onApplicationResume();
        }
    }

    public static void onApplicationPause() {
        isAppForeground = false;
        Iterator it = appLifeCycleListeners.iterator();
        while (it.hasNext()) {
            ((IAppLifeCycleListener) it.next()).onApplicationPause();
        }
    }

    public static void addAppLifeCycleListener(IAppLifeCycleListener listener) {
        if (!appLifeCycleListeners.contains(listener)) {
            appLifeCycleListeners.add(listener);
        }
    }

    public static void removeAppLifeCycleListener(IAppLifeCycleListener listener) {
        appLifeCycleListeners.remove(listener);
    }

    public static void enableDebug(boolean isDebugEnabled) {
        Debug.ENABLED = isDebugEnabled;
    }

    public static void logMessage(String message, String logType, String stackTrace) {
        String tag = "Unity";
        String messageToDisplay = StringUtility.getBase64DecodedString(message) + "\n" + StringUtility.getBase64DecodedString(stackTrace);
        if (logType.equals("ERROR")) {
            Log.e(tag, messageToDisplay);
        } else if (logType.equals("WARNING")) {
            Log.w(tag, messageToDisplay);
        } else if (logType.equals("INFO")) {
            Log.i(tag, messageToDisplay);
        } else {
            Log.d(tag, messageToDisplay);
        }
    }
}
