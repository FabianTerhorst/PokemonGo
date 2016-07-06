package com.upsight.android.analytics.internal.session;

import com.upsight.android.analytics.UpsightLifeCycleTracker;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LifecycleTrackerModule_ProvideManualTrackerFactory implements Factory<UpsightLifeCycleTracker> {
    static final /* synthetic */ boolean $assertionsDisabled = (!LifecycleTrackerModule_ProvideManualTrackerFactory.class.desiredAssertionStatus());
    private final LifecycleTrackerModule module;
    private final Provider<ManualTracker> trackerProvider;

    public LifecycleTrackerModule_ProvideManualTrackerFactory(LifecycleTrackerModule module, Provider<ManualTracker> trackerProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || trackerProvider != null) {
                this.trackerProvider = trackerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightLifeCycleTracker get() {
        UpsightLifeCycleTracker provided = this.module.provideManualTracker((ManualTracker) this.trackerProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightLifeCycleTracker> create(LifecycleTrackerModule module, Provider<ManualTracker> trackerProvider) {
        return new LifecycleTrackerModule_ProvideManualTrackerFactory(module, trackerProvider);
    }
}
