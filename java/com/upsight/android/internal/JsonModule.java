package com.upsight.android.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class JsonModule {
    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Singleton
    @Provides
    JsonParser provideJsonParser() {
        return new JsonParser();
    }
}
