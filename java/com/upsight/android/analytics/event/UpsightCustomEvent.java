package com.upsight.android.analytics.event;

import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("com.upsight.custom")
public final class UpsightCustomEvent extends AnalyticsEvent<UpsightPublisherData> {

    public static final class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCustomEvent, UpsightPublisherData> {
        private static final String FORMAT = "pub.%s";
        private String type;
        private com.upsight.android.analytics.event.UpsightPublisherData.Builder upsightDataBuilder;

        private Builder(String type) {
            this.upsightDataBuilder = new com.upsight.android.analytics.event.UpsightPublisherData.Builder();
            this.type = String.format(FORMAT, new Object[]{type});
        }

        protected UpsightCustomEvent build() {
            return new UpsightCustomEvent(this.type, this.upsightDataBuilder.build(), this.mPublisherDataBuilder.build());
        }
    }

    public static Builder createBuilder(String type) {
        return new Builder(type);
    }

    UpsightCustomEvent(String type, UpsightPublisherData upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    UpsightCustomEvent() {
    }
}
