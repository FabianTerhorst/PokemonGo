package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class OptOutStatus_Factory implements Factory<OptOutStatus> {
    static final /* synthetic */ boolean $assertionsDisabled = (!OptOutStatus_Factory.class.desiredAssertionStatus());
    private final MembersInjector<OptOutStatus> membersInjector;
    private final Provider<UpsightContext> upsightProvider;

    public OptOutStatus_Factory(MembersInjector<OptOutStatus> membersInjector, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || membersInjector != null) {
            this.membersInjector = membersInjector;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public OptOutStatus get() {
        OptOutStatus instance = new OptOutStatus((UpsightContext) this.upsightProvider.get());
        this.membersInjector.injectMembers(instance);
        return instance;
    }

    public static Factory<OptOutStatus> create(MembersInjector<OptOutStatus> membersInjector, Provider<UpsightContext> upsightProvider) {
        return new OptOutStatus_Factory(membersInjector, upsightProvider);
    }
}
