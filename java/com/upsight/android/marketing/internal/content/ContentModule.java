package com.upsight.android.marketing.internal.content;

import com.squareup.otto.Bus;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightCoreComponent;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import com.upsight.android.marketing.internal.vast.VastContentMediator;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Scheduler;

@Module
public final class ContentModule {
    @Singleton
    @Provides
    MarketingContentFactory provideMarketingContentFactory(UpsightContext upsight, @Named("main") Scheduler scheduler, MarketingContentMediatorManager contentMediatorManager, MarketingContentStore contentStore, ContentTemplateWebViewClientFactory contentTemplateWebViewClientFactory) {
        UpsightCoreComponent coreComponent = upsight.getCoreComponent();
        return new MarketingContentFactory(new MarketingContentActionContext(upsight, coreComponent.bus(), coreComponent.gson(), ((UpsightAnalyticsComponent) ((UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME)).getComponent()).clock(), scheduler.createWorker(), upsight.getLogger(), contentMediatorManager, contentStore, contentTemplateWebViewClientFactory));
    }

    @Singleton
    @Provides
    MarketingContentStoreImpl provideMarketingContentStoreImpl(UpsightContext upsight) {
        UpsightCoreComponent coreComponent = upsight.getCoreComponent();
        UpsightAnalyticsExtension analyticsExtension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        Bus bus = null;
        Clock clock = null;
        if (!(coreComponent == null || analyticsExtension == null)) {
            bus = coreComponent.bus();
            clock = ((UpsightAnalyticsComponent) analyticsExtension.getComponent()).clock();
        }
        return new MarketingContentStoreImpl(bus, clock, upsight.getLogger());
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
    MarketingContentMediatorManager provideMarketingContentMediatorManager(DefaultContentMediator defaultContentMediator) {
        return new MarketingContentMediatorManager(defaultContentMediator);
    }

    @Singleton
    @Provides
    DefaultContentMediator provideDefaultContentMediator() {
        return new DefaultContentMediator();
    }

    @Singleton
    @Provides
    VastContentMediator provideVastContentMediator(UpsightContext upsight) {
        return new VastContentMediator(upsight.getLogger(), upsight.getCoreComponent().bus());
    }
}
