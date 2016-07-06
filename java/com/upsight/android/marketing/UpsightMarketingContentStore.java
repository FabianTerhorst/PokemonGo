package com.upsight.android.marketing;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightMarketingExtension;

public abstract class UpsightMarketingContentStore {
    public abstract boolean isContentReady(String str);

    protected UpsightMarketingContentStore() {
    }

    public static boolean isContentReady(UpsightContext upsight, String scope) {
        UpsightMarketingExtension extension = (UpsightMarketingExtension) upsight.getUpsightExtension(UpsightMarketingExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().isContentReady(scope);
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.marketing must be registered in your Android Manifest", new Object[0]);
        return false;
    }
}
