package com.nianticlabs.nia.useractivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;

public class ActivityRecognitionService extends IntentService {
    private static final String TAG = "NianticActivityManager";
    private GoogleApiClient googleApiClient = null;

    public ActivityRecognitionService() {
        super("ActivityRecognitionService");
    }

    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Log.d(TAG, "Got activity result" + result.getMostProbableActivity());
        NianticActivityManager activityManager = NianticActivityManager.getInstance();
        if (activityManager != null) {
            activityManager.receiveUpdateActivity(result);
            return;
        }
        Log.e(TAG, "The app has closed while the ActivityRecognitionService is still receiving updates and draining the phone's battery");
        unregisterIntent();
    }

    private void unregisterIntent() {
        this.googleApiClient = new Builder(this).addApi(ActivityRecognition.API).addConnectionCallbacks(new ConnectionCallbacks() {
            public void onConnected(Bundle bundle) {
                synchronized (NianticActivityManager.getInstanceLock()) {
                    if (NianticActivityManager.getInstance() == null) {
                        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(ActivityRecognitionService.this.googleApiClient, NianticActivityManager.createPendingIntent(ActivityRecognitionService.this));
                        ActivityRecognitionService.this.googleApiClient.disconnect();
                    }
                }
            }

            public void onConnectionSuspended(int i) {
            }
        }).build();
        this.googleApiClient.connect();
    }
}
