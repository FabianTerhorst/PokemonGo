package com.upsight.android.analytics.event.comm;

import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.comm.unregister")
public class UpsightCommUnregisterEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCommUnregisterEvent, UpsightData> {
        protected Builder() {
        }

        protected UpsightCommUnregisterEvent build() {
            return new UpsightCommUnregisterEvent("upsight.comm.unregister", new UpsightData(this), this.mPublisherDataBuilder.build());
        }
    }

    static class UpsightData {
        protected UpsightData(Builder builder) {
        }

        protected UpsightData() {
        }
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    protected UpsightCommUnregisterEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightCommUnregisterEvent() {
    }
}
