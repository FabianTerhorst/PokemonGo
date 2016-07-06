package com.upsight.android.analytics.internal.session;

import android.app.Application.ActivityLifecycleCallbacks;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LifecycleTrackerModule_ProvideUpsightLifeCycleCallbacksFactory implements Factory<ActivityLifecycleCallbacks> {
    static final /* synthetic */ boolean $assertionsDisabled = (!LifecycleTrackerModule_ProvideUpsightLifeCycleCallbacksFactory.class.desiredAssertionStatus());
    private final Provider<ActivityLifecycleTracker> handlerProvider;
    private final LifecycleTrackerModule module;

    public LifecycleTrackerModule_ProvideUpsightLifeCycleCallbacksFactory(LifecycleTrackerModule module, Provider<ActivityLifecycleTracker> handlerProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || handlerProvider != null) {
                this.handlerProvider = handlerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public ActivityLifecycleCallbacks get() {
        ActivityLifecycleCallbacks provided = this.module.provideUpsightLifeCycleCallbacks((ActivityLifecycleTracker) this.handlerProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ActivityLifecycleCallbacks> create(LifecycleTrackerModule module, Provider<ActivityLifecycleTracker> handlerProvider) {
        return new LifecycleTrackerModule_ProvideUpsightLifeCycleCallbacksFactory(module, handlerProvider);
    }
}
