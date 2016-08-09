package com.upsight.android.analytics.event.uxm;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONArraySerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONArray;

@UpsightStorableType("upsight.uxm.enumerate")
public class UpsightUxmEnumerateEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightUxmEnumerateEvent, UpsightData> {
        private JsonArray uxm;

        protected Builder(JSONArray uxm) {
            this.uxm = JSONArraySerializer.toJsonArray(uxm);
        }

        protected UpsightUxmEnumerateEvent build() {
            return new UpsightUxmEnumerateEvent("upsight.uxm.enumerate", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("uxm")
        @Expose
        JsonArray uxm;

        protected UpsightData(Builder builder) {
            this.uxm = builder.uxm;
        }

        protected UpsightData() {
        }

        public JSONArray getUxm() {
            return JSONArraySerializer.fromJsonArray(this.uxm);
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
