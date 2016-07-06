package com.upsight.android.marketing.internal.content;

import com.upsight.android.marketing.UpsightMarketingContentStore;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ContentModule_ProvideUpsightMarketingContentStoreFactory implements Factory<UpsightMarketingContentStore> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContentModule_ProvideUpsightMarketingContentStoreFactory.class.desiredAssertionStatus());
    private final Provider<MarketingContentStoreImpl> implProvider;
    private final ContentModule module;

    public ContentModule_ProvideUpsightMarketingContentStoreFactory(ContentModule module, Provider<MarketingContentStoreImpl> implProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || implProvider != null) {
                this.implProvider = implProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightMarketingContentStore get() {
        UpsightMarketingContentStore provided = this.module.provideUpsightMarketingContentStore((MarketingContentStoreImpl) this.implProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightMarketingContentStore> create(ContentModule module, Provider<MarketingContentStoreImpl> implProvider) {
        return new ContentModule_ProvideUpsightMarketingContentStoreFactory(module, implProvider);
    }
}
