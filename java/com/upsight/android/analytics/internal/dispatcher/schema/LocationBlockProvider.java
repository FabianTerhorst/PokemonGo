package com.upsight.android.analytics.internal.dispatcher.schema;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class LocationBlockProvider extends UpsightDataProvider {
    private static final String DATETIME_FORMAT_ISO_8601 = "yyyy-MM-dd HH:mm:ss.SSSZ";
    public static final String LATITUDE_KEY = "location.lat";
    public static final String LONGITUDE_KEY = "location.lon";
    public static final String TIME_ZONE_KEY = "location.tz";
    private static final int TIME_ZONE_OFFSET_LENGTH = 5;
    private static final Pattern TIME_ZONE_OFFSET_PATTERN = Pattern.compile("[+-][0-9]{4}");
    private TimeZone mCurrentTimeZone = null;
    private String mTimeZoneOffset = null;
    private UpsightContext mUpsight;

    LocationBlockProvider(UpsightContext upsight) {
        this.mUpsight = upsight;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.Object get(java.lang.String r6) {
        /*
        r5 = this;
        r1 = 0;
        r2 = 0;
        monitor-enter(r5);
        r3 = -1;
        r4 = r6.hashCode();	 Catch:{ all -> 0x0057 }
        switch(r4) {
            case -59422746: goto L_0x001e;
            case -59422318: goto L_0x0028;
            case 552272735: goto L_0x0015;
            default: goto L_0x000b;
        };	 Catch:{ all -> 0x0057 }
    L_0x000b:
        r2 = r3;
    L_0x000c:
        switch(r2) {
            case 0: goto L_0x0032;
            case 1: goto L_0x0037;
            case 2: goto L_0x0047;
            default: goto L_0x000f;
        };	 Catch:{ all -> 0x0057 }
    L_0x000f:
        r1 = super.get(r6);	 Catch:{ all -> 0x0057 }
    L_0x0013:
        monitor-exit(r5);
        return r1;
    L_0x0015:
        r4 = "location.tz";
        r4 = r6.equals(r4);	 Catch:{ all -> 0x0057 }
        if (r4 == 0) goto L_0x000b;
    L_0x001d:
        goto L_0x000c;
    L_0x001e:
        r2 = "location.lat";
        r2 = r6.equals(r2);	 Catch:{ all -> 0x0057 }
        if (r2 == 0) goto L_0x000b;
    L_0x0026:
        r2 = 1;
        goto L_0x000c;
    L_0x0028:
        r2 = "location.lon";
        r2 = r6.equals(r2);	 Catch:{ all -> 0x0057 }
        if (r2 == 0) goto L_0x000b;
    L_0x0030:
        r2 = 2;
        goto L_0x000c;
    L_0x0032:
        r1 = r5.fetchCurrentTimeZone();	 Catch:{ all -> 0x0057 }
        goto L_0x0013;
    L_0x0037:
        r0 = r5.fetchLatestLocation();	 Catch:{ all -> 0x0057 }
        if (r0 == 0) goto L_0x0013;
    L_0x003d:
        r2 = r0.getLatitude();	 Catch:{ all -> 0x0057 }
        r1 = 0;
        r1 = android.location.Location.convert(r2, r1);	 Catch:{ all -> 0x0057 }
        goto L_0x0013;
    L_0x0047:
        r0 = r5.fetchLatestLocation();	 Catch:{ all -> 0x0057 }
        if (r0 == 0) goto L_0x0013;
    L_0x004d:
        r2 = r0.getLongitude();	 Catch:{ all -> 0x0057 }
        r1 = 0;
        r1 = android.location.Location.convert(r2, r1);	 Catch:{ all -> 0x0057 }
        goto L_0x0013;
    L_0x0057:
        r1 = move-exception;
        monitor-exit(r5);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.analytics.internal.dispatcher.schema.LocationBlockProvider.get(java.lang.String):java.lang.Object");
    }

    public Set<String> availableKeys() {
        return new HashSet(Arrays.asList(new String[]{TIME_ZONE_KEY, LATITUDE_KEY, LONGITUDE_KEY}));
    }

    private Data fetchLatestLocation() {
        return (Data) this.mUpsight.getDataStore().fetchObservable(Data.class).lastOrDefault(null).toBlocking().first();
    }

    private String fetchCurrentTimeZone() {
        TimeZone latestTimeZone = TimeZone.getDefault();
        if (!(latestTimeZone == null || latestTimeZone.equals(this.mCurrentTimeZone))) {
            SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT_ISO_8601, Locale.US);
            datetimeFormat.setTimeZone(latestTimeZone);
            String datetime = datetimeFormat.format(new Date());
            if (datetime != null) {
                int length = datetime.length();
                if (length > TIME_ZONE_OFFSET_LENGTH) {
                    String latestOffset = datetime.substring(length - 5, length);
                    if (isTimeZoneOffsetValid(latestOffset)) {
                        this.mCurrentTimeZone = latestTimeZone;
                        this.mTimeZoneOffset = latestOffset;
                    }
                }
            }
        }
        return this.mTimeZoneOffset;
    }

    boolean isTimeZoneOffsetValid(String timeZoneOffset) {
        return TIME_ZONE_OFFSET_PATTERN.matcher(timeZoneOffset).matches();
    }
}
