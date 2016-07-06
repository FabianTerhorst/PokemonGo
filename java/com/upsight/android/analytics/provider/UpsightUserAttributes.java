package com.upsight.android.analytics.provider;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import java.util.HashSet;
import java.util.Set;

public abstract class UpsightUserAttributes {
    public static final String USER_ATTRIBUTES_PREFIX = "com.upsight.user_attribute.";

    public static class Entry {
        private Object mDefaultValue;
        private String mKey;

        public Entry(String key, Object defaultValue) {
            this.mKey = key;
            this.mDefaultValue = defaultValue;
        }

        public Class getType() {
            return this.mDefaultValue.getClass();
        }

        public String getKey() {
            return this.mKey;
        }

        public Object getDefaultValue() {
            return this.mDefaultValue;
        }
    }

    public abstract Boolean getBoolean(String str);

    public abstract Set<Entry> getDefault();

    public abstract Float getFloat(String str);

    public abstract Integer getInt(String str);

    public abstract String getString(String str);

    public abstract void put(String str, Boolean bool);

    public abstract void put(String str, Float f);

    public abstract void put(String str, Integer num);

    public abstract void put(String str, String str2);

    public static void put(UpsightContext upsight, String key, String value) throws IllegalArgumentException {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().putUserAttribute(key, value);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static void put(UpsightContext upsight, String key, Integer value) throws IllegalArgumentException {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().putUserAttribute(key, value);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static void put(UpsightContext upsight, String key, Boolean value) throws IllegalArgumentException {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().putUserAttribute(key, value);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static void put(UpsightContext upsight, String key, Float value) throws IllegalArgumentException {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().putUserAttribute(key, value);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static String getString(UpsightContext upsight, String key) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().getStringUserAttribute(key);
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        return null;
    }

    public static Integer getInteger(UpsightContext upsight, String key) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().getIntUserAttribute(key);
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        return null;
    }

    public static Boolean getBoolean(UpsightContext upsight, String key) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().getBooleanUserAttribute(key);
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        return null;
    }

    public static Float getFloat(UpsightContext upsight, String key) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().getFloatUserAttribute(key);
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        return null;
    }

    public static Set<Entry> getDefault(UpsightContext upsight) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().getDefaultUserAttributes();
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        return new HashSet();
    }
}
