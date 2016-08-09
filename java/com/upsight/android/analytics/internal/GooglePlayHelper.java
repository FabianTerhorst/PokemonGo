package com.upsight.android.analytics.internal;

import android.content.Intent;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.event.monetization.UpsightMonetizationIapEvent;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.R;

class GooglePlayHelper extends UpsightGooglePlayHelper {
    private static final String STORE_NAME = "google_play";
    private Gson mGson;
    private UpsightContext mUpsight;

    static class PurchaseData {
        @SerializedName("developerPayload")
        @Expose
        String developerPayload;
        @SerializedName("orderId")
        @Expose
        String orderId;
        @SerializedName("packageName")
        @Expose
        String packageName;
        @SerializedName("productId")
        @Expose
        String productId;
        @SerializedName("purchaseState")
        @Expose
        int purchaseState;
        @SerializedName("purchaseTime")
        @Expose
        long purchaseTime;
        @SerializedName("purchaseToken")
        @Expose
        String purchaseToken;

        PurchaseData() {
        }
    }

    public enum Resolution {
        buy,
        cancel,
        refund
    }

    GooglePlayHelper(UpsightContext upsight, Gson gson) {
        this.mUpsight = upsight;
        this.mGson = gson;
    }

    public void trackPurchase(int quantity, String currency, double price, double totalPrice, String product, Intent responseData, UpsightPublisherData publisherData) throws UpsightException {
        String msg;
        int responseCode = responseData.getIntExtra(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE, Integer.MIN_VALUE);
        switch (responseCode) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                String purchaseData = responseData.getStringExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_PURCHASE_DATA);
                String dataSignature = responseData.getStringExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_DATA_SIGNATURE);
                if (TextUtils.isEmpty(purchaseData)) {
                    msg = "Failed to track Google Play purchase due to null or empty purchase data.";
                    this.mUpsight.getLogger().e(Upsight.LOG_TAG, msg, new Object[0]);
                    throw new UpsightException(msg, new Object[0]);
                } else if (TextUtils.isEmpty(dataSignature)) {
                    msg = "Failed to track Google Play purchase due to null or empty data signature.";
                    this.mUpsight.getLogger().e(Upsight.LOG_TAG, msg, new Object[0]);
                    throw new UpsightException(msg, new Object[0]);
                } else {
                    try {
                        PurchaseData purchase = (PurchaseData) this.mGson.fromJson(purchaseData, PurchaseData.class);
                        if (purchase != null) {
                            Resolution resolution;
                            switch (purchase.purchaseState) {
                                case R.styleable.AdsAttrs_adSize /*0*/:
                                    resolution = Resolution.buy;
                                    break;
                                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                                    resolution = Resolution.cancel;
                                    break;
                                case R.styleable.LoadingImageView_circleCrop /*2*/:
                                    resolution = Resolution.refund;
                                    break;
                                default:
                                    msg = "Failed to track Google Play purchase. Invalid purchase state.";
                                    this.mUpsight.getLogger().e(Upsight.LOG_TAG, msg, new Object[0]);
                                    throw new UpsightException(msg, new Object[0]);
                            }
                            try {
                                UpsightMonetizationIapEvent.createBuilder(STORE_NAME, createIapBundle(responseCode, purchaseData, dataSignature), Double.valueOf(totalPrice), Double.valueOf(price), Integer.valueOf(quantity), currency, product).setResolution(resolution.toString()).put(publisherData).record(this.mUpsight);
                                return;
                            } catch (JSONException e) {
                                msg = "Failed to track Google Play purchase. Unable to create iap_bundle.";
                                this.mUpsight.getLogger().e(Upsight.LOG_TAG, e, msg, new Object[0]);
                                throw new UpsightException(e, msg, new Object[0]);
                            }
                        }
                        msg = "Failed to track Google Play purchase due to missing fields in purchase data.";
                        this.mUpsight.getLogger().e(Upsight.LOG_TAG, msg, new Object[0]);
                        throw new UpsightException(msg, new Object[0]);
                    } catch (JsonSyntaxException e2) {
                        msg = "Failed to track Google Play purchase due to malformed purchase data JSON.";
                        this.mUpsight.getLogger().e(Upsight.LOG_TAG, e2, msg, new Object[0]);
                        throw new UpsightException(e2, msg, new Object[0]);
                    }
                }
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                UpsightMonetizationIapEvent.createBuilder(STORE_NAME, null, Double.valueOf(totalPrice), Double.valueOf(price), Integer.valueOf(quantity), currency, product).setResolution(Resolution.cancel.toString()).put(publisherData).record(this.mUpsight);
                return;
            default:
                msg = "Failed to track Google Play purchase. See response code for details. responseCode=" + responseCode;
                this.mUpsight.getLogger().e(Upsight.LOG_TAG, msg, new Object[0]);
                throw new UpsightException(msg, new Object[0]);
        }
    }

    private JSONObject createIapBundle(int responseCode, String purchaseData, String dataSignature) throws JSONException {
        return new JSONObject().put(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE, responseCode).put(UpsightGooglePlayHelper.PURCHASE_INAPP_PURCHASE_DATA, purchaseData).put(UpsightGooglePlayHelper.PURCHASE_INAPP_DATA_SIGNATURE, dataSignature);
    }
}
