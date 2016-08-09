package com.upsight.android.analytics.internal;

import com.upsight.android.analytics.internal.session.Clock;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BaseAnalyticsModule_ProvideClockFactory implements Factory<Clock> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseAnalyticsModule_ProvideClockFactory.class.desiredAssertionStatus());
    private final BaseAnalyticsModule module;

    public BaseAnalyticsModule_ProvideClockFactory(BaseAnalyticsModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Clock get() {
        return (Clock) Preconditions.checkNotNull(this.module.provideClock(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Clock> create(BaseAnalyticsModule module) {
        return new BaseAnalyticsModule_ProvideClockFactory(module);
    }
}
