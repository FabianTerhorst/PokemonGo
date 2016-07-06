package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AnalyticsContext_Factory implements Factory<AnalyticsContext> {
    static final /* synthetic */ boolean $assertionsDisabled = (!AnalyticsContext_Factory.class.desiredAssertionStatus());
    private final MembersInjector<AnalyticsContext> membersInjector;
    private final Provider<UpsightContext> upsightProvider;

    public AnalyticsContext_Factory(MembersInjector<AnalyticsContext> membersInjector, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || membersInjector != null) {
            this.membersInjector = membersInjector;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public AnalyticsContext get() {
        AnalyticsContext instance = new AnalyticsContext((UpsightContext) this.upsightProvider.get());
        this.membersInjector.injectMembers(instance);
        return instance;
    }

    public static Factory<AnalyticsContext> create(MembersInjector<AnalyticsContext> membersInjector, Provider<UpsightContext> upsightProvider) {
        return new AnalyticsContext_Factory(membersInjector, upsightProvider);
    }
}
