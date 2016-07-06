package com.upsight.android.marketing.internal;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class Marketing_Factory implements Factory<Marketing> {
    static final /* synthetic */ boolean $assertionsDisabled = (!Marketing_Factory.class.desiredAssertionStatus());
    private final Provider<UpsightBillboardManager> billboardManagerProvider;
    private final Provider<UpsightMarketingContentStore> marketingContentStoreProvider;

    public Marketing_Factory(Provider<UpsightBillboardManager> billboardManagerProvider, Provider<UpsightMarketingContentStore> marketingContentStoreProvider) {
        if ($assertionsDisabled || billboardManagerProvider != null) {
            this.billboardManagerProvider = billboardManagerProvider;
            if ($assertionsDisabled || marketingContentStoreProvider != null) {
                this.marketingContentStoreProvider = marketingContentStoreProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public Marketing get() {
        return new Marketing((UpsightBillboardManager) this.billboardManagerProvider.get(), (UpsightMarketingContentStore) this.marketingContentStoreProvider.get());
    }

    public static Factory<Marketing> create(Provider<UpsightBillboardManager> billboardManagerProvider, Provider<UpsightMarketingContentStore> marketingContentStoreProvider) {
        return new Marketing_Factory(billboardManagerProvider, marketingContentStoreProvider);
    }
}
