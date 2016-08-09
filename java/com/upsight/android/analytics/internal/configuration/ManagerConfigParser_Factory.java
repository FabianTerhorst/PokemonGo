package com.upsight.android.analytics.internal.configuration;

import com.google.gson.Gson;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ManagerConfigParser_Factory implements Factory<ManagerConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ManagerConfigParser_Factory.class.desiredAssertionStatus());
    private final Provider<Gson> gsonProvider;

    public ManagerConfigParser_Factory(Provider<Gson> gsonProvider) {
        if ($assertionsDisabled || gsonProvider != null) {
            this.gsonProvider = gsonProvider;
            return;
        }
        throw new AssertionError();
    }

    public ManagerConfigParser get() {
        return new ManagerConfigParser((Gson) this.gsonProvider.get());
    }

    public static Factory<ManagerConfigParser> create(Provider<Gson> gsonProvider) {
        return new ManagerConfigParser_Factory(gsonProvider);
    }
}
