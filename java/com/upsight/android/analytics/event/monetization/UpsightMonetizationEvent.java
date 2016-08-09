package com.upsight.android.analytics.event.monetization;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONObjectSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONObject;

@UpsightStorableType("upsight.monetization")
public class UpsightMonetizationEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightMonetizationEvent, UpsightData> {
        private String cookie;
        private String currency;
        private JsonObject iapBundle;
        private Double price;
        private String product;
        private Integer quantity;
        private String resolution;
        private String store;
        private String streamId;
        private String streamStartTs;
        private Double totalPrice;

        protected Builder(Double totalPrice, String currency) {
            this.totalPrice = totalPrice;
            this.currency = currency;
        }

        public Builder setProduct(String product) {
            this.product = product;
            return this;
        }

        public Builder setStreamId(String streamId) {
            this.streamId = streamId;
            return this;
        }

        public Builder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public Builder setCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder setIapBundle(JSONObject iapBundle) {
            this.iapBundle = JSONObjectSerializer.toJsonObject(iapBundle);
            return this;
        }

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        public Builder setResolution(String resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder setStore(String store) {
            this.store = store;
            return this;
        }

        public Builder setQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        protected UpsightMonetizationEvent build() {
            return new UpsightMonetizationEvent("upsight.monetization", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("cookie")
        @Expose
        String cookie;
        @SerializedName("currency")
        @Expose
        String currency;
        @SerializedName("iap_bundle")
        @Expose
        JsonObject iapBundle;
        @SerializedName("price")
        @Expose
        Double price;
        @SerializedName("product")
        @Expose
        String product;
        @SerializedName("quantity")
        @Expose
        Integer quantity;
        @SerializedName("resolution")
        @Expose
        String resolution;
        @SerializedName("store")
        @Expose
        String store;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;
        @SerializedName("total_price")
        @Expose
        Double totalPrice;

        protected UpsightData(Builder builder) {
            this.product = builder.product;
            this.totalPrice = builder.totalPrice;
            this.streamId = builder.streamId;
            this.price = builder.price;
            this.currency = builder.currency;
            this.cookie = builder.cookie;
            this.iapBundle = builder.iapBundle;
            this.streamStartTs = builder.streamStartTs;
            this.resolution = builder.resolution;
            this.store = builder.store;
            this.quantity = builder.quantity;
        }

        protected UpsightData() {
        }

        public String getProduct() {
            return this.product;
        }

        public Double getTotalPrice() {
            return this.totalPrice;
        }

        public String getStreamId() {
            return this.streamId;
        }

        public Double getPrice() {
            return this.price;
        }

        public String getCurrency() {
            return this.currency;
        }

        public String getCookie() {
            return this.cookie;
        }

        public JSONObject getIapBundle() {
            return JSONObjectSerializer.fromJsonObject(this.iapBundle);
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }

        public String getResolution() {
            return this.resolution;
        }

        public String getStore() {
            return this.store;
        }

        public Integer getQuantity() {
            return this.quantity;
        }
    }

    public static Builder createBuilder(Double totalPrice, String currency) {
        return new Builder(totalPrice, currency);
    }

    protected UpsightMonetizationEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightMonetizationEvent() {
    }
}
