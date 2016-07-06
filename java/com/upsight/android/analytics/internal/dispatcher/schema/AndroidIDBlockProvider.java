package com.upsight.android.analytics.internal.dispatcher.schema;

import android.content.Context;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.text.TextUtils;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AndroidIDBlockProvider extends UpsightDataProvider {
    public static final String ANDROID_ID_KEY = "ids.android_id";
    private static final String ANDROID_ID_NON_UNIQUE = "9774d56d682e549c";

    AndroidIDBlockProvider(Context context) {
        put(ANDROID_ID_KEY, getAndroidID(context));
    }

    private String getAndroidID(Context context) {
        String androidID = ANDROID_ID_NON_UNIQUE;
        String secureId = Secure.getString(context.getContentResolver(), "android_id");
        if (!TextUtils.isEmpty(secureId) && !androidID.equals(secureId)) {
            return secureId;
        }
        String systemId = System.getString(context.getContentResolver(), "android_id");
        if (TextUtils.isEmpty(systemId)) {
            return androidID;
        }
        return systemId;
    }

    public Set<String> availableKeys() {
        return new HashSet(Arrays.asList(new String[]{ANDROID_ID_KEY}));
    }
}
