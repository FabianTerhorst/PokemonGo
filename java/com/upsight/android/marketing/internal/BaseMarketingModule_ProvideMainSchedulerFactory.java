package com.upsight.android.marketing.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import rx.Scheduler;

public final class BaseMarketingModule_ProvideMainSchedulerFactory implements Factory<Scheduler> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseMarketingModule_ProvideMainSchedulerFactory.class.desiredAssertionStatus());
    private final BaseMarketingModule module;

    public BaseMarketingModule_ProvideMainSchedulerFactory(BaseMarketingModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Scheduler get() {
        return (Scheduler) Preconditions.checkNotNull(this.module.provideMainScheduler(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Scheduler> create(BaseMarketingModule module) {
        return new BaseMarketingModule_ProvideMainSchedulerFactory(module);
    }
}
