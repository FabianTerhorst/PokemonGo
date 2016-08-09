package com.upsight.android.analytics.event.comm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.comm.register")
public class UpsightCommRegisterEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCommRegisterEvent, UpsightData> {
        private String token;

        protected Builder() {
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        protected UpsightCommRegisterEvent build() {
            return new UpsightCommRegisterEvent("upsight.comm.register", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        @SerializedName("token")
        @Expose
        String token;

        protected UpsightData(Builder builder) {
            this.token = builder.token;
        }

        protected UpsightData() {
        }

        public String getToken() {
            return this.token;
        }
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    protected UpsightCommRegisterEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightCommRegisterEvent() {
    }
}
