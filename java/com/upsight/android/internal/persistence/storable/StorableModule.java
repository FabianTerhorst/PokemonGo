package com.upsight.android.internal.persistence.storable;

import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class StorableModule {
    @Singleton
    @Provides
    public StorableInfoCache provideStorableInfoCache(Gson gson) {
        return new StorableInfoCache(gson);
    }
}
