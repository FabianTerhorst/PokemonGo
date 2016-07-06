package com.upsight.android.analytics.event.uxm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.JacksonHelper.JSONArraySerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONArray;

@UpsightStorableType("upsight.uxm.enumerate")
public class UpsightUxmEnumerateEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightUxmEnumerateEvent, UpsightData> {
        private ArrayNode uxm;

        protected Builder(JSONArray uxm) {
            this.uxm = JSONArraySerializer.toArrayNode(uxm);
        }

        protected UpsightUxmEnumerateEvent build() {
            return new UpsightUxmEnumerateEvent("upsight.uxm.enumerate", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @JsonProperty("uxm")
        ArrayNode uxm;

        protected UpsightData(Builder builder) {
            this.uxm = builder.uxm;
        }

        protected UpsightData() {
        }

        public JSONArray getUxm() {
            return JSONArraySerializer.fromArrayNode(this.uxm);
        }
    }

    public static Builder createBuilder(JSONArray uxm) {
        return new Builder(uxm);
    }

    protected UpsightUxmEnumerateEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightUxmEnumerateEvent() {
    }
}
