package com.upsight.android.unity;

import android.util.Log;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.session.UpsightSessionCallbacks;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.experience.UpsightUserExperience.Handler;
import java.util.List;
import org.json.JSONArray;

public class UnitySessionCallbacks implements UpsightSessionCallbacks {
    protected static final String TAG = "UnitySessionCallbacks";
    private static boolean mShouldSynchronizeManagedVariables = true;

    public static void setShouldSynchronizeManagedVariables(boolean shouldSynchronizeManagedVariables) {
        mShouldSynchronizeManagedVariables = shouldSynchronizeManagedVariables;
    }

    public void onStart(UpsightContext upsight) {
        UpsightUserExperience.registerHandler(upsight, new Handler() {
            public boolean onReceive() {
                return UnitySessionCallbacks.mShouldSynchronizeManagedVariables;
            }

            public void onSynchronize(List<String> tags) {
                Log.i(UnitySessionCallbacks.TAG, "onSynchronize");
                JSONArray json = new JSONArray();
                for (String t : tags) {
                    json.put(t);
                }
                UnityBridge.UnitySendMessage("managedVariablesDidSynchronize", json.toString());
            }
        });
    }
}
