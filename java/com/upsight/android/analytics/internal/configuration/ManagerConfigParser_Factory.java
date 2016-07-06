package com.upsight.android.analytics.internal.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ManagerConfigParser_Factory implements Factory<ManagerConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ManagerConfigParser_Factory.class.desiredAssertionStatus());
    private final Provider<ObjectMapper> mapperProvider;

    public ManagerConfigParser_Factory(Provider<ObjectMapper> mapperProvider) {
        if ($assertionsDisabled || mapperProvider != null) {
            this.mapperProvider = mapperProvider;
            return;
        }
        throw new AssertionError();
    }

    public ManagerConfigParser get() {
        return new ManagerConfigParser((ObjectMapper) this.mapperProvider.get());
    }

    public static Factory<ManagerConfigParser> create(Provider<ObjectMapper> mapperProvider) {
        return new ManagerConfigParser_Factory(mapperProvider);
    }
}
