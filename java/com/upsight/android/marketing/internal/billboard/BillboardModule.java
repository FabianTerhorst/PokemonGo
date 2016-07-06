package com.upsight.android.marketing.internal.billboard;

import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class BillboardModule {
    @Singleton
    @Provides
    UpsightBillboardManager provideBillboardManager(UpsightContext upsight, MarketingContentStore contentStore) {
        return new BillboardManagerImpl(upsight.getDataStore(), contentStore, upsight.getCoreComponent().bus());
    }
}
