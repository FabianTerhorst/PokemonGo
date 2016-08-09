package com.upsight.android.analytics.event.install;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.install")
public class UpsightInstallEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightInstallEvent, UpsightData> {
        private String referrer;
        private String sourceId;
        private String streamId;
        private String streamStartTs;

        protected Builder() {
        }

        public Builder setSourceId(String sourceId) {
            this.sourceId = sourceId;
            return this;
        }

        public Builder setReferrer(String referrer) {
            this.referrer = referrer;
            return this;
        }

        public Builder setStreamStartTs(String streamStartTs) {
            this.streamStartTs = streamStartTs;
            return this;
        }

        public Builder setStreamId(String streamId) {
            this.streamId = streamId;
            return this;
        }

        protected UpsightInstallEvent build() {
            return new UpsightInstallEvent("upsight.install", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("referrer")
        @Expose
        String referrer;
        @SerializedName("source_id")
        @Expose
        String sourceId;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;

        protected UpsightData(Builder builder) {
            this.sourceId = builder.sourceId;
            this.referrer = builder.referrer;
            this.streamStartTs = builder.streamStartTs;
            this.streamId = builder.streamId;
        }

        protected UpsightData() {
        }

        public String getSourceId() {
            return this.sourceId;
        }

        public String getReferrer() {
            return this.referrer;
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }

        public String getStreamId() {
            return this.streamId;
        }
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    protected UpsightInstallEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightInstallEvent() {
    }
}
