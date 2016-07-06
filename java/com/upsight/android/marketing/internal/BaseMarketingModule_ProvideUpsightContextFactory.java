package com.upsight.android.marketing.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;

public final class BaseMarketingModule_ProvideUpsightContextFactory implements Factory<UpsightContext> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseMarketingModule_ProvideUpsightContextFactory.class.desiredAssertionStatus());
    private final BaseMarketingModule module;

    public BaseMarketingModule_ProvideUpsightContextFactory(BaseMarketingModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public UpsightContext get() {
        UpsightContext provided = this.module.provideUpsightContext();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightContext> create(BaseMarketingModule module) {
        return new BaseMarketingModule_ProvideUpsightContextFactory(module);
    }
}
