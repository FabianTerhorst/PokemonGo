package com.upsight.android;

import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.internal.content.DefaultContentMediator;
import com.upsight.android.marketing.internal.content.MarketingContentFactory;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightMarketingExtension_MembersInjector implements MembersInjector<UpsightMarketingExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightMarketingExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<DefaultContentMediator> mDefaultContentMediatorProvider;
    private final Provider<MarketingContentFactory> mMarketingContentFactoryProvider;
    private final Provider<UpsightMarketingApi> mMarketingProvider;

    public UpsightMarketingExtension_MembersInjector(Provider<UpsightMarketingApi> mMarketingProvider, Provider<MarketingContentFactory> mMarketingContentFactoryProvider, Provider<DefaultContentMediator> mDefaultContentMediatorProvider) {
        if ($assertionsDisabled || mMarketingProvider != null) {
            this.mMarketingProvider = mMarketingProvider;
            if ($assertionsDisabled || mMarketingContentFactoryProvider != null) {
                this.mMarketingContentFactoryProvider = mMarketingContentFactoryProvider;
                if ($assertionsDisabled || mDefaultContentMediatorProvider != null) {
                    this.mDefaultContentMediatorProvider = mDefaultContentMediatorProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightMarketingExtension> create(Provider<UpsightMarketingApi> mMarketingProvider, Provider<MarketingContentFactory> mMarketingContentFactoryProvider, Provider<DefaultContentMediator> mDefaultContentMediatorProvider) {
        return new UpsightMarketingExtension_MembersInjector(mMarketingProvider, mMarketingContentFactoryProvider, mDefaultContentMediatorProvider);
    }

    public void injectMembers(UpsightMarketingExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mMarketing = (UpsightMarketingApi) this.mMarketingProvider.get();
        instance.mMarketingContentFactory = (MarketingContentFactory) this.mMarketingContentFactoryProvider.get();
        instance.mDefaultContentMediator = (DefaultContentMediator) this.mDefaultContentMediatorProvider.get();
    }

    public static void injectMMarketing(UpsightMarketingExtension instance, Provider<UpsightMarketingApi> mMarketingProvider) {
        instance.mMarketing = (UpsightMarketingApi) mMarketingProvider.get();
    }

    public static void injectMMarketingContentFactory(UpsightMarketingExtension instance, Provider<MarketingContentFactory> mMarketingContentFactoryProvider) {
        instance.mMarketingContentFactory = (MarketingContentFactory) mMarketingContentFactoryProvider.get();
    }

    public static void injectMDefaultContentMediator(UpsightMarketingExtension instance, Provider<DefaultContentMediator> mDefaultContentMediatorProvider) {
        instance.mDefaultContentMediator = (DefaultContentMediator) mDefaultContentMediatorProvider.get();
    }
}
