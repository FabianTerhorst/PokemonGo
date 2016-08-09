package com.upsight.android.analytics.event.comm;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONObjectSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONObject;

@UpsightStorableType("upsight.comm.send")
public class UpsightCommSendEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCommSendEvent, UpsightData> {
        private Integer msgCampaignId;
        private Integer msgId;
        private JsonObject payload;
        private String token;

        protected Builder(Integer msgId, String token) {
            this.msgId = msgId;
            this.token = token;
        }

        public Builder setPayload(JSONObject payload) {
            this.payload = JSONObjectSerializer.toJsonObject(payload);
            return this;
        }

        public Builder setMsgCampaignId(Integer msgCampaignId) {
            this.msgCampaignId = msgCampaignId;
            return this;
        }

        protected UpsightCommSendEvent build() {
            return new UpsightCommSendEvent("upsight.comm.send", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("msg_campaign_id")
        @Expose
        Integer msgCampaignId;
        @SerializedName("msg_id")
        @Expose
        Integer msgId;
        @SerializedName("payload")
        @Expose
        JsonObject payload;
        @SerializedName("token")
        @Expose
        String token;

        protected UpsightData(Builder builder) {
            this.token = builder.token;
            this.msgId = builder.msgId;
            this.payload = builder.payload;
            this.msgCampaignId = builder.msgCampaignId;
        }

        protected UpsightData() {
        }

        public String getToken() {
            return this.token;
        }

        public Integer getMsgId() {
            return this.msgId;
        }

        public JSONObject getPayload() {
            return JSONObjectSerializer.fromJsonObject(this.payload);
        }

        public Integer getMsgCampaignId() {
            return this.msgCampaignId;
        }
    }

    public static Builder createBuilder(Integer msgId, String token) {
        return new Builder(msgId, token);
    }

    protected UpsightCommSendEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightCommSendEvent() {
    }
}
