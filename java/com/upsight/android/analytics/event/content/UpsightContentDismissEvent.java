package com.upsight.android.analytics.event.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.content.dismiss")
public class UpsightContentDismissEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightContentDismissEvent, UpsightData> {
        private String action;
        private Integer contentId;
        private String scope;
        private String streamId;
        private String streamStartTs;

        protected Builder(String streamId, Integer contentId, String action) {
            this.streamId = streamId;
            this.contentId = contentId;
            this.action = action;
        }

        public Builder setScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        protected UpsightContentDismissEvent build() {
            return new UpsightContentDismissEvent("upsight.content.dismiss", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @JsonProperty("action")
        String action;
        @JsonProperty("content_id")
        Integer contentId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("scope")
        String scope;
        @JsonProperty("stream_id")
        String streamId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("stream_start_ts")
        String streamStartTs;

        protected UpsightData(Builder builder) {
            this.action = builder.action;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
            this.streamStartTs = builder.streamStartTs;
            this.streamId = builder.streamId;
        }

        protected UpsightData() {
        }

        public String getAction() {
            return this.action;
        }

        public String getScope() {
            return this.scope;
        }

        public Integer getContentId() {
            return this.contentId;
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }

        public String getStreamId() {
            return this.streamId;
        }
    }

    public static Builder createBuilder(String streamId, Integer contentId, String action) {
        return new Builder(streamId, contentId, action);
    }

    protected UpsightContentDismissEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightContentDismissEvent() {
    }
}
