package com.upsight.android.analytics.event.monetization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.JacksonHelper.JSONObjectSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONObject;

@UpsightStorableType("upsight.monetization.iap")
public class UpsightMonetizationIapEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightMonetizationIapEvent, UpsightData> {
        private String currency;
        private ObjectNode iapBundle;
        private Double price;
        private String product;
        private Integer quantity;
        private String resolution;
        private String store;
        private String streamId;
        private String streamStartTs;
        private Double totalPrice;

        protected Builder(String store, JSONObject iapBundle, Double totalPrice, Double price, Integer quantity, String currency, String product) {
            this.store = store;
            this.iapBundle = JSONObjectSerializer.toObjectNode(iapBundle);
            this.totalPrice = totalPrice;
            this.price = price;
            this.quantity = quantity;
            this.currency = currency;
            this.product = product;
        }

        public Builder setStreamId(String streamId) {
            this.streamId = streamId;
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

        protected UpsightMonetizationIapEvent build() {
            return new UpsightMonetizationIapEvent("upsight.monetization.iap", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @JsonProperty("currency")
        String currency;
        @JsonProperty("iap_bundle")
        ObjectNode iapBundle;
        @JsonProperty("price")
        Double price;
        @JsonProperty("product")
        String product;
        @JsonProperty("quantity")
        Integer quantity;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("resolution")
        String resolution;
        @JsonProperty("store")
        String store;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("stream_id")
        String streamId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("stream_start_ts")
        String streamStartTs;
        @JsonProperty("total_price")
        Double totalPrice;

        protected UpsightData(Builder builder) {
            this.product = builder.product;
            this.totalPrice = builder.totalPrice;
            this.streamId = builder.streamId;
            this.price = builder.price;
            this.currency = builder.currency;
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

        public JSONObject getIapBundle() {
            return JSONObjectSerializer.fromObjectNode(this.iapBundle);
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

    public static Builder createBuilder(String store, JSONObject iapBundle, Double totalPrice, Double price, Integer quantity, String currency, String product) {
        return new Builder(store, iapBundle, totalPrice, price, quantity, currency, product);
    }

    protected UpsightMonetizationIapEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightMonetizationIapEvent() {
    }
}
