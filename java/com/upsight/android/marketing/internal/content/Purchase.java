package com.upsight.android.marketing.internal.content;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.marketing.UpsightPurchase;
import java.io.IOException;

public final class Purchase implements UpsightPurchase {
    @SerializedName("product")
    @Expose
    String product;
    @SerializedName("quantity")
    @Expose
    int quantity;

    static UpsightPurchase from(JsonElement json, Gson gson) throws IOException {
        try {
            return (UpsightPurchase) gson.fromJson(json, Purchase.class);
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
    }

    Purchase() {
    }

    public String getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
