package com.upsight.android.analytics.event.comm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.comm.click")
public class UpsightCommClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCommClickEvent, UpsightData> {
        private Integer msgCampaignId;
        private Integer msgId;

        protected Builder(Integer msgId) {
            this.msgId = msgId;
        }

        public Builder setMsgCampaignId(Integer msgCampaignId) {
            this.msgCampaignId = msgCampaignId;
            return this;
        }

        protected UpsightCommClickEvent build() {
            return new UpsightCommClickEvent("upsight.comm.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("msg_campaign_id")
        @Expose
        Integer msgCampaignId;
        @SerializedName("msg_id")
        @Expose
        Integer msgId;

        protected UpsightData(Builder builder) {
            this.msgId = builder.msgId;
            this.msgCampaignId = builder.msgCampaignId;
        }

        protected UpsightData() {
        }

        public Integer getMsgId() {
            return this.msgId;
        }

        public Integer getMsgCampaignId() {
            return this.msgCampaignId;
        }
    }

    public static Builder createBuilder(Integer msgId) {
        return new Builder(msgId);
    }

    protected UpsightCommClickEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightCommClickEvent() {
    }
}
