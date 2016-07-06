package com.upsight.android.analytics.internal.session;

import android.app.Activity;
import android.content.Intent;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
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
class ManualTracker implements UpsightLifeCycleTracker {
    private Set<WeakReference<Activity>> mActivitySet = new HashSet();
    private UpsightDataStore mDataStore;
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
    }

    public void track(Activity activity, ActivityState activityState, SessionInitializer sessionInitializer) {
        switch (AnonymousClass3.$SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[activityState.ordinal()]) {
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                if (this.mActivitySet.isEmpty()) {
                    this.mDataStore.fetch(ApplicationStatus.class, new UpsightDataStoreListener<Set<ApplicationStatus>>() {
                        public void onSuccess(Set<ApplicationStatus> statusEvents) {
                            if (statusEvents.isEmpty()) {
                                ManualTracker.this.mDataStore.store(new ApplicationStatus(State.FOREGROUND));
                                return;
                            }
                            boolean updatedFlag = false;
                            for (ApplicationStatus statusEvent : statusEvents) {
                                if (updatedFlag) {
                                    ManualTracker.this.mDataStore.remove(statusEvent);
                                } else {
                                    statusEvent.state = State.FOREGROUND;
                                    ManualTracker.this.mDataStore.store(statusEvent);
                                    updatedFlag = true;
                                }
                            }
                        }

                        public void onFailure(UpsightException exception) {
                        }
                    });
                    Intent activityIntent = activity.getIntent();
                    if (activityIntent == null || !activityIntent.hasExtra(UpsightLifeCycleTracker.STARTED_FROM_PUSH)) {
                        this.mSessionManager.startSession(sessionInitializer);
                    }
                }
                this.mActivitySet.add(new WeakReference(activity));
                return;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                removeAndPurge(this.mActivitySet, activity);
                if (!activity.isChangingConfigurations() && this.mActivitySet.isEmpty()) {
                    this.mDataStore.fetch(ApplicationStatus.class, new UpsightDataStoreListener<Set<ApplicationStatus>>() {
                        public void onSuccess(Set<ApplicationStatus> statusEvents) {
                            if (statusEvents.isEmpty()) {
                                ManualTracker.this.mDataStore.store(new ApplicationStatus(State.BACKGROUND));
                                return;
                            }
                            Iterator<ApplicationStatus> itr = statusEvents.iterator();
                            boolean updatedFlag = false;
                            while (itr.hasNext()) {
                                ApplicationStatus statusEvent = (ApplicationStatus) itr.next();
                                if (updatedFlag) {
                                    ManualTracker.this.mDataStore.remove(statusEvent);
                                    itr.remove();
                                } else {
                                    statusEvent.state = State.BACKGROUND;
                                    ManualTracker.this.mDataStore.store(statusEvent);
                                    updatedFlag = true;
                                }
                            }
                        }

                        public void onFailure(UpsightException exception) {
                        }
                    });
                    this.mSessionManager.stopSession();
                    return;
                }
                return;
            default:
                return;
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
