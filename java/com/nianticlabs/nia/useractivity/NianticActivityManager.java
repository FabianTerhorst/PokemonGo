package com.nianticlabs.nia.useractivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.GoogleApiManager;
import com.nianticlabs.nia.contextservice.GoogleApiManager.Listener;
import com.nianticlabs.nia.contextservice.ServiceStatus;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import spacemadness.com.lunarconsole.R;

public class NianticActivityManager extends ContextService {
    private static final long ACTIVITY_DETECTION_INTERVAL_MS = 5000;
    private static final String TAG = "NianticActivityManager";
    private static WeakReference<NianticActivityManager> instance = null;
    private static Object instanceLock = new Object();
    private final PendingIntent activityRecognitionIntent;
    private AppState appState = AppState.STOP;
    Listener googleApiListener = new Listener() {
        public void onConnected() {
            if (NianticActivityManager.this.appState == AppState.RESUME) {
                NianticActivityManager.this.requestActivityUpdates();
            }
        }

        public void onDisconnected() {
            NianticActivityManager.this.googleApiState = GoogleApiState.STOPPED;
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            NianticActivityManager.this.googleApiState = GoogleApiState.STOPPED;
            switch (connectionResult.getErrorCode()) {
                case R.styleable.AdsAttrs_adSize /*0*/:
                    NianticActivityManager.this.status = ServiceStatus.INITIALIZED;
                    break;
                case 19:
                    NianticActivityManager.this.status = ServiceStatus.PERMISSION_DENIED;
                    break;
                default:
                    NianticActivityManager.this.status = ServiceStatus.FAILED;
                    break;
            }
            if (NianticActivityManager.this.status != ServiceStatus.INITIALIZED) {
                Log.e(NianticActivityManager.TAG, "Connection to activity recognition failed: " + connectionResult.getErrorMessage());
            }
            Log.d(NianticActivityManager.TAG, "Connection failed, updating status.");
            NianticActivityManager.this.safeUpdateActivity(null, NianticActivityManager.this.status.ordinal());
        }
    };
    private final GoogleApiManager googleApiManager;
    private GoogleApiState googleApiState = GoogleApiState.STOPPED;
    private ServiceStatus status;

    private enum AppState {
        START,
        STOP,
        PAUSE,
        RESUME
    }

    private enum GoogleApiState {
        STARTED,
        STOPPED
    }

    private native void nativeUpdateActivity(@Nullable long[] jArr, int i);

    public static Object getInstanceLock() {
        return instanceLock;
    }

    public static NianticActivityManager getInstance() {
        NianticActivityManager nianticActivityManager;
        synchronized (instanceLock) {
            if (instance != null) {
                nianticActivityManager = (NianticActivityManager) instance.get();
            } else {
                nianticActivityManager = null;
            }
        }
        return nianticActivityManager;
    }

    public NianticActivityManager(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.googleApiManager = new GoogleApiManager(context);
        this.googleApiManager.setListener(this.googleApiListener);
        this.googleApiManager.builder().addApi(ActivityRecognition.API);
        this.googleApiManager.build();
        this.activityRecognitionIntent = createPendingIntent(context);
        synchronized (instanceLock) {
            instance = new WeakReference(this);
        }
    }

    public static PendingIntent createPendingIntent(Context context) {
        return PendingIntent.getService(context, 0, new Intent(context, ActivityRecognitionService.class), 134217728);
    }

    private void requestActivityUpdates() {
        try {
            Status resultStatus = (Status) ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(this.googleApiManager.getClient(), ACTIVITY_DETECTION_INTERVAL_MS, this.activityRecognitionIntent).await(0, TimeUnit.MILLISECONDS);
            if (resultStatus.isSuccess()) {
                this.status = ServiceStatus.RUNNING;
            } else {
                this.status = ServiceStatus.FAILED;
                Log.d(TAG, "Request updates failed " + resultStatus.getStatusMessage());
                Log.d(TAG, "Request status has resolution " + resultStatus.hasResolution());
            }
        } catch (Exception e) {
            Log.e(TAG, "requestActivityUpdates throws" + e);
            e.printStackTrace();
            this.status = ServiceStatus.FAILED;
        }
        safeUpdateActivity(null, this.status.ordinal());
    }

    private void stopActivityUpdates() {
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(this.googleApiManager.getClient(), this.activityRecognitionIntent);
        this.status = ServiceStatus.STOPPED;
        safeUpdateActivity(null, this.status.ordinal());
    }

    public void receiveUpdateActivity(final ActivityRecognitionResult result) {
        ContextService.runOnServiceHandler(new Runnable() {
            public void run() {
                if (NianticActivityManager.this.appState == AppState.RESUME) {
                    NianticActivityManager.this.status = ServiceStatus.RUNNING;
                    long[] results = new long[((result.getProbableActivities().size() * 2) + 1)];
                    results[0] = result.getTime();
                    int i = 1;
                    for (DetectedActivity activity : result.getProbableActivities()) {
                        int i2 = i + 1;
                        results[i] = (long) activity.getType();
                        i = i2 + 1;
                        results[i2] = (long) activity.getConfidence();
                    }
                    NianticActivityManager.this.safeUpdateActivity(results, NianticActivityManager.this.status.ordinal());
                }
            }
        });
    }

    public void onStart() {
        this.appState = AppState.START;
        this.googleApiManager.onStart();
    }

    public void onStop() {
        this.appState = AppState.STOP;
        this.googleApiManager.onStop();
    }

    public void onResume() {
        this.appState = AppState.RESUME;
        if (this.googleApiState == GoogleApiState.STARTED) {
            requestActivityUpdates();
        }
        this.googleApiManager.onResume();
    }

    public void onPause() {
        this.appState = AppState.PAUSE;
        if (this.googleApiState == GoogleApiState.STARTED) {
            stopActivityUpdates();
        }
        this.googleApiManager.onPause();
    }

    private void safeUpdateActivity(@Nullable long[] result, int status) {
        synchronized (this.callbackLock) {
            nativeUpdateActivity(result, status);
        }
    }
}
