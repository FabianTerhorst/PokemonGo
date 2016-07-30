package com.nianticlabs.nia.iap;

import android.util.Log;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Billing;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

class GetSkuDetailsResponseItem {
    private static final String TAG = "SkuDetailsResponseItem";
    private String description;
    private String price;
    private String price_amount_micros;
    private String price_currency_code;
    private String productId;
    private String title;
    private String type;

    GetSkuDetailsResponseItem() {
    }

    static GetSkuDetailsResponseItem fromJson(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            GetSkuDetailsResponseItem getSkuDetailsResponseItem = new GetSkuDetailsResponseItem();
            getSkuDetailsResponseItem.productId = stringFromJson(jObject, Billing.PRODUCT_IDENTIFIER);
            getSkuDetailsResponseItem.type = stringFromJson(jObject, Keys.TYPE);
            getSkuDetailsResponseItem.price = stringFromJson(jObject, "price");
            getSkuDetailsResponseItem.price_amount_micros = stringFromJson(jObject, "price_amount_micros");
            getSkuDetailsResponseItem.price_currency_code = stringFromJson(jObject, Billing.PRODUCT_CURRENCY_CODE);
            getSkuDetailsResponseItem.title = stringFromJson(jObject, Keys.TITLE);
            getSkuDetailsResponseItem.description = stringFromJson(jObject, "description");
            return getSkuDetailsResponseItem;
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse GetSkuDetailsResponseItem", e);
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

    static GetSkuDetailsResponseItem fromPurchasableItemDetails(PurchasableItemDetails purchasableItemDetails) {
        GetSkuDetailsResponseItem getSkuDetailsResponseItem = new GetSkuDetailsResponseItem();
        getSkuDetailsResponseItem.productId = purchasableItemDetails.getItemId();
        getSkuDetailsResponseItem.type = "inapp";
        getSkuDetailsResponseItem.price = purchasableItemDetails.getPrice();
        getSkuDetailsResponseItem.price_amount_micros = BuildConfig.FLAVOR;
        getSkuDetailsResponseItem.price_currency_code = BuildConfig.FLAVOR;
        getSkuDetailsResponseItem.title = purchasableItemDetails.getTitle();
        getSkuDetailsResponseItem.description = purchasableItemDetails.getDescription();
        return getSkuDetailsResponseItem;
    }

    static PurchasableItemDetails toPurchasableItemDetails(GetSkuDetailsResponseItem item) {
        return new PurchasableItemDetails(item.productId, item.title, item.description, item.price);
    }

    String getCurrencyCode() {
        return this.price_currency_code;
    }

    int getPriceE6() {
        try {
            return Integer.parseInt(this.price_amount_micros);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    String getProductId() {
        return this.productId;
    }
}
