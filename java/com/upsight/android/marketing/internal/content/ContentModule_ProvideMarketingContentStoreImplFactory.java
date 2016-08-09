package com.upsight.android.marketing.internal.content;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ContentModule_ProvideMarketingContentStoreImplFactory implements Factory<MarketingContentStoreImpl> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContentModule_ProvideMarketingContentStoreImplFactory.class.desiredAssertionStatus());
    private final ContentModule module;
    private final Provider<UpsightContext> upsightProvider;

    public ContentModule_ProvideMarketingContentStoreImplFactory(ContentModule module, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public MarketingContentStoreImpl get() {
        return (MarketingContentStoreImpl) Preconditions.checkNotNull(this.module.provideMarketingContentStoreImpl((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<MarketingContentStoreImpl> create(ContentModule module, Provider<UpsightContext> upsightProvider) {
        return new ContentModule_ProvideMarketingContentStoreImplFactory(module, upsightProvider);
    }
}
