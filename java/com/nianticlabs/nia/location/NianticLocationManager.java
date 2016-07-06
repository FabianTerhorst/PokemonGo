package com.nianticlabs.nia.location;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.Location;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.ServiceStatus;
import com.nianticlabs.nia.location.GpsProvider.GpsProviderListener;
import com.nianticlabs.nia.location.Provider.ProviderListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NianticLocationManager extends ContextService {
    static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final String FUSED_PROVIDER_NAME = "fused";
    private static final float GPS_UPDATE_DISTANCE_M = 0.0f;
    private static final int GPS_UPDATE_TIME_MSEC = 1000;
    private static final long INITIALIZATION_WAIT_TIME_MS = 2000;
    private static final float NET_UPDATE_DISTANCE_M = 0.0f;
    private static final int NET_UPDATE_TIME_MSEC = 5000;
    private static final String TAG = "NianticLocationManager";
    private float gpsUpdateDistanceM = NET_UPDATE_DISTANCE_M;
    private int gpsUpdateTimeMs = GPS_UPDATE_TIME_MSEC;
    private float netUpdateDistanceM = NET_UPDATE_DISTANCE_M;
    private int netUpdateTimeMs = NET_UPDATE_TIME_MSEC;
    private final List<Provider> providers;
    private boolean started = ENABLE_VERBOSE_LOGS;
    private final Map<String, ServiceStatus> statusMap = new HashMap();

    private native void nativeGpsStatusUpdate(int i, GpsSatellite[] gpsSatelliteArr);

    private native void nativeLocationUpdate(Location location, int[] iArr, Context context);

    public NianticLocationManager(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.statusMap.put("gps", ServiceStatus.UNDEFINED);
        this.statusMap.put("network", ServiceStatus.UNDEFINED);
        this.statusMap.put(FUSED_PROVIDER_NAME, ServiceStatus.UNDEFINED);
        this.providers = new ArrayList(3);
    }

    private void createProviders() {
        if (this.providers.size() != 3) {
            addProvider(FUSED_PROVIDER_NAME, new FusedLocationProvider(this.context, this.gpsUpdateTimeMs, this.gpsUpdateDistanceM));
            addProvider("gps", new LocationManagerProvider(this.context, "gps", this.gpsUpdateTimeMs, this.gpsUpdateDistanceM));
            addProvider("network", new LocationManagerProvider(this.context, "network", this.netUpdateTimeMs, this.netUpdateDistanceM));
        }
    }

    private void addProvider(final String name, Provider provider) {
        this.providers.add(provider);
        if (provider instanceof GpsProvider) {
            provider.setListener(new GpsProviderListener() {
                public void onGpsStatusUpdate(int timeToFix, GpsSatellite[] satellites) {
                    NianticLocationManager.this.gpsStatusUpdate(timeToFix, satellites);
                }

                public void onProviderStatus(ServiceStatus status) {
                    NianticLocationManager.this.statusMap.put(name, status);
                    NianticLocationManager.this.locationUpdate(null, NianticLocationManager.this.statusArray());
                }

                public void onProviderLocation(Location location) {
                    NianticLocationManager.this.locationUpdate(location, NianticLocationManager.this.statusArray());
                }
            });
        } else {
            provider.setListener(new ProviderListener() {
                public void onProviderStatus(ServiceStatus status) {
                    NianticLocationManager.this.statusMap.put(name, status);
                }

                public void onProviderLocation(Location location) {
                    NianticLocationManager.this.locationUpdate(location, NianticLocationManager.this.statusArray());
                }
            });
        }
    }

    private int[] statusArray() {
        return new int[]{((ServiceStatus) this.statusMap.get("gps")).ordinal(), ((ServiceStatus) this.statusMap.get("network")).ordinal(), ((ServiceStatus) this.statusMap.get(FUSED_PROVIDER_NAME)).ordinal()};
    }

    public void onStart() {
    }

    private void doStart() {
        if (!this.started) {
            createProviders();
            locationUpdate(null, statusArray());
            for (Provider provider : this.providers) {
                provider.onStart();
            }
            this.started = true;
        }
    }

    public void onStop() {
        for (Provider provider : this.providers) {
            provider.onStop();
        }
        this.started = ENABLE_VERBOSE_LOGS;
    }

    public void onPause() {
        for (Provider provider : this.providers) {
            provider.onPause();
        }
    }

    public void onResume() {
        if (!this.started) {
            doStart();
        }
        for (Provider provider : this.providers) {
            provider.onResume();
        }
    }

    public void configureLocationParameters(double update_distance, int gps_update_time_ms, int net_update_time_ms) {
        final int i = gps_update_time_ms;
        final double d = update_distance;
        final int i2 = net_update_time_ms;
        ContextService.runOnServiceHandler(new Runnable() {
            public void run() {
                if (NianticLocationManager.this.started) {
                    throw new IllegalStateException("Already started.");
                }
                NianticLocationManager.this.gpsUpdateTimeMs = i;
                NianticLocationManager.this.gpsUpdateDistanceM = (float) d;
                NianticLocationManager.this.netUpdateTimeMs = i2;
                NianticLocationManager.this.netUpdateDistanceM = (float) d;
                NianticLocationManager.this.doStart();
            }
        });
    }

    private void locationUpdate(Location location, int[] status) {
        synchronized (this.callbackLock) {
            nativeLocationUpdate(location, status, this.context);
        }
    }

    private void gpsStatusUpdate(int timeToFix, GpsSatellite[] satellites) {
        synchronized (this.callbackLock) {
            nativeGpsStatusUpdate(timeToFix, satellites);
        }
    }
}
