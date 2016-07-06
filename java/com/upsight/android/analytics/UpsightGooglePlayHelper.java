package com.upsight.android.analytics;

import android.content.Intent;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.event.UpsightPublisherData;

public abstract class UpsightGooglePlayHelper {
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final String PURCHASE_INAPP_DATA_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String PURCHASE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String PURCHASE_RESPONSE_CODE = "RESPONSE_CODE";
    public static final int PURCHASE_STATE_CANCELED = 1;
    public static final int PURCHASE_STATE_PURCHASED = 0;
    public static final int PURCHASE_STATE_REFUNDED = 2;

    public abstract void trackPurchase(int i, String str, double d, double d2, String str2, Intent intent, UpsightPublisherData upsightPublisherData) throws UpsightException;

    public static void trackPurchase(UpsightContext upsight, int quantity, String currency, double price, double totalPrice, String product, Intent responseData, UpsightPublisherData publisherData) throws UpsightException {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().trackPurchase(quantity, currency, price, totalPrice, product, responseData, publisherData);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[PURCHASE_STATE_PURCHASED]);
        }
    }
}
