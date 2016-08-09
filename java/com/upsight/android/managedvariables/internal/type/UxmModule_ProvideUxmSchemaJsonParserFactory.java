package com.upsight.android.managedvariables.internal.type;

import com.google.gson.JsonParser;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaJsonParserFactory implements Factory<JsonParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideUxmSchemaJsonParserFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;

    public UxmModule_ProvideUxmSchemaJsonParserFactory(UxmModule module, Provider<UpsightContext> upsightProvider) {
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

    public JsonParser get() {
        return (JsonParser) Preconditions.checkNotNull(this.module.provideUxmSchemaJsonParser((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<JsonParser> create(UxmModule module, Provider<UpsightContext> upsightProvider) {
        return new UxmModule_ProvideUxmSchemaJsonParserFactory(module, upsightProvider);
    }
}
