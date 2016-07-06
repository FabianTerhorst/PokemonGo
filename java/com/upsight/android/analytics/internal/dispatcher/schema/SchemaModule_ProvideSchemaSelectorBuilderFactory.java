package com.upsight.android.analytics.internal.dispatcher.schema;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SchemaModule_ProvideSchemaSelectorBuilderFactory implements Factory<SchemaSelectorBuilder> {
    static final /* synthetic */ boolean $assertionsDisabled = (!SchemaModule_ProvideSchemaSelectorBuilderFactory.class.desiredAssertionStatus());
    private final SchemaModule module;
    private final Provider<UpsightContext> upsightProvider;

    public SchemaModule_ProvideSchemaSelectorBuilderFactory(SchemaModule module, Provider<UpsightContext> upsightProvider) {
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

    public SchemaSelectorBuilder get() {
        SchemaSelectorBuilder provided = this.module.provideSchemaSelectorBuilder((UpsightContext) this.upsightProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SchemaSelectorBuilder> create(SchemaModule module, Provider<UpsightContext> upsightProvider) {
        return new SchemaModule_ProvideSchemaSelectorBuilderFactory(module, upsightProvider);
    }
}
