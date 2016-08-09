package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class LocationTracker_Factory implements Factory<LocationTracker> {
    static final /* synthetic */ boolean $assertionsDisabled = (!LocationTracker_Factory.class.desiredAssertionStatus());
    private final MembersInjector<LocationTracker> locationTrackerMembersInjector;
    private final Provider<UpsightContext> upsightProvider;

    public LocationTracker_Factory(MembersInjector<LocationTracker> locationTrackerMembersInjector, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || locationTrackerMembersInjector != null) {
            this.locationTrackerMembersInjector = locationTrackerMembersInjector;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public LocationTracker get() {
        return (LocationTracker) MembersInjectors.injectMembers(this.locationTrackerMembersInjector, new LocationTracker((UpsightContext) this.upsightProvider.get()));
    }

    public static Factory<LocationTracker> create(MembersInjector<LocationTracker> locationTrackerMembersInjector, Provider<UpsightContext> upsightProvider) {
        return new LocationTracker_Factory(locationTrackerMembersInjector, upsightProvider);
    }
}
