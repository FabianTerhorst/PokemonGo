package com.upsight.android.managedvariables.internal.type;

import com.google.gson.Gson;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaGsonFactory implements Factory<Gson> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideUxmSchemaGsonFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;

    public UxmModule_ProvideUxmSchemaGsonFactory(UxmModule module, Provider<UpsightContext> upsightProvider) {
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
        return (Gson) Preconditions.checkNotNull(this.module.provideUxmSchemaGson((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Gson> create(UxmModule module, Provider<UpsightContext> upsightProvider) {
        return new UxmModule_ProvideUxmSchemaGsonFactory(module, upsightProvider);
    }
}
