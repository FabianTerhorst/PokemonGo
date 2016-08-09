package com.upsight.android;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.ContextWrapper;
import com.upsight.android.internal.persistence.Content;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Named;

public class UpsightContext extends ContextWrapper {
    private final String mAppToken;
    private UpsightCoreComponent mCoreComponent;
    private final UpsightDataStore mDataStore;
    private final Map<String, UpsightExtension> mExtensionsMap = new ConcurrentHashMap();
    private final UpsightLogger mLogger;
    private final String mPublicKey;
    private final String mSdkPlugin;
    private final String mSid;

    public UpsightContext(Context baseContext, @Named("com.upsight.sdk_plugin") String sdkPlugin, @Named("com.upsight.app_token") String appToken, @Named("com.upsight.public_key") String publicKey, String sid, UpsightDataStore dataStore, UpsightLogger logger) {
        super(baseContext);
        this.mSdkPlugin = sdkPlugin;
        this.mAppToken = appToken;
        this.mPublicKey = publicKey;
        this.mSid = sid;
        this.mDataStore = dataStore;
        this.mLogger = logger;
    }

    void onCreate(UpsightCoreComponent coreComponent, Map<String, UpsightExtension> extensions) {
        this.mCoreComponent = coreComponent;
        ContentProviderClient client = getContentResolver().acquireContentProviderClient(Content.getAuthoritytUri(this));
        if (client == null) {
            throw new IllegalStateException("Verify that the Upsight content provider is configured correctly in the Android Manifest:\n        <provider\n            android:name=\"com.upsight.android.internal.persistence.ContentProvider\"\n            android:authorities=\"" + getPackageName() + ".upsight\"\n" + "            android:enabled=\"true\"\n" + "            android:exported=\"false\" />");
        }
        client.release();
        for (Entry<String, UpsightExtension> entry : extensions.entrySet()) {
            UpsightExtension extension = (UpsightExtension) entry.getValue();
            extension.setComponent(extension.onResolve(coreComponent.upsightContext()));
            this.mExtensionsMap.put(entry.getKey(), extension);
        }
        for (UpsightExtension extension2 : extensions.values()) {
            extension2.getComponent().inject(extension2);
        }
        for (UpsightExtension extension22 : extensions.values()) {
            extension22.onCreate(this);
        }
        for (UpsightExtension extension222 : extensions.values()) {
            extension222.onPostCreate(this);
        }
    }

    public String getSdkVersion() {
        return getString(R.string.upsight_sdk_version);
    }

    public String getSdkBuild() {
        return getString(R.string.upsight_sdk_build);
    }

    public String getSdkPlugin() {
        return this.mSdkPlugin;
    }

    public String getApplicationToken() {
        return this.mAppToken;
    }

    public String getPublicKey() {
        return this.mPublicKey;
    }

    public String getSid() {
        return this.mSid;
    }

    public UpsightDataStore getDataStore() {
        return this.mDataStore;
    }

    public UpsightLogger getLogger() {
        return this.mLogger;
    }

    public UpsightCoreComponent getCoreComponent() {
        return this.mCoreComponent;
    }

    public UpsightExtension<?, ?> getUpsightExtension(String extensionName) {
        return (UpsightExtension) this.mExtensionsMap.get(extensionName);
    }
}
