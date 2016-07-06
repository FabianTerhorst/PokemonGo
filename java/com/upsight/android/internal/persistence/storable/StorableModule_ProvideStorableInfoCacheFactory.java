package com.upsight.android.internal.persistence.storable;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StorableModule_ProvideStorableInfoCacheFactory implements Factory<StorableInfoCache> {
    static final /* synthetic */ boolean $assertionsDisabled = (!StorableModule_ProvideStorableInfoCacheFactory.class.desiredAssertionStatus());
    private final StorableModule module;
    private final Provider<ObjectMapper> objectMapperProvider;

    public StorableModule_ProvideStorableInfoCacheFactory(StorableModule module, Provider<ObjectMapper> objectMapperProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || objectMapperProvider != null) {
                this.objectMapperProvider = objectMapperProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public StorableInfoCache get() {
        StorableInfoCache provided = this.module.provideStorableInfoCache((ObjectMapper) this.objectMapperProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<StorableInfoCache> create(StorableModule module, Provider<ObjectMapper> objectMapperProvider) {
        return new StorableModule_ProvideStorableInfoCacheFactory(module, objectMapperProvider);
    }
}
