package com.upsight.android.internal.persistence;

import android.content.Context;
import com.squareup.otto.Bus;
import com.upsight.android.internal.persistence.storable.StorableIdFactory;
import com.upsight.android.internal.persistence.storable.StorableInfoCache;
import com.upsight.android.persistence.UpsightDataStore;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module
public final class PersistenceModule {
    public static final String DATA_STORE_BACKGROUND = "background";

    @Singleton
    @Provides
    UpsightDataStore provideDataStore(Context context, StorableInfoCache infoCache, StorableIdFactory idFactory, @Named("execution") Scheduler subscribeOnScheduler, @Named("callback") Scheduler observeOnScheduler, Bus bus) {
        return new DataStore(context, infoCache, idFactory, subscribeOnScheduler, observeOnScheduler, bus);
    }

    @Singleton
    @Provides
    @Named("background")
    public UpsightDataStore provideBackgroundDataStore(Context context, @Named("execution") Scheduler subscribeOnScheduler, StorableIdFactory idFactory, StorableInfoCache infoCache, Bus bus) {
        return new DataStore(context, infoCache, idFactory, subscribeOnScheduler, Schedulers.immediate(), bus);
    }
}
