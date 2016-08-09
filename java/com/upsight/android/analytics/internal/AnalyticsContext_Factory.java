package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class AnalyticsContext_Factory implements Factory<AnalyticsContext> {
    static final /* synthetic */ boolean $assertionsDisabled = (!AnalyticsContext_Factory.class.desiredAssertionStatus());
    private final MembersInjector<AnalyticsContext> analyticsContextMembersInjector;
    private final Provider<UpsightContext> upsightProvider;

    public AnalyticsContext_Factory(MembersInjector<AnalyticsContext> analyticsContextMembersInjector, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || analyticsContextMembersInjector != null) {
            this.analyticsContextMembersInjector = analyticsContextMembersInjector;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public AnalyticsContext get() {
        return (AnalyticsContext) MembersInjectors.injectMembers(this.analyticsContextMembersInjector, new AnalyticsContext((UpsightContext) this.upsightProvider.get()));
    }

    public static Factory<AnalyticsContext> create(MembersInjector<AnalyticsContext> analyticsContextMembersInjector, Provider<UpsightContext> upsightProvider) {
        return new AnalyticsContext_Factory(analyticsContextMembersInjector, upsightProvider);
    }
}
