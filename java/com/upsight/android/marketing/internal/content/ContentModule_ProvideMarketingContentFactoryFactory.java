package com.upsight.android.marketing.internal.content;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;
import rx.Scheduler;

public final class ContentModule_ProvideMarketingContentFactoryFactory implements Factory<MarketingContentFactory> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContentModule_ProvideMarketingContentFactoryFactory.class.desiredAssertionStatus());
    private final Provider<MarketingContentStore> contentStoreProvider;
    private final Provider<ContentTemplateWebViewClientFactory> contentTemplateWebViewClientFactoryProvider;
    private final ContentModule module;
    private final Provider<Scheduler> schedulerProvider;
    private final Provider<UpsightContext> upsightProvider;

    public ContentModule_ProvideMarketingContentFactoryFactory(ContentModule module, Provider<UpsightContext> upsightProvider, Provider<Scheduler> schedulerProvider, Provider<MarketingContentStore> contentStoreProvider, Provider<ContentTemplateWebViewClientFactory> contentTemplateWebViewClientFactoryProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || schedulerProvider != null) {
                    this.schedulerProvider = schedulerProvider;
                    if ($assertionsDisabled || contentStoreProvider != null) {
                        this.contentStoreProvider = contentStoreProvider;
                        if ($assertionsDisabled || contentTemplateWebViewClientFactoryProvider != null) {
                            this.contentTemplateWebViewClientFactoryProvider = contentTemplateWebViewClientFactoryProvider;
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
        throw new AssertionError();
    }

    public MarketingContentFactory get() {
        MarketingContentFactory provided = this.module.provideMarketingContentFactory((UpsightContext) this.upsightProvider.get(), (Scheduler) this.schedulerProvider.get(), (MarketingContentStore) this.contentStoreProvider.get(), (ContentTemplateWebViewClientFactory) this.contentTemplateWebViewClientFactoryProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<MarketingContentFactory> create(ContentModule module, Provider<UpsightContext> upsightProvider, Provider<Scheduler> schedulerProvider, Provider<MarketingContentStore> contentStoreProvider, Provider<ContentTemplateWebViewClientFactory> contentTemplateWebViewClientFactoryProvider) {
        return new ContentModule_ProvideMarketingContentFactoryFactory(module, upsightProvider, schedulerProvider, contentStoreProvider, contentTemplateWebViewClientFactoryProvider);
    }
}
