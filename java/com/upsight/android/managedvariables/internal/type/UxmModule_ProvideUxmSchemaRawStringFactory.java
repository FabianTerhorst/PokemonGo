package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaRawStringFactory implements Factory<String> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideUxmSchemaRawStringFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<Integer> uxmSchemaResProvider;

    public UxmModule_ProvideUxmSchemaRawStringFactory(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<Integer> uxmSchemaResProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || uxmSchemaResProvider != null) {
                    this.uxmSchemaResProvider = uxmSchemaResProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public String get() {
        return (String) Preconditions.checkNotNull(this.module.provideUxmSchemaRawString((UpsightContext) this.upsightProvider.get(), (Integer) this.uxmSchemaResProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<String> create(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<Integer> uxmSchemaResProvider) {
        return new UxmModule_ProvideUxmSchemaRawStringFactory(module, upsightProvider, uxmSchemaResProvider);
    }
}
