package com.upsight.android.managedvariables.internal.type;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.text.TextUtils;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.dispatcher.schema.AbstractUxmBlockProvider;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.managedvariables.internal.type.UxmSchema.BaseSchema;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;
import javax.inject.Named;

public class UxmBlockProvider extends AbstractUxmBlockProvider implements OnSharedPreferenceChangeListener {
    private static final String HASH_ALGORITHM = "SHA-1";
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    private Observer mBundleHashObserver = new Observer() {
        public void update(Observable observable, Object data) {
            UxmBlockProvider.this.put(AbstractUxmBlockProvider.BUNDLE_HASH, UxmBlockProvider.this.getBundleHash());
        }
    };
    private MessageDigest mDigest;
    private UpsightContext mUpsight;
    private UxmSchema mUxmSchema;
    private String mUxmSchemaRawString;

    UxmBlockProvider(UpsightContext upsight, @Named("stringRawUxmSchema") String uxmSchemaRawString, UxmSchema uxmSchema) {
        this.mUpsight = upsight;
        this.mUxmSchemaRawString = uxmSchemaRawString;
        this.mUxmSchema = uxmSchema;
        try {
            this.mDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            upsight.getLogger().e(Upsight.LOG_TAG, e, "Failed to generate UXM hashes because SHA-1 is unavailable on this device", new Object[0]);
        }
        PreferencesHelper.registerListener(upsight, this);
        subscribeManagedVariables();
        put(AbstractUxmBlockProvider.BUNDLE_SCHEMA_HASH, getBundleSchemaHash());
        put(AbstractUxmBlockProvider.BUNDLE_ID, getBundleId());
        put(AbstractUxmBlockProvider.BUNDLE_HASH, getBundleHash());
    }

    public String getBundleSchemaHash() {
        return generateHash(this.mUxmSchemaRawString);
    }

    public String getBundleId() {
        return PreferencesHelper.getString(this.mUpsight, UxmContent.PREFERENCES_KEY_UXM_BUNDLE_ID, null);
    }

    public String getBundleHash() {
        StringBuilder sb = new StringBuilder();
        for (BaseSchema itemSchema : this.mUxmSchema.getAllOrdered()) {
            Object value = null;
            if ("com.upsight.uxm.string".equals(itemSchema.type)) {
                value = UpsightManagedString.fetch(this.mUpsight, itemSchema.tag).get();
            } else if ("com.upsight.uxm.boolean".equals(itemSchema.type)) {
                value = UpsightManagedBoolean.fetch(this.mUpsight, itemSchema.tag).get();
            } else if ("com.upsight.uxm.integer".equals(itemSchema.type)) {
                value = UpsightManagedInt.fetch(this.mUpsight, itemSchema.tag).get();
            } else if ("com.upsight.uxm.float".equals(itemSchema.type)) {
                value = UpsightManagedFloat.fetch(this.mUpsight, itemSchema.tag).get();
            }
            sb.append(itemSchema.tag).append(value).append(itemSchema.type);
        }
        return generateHash(sb.toString());
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (UxmContent.PREFERENCES_KEY_UXM_BUNDLE_ID.equals(key)) {
            put(AbstractUxmBlockProvider.BUNDLE_ID, getBundleId());
        }
    }

    private void subscribeManagedVariables() {
        for (BaseSchema itemSchema : this.mUxmSchema.getAllOrdered()) {
            if ("com.upsight.uxm.string".equals(itemSchema.type)) {
                UpsightManagedString.fetch(this.mUpsight, itemSchema.tag).addObserver(this.mBundleHashObserver);
            } else if ("com.upsight.uxm.boolean".equals(itemSchema.type)) {
                UpsightManagedBoolean.fetch(this.mUpsight, itemSchema.tag).addObserver(this.mBundleHashObserver);
            } else if ("com.upsight.uxm.integer".equals(itemSchema.type)) {
                UpsightManagedInt.fetch(this.mUpsight, itemSchema.tag).addObserver(this.mBundleHashObserver);
            } else if ("com.upsight.uxm.float".equals(itemSchema.type)) {
                UpsightManagedFloat.fetch(this.mUpsight, itemSchema.tag).addObserver(this.mBundleHashObserver);
            }
        }
    }

    private synchronized String generateHash(String in) {
        String hash;
        hash = null;
        if (!(this.mDigest == null || TextUtils.isEmpty(in))) {
            byte[] inBytes = in.getBytes();
            this.mDigest.update(inBytes, 0, inBytes.length);
            hash = bytesToHex(this.mDigest.digest());
        }
        return hash;
    }

    private static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        char[] hexChars = new char[(bytes.length * 2)];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 255;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[(i * 2) + 1] = HEX_ARRAY[v & 15];
        }
        return new String(hexChars);
    }
}
