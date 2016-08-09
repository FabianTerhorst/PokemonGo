package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class OptOutStatus_Factory implements Factory<OptOutStatus> {
    static final /* synthetic */ boolean $assertionsDisabled = (!OptOutStatus_Factory.class.desiredAssertionStatus());
    private final MembersInjector<OptOutStatus> optOutStatusMembersInjector;
    private final Provider<UpsightContext> upsightProvider;

    public OptOutStatus_Factory(MembersInjector<OptOutStatus> optOutStatusMembersInjector, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || optOutStatusMembersInjector != null) {
            this.optOutStatusMembersInjector = optOutStatusMembersInjector;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public OptOutStatus get() {
        return (OptOutStatus) MembersInjectors.injectMembers(this.optOutStatusMembersInjector, new OptOutStatus((UpsightContext) this.upsightProvider.get()));
    }

    public static Factory<OptOutStatus> create(MembersInjector<OptOutStatus> optOutStatusMembersInjector, Provider<UpsightContext> upsightProvider) {
        return new OptOutStatus_Factory(optOutStatusMembersInjector, upsightProvider);
    }
}
