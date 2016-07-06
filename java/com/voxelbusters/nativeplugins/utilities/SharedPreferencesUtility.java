package com.voxelbusters.nativeplugins.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.json.JSONArray;
import spacemadness.com.lunarconsole.BuildConfig;

public class SharedPreferencesUtility {
    public static JSONArray getJsonArray(String preferencesName, int mode, Context context, String keyName) {
        return JSONUtility.getJSONArray(getSharedPreferences(preferencesName, mode, context).getString(keyName, BuildConfig.FLAVOR));
    }

    public static SharedPreferences getSharedPreferences(String preferencesName, int mode, Context context) {
        return context.getSharedPreferences(preferencesName, mode);
    }

    public static void setJSONArray(String preferencesName, int mode, Context context, String keyName, JSONArray array) {
        Editor editor = getSharedPreferences(preferencesName, mode, context).edit();
        editor.putString(keyName, array.toString());
        editor.commit();
    }
}
