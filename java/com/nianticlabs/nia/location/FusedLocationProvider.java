package com.nianticlabs.nia.location;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.GoogleApiManager;
import com.nianticlabs.nia.contextservice.GoogleApiManager.Listener;
import com.nianticlabs.nia.contextservice.ServiceStatus;
import com.nianticlabs.nia.location.Provider.ProviderListener;

public class FusedLocationProvider implements Provider {
    private static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final String TAG = "FusedLocationProvider";
    private AppState appState = AppState.STOP;
    private LocationListener fusedListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            ProviderListener listener = FusedLocationProvider.this.providerListener;
            if (listener != null) {
                listener.onProviderLocation(location);
            }
        }
    };
    Listener googleApiListener = new Listener() {
        public void onConnected() {
            FusedLocationProvider.this.googleApiState = GoogleApiState.STARTED;
            FusedLocationProvider.this.updateStatus(ServiceStatus.INITIALIZED);
            if (FusedLocationProvider.this.appState == AppState.RESUME) {
                FusedLocationProvider.this.startProvider();
            }
        }

        public void onDisconnected() {
            FusedLocationProvider.this.googleApiState = GoogleApiState.STOPPED;
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            FusedLocationProvider.this.googleApiState = GoogleApiState.STOPPED;
            if (connectionResult != null) {
                switch (connectionResult.getErrorCode()) {
                    case 19:
                        FusedLocationProvider.this.updateStatus(ServiceStatus.PERMISSION_DENIED);
                        return;
                    default:
                        FusedLocationProvider.this.updateStatus(ServiceStatus.FAILED);
                        return;
                }
            }
            FusedLocationProvider.this.updateStatus(ServiceStatus.FAILED);
        }
    };
    private final GoogleApiManager googleApiManager;
    private GoogleApiState googleApiState = GoogleApiState.STOPPED;
    private LocationRequest locationRequest = new LocationRequest();
    private ProviderListener providerListener = null;

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

    public FusedLocationProvider(Context context, int updateTime, float smallestDisplacement) {
        this.locationRequest.setInterval((long) updateTime);
        this.locationRequest.setFastestInterval((long) updateTime);
        this.locationRequest.setPriority(100);
        this.locationRequest.setSmallestDisplacement(smallestDisplacement);
        this.googleApiManager = new GoogleApiManager(context);
        this.googleApiManager.setListener(this.googleApiListener);
        this.googleApiManager.builder().addApi(LocationServices.API);
        this.googleApiManager.build();
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
            startProvider();
        }
        this.googleApiManager.onResume();
    }

    public void onPause() {
        this.appState = AppState.PAUSE;
        if (this.googleApiState == GoogleApiState.STARTED) {
            stopProvider();
        }
        this.googleApiManager.onPause();
    }

    public void setListener(ProviderListener listener) {
        this.providerListener = listener;
    }

    private void updateStatus(ServiceStatus status) {
        ProviderListener listener = this.providerListener;
        if (listener != null) {
            listener.onProviderStatus(status);
        }
    }

    private void startProvider() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiManager.getClient(), this.locationRequest, this.fusedListener, ContextService.getServiceLooper()).setResultCallback(new ResultCallback<Status>() {
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        FusedLocationProvider.this.updateStatus(ServiceStatus.RUNNING);
                    } else {
                        FusedLocationProvider.this.updateStatus(ServiceStatus.FAILED);
                    }
                }
            });
        } catch (SecurityException e) {
            updateStatus(ServiceStatus.PERMISSION_DENIED);
        }
    }

    private void stopProvider() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.googleApiManager.getClient(), this.fusedListener).setResultCallback(new ResultCallback<Status>() {
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    FusedLocationProvider.this.updateStatus(ServiceStatus.STOPPED);
                }
            }
        });
    }
}
