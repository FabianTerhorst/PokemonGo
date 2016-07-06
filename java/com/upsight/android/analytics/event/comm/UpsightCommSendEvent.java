package com.upsight.android.analytics.event.comm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.JacksonHelper.JSONObjectSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONObject;

@UpsightStorableType("upsight.comm.send")
public class UpsightCommSendEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCommSendEvent, UpsightData> {
        private Integer msgCampaignId;
        private Integer msgId;
        private ObjectNode payload;
        private String token;

        protected Builder(Integer msgId, String token) {
            this.msgId = msgId;
            this.token = token;
        }

        public Builder setPayload(JSONObject payload) {
            this.payload = JSONObjectSerializer.toObjectNode(payload);
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
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("msg_campaign_id")
        Integer msgCampaignId;
        @JsonProperty("msg_id")
        Integer msgId;
        @JsonInclude(Include.NON_NULL)
        @JsonProperty("payload")
        ObjectNode payload;
        @JsonProperty("token")
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
            return JSONObjectSerializer.fromObjectNode(this.payload);
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
