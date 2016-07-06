package com.upsight.android.analytics.internal;

import com.upsight.android.analytics.event.UpsightAnalyticsEvent;
import com.upsight.android.analytics.event.UpsightPublisherData;

public abstract class AnalyticsEvent<U> extends UpsightAnalyticsEvent<U, UpsightPublisherData> {

    public static abstract class Builder<T extends AnalyticsEvent<U>, U> extends com.upsight.android.analytics.event.UpsightAnalyticsEvent.Builder<T, U, UpsightPublisherData> {
        protected final com.upsight.android.analytics.event.UpsightPublisherData.Builder mPublisherDataBuilder = new com.upsight.android.analytics.event.UpsightPublisherData.Builder();

        protected Builder() {
        }

        public Builder<T, U> put(String key, boolean value) {
            this.mPublisherDataBuilder.put(key, value);
            return this;
        }

        public Builder<T, U> put(String key, int value) {
            this.mPublisherDataBuilder.put(key, value);
            return this;
        }

        public Builder<T, U> put(String key, long value) {
            this.mPublisherDataBuilder.put(key, value);
            return this;
        }

        public Builder<T, U> put(String key, float value) {
            this.mPublisherDataBuilder.put(key, value);
            return this;
        }

        public Builder<T, U> put(String key, double value) {
            this.mPublisherDataBuilder.put(key, value);
            return this;
        }

        public Builder<T, U> put(String key, char value) {
            this.mPublisherDataBuilder.put(key, value);
            return this;
        }

        public Builder<T, U> put(String key, CharSequence value) {
            this.mPublisherDataBuilder.put(key, value);
            return this;
        }

        public Builder<T, U> put(UpsightPublisherData data) {
            this.mPublisherDataBuilder.put(data);
            return this;
        }
    }

    protected AnalyticsEvent(String type, U upsightData, UpsightPublisherData publisherData) {
        super(type, upsightData, publisherData);
    }

    protected AnalyticsEvent() {
    }
}
