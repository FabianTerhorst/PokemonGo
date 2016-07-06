package com.upsight.android.analytics.internal.dispatcher.schema;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LocationBlockProvider extends UpsightDataProvider {
    public static final String LATITUDE_KEY = "location.lat";
    public static final String LONGITUDE_KEY = "location.lon";
    public static final String TIME_ZONE_KEY = "location.tz";
    private UpsightContext mUpsight;

    LocationBlockProvider(UpsightContext upsight) {
        this.mUpsight = upsight;
    }

    private Data fetchLatestLocation() {
        return (Data) this.mUpsight.getDataStore().fetchObservable(Data.class).lastOrDefault(null).toBlocking().first();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.Object get(java.lang.String r5) {
        /*
        r4 = this;
        r1 = 0;
        monitor-enter(r4);
        r0 = r4.fetchLatestLocation();	 Catch:{ all -> 0x0052 }
        if (r0 != 0) goto L_0x000b;
    L_0x0008:
        r1 = 0;
    L_0x0009:
        monitor-exit(r4);
        return r1;
    L_0x000b:
        r2 = -1;
        r3 = r5.hashCode();	 Catch:{ all -> 0x0052 }
        switch(r3) {
            case -59422746: goto L_0x0025;
            case -59422318: goto L_0x002f;
            case 552272735: goto L_0x001c;
            default: goto L_0x0013;
        };	 Catch:{ all -> 0x0052 }
    L_0x0013:
        r1 = r2;
    L_0x0014:
        switch(r1) {
            case 0: goto L_0x0039;
            case 1: goto L_0x003e;
            case 2: goto L_0x0048;
            default: goto L_0x0017;
        };	 Catch:{ all -> 0x0052 }
    L_0x0017:
        r1 = super.get(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0009;
    L_0x001c:
        r3 = "location.tz";
        r3 = r5.equals(r3);	 Catch:{ all -> 0x0052 }
        if (r3 == 0) goto L_0x0013;
    L_0x0024:
        goto L_0x0014;
    L_0x0025:
        r1 = "location.lat";
        r1 = r5.equals(r1);	 Catch:{ all -> 0x0052 }
        if (r1 == 0) goto L_0x0013;
    L_0x002d:
        r1 = 1;
        goto L_0x0014;
    L_0x002f:
        r1 = "location.lon";
        r1 = r5.equals(r1);	 Catch:{ all -> 0x0052 }
        if (r1 == 0) goto L_0x0013;
    L_0x0037:
        r1 = 2;
        goto L_0x0014;
    L_0x0039:
        r1 = r0.getTimeZone();	 Catch:{ all -> 0x0052 }
        goto L_0x0009;
    L_0x003e:
        r2 = r0.getLatitude();	 Catch:{ all -> 0x0052 }
        r1 = 0;
        r1 = android.location.Location.convert(r2, r1);	 Catch:{ all -> 0x0052 }
        goto L_0x0009;
    L_0x0048:
        r2 = r0.getLongitude();	 Catch:{ all -> 0x0052 }
        r1 = 0;
        r1 = android.location.Location.convert(r2, r1);	 Catch:{ all -> 0x0052 }
        goto L_0x0009;
    L_0x0052:
        r1 = move-exception;
        monitor-exit(r4);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.analytics.internal.dispatcher.schema.LocationBlockProvider.get(java.lang.String):java.lang.Object");
    }

    public Set<String> availableKeys() {
        return new HashSet(Arrays.asList(new String[]{TIME_ZONE_KEY, LATITUDE_KEY, LONGITUDE_KEY}));
    }
}
