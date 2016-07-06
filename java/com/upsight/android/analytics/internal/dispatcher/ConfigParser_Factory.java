package com.upsight.android.analytics.internal.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigParser_Factory implements Factory<ConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigParser_Factory.class.desiredAssertionStatus());
    private final Provider<ObjectMapper> mapperProvider;
    private final Provider<UpsightContext> upsightProvider;

    public ConfigParser_Factory(Provider<UpsightContext> upsightProvider, Provider<ObjectMapper> mapperProvider) {
        if ($assertionsDisabled || upsightProvider != null) {
            this.upsightProvider = upsightProvider;
            if ($assertionsDisabled || mapperProvider != null) {
                this.mapperProvider = mapperProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public ConfigParser get() {
        return new ConfigParser((UpsightContext) this.upsightProvider.get(), (ObjectMapper) this.mapperProvider.get());
    }

    public static Factory<ConfigParser> create(Provider<UpsightContext> upsightProvider, Provider<ObjectMapper> mapperProvider) {
        return new ConfigParser_Factory(upsightProvider, mapperProvider);
    }
}
