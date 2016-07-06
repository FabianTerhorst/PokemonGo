package com.upsight.android.internal.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public final class PreferencesHelper {
    public static final String INSTALL_TIMESTAMP_NAME = "install_ts";
    private static final String SHARED_PREFERENCES_NAME = "upsight";

    public static boolean contains(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).contains(key);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).getString(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        Editor edit = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).getInt(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        Editor edit = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).getLong(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        Editor edit = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        Editor edit = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).getFloat(key, defaultValue);
    }

    public static void putFloat(Context context, String key, float value) {
        Editor edit = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
        edit.putFloat(key, value);
        edit.apply();
    }

    public static void clear(Context context, String key) {
        Editor edit = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
        edit.remove(key);
        edit.apply();
    }

    public static void registerListener(Context context, OnSharedPreferenceChangeListener listener) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterListener(Context context, OnSharedPreferenceChangeListener listener) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).unregisterOnSharedPreferenceChangeListener(listener);
    }
}
