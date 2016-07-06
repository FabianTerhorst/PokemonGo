package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;
import rx.Scheduler;

public final class UxmModule_ProvideManagedVariableManagerFactory implements Factory<ManagedVariableManager> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideManagedVariableManagerFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<Scheduler> schedulerProvider;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<UxmSchema> uxmSchemaProvider;

    public UxmModule_ProvideManagedVariableManagerFactory(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<Scheduler> schedulerProvider, Provider<UxmSchema> uxmSchemaProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || schedulerProvider != null) {
                    this.schedulerProvider = schedulerProvider;
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

    public ManagedVariableManager get() {
        ManagedVariableManager provided = this.module.provideManagedVariableManager((UpsightContext) this.upsightProvider.get(), (Scheduler) this.schedulerProvider.get(), (UxmSchema) this.uxmSchemaProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ManagedVariableManager> create(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<Scheduler> schedulerProvider, Provider<UxmSchema> uxmSchemaProvider) {
        return new UxmModule_ProvideManagedVariableManagerFactory(module, upsightProvider, schedulerProvider, uxmSchemaProvider);
    }
}
