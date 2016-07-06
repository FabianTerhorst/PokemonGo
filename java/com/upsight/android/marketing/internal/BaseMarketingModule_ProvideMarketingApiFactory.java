package com.upsight.android.marketing.internal;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BaseMarketingModule_ProvideMarketingApiFactory implements Factory<UpsightMarketingApi> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseMarketingModule_ProvideMarketingApiFactory.class.desiredAssertionStatus());
    private final Provider<UpsightBillboardManager> billboardManagerProvider;
    private final Provider<UpsightMarketingContentStore> marketingContentStoreProvider;
    private final BaseMarketingModule module;

    public BaseMarketingModule_ProvideMarketingApiFactory(BaseMarketingModule module, Provider<UpsightBillboardManager> billboardManagerProvider, Provider<UpsightMarketingContentStore> marketingContentStoreProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
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
        throw new AssertionError();
    }

    public UpsightMarketingApi get() {
        UpsightMarketingApi provided = this.module.provideMarketingApi((UpsightBillboardManager) this.billboardManagerProvider.get(), (UpsightMarketingContentStore) this.marketingContentStoreProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightMarketingApi> create(BaseMarketingModule module, Provider<UpsightBillboardManager> billboardManagerProvider, Provider<UpsightMarketingContentStore> marketingContentStoreProvider) {
        return new BaseMarketingModule_ProvideMarketingApiFactory(module, billboardManagerProvider, marketingContentStoreProvider);
    }
}
