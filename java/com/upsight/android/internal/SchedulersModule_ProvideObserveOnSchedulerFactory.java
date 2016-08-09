package com.upsight.android.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import rx.Scheduler;

public final class SchedulersModule_ProvideObserveOnSchedulerFactory implements Factory<Scheduler> {
    static final /* synthetic */ boolean $assertionsDisabled = (!SchedulersModule_ProvideObserveOnSchedulerFactory.class.desiredAssertionStatus());
    private final SchedulersModule module;

    public SchedulersModule_ProvideObserveOnSchedulerFactory(SchedulersModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Scheduler get() {
        return (Scheduler) Preconditions.checkNotNull(this.module.provideObserveOnScheduler(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Scheduler> create(SchedulersModule module) {
        return new SchedulersModule_ProvideObserveOnSchedulerFactory(module);
    }
}
