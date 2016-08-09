package com.upsight.android.analytics.internal.provider;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class LocationTracker extends UpsightLocationTracker {
    private static final String LOG_TAG = LocationTracker.class.getSimpleName();
    private UpsightDataStore mDataStore;
    private UpsightLogger mLogger;

    @Inject
    LocationTracker(UpsightContext upsight) {
        this.mDataStore = upsight.getDataStore();
        this.mLogger = upsight.getLogger();
    }

    public void track(final Data newLocation) {
        this.mDataStore.fetch(Data.class, new UpsightDataStoreListener<Set<Data>>() {
            public void onSuccess(Set<Data> result) {
                Data location = null;
                Iterator<Data> iterator = result.iterator();
                if (iterator.hasNext()) {
                    location = (Data) iterator.next();
                    location.setLatitude(newLocation.getLatitude());
                    location.setLongitude(newLocation.getLongitude());
                }
                if (location == null) {
                    location = newLocation;
                }
                LocationTracker.this.mDataStore.store(location);
            }

            public void onFailure(UpsightException exception) {
                LocationTracker.this.mLogger.e(LocationTracker.LOG_TAG, exception, "Failed to fetch location data.", new Object[0]);
            }
        });
    }

    public void purge() {
        this.mDataStore.fetch(Data.class, new UpsightDataStoreListener<Set<Data>>() {
            public void onSuccess(Set<Data> result) {
                for (Data data : result) {
                    LocationTracker.this.mDataStore.remove(data);
                }
            }

            public void onFailure(UpsightException exception) {
                LocationTracker.this.mLogger.e(Upsight.LOG_TAG, "Failed to remove stale location data.", exception);
            }
        });
    }
}
