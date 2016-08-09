package com.upsight.android.marketing.internal;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContentMediatorManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class BaseMarketingModule_ProvideMarketingApiFactory implements Factory<UpsightMarketingApi> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseMarketingModule_ProvideMarketingApiFactory.class.desiredAssertionStatus());
    private final Provider<UpsightBillboardManager> billboardManagerProvider;
    private final Provider<MarketingContentMediatorManager> contentMediatorManagerProvider;
    private final Provider<UpsightMarketingContentStore> contentStoreProvider;
    private final BaseMarketingModule module;

    public BaseMarketingModule_ProvideMarketingApiFactory(BaseMarketingModule module, Provider<UpsightBillboardManager> billboardManagerProvider, Provider<UpsightMarketingContentStore> contentStoreProvider, Provider<MarketingContentMediatorManager> contentMediatorManagerProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || billboardManagerProvider != null) {
                this.billboardManagerProvider = billboardManagerProvider;
                if ($assertionsDisabled || contentStoreProvider != null) {
                    this.contentStoreProvider = contentStoreProvider;
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
        throw new AssertionError();
    }

    public UpsightMarketingApi get() {
        return (UpsightMarketingApi) Preconditions.checkNotNull(this.module.provideMarketingApi((UpsightBillboardManager) this.billboardManagerProvider.get(), (UpsightMarketingContentStore) this.contentStoreProvider.get(), (MarketingContentMediatorManager) this.contentMediatorManagerProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightMarketingApi> create(BaseMarketingModule module, Provider<UpsightBillboardManager> billboardManagerProvider, Provider<UpsightMarketingContentStore> contentStoreProvider, Provider<MarketingContentMediatorManager> contentMediatorManagerProvider) {
        return new BaseMarketingModule_ProvideMarketingApiFactory(module, billboardManagerProvider, contentStoreProvider, contentMediatorManagerProvider);
    }
}
