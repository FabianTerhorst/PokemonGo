package com.upsight.android.analytics.event.session;

import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.session.pause")
public class UpsightSessionPauseEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightSessionPauseEvent, UpsightData> {
        protected Builder() {
        }

        protected UpsightSessionPauseEvent build() {
            return new UpsightSessionPauseEvent("upsight.session.pause", new UpsightData(this), this.mPublisherDataBuilder.build());
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

    protected UpsightSessionPauseEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected UpsightSessionPauseEvent() {
    }
}
