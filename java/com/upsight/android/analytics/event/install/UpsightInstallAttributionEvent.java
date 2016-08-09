package com.upsight.android.analytics.event.install;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.install.attribution")
public class UpsightInstallAttributionEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightInstallAttributionEvent, UpsightData> {
        private String attributionCampaign;
        private String attributionCreative;
        private String attributionSource;
        private String streamId;
        private String streamStartTs;

        protected Builder() {
        }

        public Builder setAttributionCampaign(String attributionCampaign) {
            this.attributionCampaign = attributionCampaign;
            return this;
        }

        public Builder setAttributionCreative(String attributionCreative) {
            this.attributionCreative = attributionCreative;
            return this;
        }

        public Builder setAttributionSource(String attributionSource) {
            this.attributionSource = attributionSource;
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

        protected UpsightInstallAttributionEvent build() {
            return new UpsightInstallAttributionEvent("upsight.install.attribution", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("attribution_campaign")
        @Expose
        String attributionCampaign;
        @SerializedName("attribution_creative")
        @Expose
        String attributionCreative;
        @SerializedName("attribution_source")
        @Expose
        String attributionSource;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;

        protected UpsightData(Builder builder) {
            this.attributionCampaign = builder.attributionCampaign;
            this.attributionCreative = builder.attributionCreative;
            this.attributionSource = builder.attributionSource;
            this.streamStartTs = builder.streamStartTs;
            this.streamId = builder.streamId;
        }

        protected UpsightData() {
        }

        public String getAttributionCampaign() {
            return this.attributionCampaign;
        }

        public String getAttributionCreative() {
            return this.attributionCreative;
        }

        public String getAttributionSource() {
            return this.attributionSource;
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

    protected UpsightInstallAttributionEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightInstallAttributionEvent() {
    }
}
