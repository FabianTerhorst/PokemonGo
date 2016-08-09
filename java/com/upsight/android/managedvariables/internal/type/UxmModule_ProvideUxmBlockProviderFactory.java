package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmBlockProviderFactory implements Factory<UxmBlockProvider> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideUxmBlockProviderFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<UxmSchema> uxmSchemaProvider;
    private final Provider<String> uxmSchemaRawStringProvider;

    public UxmModule_ProvideUxmBlockProviderFactory(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<String> uxmSchemaRawStringProvider, Provider<UxmSchema> uxmSchemaProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || uxmSchemaRawStringProvider != null) {
                    this.uxmSchemaRawStringProvider = uxmSchemaRawStringProvider;
                    if ($assertionsDisabled || uxmSchemaProvider != null) {
                        this.uxmSchemaProvider = uxmSchemaProvider;
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

    public UxmBlockProvider get() {
        return (UxmBlockProvider) Preconditions.checkNotNull(this.module.provideUxmBlockProvider((UpsightContext) this.upsightProvider.get(), (String) this.uxmSchemaRawStringProvider.get(), (UxmSchema) this.uxmSchemaProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UxmBlockProvider> create(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<String> uxmSchemaRawStringProvider, Provider<UxmSchema> uxmSchemaProvider) {
        return new UxmModule_ProvideUxmBlockProviderFactory(module, upsightProvider, uxmSchemaRawStringProvider, uxmSchemaProvider);
    }
}
