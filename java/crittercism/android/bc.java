package crittercism.android;

import android.location.Location;

public final class bc {
    private static Location a;

    public static synchronized void a(Location location) {
        synchronized (bc.class) {
            if (location != null) {
                location = new Location(location);
            }
            a = location;
        }
    }

    public static synchronized Location a() {
        Location location;
        synchronized (bc.class) {
            location = a;
        }
        return location;
    }

    public static synchronized boolean b() {
        boolean z;
        synchronized (bc.class) {
            z = a != null;
        }
        return z;
    }
}
