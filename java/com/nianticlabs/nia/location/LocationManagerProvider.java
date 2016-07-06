package com.nianticlabs.nia.location;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.ServiceStatus;
import com.nianticlabs.nia.location.GpsProvider.GpsProviderListener;
import com.nianticlabs.nia.location.Provider.ProviderListener;
import java.util.ArrayList;
import java.util.List;

public class LocationManagerProvider implements GpsProvider {
    private static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final String TAG = "LocationManagerProvider";
    private final Context context;
    private boolean firstLocationUpdate = ENABLE_VERBOSE_LOGS;
    private final Listener gpsStatusListener = new Listener() {
        private GpsSatellite[] getSatellites(GpsStatus gpsStatus) {
            List<GpsSatellite> list = new ArrayList();
            for (GpsSatellite sat : gpsStatus.getSatellites()) {
                list.add(sat);
            }
            return (GpsSatellite[]) list.toArray(new GpsSatellite[list.size()]);
        }

        public void onGpsStatusChanged(int event) {
            if (LocationManagerProvider.this.running) {
                GpsStatus status = LocationManagerProvider.this.locationManager.getGpsStatus(null);
                LocationManagerProvider.this.updateGpsStatus(status.getTimeToFirstFix(), getSatellites(status));
            }
        }
    };
    private LocationListener listener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (LocationManagerProvider.this.running) {
                LocationManagerProvider.this.updateLocation(location);
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
            LocationManagerProvider.this.updateStatus(ServiceStatus.RUNNING);
        }

        public void onProviderDisabled(String provider) {
            LocationManagerProvider.this.updateStatus(ServiceStatus.PERMISSION_DENIED);
        }
    };
    private LocationManager locationManager;
    private final String provider;
    private ProviderListener providerListener = null;
    private boolean running = ENABLE_VERBOSE_LOGS;
    private final float updateDistance;
    private final int updateTime;

    public LocationManagerProvider(Context context, String provider, int updateTime, float updateDistance) {
        this.context = context;
        this.provider = provider;
        this.updateTime = updateTime;
        this.updateDistance = updateDistance;
    }

    public void onStart() {
        this.locationManager = (LocationManager) this.context.getSystemService("location");
    }

    public void onStop() {
        this.locationManager = null;
    }

    public void onPause() {
        if (this.running) {
            try {
                this.locationManager.removeUpdates(this.listener);
                this.running = ENABLE_VERBOSE_LOGS;
            } catch (SecurityException e) {
                Log.e(TAG, "Not allowed to access " + this.provider + " for updates", e);
            }
            updateStatus(ServiceStatus.STOPPED);
        }
    }

    public void onResume() {
        this.firstLocationUpdate = true;
        ServiceStatus statusFailed = ServiceStatus.FAILED;
        try {
            this.locationManager.requestLocationUpdates(this.provider, (long) this.updateTime, this.updateDistance, this.listener, ContextService.getServiceLooper());
            Log.d(TAG, "Location manager initialized");
            if (this.provider == "gps") {
                this.locationManager.addGpsStatusListener(this.gpsStatusListener);
            }
            this.running = true;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Could not request " + this.provider + " updates", e);
        } catch (SecurityException e2) {
            Log.e(TAG, "Not allowed to access " + this.provider + " for updates", e2);
            statusFailed = ServiceStatus.PERMISSION_DENIED;
        }
        ServiceStatus statusFailedCapture = statusFailed;
        if (this.running) {
            updateStatus(ServiceStatus.INITIALIZED);
            try {
                updateLocation(this.locationManager.getLastKnownLocation(this.provider));
                return;
            } catch (SecurityException e3) {
                return;
            }
        }
        updateStatus(statusFailedCapture);
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

    private void updateLocation(Location location) {
        ProviderListener listener = this.providerListener;
        if (listener != null) {
            if (this.firstLocationUpdate) {
                this.firstLocationUpdate = ENABLE_VERBOSE_LOGS;
                updateStatus(ServiceStatus.RUNNING);
            }
            listener.onProviderLocation(location);
        }
    }

    private void updateGpsStatus(int timeToFix, GpsSatellite[] satellites) {
        ProviderListener listener = this.providerListener;
        if (listener != null && (listener instanceof GpsProviderListener)) {
            ((GpsProviderListener) listener).onGpsStatusUpdate(timeToFix, satellites);
        }
    }
}
