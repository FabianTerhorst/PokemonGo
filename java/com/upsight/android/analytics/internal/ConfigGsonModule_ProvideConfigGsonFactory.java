package com.upsight.android.analytics.internal;

import com.google.gson.Gson;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ConfigGsonModule_ProvideConfigGsonFactory implements Factory<Gson> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigGsonModule_ProvideConfigGsonFactory.class.desiredAssertionStatus());
    private final ConfigGsonModule module;
    private final Provider<UpsightContext> upsightProvider;

    public ConfigGsonModule_ProvideConfigGsonFactory(ConfigGsonModule module, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public Gson get() {
        return (Gson) Preconditions.checkNotNull(this.module.provideConfigGson((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Gson> create(ConfigGsonModule module, Provider<UpsightContext> upsightProvider) {
        return new ConfigGsonModule_ProvideConfigGsonFactory(module, upsightProvider);
    }
}
