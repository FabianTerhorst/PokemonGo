package com.upsight.android.analytics.provider;

import com.google.gson.annotations.Expose;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

public abstract class UpsightLocationTracker {

    @UpsightStorableType("upsight.model.location")
    public static final class Data {
        @UpsightStorableIdentifier
        String id;
        @Expose
        double latitude;
        @Expose
        double longitude;

        public static Data create(double latitude, double longitude) {
            return new Data(latitude, longitude);
        }

        private Data(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        Data() {
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Data that = (Data) o;
            if (this.id != null) {
                if (this.id.equals(that.id)) {
                    return true;
                }
            } else if (that.id == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.id != null ? this.id.hashCode() : 0;
        }
    }

    public abstract void purge();

    public abstract void track(Data data);

    public static void track(UpsightContext upsight, Data locationData) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().trackLocation(locationData);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static void purge(UpsightContext upsight) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().purgeLocation();
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }
}
