package com.upsight.android;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.upsight.android.UpsightCoreComponent.Factory;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class Upsight {
    private static final String CORE_COMPONENT_FACTORY = "com.upsight.core";
    private static final String EXTENSION_PREFIX = "com.upsight.extension.";
    public static final String LOG_TAG = "Upsight";
    private static final int MIN_ANDROID_API_LEVEL = 14;
    private static UpsightContext sUpsight;

    public static synchronized UpsightContext createContext(Context context) {
        UpsightContext upsightContext;
        synchronized (Upsight.class) {
            if (sUpsight == null) {
                sUpsight = create(context);
            }
            upsightContext = sUpsight;
        }
        return upsightContext;
    }

    static UpsightContext create(Context context) {
        if (VERSION.SDK_INT < MIN_ANDROID_API_LEVEL) {
            return new UpsightContextCompat(context);
        }
        UpsightCoreComponent component = loadCoreComponent(context);
        Map<String, UpsightExtension> extensions = loadExtensions(context);
        UpsightContext upsight = component.upsightContext();
        upsight.onCreate(component, extensions);
        return upsight;
    }

    private static UpsightCoreComponent loadCoreComponent(Context context) {
        Pair<String, String> entry = loadMetadataByName(context, CORE_COMPONENT_FACTORY);
        if (entry == null) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName((String) entry.second);
            if (Factory.class.isAssignableFrom(clazz)) {
                return ((Factory) clazz.newInstance()).create(context);
            }
            throw new IllegalStateException(String.format("Class %s must implement %s", new Object[]{clazz.getName(), Factory.class.getName()}));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (InstantiationException e2) {
            throw new IllegalStateException(e2.getMessage(), e2);
        } catch (IllegalAccessException e3) {
            throw new IllegalStateException(e3.getMessage(), e3);
        }
    }

    private static Map<String, UpsightExtension> loadExtensions(Context context) throws IllegalStateException {
        Map<String, UpsightExtension> extensions = new HashMap();
        for (Entry<String, String> entry : loadMetadataByPrefix(context, EXTENSION_PREFIX).entrySet()) {
            try {
                Class<?> clazz = Class.forName((String) entry.getValue());
                if (UpsightExtension.class.isAssignableFrom(clazz)) {
                    extensions.put(entry.getKey(), (UpsightExtension) clazz.newInstance());
                } else {
                    throw new IllegalStateException(String.format("Class %s must implement %s", new Object[]{clazz.getName(), UpsightExtension.class.getName()}));
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Unable to load extension: " + ((String) entry.getKey()), e);
            } catch (InstantiationException e2) {
                throw new IllegalStateException("Unable to load extension: " + ((String) entry.getKey()), e2);
            } catch (IllegalAccessException e3) {
                throw new IllegalStateException("Unable to load extension: " + ((String) entry.getKey()), e3);
            }
        }
        return extensions;
    }

    private static Pair<String, String> loadMetadataByName(Context context, String name) {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            if (bundle == null) {
                return null;
            }
            for (String key : bundle.keySet()) {
                if (!TextUtils.isEmpty(key) && key.equals(name)) {
                    String value = bundle.getString(key);
                    if (!TextUtils.isEmpty(value)) {
                        return new Pair(key, value);
                    }
                }
            }
            return null;
        } catch (NameNotFoundException e) {
            Log.e(LOG_TAG, "Unexpected error: Package name missing", e);
            return null;
        }
    }

    private static Map<String, String> loadMetadataByPrefix(Context context, String prefix) {
        Map<String, String> entries = new HashMap();
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    if (!TextUtils.isEmpty(key) && key.startsWith(prefix)) {
                        String value = bundle.getString(key);
                        if (!TextUtils.isEmpty(value)) {
                            entries.put(key, value);
                        }
                    }
                }
            }
        } catch (NameNotFoundException e) {
            Log.e(LOG_TAG, "Unexpected error: Package name missing", e);
        }
        return entries;
    }
}
