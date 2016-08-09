package com.upsight.android.analytics.event.datacollection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
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
        @SerializedName("data_bundle")
        @Expose
        String dataBundle;
        @SerializedName("format")
        @Expose
        String format;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
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
