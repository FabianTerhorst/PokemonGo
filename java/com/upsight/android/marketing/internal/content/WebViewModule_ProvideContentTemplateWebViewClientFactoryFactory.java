package com.upsight.android.marketing.internal.content;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WebViewModule_ProvideContentTemplateWebViewClientFactoryFactory implements Factory<ContentTemplateWebViewClientFactory> {
    static final /* synthetic */ boolean $assertionsDisabled = (!WebViewModule_ProvideContentTemplateWebViewClientFactoryFactory.class.desiredAssertionStatus());
    private final WebViewModule module;
    private final Provider<UpsightContext> upsightProvider;

    public WebViewModule_ProvideContentTemplateWebViewClientFactoryFactory(WebViewModule module, Provider<UpsightContext> upsightProvider) {
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

    public ContentTemplateWebViewClientFactory get() {
        return (ContentTemplateWebViewClientFactory) Preconditions.checkNotNull(this.module.provideContentTemplateWebViewClientFactory((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ContentTemplateWebViewClientFactory> create(WebViewModule module, Provider<UpsightContext> upsightProvider) {
        return new WebViewModule_ProvideContentTemplateWebViewClientFactoryFactory(module, upsightProvider);
    }
}
