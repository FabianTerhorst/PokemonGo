package com.upsight.android.analytics.event.datacollection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.data_collection")
public class UpsightDataCollectionEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightDataCollectionEvent, UpsightData> {
        private String dataBundle;
        private String format;
        private String streamId;
        private String streamStartTs;

        protected Builder(String dataBundle, String streamId) {
            this.dataBundle = dataBundle;
            this.streamId = streamId;
        }

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        public Builder setFormat(String format) {
            this.format = format;
            return this;
        }

        protected UpsightDataCollectionEvent build() {
            return new UpsightDataCollectionEvent("upsight.data_collection", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @JsonProperty("data_bundle")
        String dataBundle;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("format")
        String format;
        @JsonProperty("stream_id")
        String streamId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("stream_start_ts")
        String streamStartTs;

        protected UpsightData(Builder builder) {
            this.streamStartTs = builder.streamStartTs;
            this.streamId = builder.streamId;
            this.dataBundle = builder.dataBundle;
            this.format = builder.format;
        }

        protected UpsightData() {
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }

        public String getStreamId() {
            return this.streamId;
        }

        public String getDataBundle() {
            return this.dataBundle;
        }

        public String getFormat() {
            return this.format;
        }
    }

    public static Builder createBuilder(String dataBundle, String streamId) {
        return new Builder(dataBundle, streamId);
    }

    protected UpsightDataCollectionEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightDataCollectionEvent() {
    }
}
