package com.upsight.android.analytics.internal.dispatcher.schema;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.internal.util.NetworkHelper;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeviceBlockProvider extends UpsightDataProvider {
    public static final String CARRIER_KEY = "device.carrier";
    public static final String CONNECTION_KEY = "device.connection";
    private static final String DEVICE_TYPE_PHONE = "phone";
    private static final String DEVICE_TYPE_TABLET = "tablet";
    public static final String HARDWARE_KEY = "device.hardware";
    public static final String JAILBROKEN_KEY = "device.jailbroken";
    private static final String KERNEL_BUILD_KEY_TEST = "test-keys";
    public static final String LIMITED_AD_TRACKING_KEY = "device.limit_ad_tracking";
    public static final String MANUFACTURER_KEY = "device.manufacturer";
    private static final String OS_ANDROID = "android";
    public static final String OS_KEY = "device.os";
    public static final String OS_VERSION_KEY = "device.os_version";
    private static final String SPACE = " ";
    public static final String TYPE_KEY = "device.type";

    DeviceBlockProvider(Context context) {
        put(CARRIER_KEY, NetworkHelper.getNetworkOperatorName(context));
        put(CONNECTION_KEY, NetworkHelper.getActiveNetworkType(context));
        put(HARDWARE_KEY, Build.MODEL);
        put(JAILBROKEN_KEY, Boolean.valueOf(isRooted()));
        put(MANUFACTURER_KEY, Build.MANUFACTURER);
        put(OS_KEY, OS_ANDROID);
        put(OS_VERSION_KEY, VERSION.RELEASE + SPACE + VERSION.SDK_INT);
        put(TYPE_KEY, getDeviceType(context));
    }

    private boolean isRooted() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains(KERNEL_BUILD_KEY_TEST);
    }

    private String getDeviceType(Context context) {
        String type = DEVICE_TYPE_PHONE;
        if ((context.getResources().getConfiguration().screenLayout & 15) >= 3) {
            return DEVICE_TYPE_TABLET;
        }
        return type;
    }

    public Set<String> availableKeys() {
        return new HashSet(Arrays.asList(new String[]{OS_KEY, OS_VERSION_KEY, TYPE_KEY, HARDWARE_KEY, MANUFACTURER_KEY, CARRIER_KEY, CONNECTION_KEY, JAILBROKEN_KEY}));
    }
}
