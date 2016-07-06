package com.upsight.android.analytics.internal.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.analytics.internal.session.SessionManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigurationResponseParser_Factory implements Factory<ConfigurationResponseParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigurationResponseParser_Factory.class.desiredAssertionStatus());
    private final Provider<ObjectMapper> mapperProvider;
    private final Provider<SessionManager> sessionManagerProvider;

    public ConfigurationResponseParser_Factory(Provider<ObjectMapper> mapperProvider, Provider<SessionManager> sessionManagerProvider) {
        if ($assertionsDisabled || mapperProvider != null) {
            this.mapperProvider = mapperProvider;
            if ($assertionsDisabled || sessionManagerProvider != null) {
                this.sessionManagerProvider = sessionManagerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public ConfigurationResponseParser get() {
        return new ConfigurationResponseParser((ObjectMapper) this.mapperProvider.get(), (SessionManager) this.sessionManagerProvider.get());
    }

    public static Factory<ConfigurationResponseParser> create(Provider<ObjectMapper> mapperProvider, Provider<SessionManager> sessionManagerProvider) {
        return new ConfigurationResponseParser_Factory(mapperProvider, sessionManagerProvider);
    }
}
