package com.upsight.android.analytics.internal.dispatcher;

import com.google.gson.Gson;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigParser_Factory implements Factory<ConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigParser_Factory.class.desiredAssertionStatus());
    private final Provider<Gson> gsonProvider;
    private final Provider<UpsightContext> upsightProvider;

    public ConfigParser_Factory(Provider<UpsightContext> upsightProvider, Provider<Gson> gsonProvider) {
        if ($assertionsDisabled || upsightProvider != null) {
            this.upsightProvider = upsightProvider;
            if ($assertionsDisabled || gsonProvider != null) {
                this.gsonProvider = gsonProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public ConfigParser get() {
        return new ConfigParser((UpsightContext) this.upsightProvider.get(), (Gson) this.gsonProvider.get());
    }

    public static Factory<ConfigParser> create(Provider<UpsightContext> upsightProvider, Provider<Gson> gsonProvider) {
        return new ConfigParser_Factory(upsightProvider, gsonProvider);
    }
}
