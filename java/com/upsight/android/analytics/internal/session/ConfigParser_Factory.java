package com.upsight.android.analytics.internal.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigParser_Factory implements Factory<ConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigParser_Factory.class.desiredAssertionStatus());
    private final Provider<ObjectMapper> mapperProvider;

    public ConfigParser_Factory(Provider<ObjectMapper> mapperProvider) {
        if ($assertionsDisabled || mapperProvider != null) {
            this.mapperProvider = mapperProvider;
            return;
        }
        throw new AssertionError();
    }

    public ConfigParser get() {
        return new ConfigParser((ObjectMapper) this.mapperProvider.get());
    }

    public static Factory<ConfigParser> create(Provider<ObjectMapper> mapperProvider) {
        return new ConfigParser_Factory(mapperProvider);
    }
}
