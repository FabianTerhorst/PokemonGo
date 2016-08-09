package com.upsight.android.analytics.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import rx.Scheduler;

public final class AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory implements Factory<Scheduler> {
    static final /* synthetic */ boolean $assertionsDisabled = (!AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory.class.desiredAssertionStatus());
    private final AnalyticsSchedulersModule module;

    public AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory(AnalyticsSchedulersModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Scheduler get() {
        return (Scheduler) Preconditions.checkNotNull(this.module.provideSchedulingExecutor(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Scheduler> create(AnalyticsSchedulersModule module) {
        return new AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory(module);
    }
}
