package com.upsight.android.unity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.event.UpsightCustomEvent;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.event.UpsightPublisherData.Builder;
import com.upsight.android.analytics.event.milestone.UpsightMilestoneEvent;
import com.upsight.android.analytics.event.monetization.UpsightMonetizationEvent;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.googlepushservices.UpsightGooglePushServices;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnRegisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnUnregisterListener;
import com.upsight.android.googlepushservices.UpsightPushBillboard;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import com.upsight.android.marketing.UpsightBillboard;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"NewApi"})
public class UpsightPlugin extends AbstractUpsightPlugin implements ActivityLifecycleCallbacks {
    private static UpsightPlugin sInstance;
    private BillboardHandler mBillboardHandler;
    private Map<String, UpsightBillboard> mBillboardMap = new HashMap();
    private boolean mHasActiveBillboard = false;
    private List<String> mJettisonedBillboardScopes;
    private UpsightBillboard mPushBillboard;
    private boolean mShouldSynchronizeManagedVariables = true;
    private UpsightContext mUpsight;

    @SuppressLint({"NewApi"})
    public static synchronized UpsightPlugin instance() {
        UpsightPlugin upsightPlugin;
        synchronized (UpsightPlugin.class) {
            if (sInstance == null) {
                sInstance = new UpsightPlugin();
                Activity activity = sInstance.getActivity();
                if (activity != null) {
                    sInstance.mUpsight = Upsight.createContext(activity);
                    sInstance.mBillboardHandler = new BillboardHandler(activity, sInstance);
                    Log.i(Upsight.LOG_TAG, "creating UpsightPushBillboard");
                    sInstance.mPushBillboard = UpsightPushBillboard.create(sInstance.mUpsight, sInstance.mBillboardHandler);
                    if (VERSION.SDK_INT >= 14) {
                        Log.i(Upsight.LOG_TAG, "wiring up an ActivityLifecycleCallback listener since we are on API 14+");
                        activity.getApplication().registerActivityLifecycleCallbacks(sInstance);
                    }
                }
            }
            upsightPlugin = sInstance;
        }
        return upsightPlugin;
    }

    private static UpsightPublisherData publisherDataFromJsonString(String json) {
        Builder pubData = new Builder();
        if (json != null && json.length() > 0) {
            try {
                JSONObject jObject = new JSONObject(json);
                Iterator<?> itr = jObject.keys();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    try {
                        Object value = jObject.get(key);
                        if (value instanceof String) {
                            pubData.put(key, (String) value);
                        } else if (value instanceof Float) {
                            pubData.put(key, ((Float) value).floatValue());
                        } else if (value instanceof Double) {
                            pubData.put(key, ((Double) value).doubleValue());
                        } else if (value instanceof Long) {
                            pubData.put(key, ((Long) value).longValue());
                        } else if (value instanceof Boolean) {
                            pubData.put(key, ((Boolean) value).booleanValue());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return pubData.build();
    }

    public String getAppToken() {
        return this.mUpsight.getApplicationToken();
    }

    public String getPublicKey() {
        return this.mUpsight.getPublicKey();
    }

    public String getSid() {
        return this.mUpsight.getSid();
    }

    public void setLoggerLevel(String logLevel) {
        if (logLevel.toLowerCase().equals("verbose")) {
            Log.i(Upsight.LOG_TAG, "enabling verbose logs");
            this.mUpsight.getLogger().setLogLevel(Upsight.LOG_TAG, EnumSet.allOf(Level.class));
            return;
        }
        this.mUpsight.getLogger().setLogLevel(Upsight.LOG_TAG, EnumSet.of(Level.valueOf(logLevel)));
    }

    public String getPluginVersion() {
        return this.mUpsight.getSdkPlugin();
    }

    public boolean getOptOutStatus() {
        return UpsightOptOutStatus.get(this.mUpsight);
    }

    public void setOptOutStatus(boolean optOutStatus) {
        UpsightOptOutStatus.set(this.mUpsight, optOutStatus);
    }

    public void setLocation(double lat, double lon, String timezone) {
        Data data = Data.create(lat, lon);
        if (timezone != null && timezone.length() > 0) {
            data.setTimeZone(timezone);
        }
        UpsightLocationTracker.track(this.mUpsight, data);
    }

    public void purgeLocation() {
        UpsightLocationTracker.purge(this.mUpsight);
    }

    public void unregisterForPushNotifications() {
        Log.i(Upsight.LOG_TAG, "unregistering for push notifications");
        UpsightGooglePushServices.unregister(this.mUpsight, new OnUnregisterListener() {
            public void onSuccess() {
                Log.e(Upsight.LOG_TAG, "unregistration succeeded");
            }

            public void onFailure(UpsightException e) {
                Log.e(Upsight.LOG_TAG, "unregistration failed: " + e);
            }
        });
    }

    public void registerForPushNotifications() {
        Log.i(Upsight.LOG_TAG, "registering for push notifications");
        UpsightGooglePushServices.register(this.mUpsight, new OnRegisterListener() {
            public void onSuccess(String arg0) {
                Log.e(Upsight.LOG_TAG, "registration succeeded");
            }

            public void onFailure(UpsightException ex) {
                Log.e(Upsight.LOG_TAG, "registration failed: " + ex);
            }
        });
    }

    public void setUserAttributesString(String key, String value) {
        try {
            UpsightUserAttributes.put(this.mUpsight, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserAttributesFloat(String key, float value) {
        try {
            UpsightUserAttributes.put(this.mUpsight, key, Float.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserAttributesInt(String key, int value) {
        try {
            UpsightUserAttributes.put(this.mUpsight, key, Integer.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserAttributesBool(String key, boolean value) {
        try {
            UpsightUserAttributes.put(this.mUpsight, key, Boolean.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserAttributesString(String key) {
        try {
            return UpsightUserAttributes.getString(this.mUpsight, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getUserAttributesFloat(String key) {
        try {
            return UpsightUserAttributes.getFloat(this.mUpsight, key).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public int getUserAttributesInt(String key) {
        try {
            return UpsightUserAttributes.getInteger(this.mUpsight, key).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean getUserAttributesBool(String key) {
        try {
            return UpsightUserAttributes.getBoolean(this.mUpsight, key).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getManagedString(String key) {
        try {
            UpsightManagedString managedString = UpsightManagedString.fetch(this.mUpsight, key);
            if (managedString != null) {
                return (String) managedString.get();
            }
            Log.e(Upsight.LOG_TAG, "Unknown tag " + key + " for managed string, please check your UXM schema");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getManagedFloat(String key) {
        try {
            UpsightManagedFloat managedFloat = UpsightManagedFloat.fetch(this.mUpsight, key);
            if (managedFloat != null) {
                return ((Float) managedFloat.get()).floatValue();
            }
            Log.e(Upsight.LOG_TAG, "Unknown tag " + key + " for managed float, please check your UXM schema");
            return 0.0f;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public int getManagedInt(String key) {
        try {
            UpsightManagedInt managedInt = UpsightManagedInt.fetch(this.mUpsight, key);
            if (managedInt != null) {
                return ((Integer) managedInt.get()).intValue();
            }
            Log.e(Upsight.LOG_TAG, "Unknown tag " + key + " for managed int, please check your UXM schema");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean getManagedBool(String key) {
        try {
            UpsightManagedBoolean managedBoolean = UpsightManagedBoolean.fetch(this.mUpsight, key);
            if (managedBoolean != null) {
                return ((Boolean) managedBoolean.get()).booleanValue();
            }
            Log.e(Upsight.LOG_TAG, "Unknown tag " + key + " for managed bool, please check your UXM schema");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void recordAnalyticsEvent(String eventName, String properties) {
        UpsightCustomEvent.Builder builder = UpsightCustomEvent.createBuilder(eventName);
        builder.put(publisherDataFromJsonString(properties));
        builder.record(this.mUpsight);
    }

    public void recordMilestoneEvent(String scope, String properties) {
        UpsightMilestoneEvent.Builder builder = UpsightMilestoneEvent.createBuilder(scope);
        builder.put(publisherDataFromJsonString(properties));
        builder.record(this.mUpsight);
    }

    public void recordMonetizationEvent(double totalPrice, String currency, String product, double price, String resolution, int quantity, String properties) {
        UpsightMonetizationEvent.Builder builder = UpsightMonetizationEvent.createBuilder(Double.valueOf(totalPrice), currency);
        builder.put(publisherDataFromJsonString(properties));
        if (product != null) {
            builder.setProduct(product);
        }
        if (price >= 0.0d) {
            builder.setPrice(Double.valueOf(price));
        }
        if (resolution != null) {
            builder.setResolution(resolution);
        }
        if (quantity > 0) {
            builder.setQuantity(Integer.valueOf(quantity));
        }
        builder.record(this.mUpsight);
    }

    public void recordGooglePlayPurchase(int quantity, String currency, double price, double totalPrice, String product, int reponseCode, String inAppPurchaseData, String inAppDataSignature, String properties) {
        Builder builder = new Builder();
        builder.put(publisherDataFromJsonString(properties));
        try {
            Intent responseData = new Intent();
            responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE, reponseCode);
            responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_PURCHASE_DATA, inAppPurchaseData);
            responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_DATA_SIGNATURE, inAppDataSignature);
            UpsightGooglePlayHelper.trackPurchase(this.mUpsight, quantity, currency, price, totalPrice, product, responseData, builder.build());
        } catch (UpsightException e) {
            Log.i(Upsight.LOG_TAG, "Failed to recordGooglePlayPurchase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isContentReadyForBillboardWithScope(String scope) {
        return UpsightMarketingContentStore.isContentReady(this.mUpsight, scope);
    }

    public void prepareBillboard(String scope) {
        if (!this.mBillboardMap.containsKey(scope) && !getHasActiveBillboard()) {
            if (this.mBillboardMap.size() > 0) {
                for (String s : this.mBillboardMap.keySet()) {
                    Log.i(Upsight.LOG_TAG, "clearing out cached billboard [" + s + "] to make room for the new billboard: " + scope);
                    ((UpsightBillboard) this.mBillboardMap.get(s)).destroy();
                }
                this.mBillboardMap.clear();
            }
            this.mBillboardMap.put(scope, UpsightBillboard.create(this.mUpsight, scope, this.mBillboardHandler));
        }
    }

    public void destroyBillboard(String scope) {
        if (this.mBillboardMap.containsKey(scope) && !getHasActiveBillboard()) {
            Log.i(Upsight.LOG_TAG, "Destroying billboard for scope: " + scope);
            ((UpsightBillboard) this.mBillboardMap.get(scope)).destroy();
            this.mBillboardMap.remove(scope);
        }
    }

    public void removeBillboardFromMap(String scope) {
        if (this.mBillboardMap.containsKey(scope)) {
            Log.i(Upsight.LOG_TAG, "Removing used billboard from internal map for scope: " + scope);
            this.mBillboardMap.remove(scope);
        }
    }

    public void setShouldSynchronizeManagedVariables(boolean shouldSynchronizeManagedVariables) {
        this.mShouldSynchronizeManagedVariables = shouldSynchronizeManagedVariables;
    }

    public boolean getShouldSynchronizeManagedVariables() {
        return this.mShouldSynchronizeManagedVariables;
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
        this.mPushBillboard.destroy();
        this.mPushBillboard = null;
        this.mJettisonedBillboardScopes = new ArrayList();
        for (String scope : this.mBillboardMap.keySet()) {
            this.mJettisonedBillboardScopes.add(scope);
            ((UpsightBillboard) this.mBillboardMap.get(scope)).destroy();
        }
        this.mBillboardMap.clear();
        Log.i(Upsight.LOG_TAG, "tombstoned " + this.mJettisonedBillboardScopes.size() + " scopes when pausing");
    }

    public void onActivityResumed(Activity activity) {
        Log.i(Upsight.LOG_TAG, "resurrecting " + this.mJettisonedBillboardScopes.size() + " scopes when resuming and push billboard");
        if (this.mPushBillboard == null) {
            this.mPushBillboard = UpsightPushBillboard.create(this.mUpsight, this.mBillboardHandler);
        }
        for (String scope : this.mJettisonedBillboardScopes) {
            prepareBillboard(scope);
        }
        this.mJettisonedBillboardScopes = null;
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    void setHasActiveBillboard(boolean hasActiveBillboard) {
        this.mHasActiveBillboard = hasActiveBillboard;
    }

    boolean getHasActiveBillboard() {
        return this.mHasActiveBillboard;
    }
}
