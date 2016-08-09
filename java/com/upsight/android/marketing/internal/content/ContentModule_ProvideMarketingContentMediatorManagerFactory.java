package com.upsight.android.marketing.internal.content;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ContentModule_ProvideMarketingContentMediatorManagerFactory implements Factory<MarketingContentMediatorManager> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContentModule_ProvideMarketingContentMediatorManagerFactory.class.desiredAssertionStatus());
    private final Provider<DefaultContentMediator> defaultContentMediatorProvider;
    private final ContentModule module;

    public ContentModule_ProvideMarketingContentMediatorManagerFactory(ContentModule module, Provider<DefaultContentMediator> defaultContentMediatorProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || defaultContentMediatorProvider != null) {
                this.defaultContentMediatorProvider = defaultContentMediatorProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public MarketingContentMediatorManager get() {
        return (MarketingContentMediatorManager) Preconditions.checkNotNull(this.module.provideMarketingContentMediatorManager((DefaultContentMediator) this.defaultContentMediatorProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<MarketingContentMediatorManager> create(ContentModule module, Provider<DefaultContentMediator> defaultContentMediatorProvider) {
        return new ContentModule_ProvideMarketingContentMediatorManagerFactory(module, defaultContentMediatorProvider);
    }
}
