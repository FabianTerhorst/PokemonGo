package com.upsight.android.internal.persistence.storable;

import com.google.gson.Gson;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StorableModule_ProvideStorableInfoCacheFactory implements Factory<StorableInfoCache> {
    static final /* synthetic */ boolean $assertionsDisabled = (!StorableModule_ProvideStorableInfoCacheFactory.class.desiredAssertionStatus());
    private final Provider<Gson> gsonProvider;
    private final StorableModule module;

    public StorableModule_ProvideStorableInfoCacheFactory(StorableModule module, Provider<Gson> gsonProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || gsonProvider != null) {
                this.gsonProvider = gsonProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public StorableInfoCache get() {
        return (StorableInfoCache) Preconditions.checkNotNull(this.module.provideStorableInfoCache((Gson) this.gsonProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<StorableInfoCache> create(StorableModule module, Provider<Gson> gsonProvider) {
        return new StorableModule_ProvideStorableInfoCacheFactory(module, gsonProvider);
    }
}
