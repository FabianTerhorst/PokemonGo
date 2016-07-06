package com.upsight.android;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingComponent;
import com.upsight.android.marketing.internal.content.DefaultContentMediator;
import com.upsight.android.marketing.internal.content.MarketingContentFactory;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightMarketingExtension_MembersInjector implements MembersInjector<UpsightMarketingExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightMarketingExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<UpsightBillboardManager> mBillboardManagerProvider;
    private final Provider<DefaultContentMediator> mDefaultContentMediatorProvider;
    private final Provider<MarketingContentFactory> mMarketingContentFactoryProvider;
    private final Provider<UpsightMarketingApi> mMarketingProvider;
    private final MembersInjector<UpsightExtension<UpsightMarketingComponent, UpsightMarketingApi>> supertypeInjector;

    public UpsightMarketingExtension_MembersInjector(MembersInjector<UpsightExtension<UpsightMarketingComponent, UpsightMarketingApi>> supertypeInjector, Provider<UpsightMarketingApi> mMarketingProvider, Provider<MarketingContentFactory> mMarketingContentFactoryProvider, Provider<UpsightBillboardManager> mBillboardManagerProvider, Provider<DefaultContentMediator> mDefaultContentMediatorProvider) {
        if ($assertionsDisabled || supertypeInjector != null) {
            this.supertypeInjector = supertypeInjector;
            if ($assertionsDisabled || mMarketingProvider != null) {
                this.mMarketingProvider = mMarketingProvider;
                if ($assertionsDisabled || mMarketingContentFactoryProvider != null) {
                    this.mMarketingContentFactoryProvider = mMarketingContentFactoryProvider;
                    if ($assertionsDisabled || mBillboardManagerProvider != null) {
                        this.mBillboardManagerProvider = mBillboardManagerProvider;
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
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public void injectMembers(UpsightMarketingExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        this.supertypeInjector.injectMembers(instance);
        instance.mMarketing = (UpsightMarketingApi) this.mMarketingProvider.get();
        instance.mMarketingContentFactory = (MarketingContentFactory) this.mMarketingContentFactoryProvider.get();
        instance.mBillboardManager = (UpsightBillboardManager) this.mBillboardManagerProvider.get();
        instance.mDefaultContentMediator = (DefaultContentMediator) this.mDefaultContentMediatorProvider.get();
    }

    public static MembersInjector<UpsightMarketingExtension> create(MembersInjector<UpsightExtension<UpsightMarketingComponent, UpsightMarketingApi>> supertypeInjector, Provider<UpsightMarketingApi> mMarketingProvider, Provider<MarketingContentFactory> mMarketingContentFactoryProvider, Provider<UpsightBillboardManager> mBillboardManagerProvider, Provider<DefaultContentMediator> mDefaultContentMediatorProvider) {
        return new UpsightMarketingExtension_MembersInjector(supertypeInjector, mMarketingProvider, mMarketingContentFactoryProvider, mBillboardManagerProvider, mDefaultContentMediatorProvider);
    }
}
