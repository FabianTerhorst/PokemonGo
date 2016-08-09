package com.upsight.android.analytics.event.content;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONArraySerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONArray;

@UpsightStorableType("upsight.content.view")
public class UpsightContentViewEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightContentViewEvent, UpsightData> {
        private JsonArray ads;
        private Integer contentId;
        private String impressionId;
        private String scope;
        private String streamId;
        private String streamStartTs;
        private Boolean testDevice;

        protected Builder(String streamId, Integer contentId) {
            this.streamId = streamId;
            this.contentId = contentId;
        }

        public Builder setAds(JSONArray ads) {
            this.ads = JSONArraySerializer.toJsonArray(ads);
            return this;
        }

        public Builder setImpressionId(String impressionId) {
            this.impressionId = impressionId;
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

        protected UpsightContentViewEvent build() {
            return new UpsightContentViewEvent("upsight.content.view", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("ads")
        @Expose
        JsonArray ads;
        @SerializedName("content_id")
        @Expose
        Integer contentId;
        @SerializedName("impression_id")
        @Expose
        String impressionId;
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
            this.ads = builder.ads;
            this.impressionId = builder.impressionId;
            this.streamId = builder.streamId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
            this.testDevice = builder.testDevice;
        }

        protected UpsightData() {
        }

        public JSONArray getAds() {
            return JSONArraySerializer.fromJsonArray(this.ads);
        }

        public String getImpressionId() {
            return this.impressionId;
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

        public Boolean getTestDevice() {
            return this.testDevice;
        }
    }

    public static Builder createBuilder(String streamId, Integer contentId) {
        return new Builder(streamId, contentId);
    }

    protected UpsightContentViewEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightContentViewEvent() {
    }
}
