package com.upsight.android.unity;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import spacemadness.com.lunarconsole.BuildConfig;

public abstract class AbstractUpsightPlugin {
    protected static final String MANAGER_NAME = "UpsightManager";
    protected static final String TAG = "Upsight";
    private Field mUnityPlayerActivityField;
    private Class<?> mUnityPlayerClass;
    private Method mUnitySendMessageMethod;

    public AbstractUpsightPlugin() {
        try {
            this.mUnityPlayerClass = Class.forName("com.unity3d.player.UnityPlayer");
            this.mUnityPlayerActivityField = this.mUnityPlayerClass.getField("currentActivity");
            this.mUnitySendMessageMethod = this.mUnityPlayerClass.getMethod("UnitySendMessage", new Class[]{String.class, String.class, String.class});
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "could not find UnityPlayer class: " + e.getMessage());
        } catch (NoSuchFieldException e2) {
            Log.i(TAG, "could not find currentActivity field: " + e2.getMessage());
        } catch (Exception e3) {
            Log.i(TAG, "unkown exception occurred locating getActivity(): " + e3.getMessage());
        }
    }

    protected Activity getActivity() {
        if (this.mUnityPlayerActivityField != null) {
            try {
                Activity activity = (Activity) this.mUnityPlayerActivityField.get(this.mUnityPlayerClass);
                if (activity != null) {
                    return activity;
                }
                Log.e(TAG, "Something has gone terribly wrong. The Unity Activity does not exist. This could be due to a low memory situation");
                return activity;
            } catch (Exception e) {
                Log.i(TAG, "error getting currentActivity: " + e.getMessage());
            }
        }
        return null;
    }

    public void UnitySendMessage(final String method, String parameter) {
        final String nonNullParameter = parameter != null ? parameter : BuildConfig.FLAVOR;
        if (this.mUnitySendMessageMethod != null) {
            try {
                this.mUnitySendMessageMethod.invoke(null, new Object[]{MANAGER_NAME, method, nonNullParameter});
                return;
            } catch (IllegalArgumentException e) {
                Log.i(TAG, "could not find UnitySendMessage method: " + e.getMessage());
                return;
            } catch (IllegalAccessException e2) {
                Log.i(TAG, "could not find UnitySendMessage method: " + e2.getMessage());
                return;
            } catch (InvocationTargetException e3) {
                Log.i(TAG, "could not find UnitySendMessage method: " + e3.getMessage());
                return;
            }
        }
        Log.i(TAG, "UnitySendMessage: UpsightManager, " + method + ", " + nonNullParameter);
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Activity activity = AbstractUpsightPlugin.this.getActivity();
                if (activity != null) {
                    Toast.makeText(activity, "UnitySendMessage:\n" + method + "\n" + nonNullParameter, 1).show();
                }
            }
        });
    }

    protected void runSafelyOnUiThread(final Runnable r) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } catch (Exception e) {
                        Log.e(AbstractUpsightPlugin.TAG, "Exception running command on UI thread: " + e.getMessage());
                    }
                }
            });
        }
    }
}
