package com.upsight.android.analytics.internal.session;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class ActivityLifecycleTracker_Factory implements Factory<ActivityLifecycleTracker> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ActivityLifecycleTracker_Factory.class.desiredAssertionStatus());
    private final Provider<ManualTracker> trackerProvider;

    public ActivityLifecycleTracker_Factory(Provider<ManualTracker> trackerProvider) {
        if ($assertionsDisabled || trackerProvider != null) {
            this.trackerProvider = trackerProvider;
            return;
        }
        throw new AssertionError();
    }

    public ActivityLifecycleTracker get() {
        return new ActivityLifecycleTracker((ManualTracker) this.trackerProvider.get());
    }

    public static Factory<ActivityLifecycleTracker> create(Provider<ManualTracker> trackerProvider) {
        return new ActivityLifecycleTracker_Factory(trackerProvider);
    }
}
