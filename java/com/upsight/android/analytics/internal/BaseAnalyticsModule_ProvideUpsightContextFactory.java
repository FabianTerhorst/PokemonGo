package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BaseAnalyticsModule_ProvideUpsightContextFactory implements Factory<UpsightContext> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseAnalyticsModule_ProvideUpsightContextFactory.class.desiredAssertionStatus());
    private final BaseAnalyticsModule module;

    public BaseAnalyticsModule_ProvideUpsightContextFactory(BaseAnalyticsModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public UpsightContext get() {
        return (UpsightContext) Preconditions.checkNotNull(this.module.provideUpsightContext(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightContext> create(BaseAnalyticsModule module) {
        return new BaseAnalyticsModule_ProvideUpsightContextFactory(module);
    }
}
