package com.upsight.android.analytics.internal;

import com.google.gson.Gson;
import com.upsight.android.UpsightContext;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public final class ConfigGsonModule {
    public static final String GSON_CONFIG = "config-gson";

    @Singleton
    @Provides
    @Named("config-gson")
    public Gson provideConfigGson(UpsightContext upsight) {
        return upsight.getCoreComponent().gson();
    }
}
