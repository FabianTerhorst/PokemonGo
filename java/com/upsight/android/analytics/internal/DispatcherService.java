package com.upsight.android.analytics.internal;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import com.squareup.otto.Bus;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.configuration.ConfigurationManager;
import com.upsight.android.analytics.internal.dispatcher.Dispatcher;
import com.upsight.android.analytics.internal.session.ApplicationStatus;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStoreListener;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import com.upsight.android.persistence.annotation.Updated;
import java.util.Set;
import javax.inject.Inject;

public class DispatcherService extends Service {
    private static final String LOG_TAG = DispatcherService.class.getSimpleName();
    private static final long STATUS_CHECK_INTERVAL = 25000;
    private static final int STOP_AFTER_DEAD_INTERVALS = 4;
    private Bus mBus;
    @Inject
    ConfigurationManager mConfigurationManager;
    private UpsightSubscription mDataStoreSubscription;
    private int mDeadIntervalsInARow;
    @Inject
    Dispatcher mDispatcher;
    private Handler mHandler;
    private UpsightLogger mLogger;
    private Runnable mSelfStopTask = new Runnable() {
        public void run() {
            boolean hasPendingRecords = DispatcherService.this.mDispatcher.hasPendingRecords();
            DispatcherService.this.mLogger.d(DispatcherService.LOG_TAG, "Check for idle hasPendingRecords=" + hasPendingRecords + " mDeadIntervalsInARow=" + DispatcherService.this.mDeadIntervalsInARow, new Object[0]);
            if (hasPendingRecords) {
                DispatcherService.this.mDeadIntervalsInARow = 0;
                DispatcherService.this.mHandler.postDelayed(DispatcherService.this.mSelfStopTask, DispatcherService.STATUS_CHECK_INTERVAL);
            } else if (DispatcherService.this.mDeadIntervalsInARow == DispatcherService.STOP_AFTER_DEAD_INTERVALS) {
                DispatcherService.this.mLogger.d(DispatcherService.LOG_TAG, "Request to destroy", new Object[0]);
                DispatcherService.this.stopSelf();
            } else {
                DispatcherService.this.mDeadIntervalsInARow = DispatcherService.this.mDeadIntervalsInARow + 1;
                DispatcherService.this.mHandler.postDelayed(DispatcherService.this.mSelfStopTask, DispatcherService.STATUS_CHECK_INTERVAL);
            }
        }
    };

    public static final class DestroyEvent {
    }

    public void onCreate() {
        super.onCreate();
        UpsightContext upsight = Upsight.createContext(this);
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            ((UpsightAnalyticsComponent) extension.getComponent()).inject(this);
            this.mBus = upsight.getCoreComponent().bus();
            this.mLogger = upsight.getLogger();
            this.mLogger.d(LOG_TAG, "onCreate()", new Object[0]);
            this.mHandler = new Handler();
            this.mDataStoreSubscription = upsight.getDataStore().subscribe(this);
            this.mDispatcher.launch();
            this.mConfigurationManager.launch();
            upsight.getDataStore().fetch(ApplicationStatus.class, new UpsightDataStoreListener<Set<ApplicationStatus>>() {
                public void onSuccess(Set<ApplicationStatus> result) {
                    for (ApplicationStatus appStatus : result) {
                        DispatcherService.this.handle(appStatus);
                    }
                }

                public void onFailure(UpsightException exception) {
                }
            });
        }
    }

    @Updated
    @Created
    public void onApplicationStatus(ApplicationStatus appStatus) {
        handle(appStatus);
    }

    public void onDestroy() {
        if (this.mBus != null) {
            this.mBus.post(new DestroyEvent());
        }
        this.mHandler.removeCallbacks(this.mSelfStopTask);
        this.mDataStoreSubscription.unsubscribe();
        this.mLogger.d(LOG_TAG, "onDestroy()", new Object[0]);
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handle(ApplicationStatus appStatus) {
        if (appStatus.getState() == State.BACKGROUND) {
            this.mDeadIntervalsInARow = 0;
            this.mHandler.postDelayed(this.mSelfStopTask, STATUS_CHECK_INTERVAL);
            return;
        }
        this.mHandler.removeCallbacks(this.mSelfStopTask);
    }
}
