package com.upsight.android.analytics.internal.session;

import android.app.Activity;
import android.content.Intent;
import com.squareup.otto.Bus;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityTrackEvent;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import spacemadness.com.lunarconsole.R;

@Singleton
class ManualTracker extends UpsightLifeCycleTracker {
    private static final String LOG_TAG = ManualTracker.class.getSimpleName();
    private Set<WeakReference<Activity>> mActivitySet;
    private Bus mBus;
    private UpsightDataStore mDataStore;
    private boolean mIsTaskRootStopped = false;
    private UpsightLogger mLogger;
    private SessionManager mSessionManager;

    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState = new int[ActivityState.values().length];

        static {
            try {
                $SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[ActivityState.STARTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[ActivityState.STOPPED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    @Inject
    public ManualTracker(SessionManager sessionManager, UpsightContext upsight) {
        this.mSessionManager = sessionManager;
        this.mDataStore = upsight.getDataStore();
        this.mBus = upsight.getCoreComponent().bus();
        this.mLogger = upsight.getLogger();
        this.mActivitySet = new HashSet();
    }

    public void track(Activity activity, ActivityState activityState, SessionInitializer sessionInitializer) {
        if (activity != null && activityState != null) {
            switch (AnonymousClass3.$SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[activityState.ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    this.mLogger.d(LOG_TAG, "Track starting of " + activity + " isTaskRoot=" + activity.isTaskRoot(), new Object[0]);
                    if (this.mActivitySet.isEmpty()) {
                        this.mDataStore.fetch(ApplicationStatus.class, new UpsightDataStoreListener<Set<ApplicationStatus>>() {
                            public void onSuccess(Set<ApplicationStatus> statusEvents) {
                                if (statusEvents.isEmpty()) {
                                    ManualTracker.this.mDataStore.store(new ApplicationStatus(State.FOREGROUND));
                                    ManualTracker.this.mLogger.d(ManualTracker.LOG_TAG, "Create application state " + State.FOREGROUND, new Object[0]);
                                    return;
                                }
                                boolean updatedFlag = false;
                                for (ApplicationStatus statusEvent : statusEvents) {
                                    if (updatedFlag) {
                                        ManualTracker.this.mDataStore.remove(statusEvent);
                                        ManualTracker.this.mLogger.w(ManualTracker.LOG_TAG, "Remove duplicate application state " + statusEvent.state, new Object[0]);
                                    } else {
                                        statusEvent.state = State.FOREGROUND;
                                        ManualTracker.this.mDataStore.store(statusEvent);
                                        updatedFlag = true;
                                        ManualTracker.this.mLogger.d(ManualTracker.LOG_TAG, "Update application state to " + statusEvent.state, new Object[0]);
                                    }
                                }
                            }

                            public void onFailure(UpsightException exception) {
                            }
                        });
                        Intent activityIntent = activity.getIntent();
                        if (activityIntent == null || !activityIntent.hasExtra(UpsightLifeCycleTracker.STARTED_FROM_PUSH)) {
                            this.mSessionManager.startSession(sessionInitializer);
                            this.mLogger.d(LOG_TAG, "Request to start new Upsight session", new Object[0]);
                        }
                    }
                    this.mActivitySet.add(new WeakReference(activity));
                    break;
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                    this.mLogger.d(LOG_TAG, "Track stopping of " + activity, new Object[0]);
                    removeAndPurge(this.mActivitySet, activity);
                    if (activity.isTaskRoot()) {
                        this.mIsTaskRootStopped = true;
                        this.mLogger.d(LOG_TAG, "Clear task root stopped condition with task root Activity " + activity, new Object[0]);
                    }
                    if (this.mIsTaskRootStopped && !activity.isChangingConfigurations() && this.mActivitySet.isEmpty()) {
                        this.mDataStore.fetch(ApplicationStatus.class, new UpsightDataStoreListener<Set<ApplicationStatus>>() {
                            public void onSuccess(Set<ApplicationStatus> statusEvents) {
                                if (statusEvents.isEmpty()) {
                                    ManualTracker.this.mDataStore.store(new ApplicationStatus(State.BACKGROUND));
                                    ManualTracker.this.mLogger.d(ManualTracker.LOG_TAG, "Create application state " + State.BACKGROUND, new Object[0]);
                                    return;
                                }
                                Iterator<ApplicationStatus> itr = statusEvents.iterator();
                                boolean updatedFlag = false;
                                while (itr.hasNext()) {
                                    ApplicationStatus statusEvent = (ApplicationStatus) itr.next();
                                    if (updatedFlag) {
                                        ManualTracker.this.mDataStore.remove(statusEvent);
                                        itr.remove();
                                        ManualTracker.this.mLogger.w(ManualTracker.LOG_TAG, "Remove duplicate application state " + statusEvent.state, new Object[0]);
                                    } else {
                                        statusEvent.state = State.BACKGROUND;
                                        ManualTracker.this.mDataStore.store(statusEvent);
                                        updatedFlag = true;
                                        ManualTracker.this.mLogger.d(ManualTracker.LOG_TAG, "Update application state to " + statusEvent.state, new Object[0]);
                                    }
                                }
                            }

                            public void onFailure(UpsightException exception) {
                            }
                        });
                        this.mSessionManager.stopSession();
                        this.mLogger.d(LOG_TAG, "Request to stop current Upsight session", new Object[0]);
                        break;
                    }
            }
            this.mBus.post(new ActivityTrackEvent(activity, activityState));
        }
    }

    private static void removeAndPurge(Set<WeakReference<Activity>> activitySet, Activity reference) {
        Iterator<WeakReference<Activity>> itr = activitySet.iterator();
        while (itr.hasNext()) {
            Activity activity = (Activity) ((WeakReference) itr.next()).get();
            if (activity == reference || isPurgeable(activity)) {
                itr.remove();
            }
        }
    }

    private static boolean isPurgeable(Activity activity) {
        return activity == null;
    }
}
