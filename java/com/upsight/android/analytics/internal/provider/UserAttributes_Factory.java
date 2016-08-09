package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class UserAttributes_Factory implements Factory<UserAttributes> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UserAttributes_Factory.class.desiredAssertionStatus());
    private final Provider<UpsightContext> upsightProvider;
    private final MembersInjector<UserAttributes> userAttributesMembersInjector;

    public UserAttributes_Factory(MembersInjector<UserAttributes> userAttributesMembersInjector, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || userAttributesMembersInjector != null) {
            this.userAttributesMembersInjector = userAttributesMembersInjector;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UserAttributes get() {
        return (UserAttributes) MembersInjectors.injectMembers(this.userAttributesMembersInjector, new UserAttributes((UpsightContext) this.upsightProvider.get()));
    }

    public static Factory<UserAttributes> create(MembersInjector<UserAttributes> userAttributesMembersInjector, Provider<UpsightContext> upsightProvider) {
        return new UserAttributes_Factory(userAttributesMembersInjector, upsightProvider);
    }
}
