package com.upsight.android.marketing.internal.content;

import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightCoreComponent;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Scheduler;

@Module
public final class ContentModule {
    @Singleton
    @Provides
    MarketingContentFactory provideMarketingContentFactory(UpsightContext upsight, @Named("main") Scheduler scheduler, MarketingContentStore contentStore, ContentTemplateWebViewClientFactory contentTemplateWebViewClientFactory) {
        UpsightCoreComponent coreComponent = upsight.getCoreComponent();
        return new MarketingContentFactory(new MarketingContentActionContext(upsight, coreComponent.bus(), coreComponent.objectMapper(), ((UpsightAnalyticsComponent) ((UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME)).getComponent()).clock(), scheduler.createWorker(), upsight.getLogger(), contentStore, contentTemplateWebViewClientFactory));
    }

    @Singleton
    @Provides
    MarketingContentStoreImpl provideMarketingContentStoreImpl(UpsightContext upsight) {
        return new MarketingContentStoreImpl(upsight.getCoreComponent().bus(), ((UpsightAnalyticsComponent) ((UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME)).getComponent()).clock());
    }

    @Singleton
    @Provides
    MarketingContentStore provideMarketingContentStore(MarketingContentStoreImpl impl) {
        return impl;
    }

    @Singleton
    @Provides
    UpsightMarketingContentStore provideUpsightMarketingContentStore(MarketingContentStoreImpl impl) {
        return impl;
    }

    @Singleton
    @Provides
    DefaultContentMediator provideDefaultContentMediator() {
        return new DefaultContentMediator();
    }
}
