package com.upsight.android.marketing.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightMarketingExtension;
import com.upsight.android.UpsightMarketingExtension_MembersInjector;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.billboard.BillboardDialogFragment;
import com.upsight.android.marketing.internal.billboard.BillboardDialogFragment_MembersInjector;
import com.upsight.android.marketing.internal.billboard.BillboardManagementActivity;
import com.upsight.android.marketing.internal.billboard.BillboardManagementActivity_MembersInjector;
import com.upsight.android.marketing.internal.billboard.BillboardModule;
import com.upsight.android.marketing.internal.billboard.BillboardModule_ProvideBillboardManagerFactory;
import com.upsight.android.marketing.internal.content.ContentModule;
import com.upsight.android.marketing.internal.content.ContentModule_ProvideDefaultContentMediatorFactory;
import com.upsight.android.marketing.internal.content.ContentModule_ProvideMarketingContentFactoryFactory;
import com.upsight.android.marketing.internal.content.ContentModule_ProvideMarketingContentMediatorManagerFactory;
import com.upsight.android.marketing.internal.content.ContentModule_ProvideMarketingContentStoreFactory;
import com.upsight.android.marketing.internal.content.ContentModule_ProvideMarketingContentStoreImplFactory;
import com.upsight.android.marketing.internal.content.ContentModule_ProvideUpsightMarketingContentStoreFactory;
import com.upsight.android.marketing.internal.content.ContentTemplateWebViewClientFactory;
import com.upsight.android.marketing.internal.content.DefaultContentMediator;
import com.upsight.android.marketing.internal.content.MarketingContentFactory;
import com.upsight.android.marketing.internal.content.MarketingContentMediatorManager;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import com.upsight.android.marketing.internal.content.WebViewModule;
import com.upsight.android.marketing.internal.content.WebViewModule_ProvideContentTemplateWebViewClientFactoryFactory;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import rx.Scheduler;

public final class DaggerMarketingComponent implements MarketingComponent {
    static final /* synthetic */ boolean $assertionsDisabled = (!DaggerMarketingComponent.class.desiredAssertionStatus());
    private MembersInjector<BillboardDialogFragment> billboardDialogFragmentMembersInjector;
    private MembersInjector<BillboardManagementActivity> billboardManagementActivityMembersInjector;
    private Provider<UpsightBillboardManager> provideBillboardManagerProvider;
    private Provider<ContentTemplateWebViewClientFactory> provideContentTemplateWebViewClientFactoryProvider;
    private Provider<DefaultContentMediator> provideDefaultContentMediatorProvider;
    private Provider<Scheduler> provideMainSchedulerProvider;
    private Provider<UpsightMarketingApi> provideMarketingApiProvider;
    private Provider<MarketingContentFactory> provideMarketingContentFactoryProvider;
    private Provider<MarketingContentMediatorManager> provideMarketingContentMediatorManagerProvider;
    private Provider provideMarketingContentStoreImplProvider;
    private Provider<MarketingContentStore> provideMarketingContentStoreProvider;
    private Provider<UpsightContext> provideUpsightContextProvider;
    private Provider<UpsightMarketingContentStore> provideUpsightMarketingContentStoreProvider;
    private MembersInjector<UpsightMarketingExtension> upsightMarketingExtensionMembersInjector;

    public static final class Builder {
        private BaseMarketingModule baseMarketingModule;
        private BillboardModule billboardModule;
        private ContentModule contentModule;
        private WebViewModule webViewModule;

        private Builder() {
        }

        public MarketingComponent build() {
            if (this.baseMarketingModule == null) {
                throw new IllegalStateException(BaseMarketingModule.class.getCanonicalName() + " must be set");
            }
            if (this.contentModule == null) {
                this.contentModule = new ContentModule();
            }
            if (this.billboardModule == null) {
                this.billboardModule = new BillboardModule();
            }
            if (this.webViewModule == null) {
                this.webViewModule = new WebViewModule();
            }
            return new DaggerMarketingComponent();
        }

        @Deprecated
        public Builder marketingModule(MarketingModule marketingModule) {
            Preconditions.checkNotNull(marketingModule);
            return this;
        }

        public Builder billboardModule(BillboardModule billboardModule) {
            this.billboardModule = (BillboardModule) Preconditions.checkNotNull(billboardModule);
            return this;
        }

        public Builder contentModule(ContentModule contentModule) {
            this.contentModule = (ContentModule) Preconditions.checkNotNull(contentModule);
            return this;
        }

        public Builder webViewModule(WebViewModule webViewModule) {
            this.webViewModule = (WebViewModule) Preconditions.checkNotNull(webViewModule);
            return this;
        }

        public Builder baseMarketingModule(BaseMarketingModule baseMarketingModule) {
            this.baseMarketingModule = (BaseMarketingModule) Preconditions.checkNotNull(baseMarketingModule);
            return this;
        }
    }

    private DaggerMarketingComponent(Builder builder) {
        if ($assertionsDisabled || builder != null) {
            initialize(builder);
            return;
        }
        throw new AssertionError();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void initialize(Builder builder) {
        this.provideUpsightContextProvider = DoubleCheck.provider(BaseMarketingModule_ProvideUpsightContextFactory.create(builder.baseMarketingModule));
        this.provideMarketingContentStoreImplProvider = DoubleCheck.provider(ContentModule_ProvideMarketingContentStoreImplFactory.create(builder.contentModule, this.provideUpsightContextProvider));
        this.provideMarketingContentStoreProvider = DoubleCheck.provider(ContentModule_ProvideMarketingContentStoreFactory.create(builder.contentModule, this.provideMarketingContentStoreImplProvider));
        this.provideBillboardManagerProvider = DoubleCheck.provider(BillboardModule_ProvideBillboardManagerFactory.create(builder.billboardModule, this.provideUpsightContextProvider, this.provideMarketingContentStoreProvider));
        this.provideUpsightMarketingContentStoreProvider = DoubleCheck.provider(ContentModule_ProvideUpsightMarketingContentStoreFactory.create(builder.contentModule, this.provideMarketingContentStoreImplProvider));
        this.provideDefaultContentMediatorProvider = DoubleCheck.provider(ContentModule_ProvideDefaultContentMediatorFactory.create(builder.contentModule));
        this.provideMarketingContentMediatorManagerProvider = DoubleCheck.provider(ContentModule_ProvideMarketingContentMediatorManagerFactory.create(builder.contentModule, this.provideDefaultContentMediatorProvider));
        this.provideMarketingApiProvider = DoubleCheck.provider(BaseMarketingModule_ProvideMarketingApiFactory.create(builder.baseMarketingModule, this.provideBillboardManagerProvider, this.provideUpsightMarketingContentStoreProvider, this.provideMarketingContentMediatorManagerProvider));
        this.provideMainSchedulerProvider = DoubleCheck.provider(BaseMarketingModule_ProvideMainSchedulerFactory.create(builder.baseMarketingModule));
        this.provideContentTemplateWebViewClientFactoryProvider = DoubleCheck.provider(WebViewModule_ProvideContentTemplateWebViewClientFactoryFactory.create(builder.webViewModule, this.provideUpsightContextProvider));
        this.provideMarketingContentFactoryProvider = DoubleCheck.provider(ContentModule_ProvideMarketingContentFactoryFactory.create(builder.contentModule, this.provideUpsightContextProvider, this.provideMainSchedulerProvider, this.provideMarketingContentMediatorManagerProvider, this.provideMarketingContentStoreProvider, this.provideContentTemplateWebViewClientFactoryProvider));
        this.upsightMarketingExtensionMembersInjector = UpsightMarketingExtension_MembersInjector.create(this.provideMarketingApiProvider, this.provideMarketingContentFactoryProvider, this.provideDefaultContentMediatorProvider);
        this.billboardManagementActivityMembersInjector = BillboardManagementActivity_MembersInjector.create(this.provideUpsightContextProvider, this.provideMarketingContentStoreProvider);
        this.billboardDialogFragmentMembersInjector = BillboardDialogFragment_MembersInjector.create(this.provideUpsightContextProvider, this.provideMarketingContentStoreProvider);
    }

    public void inject(UpsightMarketingExtension arg0) {
        this.upsightMarketingExtensionMembersInjector.injectMembers(arg0);
    }

    public void inject(BillboardManagementActivity activity) {
        this.billboardManagementActivityMembersInjector.injectMembers(activity);
    }

    public void inject(BillboardDialogFragment fragment) {
        this.billboardDialogFragmentMembersInjector.injectMembers(fragment);
    }
}
