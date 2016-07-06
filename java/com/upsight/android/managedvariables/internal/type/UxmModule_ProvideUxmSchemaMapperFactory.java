package com.upsight.android.managedvariables.internal.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaMapperFactory implements Factory<ObjectMapper> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideUxmSchemaMapperFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;

    public UxmModule_ProvideUxmSchemaMapperFactory(UxmModule module, Provider<UpsightContext> upsightProvider) {
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
        ObjectMapper provided = this.module.provideUxmSchemaMapper((UpsightContext) this.upsightProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ObjectMapper> create(UxmModule module, Provider<UpsightContext> upsightProvider) {
        return new UxmModule_ProvideUxmSchemaMapperFactory(module, upsightProvider);
    }
}
