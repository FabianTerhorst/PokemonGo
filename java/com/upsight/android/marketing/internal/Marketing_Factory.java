package com.upsight.android.marketing.internal;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContentMediatorManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class Marketing_Factory implements Factory<Marketing> {
    static final /* synthetic */ boolean $assertionsDisabled = (!Marketing_Factory.class.desiredAssertionStatus());
    private final Provider<UpsightBillboardManager> billboardManagerProvider;
    private final Provider<MarketingContentMediatorManager> contentMediatorManagerProvider;
    private final Provider<UpsightMarketingContentStore> marketingContentStoreProvider;

    public Marketing_Factory(Provider<UpsightBillboardManager> billboardManagerProvider, Provider<UpsightMarketingContentStore> marketingContentStoreProvider, Provider<MarketingContentMediatorManager> contentMediatorManagerProvider) {
        if ($assertionsDisabled || billboardManagerProvider != null) {
            this.billboardManagerProvider = billboardManagerProvider;
            if ($assertionsDisabled || marketingContentStoreProvider != null) {
                this.marketingContentStoreProvider = marketingContentStoreProvider;
                if ($assertionsDisabled || contentMediatorManagerProvider != null) {
                    this.contentMediatorManagerProvider = contentMediatorManagerProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public Marketing get() {
        return new Marketing((UpsightBillboardManager) this.billboardManagerProvider.get(), (UpsightMarketingContentStore) this.marketingContentStoreProvider.get(), (MarketingContentMediatorManager) this.contentMediatorManagerProvider.get());
    }

    public static Factory<Marketing> create(Provider<UpsightBillboardManager> billboardManagerProvider, Provider<UpsightMarketingContentStore> marketingContentStoreProvider, Provider<MarketingContentMediatorManager> contentMediatorManagerProvider) {
        return new Marketing_Factory(billboardManagerProvider, marketingContentStoreProvider, contentMediatorManagerProvider);
    }
}
