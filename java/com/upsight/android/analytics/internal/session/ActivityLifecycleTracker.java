package com.upsight.android.analytics.internal.session;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import javax.inject.Inject;

@TargetApi(14)
public class ActivityLifecycleTracker implements ActivityLifecycleCallbacks {
    private ManualTracker mTracker;

    @Inject
    public ActivityLifecycleTracker(ManualTracker tracker) {
        this.mTracker = tracker;
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        this.mTracker.track(activity, ActivityState.CREATED, null);
    }

    public void onActivityStarted(Activity activity) {
        this.mTracker.track(activity, ActivityState.STARTED, null);
    }

    public void onActivityResumed(Activity activity) {
        this.mTracker.track(activity, ActivityState.RESUMED, null);
    }

    public void onActivityPaused(Activity activity) {
        this.mTracker.track(activity, ActivityState.PAUSED, null);
    }

    public void onActivityStopped(Activity activity) {
        this.mTracker.track(activity, ActivityState.STOPPED, null);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        this.mTracker.track(activity, ActivityState.SAVE_INSTANCE_STATE, null);
    }

    public void onActivityDestroyed(Activity activity) {
        this.mTracker.track(activity, ActivityState.DESTROYED, null);
    }
}
