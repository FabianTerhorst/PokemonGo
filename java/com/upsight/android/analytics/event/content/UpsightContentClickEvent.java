package com.upsight.android.analytics.event.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.content.click")
public class UpsightContentClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightContentClickEvent, UpsightData> {
        private Integer contentId;
        private String scope;
        private String streamId;
        private String streamStartTs;

        protected Builder(String streamId, Integer contentId) {
            this.streamId = streamId;
            this.contentId = contentId;
        }

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        public Builder setScope(String scope) {
            this.scope = scope;
            return this;
        }

        protected UpsightContentClickEvent build() {
            return new UpsightContentClickEvent("upsight.content.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
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
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
            this.streamId = builder.streamId;
        }

        protected UpsightData() {
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

        public String getStreamId() {
            return this.streamId;
        }
    }

    public static Builder createBuilder(String streamId, Integer contentId) {
        return new Builder(streamId, contentId);
    }

    protected UpsightContentClickEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightContentClickEvent() {
    }
}
