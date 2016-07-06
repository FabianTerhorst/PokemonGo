package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LocationTracker_Factory implements Factory<LocationTracker> {
    static final /* synthetic */ boolean $assertionsDisabled = (!LocationTracker_Factory.class.desiredAssertionStatus());
    private final MembersInjector<LocationTracker> membersInjector;
    private final Provider<UpsightContext> upsightProvider;

    public LocationTracker_Factory(MembersInjector<LocationTracker> membersInjector, Provider<UpsightContext> upsightProvider) {
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

    public LocationTracker get() {
        LocationTracker instance = new LocationTracker((UpsightContext) this.upsightProvider.get());
        this.membersInjector.injectMembers(instance);
        return instance;
    }

    public static Factory<LocationTracker> create(MembersInjector<LocationTracker> membersInjector, Provider<UpsightContext> upsightProvider) {
        return new LocationTracker_Factory(membersInjector, upsightProvider);
    }
}
