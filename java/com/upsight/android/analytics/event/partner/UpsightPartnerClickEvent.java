package com.upsight.android.analytics.event.partner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.partner.click")
public class UpsightPartnerClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightPartnerClickEvent, UpsightData> {
        private Integer contentId;
        private Integer partnerId;
        private String partnerName;
        private String scope;
        private String streamId;
        private String streamStartTs;

        protected Builder(Integer partnerId, String scope, String streamId, Integer contentId) {
            this.partnerId = partnerId;
            this.scope = scope;
            this.streamId = streamId;
            this.contentId = contentId;
        }

        public Builder setPartnerName(String partnerName) {
            this.partnerName = partnerName;
            return this;
        }

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        protected UpsightPartnerClickEvent build() {
            return new UpsightPartnerClickEvent("upsight.partner.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @JsonProperty("content_id")
        Integer contentId;
        @JsonProperty("partner_id")
        Integer partnerId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("partner_name")
        String partnerName;
        @JsonProperty("scope")
        String scope;
        @JsonProperty("stream_id")
        String streamId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("stream_start_ts")
        String streamStartTs;

        protected UpsightData(Builder builder) {
            this.partnerName = builder.partnerName;
            this.streamId = builder.streamId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
            this.partnerId = builder.partnerId;
        }

        protected UpsightData() {
        }

        public String getPartnerName() {
            return this.partnerName;
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
