package com.unity3d.player;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import java.util.List;

final class r implements LocationListener {
    private final Context a;
    private final UnityPlayer b;
    private Location c;
    private float d = 0.0f;
    private boolean e = false;
    private int f = 0;
    private boolean g = false;
    private int h = 0;

    protected r(Context context, UnityPlayer unityPlayer) {
        this.a = context;
        this.b = unityPlayer;
    }

    private void a(int i) {
        this.h = i;
        this.b.nativeSetLocationStatus(i);
    }

    private void a(Location location) {
        if (location != null && a(location, this.c)) {
            this.c = location;
            this.b.nativeSetLocation((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), location.getAccuracy(), ((double) location.getTime()) / 1000.0d, new GeomagneticField((float) this.c.getLatitude(), (float) this.c.getLongitude(), (float) this.c.getAltitude(), this.c.getTime()).getDeclination());
        }
    }

    private static boolean a(Location location, Location location2) {
        if (location2 == null) {
            return true;
        }
        long time = location.getTime() - location2.getTime();
        boolean z = time > 120000;
        boolean z2 = time < -120000;
        boolean z3 = time > 0;
        if (z) {
            return true;
        }
        if (z2) {
            return false;
        }
        int accuracy = (int) (location.getAccuracy() - location2.getAccuracy());
        return !(accuracy < 0) ? (!z3 || (accuracy > 0)) ? z3 && ((accuracy > 200 ? 1 : 0) | (location.getAccuracy() == 0.0f ? 1 : 0)) == 0 && a(location.getProvider(), location2.getProvider()) : true : true;
    }

    private static boolean a(String str, String str2) {
        return str == null ? str2 == null : str.equals(str2);
    }

    public final void a(float f) {
        this.d = f;
    }

    public final boolean a() {
        return !((LocationManager) this.a.getSystemService("location")).getProviders(new Criteria(), true).isEmpty();
    }

    public final void b() {
        this.g = false;
        if (this.e) {
            m.Log(5, "Location_StartUpdatingLocation already started!");
        } else if (a()) {
            LocationManager locationManager = (LocationManager) this.a.getSystemService("location");
            a(1);
            List<String> providers = locationManager.getProviders(true);
            if (providers.isEmpty()) {
                a(3);
                return;
            }
            LocationProvider locationProvider;
            if (this.f == 2) {
                for (String provider : providers) {
                    LocationProvider provider2 = locationManager.getProvider(provider);
                    if (provider2.getAccuracy() == 2) {
                        locationProvider = provider2;
                        break;
                    }
                }
            }
            locationProvider = null;
            for (String provider3 : providers) {
                if (locationProvider == null || locationManager.getProvider(provider3).getAccuracy() != 1) {
                    a(locationManager.getLastKnownLocation(provider3));
                    locationManager.requestLocationUpdates(provider3, 0, this.d, this, this.a.getMainLooper());
                    this.e = true;
                }
            }
        } else {
            a(3);
        }
    }

    public final void b(float f) {
        if (f < 100.0f) {
            this.f = 1;
        } else if (f < 500.0f) {
            this.f = 1;
        } else {
            this.f = 2;
        }
    }

    public final void c() {
        ((LocationManager) this.a.getSystemService("location")).removeUpdates(this);
        this.e = false;
        this.c = null;
        a(0);
    }

    public final void d() {
        if (this.h == 1 || this.h == 2) {
            this.g = true;
            c();
        }
    }

    public final void e() {
        if (this.g) {
            b();
        }
    }

    public final void onLocationChanged(Location location) {
        a(2);
        a(location);
    }

    public final void onProviderDisabled(String str) {
        this.c = null;
    }

    public final void onProviderEnabled(String str) {
    }

    public final void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
