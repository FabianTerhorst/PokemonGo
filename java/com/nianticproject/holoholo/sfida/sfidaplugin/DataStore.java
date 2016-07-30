package com.nianticproject.holoholo.sfida.sfidaplugin;

import android.app.Activity;
import android.content.SharedPreferences;

public class DataStore {
    public static void setBluetoothAddress(Activity activity) {
        SharedPreferences preferences = activity.getPreferences(0);
    }
}
