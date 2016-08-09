package com.upsight.android.analytics.internal.configuration;

import com.google.gson.Gson;
import com.upsight.android.analytics.internal.session.SessionManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigurationResponseParser_Factory implements Factory<ConfigurationResponseParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigurationResponseParser_Factory.class.desiredAssertionStatus());
    private final Provider<Gson> gsonProvider;
    private final Provider<SessionManager> sessionManagerProvider;

    public ConfigurationResponseParser_Factory(Provider<Gson> gsonProvider, Provider<SessionManager> sessionManagerProvider) {
        if ($assertionsDisabled || gsonProvider != null) {
            this.gsonProvider = gsonProvider;
            if ($assertionsDisabled || sessionManagerProvider != null) {
                this.sessionManagerProvider = sessionManagerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public ConfigurationResponseParser get() {
        return new ConfigurationResponseParser((Gson) this.gsonProvider.get(), (SessionManager) this.sessionManagerProvider.get());
    }

    public static Factory<ConfigurationResponseParser> create(Provider<Gson> gsonProvider, Provider<SessionManager> sessionManagerProvider) {
        return new ConfigurationResponseParser_Factory(gsonProvider, sessionManagerProvider);
    }
}
