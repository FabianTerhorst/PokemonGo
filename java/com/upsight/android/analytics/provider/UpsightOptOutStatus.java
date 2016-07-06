package com.upsight.android.analytics.provider;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;

public abstract class UpsightOptOutStatus {
    public abstract boolean get();

    public abstract void set(boolean z);

    public static void set(UpsightContext upsight, boolean optOut) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().setOptOutStatus(optOut);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static boolean get(UpsightContext upsight) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().getOptOutStatus();
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        return false;
    }
}
