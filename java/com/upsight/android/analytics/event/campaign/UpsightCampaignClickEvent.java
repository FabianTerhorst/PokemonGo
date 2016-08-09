package com.upsight.android.analytics.event.campaign;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONArraySerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONArray;

@UpsightStorableType("upsight.campaign.click")
public class UpsightCampaignClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCampaignClickEvent, UpsightData> {
        private Integer adGameId;
        private Integer adTypeId;
        private JsonArray ads;
        private Integer campaignId;
        private Integer contentId;
        private Integer contentTypeId;
        private Integer creativeId;
        private String impressionId;
        private Integer ordinal;
        private String scope;
        private String streamId;
        private String streamStartTs;
        private Boolean testDevice;

        protected Builder(String streamId, Integer campaignId, Integer creativeId, Integer contentId) {
            this.streamId = streamId;
            this.campaignId = campaignId;
            this.creativeId = creativeId;
            this.contentId = contentId;
        }

        public Builder setOrdinal(Integer ordinal) {
            this.ordinal = ordinal;
            return this;
        }

        public Builder setImpressionId(String impressionId) {
            this.impressionId = impressionId;
            return this;
        }

        public Builder setAds(JSONArray ads) {
            this.ads = JSONArraySerializer.toJsonArray(ads);
            return this;
        }

        public Builder setAdTypeId(Integer adTypeId) {
            this.adTypeId = adTypeId;
            return this;
        }

        public Builder setAdGameId(Integer adGameId) {
            this.adGameId = adGameId;
            return this;
        }

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        public Builder setScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder setTestDevice(Boolean testDevice) {
            this.testDevice = testDevice;
            return this;
        }

        public Builder setContentTypeId(Integer contentTypeId) {
            this.contentTypeId = contentTypeId;
            return this;
        }

        protected UpsightCampaignClickEvent build() {
            return new UpsightCampaignClickEvent("upsight.campaign.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("ad_game_id")
        @Expose
        Integer adGameId;
        @SerializedName("ad_type_id")
        @Expose
        Integer adTypeId;
        @SerializedName("ads")
        @Expose
        JsonArray ads;
        @SerializedName("campaign_id")
        @Expose
        Integer campaignId;
        @SerializedName("content_id")
        @Expose
        Integer contentId;
        @SerializedName("content_type_id")
        @Expose
        Integer contentTypeId;
        @SerializedName("creative_id")
        @Expose
        Integer creativeId;
        @SerializedName("impression_id")
        @Expose
        String impressionId;
        @SerializedName("ordinal")
        @Expose
        Integer ordinal;
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
            this.ordinal = builder.ordinal;
            this.impressionId = builder.impressionId;
            this.ads = builder.ads;
            this.creativeId = builder.creativeId;
            this.campaignId = builder.campaignId;
            this.adTypeId = builder.adTypeId;
            this.streamId = builder.streamId;
            this.adGameId = builder.adGameId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
            this.testDevice = builder.testDevice;
            this.contentTypeId = builder.contentTypeId;
        }

        protected UpsightData() {
        }

        public Integer getOrdinal() {
            return this.ordinal;
        }

        public String getImpressionId() {
            return this.impressionId;
        }

        public JSONArray getAds() {
            return JSONArraySerializer.fromJsonArray(this.ads);
        }

        public Integer getCreativeId() {
            return this.creativeId;
        }

        public Integer getCampaignId() {
            return this.campaignId;
        }

        public Integer getAdTypeId() {
            return this.adTypeId;
        }

        public String getStreamId() {
            return this.streamId;
        }

        public Integer getAdGameId() {
            return this.adGameId;
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

        public Boolean getTestDevice() {
            return this.testDevice;
        }

        public Integer getContentTypeId() {
            return this.contentTypeId;
        }
    }

    public static Builder createBuilder(String streamId, Integer campaignId, Integer creativeId, Integer contentId) {
        return new Builder(streamId, campaignId, creativeId, contentId);
    }

    protected UpsightCampaignClickEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightCampaignClickEvent() {
    }
}
