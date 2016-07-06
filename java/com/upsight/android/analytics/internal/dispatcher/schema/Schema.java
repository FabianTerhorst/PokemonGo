package com.upsight.android.analytics.internal.dispatcher.schema;

import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.googleadvertisingid.internal.GooglePlayAdvertisingProvider;
import com.upsight.android.internal.util.SidHelper;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Schema {
    private final Set<String> mAttributes;
    private final Map<String, UpsightDataProvider> mDataProviders;
    private final String mName;

    public static class Default extends Schema {
        static final Set<String> DEFAULT_REQUEST_ATTRIBUTES = new HashSet<String>() {
            {
                add(SidHelper.PREFERENCE_KEY_SID);
                add(AppBlockProvider.TOKEN_KEY);
                add(AppBlockProvider.VERSION_KEY);
                add(AppBlockProvider.BUNDLEID_KEY);
                add(DeviceBlockProvider.OS_KEY);
                add(DeviceBlockProvider.OS_VERSION_KEY);
                add(DeviceBlockProvider.TYPE_KEY);
                add(DeviceBlockProvider.HARDWARE_KEY);
                add(DeviceBlockProvider.MANUFACTURER_KEY);
                add(DeviceBlockProvider.CARRIER_KEY);
                add(DeviceBlockProvider.CONNECTION_KEY);
                add(DeviceBlockProvider.JAILBROKEN_KEY);
                add(GooglePlayAdvertisingProvider.LIMITED_AD_TRACKING_KEY);
                add(ScreenBlockProvider.WIDTH_KEY);
                add(ScreenBlockProvider.HEIGHT_KEY);
                add(ScreenBlockProvider.SCALE_KEY);
                add(ScreenBlockProvider.DPI_KEY);
                add(SdkBlockProvider.VERSION_KEY);
                add(SdkBlockProvider.BUILD_KEY);
                add(SdkBlockProvider.PLUGIN_KEY);
                add(LocationBlockProvider.TIME_ZONE_KEY);
                add(LocationBlockProvider.LATITUDE_KEY);
                add(LocationBlockProvider.LONGITUDE_KEY);
                add(AbstractUxmBlockProvider.BUNDLE_SCHEMA_HASH);
                add(AbstractUxmBlockProvider.BUNDLE_ID);
                add(AbstractUxmBlockProvider.BUNDLE_HASH);
            }
        };

        Default(Map<String, UpsightDataProvider> dataProviders) {
            super(null, DEFAULT_REQUEST_ATTRIBUTES, dataProviders);
        }
    }

    private Schema(String name, Set<String> attributes, Map<String, UpsightDataProvider> dataProviders) {
        this.mName = name;
        this.mAttributes = attributes;
        this.mDataProviders = dataProviders;
    }

    static Schema from(String name, Schema schema, Set<String> attributes) {
        Set<String> toAttributes = new HashSet();
        toAttributes.addAll(attributes);
        toAttributes.addAll(schema.mAttributes);
        return new Schema(name, toAttributes, schema.mDataProviders);
    }

    public String getName() {
        return this.mName;
    }

    public Set<String> availableKeys() {
        return this.mAttributes;
    }

    public Object getValueFor(String key) {
        if (!this.mAttributes.contains(key)) {
            return null;
        }
        UpsightDataProvider provider = (UpsightDataProvider) this.mDataProviders.get(key);
        if (provider != null) {
            return provider.get(key);
        }
        return null;
    }
}
