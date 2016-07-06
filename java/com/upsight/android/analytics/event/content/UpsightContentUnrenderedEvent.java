package com.upsight.android.analytics.event.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.JacksonHelper.JSONObjectSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONObject;

@UpsightStorableType("upsight.content.unrendered")
public class UpsightContentUnrenderedEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightContentUnrenderedEvent, UpsightData> {
        private Integer campaignId;
        private ObjectNode contentProvider;
        private String id;
        private String scope;
        private String streamId;
        private String streamStartTs;

        protected Builder(JSONObject contentProvider) {
            this.contentProvider = JSONObjectSerializer.toObjectNode(contentProvider);
        }

        public Builder setCampaignId(Integer campaignId) {
            this.campaignId = campaignId;
            return this;
        }

        public Builder setStreamId(String streamId) {
            this.streamId = streamId;
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

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        protected UpsightContentUnrenderedEvent build() {
            return new UpsightContentUnrenderedEvent("upsight.content.unrendered", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("campaign_id")
        Integer campaignId;
        @JsonProperty("content_provider")
        ObjectNode contentProvider;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("id")
        String id;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("scope")
        String scope;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("stream_id")
        String streamId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("stream_start_ts")
        String streamStartTs;

        protected UpsightData(Builder builder) {
            this.contentProvider = builder.contentProvider;
            this.campaignId = builder.campaignId;
            this.streamId = builder.streamId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.id = builder.id;
        }

        protected UpsightData() {
        }

        public JSONObject getContentProvider() {
            return JSONObjectSerializer.fromObjectNode(this.contentProvider);
        }

        public Integer getCampaignId() {
            return this.campaignId;
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

        public String getId() {
            return this.id;
        }
    }

    public static Builder createBuilder(JSONObject contentProvider) {
        return new Builder(contentProvider);
    }

    protected UpsightContentUnrenderedEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightContentUnrenderedEvent() {
    }
}
