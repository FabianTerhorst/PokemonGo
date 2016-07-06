package com.upsight.android.managedvariables.internal.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaFactory implements Factory<UxmSchema> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideUxmSchemaFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<ObjectMapper> uxmSchemaMapperProvider;
    private final Provider<String> uxmSchemaStringProvider;

    public UxmModule_ProvideUxmSchemaFactory(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<ObjectMapper> uxmSchemaMapperProvider, Provider<String> uxmSchemaStringProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || uxmSchemaMapperProvider != null) {
                    this.uxmSchemaMapperProvider = uxmSchemaMapperProvider;
                    if ($assertionsDisabled || uxmSchemaStringProvider != null) {
                        this.uxmSchemaStringProvider = uxmSchemaStringProvider;
                        return;
                    }
                    throw new AssertionError();
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UxmSchema get() {
        UxmSchema provided = this.module.provideUxmSchema((UpsightContext) this.upsightProvider.get(), (ObjectMapper) this.uxmSchemaMapperProvider.get(), (String) this.uxmSchemaStringProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UxmSchema> create(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<ObjectMapper> uxmSchemaMapperProvider, Provider<String> uxmSchemaStringProvider) {
        return new UxmModule_ProvideUxmSchemaFactory(module, upsightProvider, uxmSchemaMapperProvider, uxmSchemaStringProvider);
    }
}
