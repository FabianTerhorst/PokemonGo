package com.upsight.android.analytics.internal.session;

import com.google.gson.Gson;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigParser_Factory implements Factory<ConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigParser_Factory.class.desiredAssertionStatus());
    private final Provider<Gson> gsonProvider;

    public ConfigParser_Factory(Provider<Gson> gsonProvider) {
        if ($assertionsDisabled || gsonProvider != null) {
            this.gsonProvider = gsonProvider;
            return;
        }
        throw new AssertionError();
    }

    public ConfigParser get() {
        return new ConfigParser((Gson) this.gsonProvider.get());
    }

    public static Factory<ConfigParser> create(Provider<Gson> gsonProvider) {
        return new ConfigParser_Factory(gsonProvider);
    }
}
