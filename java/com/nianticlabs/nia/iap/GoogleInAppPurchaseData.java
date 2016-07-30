package com.nianticlabs.nia.iap;

import android.util.Log;
import com.voxelbusters.nativeplugins.defines.Keys.Billing;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

final class GoogleInAppPurchaseData {
    private static final String TAG = "GoogleInAppPurchaseData";
    private String developerPayload;
    private String orderId;
    private String packageName;
    private String productId;
    private String purchaseState;
    private long purchaseTime;

    GoogleInAppPurchaseData() {
    }

    static GoogleInAppPurchaseData fromJson(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            GoogleInAppPurchaseData googleInAppPurchaseData = new GoogleInAppPurchaseData();
            googleInAppPurchaseData.orderId = stringFromJson(jObject, "orderId");
            googleInAppPurchaseData.packageName = stringFromJson(jObject, "packageName");
            googleInAppPurchaseData.productId = stringFromJson(jObject, Billing.PRODUCT_IDENTIFIER);
            googleInAppPurchaseData.purchaseTime = longFromJson(jObject, "purchaseTime");
            googleInAppPurchaseData.purchaseState = stringFromJson(jObject, "purchaseState");
            googleInAppPurchaseData.developerPayload = stringFromJson(jObject, "developerPayload");
            return googleInAppPurchaseData;
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse GoogleInAppPurchaseData: %s", e);
            return null;
        }
    }

    private static String stringFromJson(JSONObject jObject, String key) {
        try {
            return jObject.getString(key);
        } catch (JSONException e) {
            return BuildConfig.FLAVOR;
        }
    }

    private static long longFromJson(JSONObject jObject, String key) {
        try {
            return jObject.getLong(key);
        } catch (JSONException e) {
            return 0;
        }
    }

    String getOrderId() {
        return this.orderId;
    }

    String getPackageName() {
        return this.packageName;
    }

    String getProductId() {
        return this.productId;
    }

    long getPurchaseTime() {
        return this.purchaseTime;
    }

    String getPurchaseState() {
        return this.purchaseState;
    }

    String getDeveloperPayload() {
        return this.developerPayload;
    }
}
