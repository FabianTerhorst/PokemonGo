package com.upsight.android.analytics.event.partner;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONArraySerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONArray;

@UpsightStorableType("upsight.partner.click")
public class UpsightPartnerClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightPartnerClickEvent, UpsightData> {
        private JsonArray ads;
        private Integer contentId;
        private String impressionId;
        private Integer partnerId;
        private String partnerName;
        private String scope;
        private String streamId;
        private String streamStartTs;
        private Boolean testDevice;

        protected Builder(Integer partnerId, String scope, String streamId, Integer contentId) {
            this.partnerId = partnerId;
            this.scope = scope;
            this.streamId = streamId;
            this.contentId = contentId;
        }

        public Builder setAds(JSONArray ads) {
            this.ads = JSONArraySerializer.toJsonArray(ads);
            return this;
        }

        public Builder setPartnerName(String partnerName) {
            this.partnerName = partnerName;
            return this;
        }

        public Builder setImpressionId(String impressionId) {
            this.impressionId = impressionId;
            return this;
        }

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        public Builder setTestDevice(Boolean testDevice) {
            this.testDevice = testDevice;
            return this;
        }

        protected UpsightPartnerClickEvent build() {
            return new UpsightPartnerClickEvent("upsight.partner.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("ads")
        @Expose
        JsonArray ads;
        @SerializedName("content_id")
        @Expose
        Integer contentId;
        @SerializedName("impression_id")
        @Expose
        String impressionId;
        @SerializedName("partner_id")
        @Expose
        Integer partnerId;
        @SerializedName("partner_name")
        @Expose
        String partnerName;
        @SerializedName("scope")
        @Expose
        String scope;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;
        @SerializedName("test_device")
        @Expose
        Boolean testDevice;

        protected UpsightData(Builder builder) {
            this.ads = builder.ads;
            this.partnerName = builder.partnerName;
            this.impressionId = builder.impressionId;
            this.streamId = builder.streamId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
            this.partnerId = builder.partnerId;
            this.testDevice = builder.testDevice;
        }

        protected UpsightData() {
        }

        public JSONArray getAds() {
            return JSONArraySerializer.fromJsonArray(this.ads);
        }

        public String getPartnerName() {
            return this.partnerName;
        }

        public String getImpressionId() {
            return this.impressionId;
        }

        public String getStreamId() {
            return this.streamId;
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }

        public String getScope() {
            return this.scope;
        }

        public Integer getContentId() {
            return this.contentId;
        }

        public Integer getPartnerId() {
            return this.partnerId;
        }

        public Boolean getTestDevice() {
            return this.testDevice;
        }
    }

    public static Builder createBuilder(Integer partnerId, String scope, String streamId, Integer contentId) {
        return new Builder(partnerId, scope, streamId, contentId);
    }

    protected UpsightPartnerClickEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightPartnerClickEvent() {
    }
}
