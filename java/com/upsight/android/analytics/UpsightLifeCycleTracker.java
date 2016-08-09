package com.upsight.android.analytics;

import android.app.Activity;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.SessionInitializer;

public abstract class UpsightLifeCycleTracker {
    public static final String STARTED_FROM_PUSH = "pushMessage";

    public enum ActivityState {
        CREATED,
        STARTED,
        RESUMED,
        PAUSED,
        STOPPED,
        SAVE_INSTANCE_STATE,
        DESTROYED
    }

    public static final class ActivityTrackEvent {
        public final Activity mActivity;
        public final ActivityState mActivityState;

        public ActivityTrackEvent(Activity activity, ActivityState activityState) {
            this.mActivity = activity;
            this.mActivityState = activityState;
        }
    }

    public abstract void track(Activity activity, ActivityState activityState, SessionInitializer sessionInitializer);

    public static void track(UpsightContext upsight, Activity activity, ActivityState activityState) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().trackActivity(activity, activityState);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }
}
