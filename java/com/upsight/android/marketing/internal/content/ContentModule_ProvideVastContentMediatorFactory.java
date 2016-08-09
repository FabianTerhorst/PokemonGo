package com.upsight.android.marketing.internal.content;

import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.internal.vast.VastContentMediator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ContentModule_ProvideVastContentMediatorFactory implements Factory<VastContentMediator> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContentModule_ProvideVastContentMediatorFactory.class.desiredAssertionStatus());
    private final ContentModule module;
    private final Provider<UpsightContext> upsightProvider;

    public ContentModule_ProvideVastContentMediatorFactory(ContentModule module, Provider<UpsightContext> upsightProvider) {
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

    public VastContentMediator get() {
        return (VastContentMediator) Preconditions.checkNotNull(this.module.provideVastContentMediator((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<VastContentMediator> create(ContentModule module, Provider<UpsightContext> upsightProvider) {
        return new ContentModule_ProvideVastContentMediatorFactory(module, upsightProvider);
    }
}
