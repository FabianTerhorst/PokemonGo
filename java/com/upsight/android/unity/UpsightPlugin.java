package com.upsight.android.unity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.event.UpsightCustomEvent;
import com.upsight.android.analytics.event.UpsightCustomEvent.Builder;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.event.install.UpsightInstallAttributionEvent;
import com.upsight.android.analytics.event.milestone.UpsightMilestoneEvent;
import com.upsight.android.analytics.event.monetization.UpsightMonetizationEvent;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class UpsightPlugin {
    protected static final String TAG = "Upsight-Unity";
    @NonNull
    private Set<IUpsightExtensionManager> mExtensions = new HashSet(2);
    protected UpsightContext mUpsight;

    public UpsightPlugin() {
        try {
            final Activity activity = UnityBridge.getActivity();
            this.mUpsight = Upsight.createContext(activity);
            this.mUpsight.getLogger().setLogLevel(Upsight.LOG_TAG, EnumSet.of(Level.ERROR));
            UnityBridge.runSafelyOnUiThread(new Runnable() {
                public void run() {
                    UpsightLifeCycleTracker.track(UpsightPlugin.this.mUpsight, activity, ActivityState.STARTED);
                    Log.i(UpsightPlugin.TAG, "Upsight initialization finished");
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Critical Error: Exception thrown while initializing. Upsight will NOT work!", e);
            throw e;
        }
    }

    public void registerExtension(IUpsightExtensionManager extension) {
        if (this.mExtensions.add(extension)) {
            extension.init(this.mUpsight);
        }
    }

    @NonNull
    public String getAppToken() {
        return this.mUpsight.getApplicationToken();
    }

    @NonNull
    public String getPublicKey() {
        return this.mUpsight.getPublicKey();
    }

    @NonNull
    public String getSid() {
        return this.mUpsight.getSid();
    }

    public void setLoggerLevel(@NonNull String logLevel) {
        try {
            if (logLevel.toLowerCase().equals("verbose")) {
                Log.i(TAG, "enabling verbose logs");
                this.mUpsight.getLogger().setLogLevel(".*", EnumSet.allOf(Level.class));
                return;
            }
            this.mUpsight.getLogger().setLogLevel(Upsight.LOG_TAG, EnumSet.of(Level.valueOf(logLevel)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public String getPluginVersion() {
        return this.mUpsight.getSdkPlugin();
    }

    public boolean getOptOutStatus() {
        try {
            return UpsightOptOutStatus.get(this.mUpsight);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setOptOutStatus(boolean optOutStatus) {
        try {
            UpsightOptOutStatus.set(this.mUpsight, optOutStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocation(double lat, double lon) {
        final double d = lat;
        final double d2 = lon;
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightLocationTracker.track(UpsightPlugin.this.mUpsight, Data.create(d, d2));
            }
        });
    }

    public void purgeLocation() {
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightLocationTracker.purge(UpsightPlugin.this.mUpsight);
            }
        });
    }

    public void setUserAttributesString(@NonNull final String key, @NonNull final String value) {
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, key, value);
            }
        });
    }

    public void setUserAttributesFloat(@NonNull final String key, final float value) {
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, key, Float.valueOf(value));
            }
        });
    }

    public void setUserAttributesInt(@NonNull final String key, final int value) {
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, key, Integer.valueOf(value));
            }
        });
    }

    public void setUserAttributesBool(@NonNull final String key, final boolean value) {
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, key, Boolean.valueOf(value));
            }
        });
    }

    public void setUserAttributesDatetime(@NonNull final String key, final long value) {
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, key, new Date(TimeUnit.MILLISECONDS.convert(value, TimeUnit.SECONDS)));
            }
        });
    }

    @Nullable
    public String getUserAttributesString(@NonNull String key) {
        try {
            return UpsightUserAttributes.getString(this.mUpsight, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getUserAttributesFloat(@NonNull String key) {
        try {
            Float value = UpsightUserAttributes.getFloat(this.mUpsight, key);
            if (value != null) {
                return value.floatValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public int getUserAttributesInt(@NonNull String key) {
        try {
            Integer value = UpsightUserAttributes.getInteger(this.mUpsight, key);
            if (value != null) {
                return value.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean getUserAttributesBool(@NonNull String key) {
        try {
            Boolean value = UpsightUserAttributes.getBoolean(this.mUpsight, key);
            if (value != null) {
                return value.booleanValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getUserAttributesDatetime(@NonNull String key) {
        try {
            Date value = UpsightUserAttributes.getDatetime(this.mUpsight, key);
            if (value != null) {
                return TimeUnit.SECONDS.convert(value.getTime(), TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Nullable
    public String getManagedString(@NonNull String key) {
        try {
            UpsightManagedString managedString = UpsightManagedString.fetch(this.mUpsight, key);
            if (managedString != null) {
                return (String) managedString.get();
            }
            Log.e(TAG, "Unknown tag " + key + " for managed string, please check your UXM schema");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getManagedFloat(@NonNull String key) {
        try {
            UpsightManagedFloat managedFloat = UpsightManagedFloat.fetch(this.mUpsight, key);
            if (managedFloat != null) {
                return ((Float) managedFloat.get()).floatValue();
            }
            Log.e(TAG, "Unknown tag " + key + " for managed float, please check your UXM schema");
            return 0.0f;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public int getManagedInt(@NonNull String key) {
        try {
            UpsightManagedInt managedInt = UpsightManagedInt.fetch(this.mUpsight, key);
            if (managedInt != null) {
                return ((Integer) managedInt.get()).intValue();
            }
            Log.e(TAG, "Unknown tag " + key + " for managed int, please check your UXM schema");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean getManagedBool(@NonNull String key) {
        try {
            UpsightManagedBoolean managedBoolean = UpsightManagedBoolean.fetch(this.mUpsight, key);
            if (managedBoolean != null) {
                return ((Boolean) managedBoolean.get()).booleanValue();
            }
            Log.e(TAG, "Unknown tag " + key + " for managed bool, please check your UXM schema");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void recordAnalyticsEvent(@NonNull final String eventName, @NonNull final String properties) {
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                Builder builder = UpsightCustomEvent.createBuilder(eventName);
                builder.put(UpsightPlugin.publisherDataFromJsonString(properties));
                builder.record(UpsightPlugin.this.mUpsight);
            }
        });
    }

    public void recordMilestoneEvent(@NonNull final String scope, @NonNull final String properties) {
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightMilestoneEvent.Builder builder = UpsightMilestoneEvent.createBuilder(scope);
                builder.put(UpsightPlugin.publisherDataFromJsonString(properties));
                builder.record(UpsightPlugin.this.mUpsight);
            }
        });
    }

    public void recordMonetizationEvent(double totalPrice, @NonNull String currency, @Nullable String product, double price, @Nullable String resolution, int quantity, @Nullable String properties) {
        final double d = totalPrice;
        final String str = currency;
        final String str2 = properties;
        final String str3 = product;
        final double d2 = price;
        final String str4 = resolution;
        final int i = quantity;
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightMonetizationEvent.Builder builder = UpsightMonetizationEvent.createBuilder(Double.valueOf(d), str);
                builder.put(UpsightPlugin.publisherDataFromJsonString(str2));
                if (str3 != null) {
                    builder.setProduct(str3);
                }
                if (d2 >= 0.0d) {
                    builder.setPrice(Double.valueOf(d2));
                }
                if (str4 != null) {
                    builder.setResolution(str4);
                }
                if (i > 0) {
                    builder.setQuantity(Integer.valueOf(i));
                }
                builder.record(UpsightPlugin.this.mUpsight);
            }
        });
    }

    public void recordGooglePlayPurchase(int quantity, @NonNull String currency, double price, double totalPrice, @NonNull String product, int reponseCode, @NonNull String inAppPurchaseData, @NonNull String inAppDataSignature, @NonNull String properties) {
        final String str = properties;
        final int i = reponseCode;
        final String str2 = inAppPurchaseData;
        final String str3 = inAppDataSignature;
        final int i2 = quantity;
        final String str4 = currency;
        final double d = price;
        final double d2 = totalPrice;
        final String str5 = product;
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightPublisherData.Builder builder = new UpsightPublisherData.Builder();
                builder.put(UpsightPlugin.publisherDataFromJsonString(str));
                try {
                    Intent responseData = new Intent();
                    responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE, i);
                    responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_PURCHASE_DATA, str2);
                    responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_DATA_SIGNATURE, str3);
                    UpsightGooglePlayHelper.trackPurchase(UpsightPlugin.this.mUpsight, i2, str4, d, d2, str5, responseData, builder.build());
                } catch (UpsightException e) {
                    Log.i(UpsightPlugin.TAG, "Failed to recordGooglePlayPurchase: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void recordAttributionEvent(@Nullable String campaign, @Nullable String creative, @Nullable String source, @Nullable String properties) {
        final String str = campaign;
        final String str2 = creative;
        final String str3 = source;
        final String str4 = properties;
        UnityBridge.runSafelyOnUiThread(new Runnable() {
            public void run() {
                UpsightInstallAttributionEvent.createBuilder().setAttributionCampaign(str).setAttributionCreative(str2).setAttributionSource(str3).put(UpsightPlugin.publisherDataFromJsonString(str4)).record(UpsightPlugin.this.mUpsight);
            }
        });
    }

    @NonNull
    private static UpsightPublisherData publisherDataFromJsonString(@Nullable String json) {
        UpsightPublisherData.Builder pubData = new UpsightPublisherData.Builder();
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

    public void onApplicationPaused() {
        for (IUpsightExtensionManager extension : this.mExtensions) {
            extension.onApplicationPaused();
        }
    }

    public void onApplicationResumed() {
        for (IUpsightExtensionManager extension : this.mExtensions) {
            extension.onApplicationResumed();
        }
    }
}
