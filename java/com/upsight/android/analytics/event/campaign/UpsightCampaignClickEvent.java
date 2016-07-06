package com.upsight.android.analytics.event.campaign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.campaign.click")
public class UpsightCampaignClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCampaignClickEvent, UpsightData> {
        private Integer adGameId;
        private Integer adTypeId;
        private Integer campaignId;
        private Integer contentId;
        private Integer contentTypeId;
        private Integer creativeId;
        private Integer ordinal;
        private String scope;
        private String streamId;
        private String streamStartTs;

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

        public Builder setContentTypeId(Integer contentTypeId) {
            this.contentTypeId = contentTypeId;
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

        protected UpsightCampaignClickEvent build() {
            return new UpsightCampaignClickEvent("upsight.campaign.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("ad_game_id")
        Integer adGameId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("ad_type_id")
        Integer adTypeId;
        @JsonProperty("campaign_id")
        Integer campaignId;
        @JsonProperty("content_id")
        Integer contentId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("content_type_id")
        Integer contentTypeId;
        @JsonProperty("creative_id")
        Integer creativeId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("ordinal")
        Integer ordinal;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("scope")
        String scope;
        @JsonProperty("stream_id")
        String streamId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("stream_start_ts")
        String streamStartTs;

        protected UpsightData(Builder builder) {
            this.ordinal = builder.ordinal;
            this.contentTypeId = builder.contentTypeId;
            this.creativeId = builder.creativeId;
            this.campaignId = builder.campaignId;
            this.adTypeId = builder.adTypeId;
            this.streamId = builder.streamId;
            this.adGameId = builder.adGameId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
        }

        protected UpsightData() {
        }

        public Integer getOrdinal() {
            return this.ordinal;
        }

        public Integer getContentTypeId() {
            return this.contentTypeId;
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
