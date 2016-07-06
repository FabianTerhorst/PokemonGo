package com.upsight.android.managedvariables.internal;

import dagger.internal.Factory;
import rx.Scheduler;

public final class BaseManagedVariablesModule_ProvideMainSchedulerFactory implements Factory<Scheduler> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseManagedVariablesModule_ProvideMainSchedulerFactory.class.desiredAssertionStatus());
    private final BaseManagedVariablesModule module;

    public BaseManagedVariablesModule_ProvideMainSchedulerFactory(BaseManagedVariablesModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Scheduler get() {
        Scheduler provided = this.module.provideMainScheduler();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Scheduler> create(BaseManagedVariablesModule module) {
        return new BaseManagedVariablesModule_ProvideMainSchedulerFactory(module);
    }
}
