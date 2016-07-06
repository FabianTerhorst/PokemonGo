package com.nianticlabs.nia.iap;

import android.util.Log;
import com.voxelbusters.nativeplugins.defines.Keys.Billing;
import org.json.JSONException;
import org.json.JSONObject;

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
            googleInAppPurchaseData.orderId = jObject.getString("orderId");
            googleInAppPurchaseData.packageName = jObject.getString("packageName");
            googleInAppPurchaseData.productId = jObject.getString(Billing.PRODUCT_IDENTIFIER);
            googleInAppPurchaseData.purchaseTime = jObject.getLong("purchaseTime");
            googleInAppPurchaseData.purchaseState = jObject.getString("purchaseState");
            googleInAppPurchaseData.developerPayload = jObject.getString("developerPayload");
            return googleInAppPurchaseData;
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse GoogleInAppPurchaseData: %s", e);
            return null;
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
