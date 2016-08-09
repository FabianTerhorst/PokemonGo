package com.upsight.android.analytics.internal;

import com.upsight.android.analytics.UpsightAnalyticsApi;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AnalyticsApiModule_ProvideUpsightAnalyticsApiFactory implements Factory<UpsightAnalyticsApi> {
    static final /* synthetic */ boolean $assertionsDisabled = (!AnalyticsApiModule_ProvideUpsightAnalyticsApiFactory.class.desiredAssertionStatus());
    private final Provider<Analytics> analyticsProvider;
    private final AnalyticsApiModule module;

    public AnalyticsApiModule_ProvideUpsightAnalyticsApiFactory(AnalyticsApiModule module, Provider<Analytics> analyticsProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || analyticsProvider != null) {
                this.analyticsProvider = analyticsProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightAnalyticsApi get() {
        return (UpsightAnalyticsApi) Preconditions.checkNotNull(this.module.provideUpsightAnalyticsApi((Analytics) this.analyticsProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightAnalyticsApi> create(AnalyticsApiModule module, Provider<Analytics> analyticsProvider) {
        return new AnalyticsApiModule_ProvideUpsightAnalyticsApiFactory(module, analyticsProvider);
    }
}
