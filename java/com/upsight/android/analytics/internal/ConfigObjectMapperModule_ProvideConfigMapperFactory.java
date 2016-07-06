package com.upsight.android.analytics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigObjectMapperModule_ProvideConfigMapperFactory implements Factory<ObjectMapper> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigObjectMapperModule_ProvideConfigMapperFactory.class.desiredAssertionStatus());
    private final ConfigObjectMapperModule module;
    private final Provider<UpsightContext> upsightProvider;

    public ConfigObjectMapperModule_ProvideConfigMapperFactory(ConfigObjectMapperModule module, Provider<UpsightContext> upsightProvider) {
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

    public ObjectMapper get() {
        ObjectMapper provided = this.module.provideConfigMapper((UpsightContext) this.upsightProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ObjectMapper> create(ConfigObjectMapperModule module, Provider<UpsightContext> upsightProvider) {
        return new ConfigObjectMapperModule_ProvideConfigMapperFactory(module, upsightProvider);
    }
}
