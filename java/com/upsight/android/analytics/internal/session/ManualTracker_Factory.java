package com.upsight.android.analytics.internal.session;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class ManualTracker_Factory implements Factory<ManualTracker> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ManualTracker_Factory.class.desiredAssertionStatus());
    private final MembersInjector<ManualTracker> manualTrackerMembersInjector;
    private final Provider<SessionManager> sessionManagerProvider;
    private final Provider<UpsightContext> upsightProvider;

    public ManualTracker_Factory(MembersInjector<ManualTracker> manualTrackerMembersInjector, Provider<SessionManager> sessionManagerProvider, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || manualTrackerMembersInjector != null) {
            this.manualTrackerMembersInjector = manualTrackerMembersInjector;
            if ($assertionsDisabled || sessionManagerProvider != null) {
                this.sessionManagerProvider = sessionManagerProvider;
                if ($assertionsDisabled || upsightProvider != null) {
                    this.upsightProvider = upsightProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public ManualTracker get() {
        return (ManualTracker) MembersInjectors.injectMembers(this.manualTrackerMembersInjector, new ManualTracker((SessionManager) this.sessionManagerProvider.get(), (UpsightContext) this.upsightProvider.get()));
    }

    public static Factory<ManualTracker> create(MembersInjector<ManualTracker> manualTrackerMembersInjector, Provider<SessionManager> sessionManagerProvider, Provider<UpsightContext> upsightProvider) {
        return new ManualTracker_Factory(manualTrackerMembersInjector, sessionManagerProvider, upsightProvider);
    }
}
