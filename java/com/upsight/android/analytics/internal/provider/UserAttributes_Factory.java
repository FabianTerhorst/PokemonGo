package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UserAttributes_Factory implements Factory<UserAttributes> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UserAttributes_Factory.class.desiredAssertionStatus());
    private final MembersInjector<UserAttributes> membersInjector;
    private final Provider<UpsightContext> upsightProvider;

    public UserAttributes_Factory(MembersInjector<UserAttributes> membersInjector, Provider<UpsightContext> upsightProvider) {
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

    public UserAttributes get() {
        UserAttributes instance = new UserAttributes((UpsightContext) this.upsightProvider.get());
        this.membersInjector.injectMembers(instance);
        return instance;
    }

    public static Factory<UserAttributes> create(MembersInjector<UserAttributes> membersInjector, Provider<UpsightContext> upsightProvider) {
        return new UserAttributes_Factory(membersInjector, upsightProvider);
    }
}
