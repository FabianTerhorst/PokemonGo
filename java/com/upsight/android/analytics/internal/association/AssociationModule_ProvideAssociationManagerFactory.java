package com.upsight.android.analytics.internal.association;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.Clock;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AssociationModule_ProvideAssociationManagerFactory implements Factory<AssociationManager> {
    static final /* synthetic */ boolean $assertionsDisabled = (!AssociationModule_ProvideAssociationManagerFactory.class.desiredAssertionStatus());
    private final Provider<Clock> clockProvider;
    private final AssociationModule module;
    private final Provider<UpsightContext> upsightProvider;

    public AssociationModule_ProvideAssociationManagerFactory(AssociationModule module, Provider<UpsightContext> upsightProvider, Provider<Clock> clockProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || clockProvider != null) {
                    this.clockProvider = clockProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public AssociationManager get() {
        return (AssociationManager) Preconditions.checkNotNull(this.module.provideAssociationManager((UpsightContext) this.upsightProvider.get(), (Clock) this.clockProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<AssociationManager> create(AssociationModule module, Provider<UpsightContext> upsightProvider, Provider<Clock> clockProvider) {
        return new AssociationModule_ProvideAssociationManagerFactory(module, upsightProvider, clockProvider);
    }
}
