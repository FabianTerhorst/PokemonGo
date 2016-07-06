package com.upsight.android.marketing.internal.content;

import dagger.internal.Factory;

public final class ContentModule_ProvideDefaultContentMediatorFactory implements Factory<DefaultContentMediator> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContentModule_ProvideDefaultContentMediatorFactory.class.desiredAssertionStatus());
    private final ContentModule module;

    public ContentModule_ProvideDefaultContentMediatorFactory(ContentModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public DefaultContentMediator get() {
        DefaultContentMediator provided = this.module.provideDefaultContentMediator();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<DefaultContentMediator> create(ContentModule module) {
        return new ContentModule_ProvideDefaultContentMediatorFactory(module);
    }
}
