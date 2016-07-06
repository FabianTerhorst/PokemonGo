package com.upsight.android.analytics.internal.dispatcher.schema;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SdkBlockProvider extends UpsightDataProvider {
    public static final String BUILD_KEY = "sdk.build";
    public static final String PLUGIN_KEY = "sdk.plugin";
    public static final String VERSION_KEY = "sdk.version";

    SdkBlockProvider(UpsightContext upsight) {
        put(VERSION_KEY, upsight.getSdkVersion());
        put(BUILD_KEY, upsight.getSdkBuild());
        put(PLUGIN_KEY, upsight.getSdkPlugin());
    }

    public Set<String> availableKeys() {
        return new HashSet(Arrays.asList(new String[]{VERSION_KEY, BUILD_KEY, PLUGIN_KEY}));
    }
}
