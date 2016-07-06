package com.upsight.android.internal;

import dagger.internal.Factory;
import rx.Scheduler;

public final class SchedulersModule_ProvideSubscribeOnSchedulerFactory implements Factory<Scheduler> {
    static final /* synthetic */ boolean $assertionsDisabled = (!SchedulersModule_ProvideSubscribeOnSchedulerFactory.class.desiredAssertionStatus());
    private final SchedulersModule module;

    public SchedulersModule_ProvideSubscribeOnSchedulerFactory(SchedulersModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Scheduler get() {
        Scheduler provided = this.module.provideSubscribeOnScheduler();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Scheduler> create(SchedulersModule module) {
        return new SchedulersModule_ProvideSubscribeOnSchedulerFactory(module);
    }
}
