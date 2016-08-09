package com.upsight.android.analytics.internal.provider;

import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ProviderModule_ProvidesOptOutStatusFactory implements Factory<UpsightOptOutStatus> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ProviderModule_ProvidesOptOutStatusFactory.class.desiredAssertionStatus());
    private final ProviderModule module;
    private final Provider<OptOutStatus> optOutStatusProvider;

    public ProviderModule_ProvidesOptOutStatusFactory(ProviderModule module, Provider<OptOutStatus> optOutStatusProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || optOutStatusProvider != null) {
                this.optOutStatusProvider = optOutStatusProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightOptOutStatus get() {
        return (UpsightOptOutStatus) Preconditions.checkNotNull(this.module.providesOptOutStatus((OptOutStatus) this.optOutStatusProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightOptOutStatus> create(ProviderModule module, Provider<OptOutStatus> optOutStatusProvider) {
        return new ProviderModule_ProvidesOptOutStatusFactory(module, optOutStatusProvider);
    }
}
