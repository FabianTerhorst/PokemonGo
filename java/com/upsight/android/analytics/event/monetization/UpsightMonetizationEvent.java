package com.upsight.android.analytics.event.monetization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.monetization")
public class UpsightMonetizationEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightMonetizationEvent, UpsightData> {
        private String currency;
        private Double price;
        private String product;
        private Integer quantity;
        private String resolution;
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

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        public Builder setResolution(String resolution) {
            this.resolution = resolution;
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
        @JsonProperty("currency")
        String currency;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("price")
        Double price;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("product")
        String product;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("quantity")
        Integer quantity;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("resolution")
        String resolution;
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
            this.streamStartTs = builder.streamStartTs;
            this.resolution = builder.resolution;
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

        public String getStreamStartTs() {
            return this.streamStartTs;
        }

        public String getResolution() {
            return this.resolution;
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
