package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class BaseAnalyticsModule_ProvideGooglePlayHelperFactory implements Factory<UpsightGooglePlayHelper> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseAnalyticsModule_ProvideGooglePlayHelperFactory.class.desiredAssertionStatus());
    private final BaseAnalyticsModule module;
    private final Provider<UpsightContext> upsightProvider;

    public BaseAnalyticsModule_ProvideGooglePlayHelperFactory(BaseAnalyticsModule module, Provider<UpsightContext> upsightProvider) {
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

    public UpsightGooglePlayHelper get() {
        return (UpsightGooglePlayHelper) Preconditions.checkNotNull(this.module.provideGooglePlayHelper((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightGooglePlayHelper> create(BaseAnalyticsModule module, Provider<UpsightContext> upsightProvider) {
        return new BaseAnalyticsModule_ProvideGooglePlayHelperFactory(module, upsightProvider);
    }
}
