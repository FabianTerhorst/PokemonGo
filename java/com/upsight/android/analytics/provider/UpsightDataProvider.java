package com.upsight.android.analytics.provider;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class UpsightDataProvider {
    private final Map<String, Object> mCachedValues = new HashMap();

    public abstract Set<String> availableKeys();

    protected UpsightDataProvider() {
    }

    protected synchronized void put(String key, Object value) {
        this.mCachedValues.put(key, value);
    }

    public synchronized Object get(String key) {
        return this.mCachedValues.get(key);
    }

    public static void register(UpsightContext upsight, UpsightDataProvider provider) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().registerDataProvider(provider);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }
}
