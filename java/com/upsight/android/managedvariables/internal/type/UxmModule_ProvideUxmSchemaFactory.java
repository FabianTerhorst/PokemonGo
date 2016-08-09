package com.upsight.android.managedvariables.internal.type;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaFactory implements Factory<UxmSchema> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideUxmSchemaFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<Gson> uxmSchemaGsonProvider;
    private final Provider<JsonParser> uxmSchemaJsonParserProvider;
    private final Provider<String> uxmSchemaStringProvider;

    public UxmModule_ProvideUxmSchemaFactory(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<Gson> uxmSchemaGsonProvider, Provider<JsonParser> uxmSchemaJsonParserProvider, Provider<String> uxmSchemaStringProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || uxmSchemaGsonProvider != null) {
                    this.uxmSchemaGsonProvider = uxmSchemaGsonProvider;
                    if ($assertionsDisabled || uxmSchemaJsonParserProvider != null) {
                        this.uxmSchemaJsonParserProvider = uxmSchemaJsonParserProvider;
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
        throw new AssertionError();
    }

    public UxmSchema get() {
        return (UxmSchema) Preconditions.checkNotNull(this.module.provideUxmSchema((UpsightContext) this.upsightProvider.get(), (Gson) this.uxmSchemaGsonProvider.get(), (JsonParser) this.uxmSchemaJsonParserProvider.get(), (String) this.uxmSchemaStringProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UxmSchema> create(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<Gson> uxmSchemaGsonProvider, Provider<JsonParser> uxmSchemaJsonParserProvider, Provider<String> uxmSchemaStringProvider) {
        return new UxmModule_ProvideUxmSchemaFactory(module, upsightProvider, uxmSchemaGsonProvider, uxmSchemaJsonParserProvider, uxmSchemaStringProvider);
    }
}
