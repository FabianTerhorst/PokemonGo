package com.upsight.android.analytics.internal.provider;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.analytics.provider.UpsightUserAttributes.Entry;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class UserAttributes extends UpsightUserAttributes {
    private static final Pattern DATETIME_DEFAULT_VALUE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}");
    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String TIMEZONE_UTC = "+0000";
    private static final Pattern USER_ATTRIBUTE_PATTERN = Pattern.compile("com\\.upsight\\.user_attribute\\.(string|boolean|integer|float|datetime)\\.([a-zA-Z0-9_]+)");
    private static final Pattern USER_ATTRIBUTE_PATTERN_INFER = Pattern.compile("com\\.upsight\\.user_attribute\\.([a-zA-Z0-9_]+)");
    private UpsightLogger mLogger;
    private UpsightContext mUpsight;
    private Map<String, Entry> mUserAttributes = new HashMap();
    private Set<Entry> mUserAttributesSet = new HashSet();

    @Inject
    UserAttributes(UpsightContext upsight) {
        this.mUpsight = upsight;
        this.mLogger = upsight.getLogger();
        loadDefaultAttributes();
    }

    public void put(String key, String value) throws IllegalArgumentException {
        if (value == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key);
        } else if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sstring.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sstring.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (String.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putString(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, value);
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type string", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type string", new Object[]{key}));
        }
    }

    public void put(String key, Integer value) {
        if (value == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key);
        } else if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sinteger.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sinteger.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Integer.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putInt(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, value.intValue());
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type integer", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type integer", new Object[]{key}));
        }
    }

    public void put(String key, Boolean value) {
        if (value == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key);
        } else if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sboolean.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sboolean.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Boolean.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putBoolean(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, value.booleanValue());
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type boolean", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type boolean", new Object[]{key}));
        }
    }

    public void put(String key, Float value) {
        if (value == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key);
        } else if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sfloat.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sfloat.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Float.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putFloat(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, value.floatValue());
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type float", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type float", new Object[]{key}));
        }
    }

    public void put(String key, Date value) {
        if (value == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key);
        } else if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Date.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putLong(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, TimeUnit.SECONDS.convert(value.getTime(), TimeUnit.MILLISECONDS));
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type datetime", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type datetime", new Object[]{key}));
        }
    }

    public String getString(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sstring.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sstring.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (String.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return PreferencesHelper.getString(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, (String) ((Entry) this.mUserAttributes.get(key)).getDefaultValue());
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type string", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type string", new Object[]{key}));
        }
    }

    public Integer getInt(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sinteger.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sinteger.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Integer.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return Integer.valueOf(PreferencesHelper.getInt(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, ((Integer) ((Entry) this.mUserAttributes.get(key)).getDefaultValue()).intValue()));
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type integer", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type integer", new Object[]{key}));
        }
    }

    public Boolean getBoolean(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sboolean.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sboolean.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Boolean.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return Boolean.valueOf(PreferencesHelper.getBoolean(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, ((Boolean) ((Entry) this.mUserAttributes.get(key)).getDefaultValue()).booleanValue()));
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type boolean", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type boolean", new Object[]{key}));
        }
    }

    public Float getFloat(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sfloat.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sfloat.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Float.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return Float.valueOf(PreferencesHelper.getFloat(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, ((Float) ((Entry) this.mUserAttributes.get(key)).getDefaultValue()).floatValue()));
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type float", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type float", new Object[]{key}));
        }
    }

    public Date getDatetime(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Date.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return new Date(TimeUnit.MILLISECONDS.convert(PreferencesHelper.getLong(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, TimeUnit.SECONDS.convert(((Date) ((Entry) this.mUserAttributes.get(key)).getDefaultValue()).getTime(), TimeUnit.MILLISECONDS)), TimeUnit.SECONDS));
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type datetime", new Object[]{key}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type datetime", new Object[]{key}));
        }
    }

    public Set<Entry> getDefault() {
        return new HashSet(this.mUserAttributesSet);
    }

    private void loadDefaultAttributes() {
        try {
            Bundle bundle = this.mUpsight.getPackageManager().getApplicationInfo(this.mUpsight.getPackageName(), 128).metaData;
            if (bundle != null) {
                for (String metaDataKey : bundle.keySet()) {
                    Entry entry = createEntry(metaDataKey, bundle.get(metaDataKey));
                    if (entry != null) {
                        this.mUserAttributes.put(entry.getKey(), entry);
                        this.mUserAttributesSet.add(entry);
                    }
                }
            }
        } catch (NameNotFoundException e) {
            this.mLogger.e(Upsight.LOG_TAG, "Unexpected error: Package name missing!?", e);
        }
    }

    Entry createEntry(String metaDataKey, Object metaDataValue) throws IllegalArgumentException {
        Matcher matcher = USER_ATTRIBUTE_PATTERN.matcher(metaDataKey);
        if (matcher.matches()) {
            String type = matcher.group(1);
            String name = matcher.group(2);
            Object value = null;
            if ("string".equals(type)) {
                value = String.valueOf(metaDataValue);
            } else if ("boolean".equals(type) || "integer".equals(type) || "float".equals(type)) {
                value = metaDataValue;
            } else if ("datetime".equals(type)) {
                String defaultDatetime = (String) metaDataValue;
                String msg;
                if (DATETIME_DEFAULT_VALUE_PATTERN.matcher(defaultDatetime).matches()) {
                    try {
                        value = new SimpleDateFormat(DATETIME_FORMAT, Locale.US).parse(defaultDatetime + TIMEZONE_UTC);
                    } catch (ParseException e) {
                        msg = String.format("Failed to parse default value of %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, metaDataKey});
                        this.mLogger.e(Upsight.LOG_TAG, msg, e);
                        throw new IllegalArgumentException(msg, e);
                    }
                }
                msg = String.format("Invalid format for the default value of %sdatetime.%s in the Android Manifest. It must match %s (e.g. 1970-01-01T00:00:00)", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, metaDataKey, DATETIME_FORMAT});
                this.mLogger.e(Upsight.LOG_TAG, msg, new Object[0]);
                throw new IllegalArgumentException(msg);
            }
            return new Entry(name, value);
        }
        Matcher matcherInfer = USER_ATTRIBUTE_PATTERN_INFER.matcher(metaDataKey);
        if (matcherInfer.matches()) {
            return new Entry(matcherInfer.group(1), metaDataValue);
        }
        return null;
    }
}
