package com.upsight.android.googleadvertisingid.internal;

import android.content.Context;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.logger.UpsightLogger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class GooglePlayAdvertisingProvider extends UpsightDataProvider {
    public static final String AID_KEY = "ids.aid";
    public static final String LIMITED_AD_TRACKING_KEY = "device.limit_ad_tracking";
    public static final String LOG_TAG = GooglePlayAdvertisingProvider.class.getSimpleName();
    private final Context mContext;
    private final UpsightLogger mLogger;

    public GooglePlayAdvertisingProvider(Context context, UpsightLogger logger) {
        this.mContext = context;
        this.mLogger = logger;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.Object get(java.lang.String r9) {
        /*
        r8 = this;
        r2 = 0;
        r5 = 1;
        r3 = 0;
        monitor-enter(r8);
        r4 = -1;
        r6 = r9.hashCode();	 Catch:{ all -> 0x0048 }
        switch(r6) {
            case 1669192966: goto L_0x0016;
            case 1983331127: goto L_0x001f;
            default: goto L_0x000c;
        };	 Catch:{ all -> 0x0048 }
    L_0x000c:
        r3 = r4;
    L_0x000d:
        switch(r3) {
            case 0: goto L_0x0029;
            case 1: goto L_0x004b;
            default: goto L_0x0010;
        };	 Catch:{ all -> 0x0048 }
    L_0x0010:
        r2 = super.get(r9);	 Catch:{ all -> 0x0048 }
    L_0x0014:
        monitor-exit(r8);
        return r2;
    L_0x0016:
        r5 = "ids.aid";
        r5 = r9.equals(r5);	 Catch:{ all -> 0x0048 }
        if (r5 == 0) goto L_0x000c;
    L_0x001e:
        goto L_0x000d;
    L_0x001f:
        r3 = "device.limit_ad_tracking";
        r3 = r9.equals(r3);	 Catch:{ all -> 0x0048 }
        if (r3 == 0) goto L_0x000c;
    L_0x0027:
        r3 = r5;
        goto L_0x000d;
    L_0x0029:
        r1 = 0;
        r3 = r8.mContext;	 Catch:{ Exception -> 0x0037 }
        r1 = com.google.android.gms.ads.identifier.AdvertisingIdClient.getAdvertisingIdInfo(r3);	 Catch:{ Exception -> 0x0037 }
    L_0x0030:
        if (r1 == 0) goto L_0x0014;
    L_0x0032:
        r2 = r1.getId();	 Catch:{ all -> 0x0048 }
        goto L_0x0014;
    L_0x0037:
        r0 = move-exception;
        r3 = r8.mLogger;	 Catch:{ all -> 0x0048 }
        r4 = LOG_TAG;	 Catch:{ all -> 0x0048 }
        r5 = "Unable to resolve Google Advertising ID";
        r6 = 1;
        r6 = new java.lang.Object[r6];	 Catch:{ all -> 0x0048 }
        r7 = 0;
        r6[r7] = r0;	 Catch:{ all -> 0x0048 }
        r3.w(r4, r5, r6);	 Catch:{ all -> 0x0048 }
        goto L_0x0030;
    L_0x0048:
        r2 = move-exception;
        monitor-exit(r8);
        throw r2;
    L_0x004b:
        r1 = 0;
        r3 = r8.mContext;	 Catch:{ Exception -> 0x005d }
        r1 = com.google.android.gms.ads.identifier.AdvertisingIdClient.getAdvertisingIdInfo(r3);	 Catch:{ Exception -> 0x005d }
    L_0x0052:
        if (r1 == 0) goto L_0x0014;
    L_0x0054:
        r2 = r1.isLimitAdTrackingEnabled();	 Catch:{ all -> 0x0048 }
        r2 = java.lang.Boolean.valueOf(r2);	 Catch:{ all -> 0x0048 }
        goto L_0x0014;
    L_0x005d:
        r0 = move-exception;
        r3 = r8.mLogger;	 Catch:{ all -> 0x0048 }
        r4 = LOG_TAG;	 Catch:{ all -> 0x0048 }
        r5 = "Unable to resolve Google limited ad tracking status";
        r6 = 1;
        r6 = new java.lang.Object[r6];	 Catch:{ all -> 0x0048 }
        r7 = 0;
        r6[r7] = r0;	 Catch:{ all -> 0x0048 }
        r3.w(r4, r5, r6);	 Catch:{ all -> 0x0048 }
        goto L_0x0052;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.googleadvertisingid.internal.GooglePlayAdvertisingProvider.get(java.lang.String):java.lang.Object");
    }

    public Set<String> availableKeys() {
        return new HashSet(Arrays.asList(new String[]{AID_KEY, LIMITED_AD_TRACKING_KEY}));
    }
}
