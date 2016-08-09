package com.upsight.android.analytics.provider;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class UpsightUserAttributes {
    public static final String DATETIME_NULL = "9999-12-31T23:59:59";
    public static final long DATETIME_NULL_S = 253402300799L;
    protected static final String TYPE_BOOLEAN = "boolean";
    protected static final String TYPE_DATETIME = "datetime";
    protected static final String TYPE_FLOAT = "float";
    protected static final String TYPE_INTEGER = "integer";
    protected static final String TYPE_STRING = "string";
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

    public abstract Date getDatetime(String str);

    public abstract Set<Entry> getDefault();

    public abstract Float getFloat(String str);

    public abstract Integer getInt(String str);

    public abstract String getString(String str);

    public abstract void put(String str, Boolean bool);

    public abstract void put(String str, Float f);

    public abstract void put(String str, Integer num);

    public abstract void put(String str, String str2);

    public abstract void put(String str, Date date);

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

    public static void put(UpsightContext upsight, String key, Date value) throws IllegalArgumentException {
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

    public static Date getDatetime(UpsightContext upsight, String key) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().getDatetimeUserAttribute(key);
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
