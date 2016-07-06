package com.upsight.android.analytics.internal.provider;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.analytics.provider.UpsightUserAttributes.Entry;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class UserAttributes extends UpsightUserAttributes {
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
            PreferencesHelper.clear(this.mUpsight, key);
        }
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (String.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putString(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, value);
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}));
        }
    }

    public void put(String key, Integer value) {
        if (value == null) {
            PreferencesHelper.clear(this.mUpsight, key);
        }
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Integer.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putInt(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, value.intValue());
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}));
        }
    }

    public void put(String key, Boolean value) {
        if (value == null) {
            PreferencesHelper.clear(this.mUpsight, key);
        }
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Boolean.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putBoolean(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, value.booleanValue());
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}));
        }
    }

    public void put(String key, Float value) {
        if (value == null) {
            PreferencesHelper.clear(this.mUpsight, key);
        }
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Float.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            PreferencesHelper.putFloat(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, value.floatValue());
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}));
        }
    }

    public String getString(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (String.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return PreferencesHelper.getString(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, (String) ((Entry) this.mUserAttributes.get(key)).getDefaultValue());
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}));
        }
    }

    public Integer getInt(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Integer.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return Integer.valueOf(PreferencesHelper.getInt(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, ((Integer) ((Entry) this.mUserAttributes.get(key)).getDefaultValue()).intValue()));
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}));
        }
    }

    public Boolean getBoolean(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Boolean.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return Boolean.valueOf(PreferencesHelper.getBoolean(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, ((Boolean) ((Entry) this.mUserAttributes.get(key)).getDefaultValue()).booleanValue()));
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}));
        }
    }

    public Float getFloat(String key) {
        if (!this.mUserAttributes.containsKey(key)) {
            this.mLogger.w(Upsight.LOG_TAG, String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %s%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, key}));
        } else if (Float.class.equals(((Entry) this.mUserAttributes.get(key)).getType())) {
            return Float.valueOf(PreferencesHelper.getFloat(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + key, ((Float) ((Entry) this.mUserAttributes.get(key)).getDefaultValue()).floatValue()));
        } else {
            this.mLogger.w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type: %s", new Object[]{key, ((Entry) this.mUserAttributes.get(key)).getType()}));
        }
    }

    public Set<Entry> getDefault() {
        return this.mUserAttributesSet;
    }

    private void loadDefaultAttributes() {
        try {
            Bundle bundle = this.mUpsight.getPackageManager().getApplicationInfo(this.mUpsight.getPackageName(), 128).metaData;
            if (bundle != null) {
                for (String metaDataKey : bundle.keySet()) {
                    if (!TextUtils.isEmpty(metaDataKey) && metaDataKey.startsWith(UpsightUserAttributes.USER_ATTRIBUTES_PREFIX)) {
                        String userAttribute = metaDataKey.substring(metaDataKey.lastIndexOf(46) + 1);
                        Entry entry = new Entry(userAttribute, bundle.get(metaDataKey));
                        this.mUserAttributes.put(userAttribute, entry);
                        this.mUserAttributesSet.add(entry);
                    }
                }
            }
        } catch (NameNotFoundException e) {
            this.mLogger.e(Upsight.LOG_TAG, "Unexpected error: Package name missing!?", e);
        }
    }
}
