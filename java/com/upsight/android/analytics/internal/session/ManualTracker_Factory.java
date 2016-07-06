package com.upsight.android.analytics.internal.session;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ManualTracker_Factory implements Factory<ManualTracker> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ManualTracker_Factory.class.desiredAssertionStatus());
    private final Provider<SessionManager> sessionManagerProvider;
    private final Provider<UpsightContext> upsightProvider;

    public ManualTracker_Factory(Provider<SessionManager> sessionManagerProvider, Provider<UpsightContext> upsightProvider) {
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

    public ManualTracker get() {
        return new ManualTracker((SessionManager) this.sessionManagerProvider.get(), (UpsightContext) this.upsightProvider.get());
    }

    public static Factory<ManualTracker> create(Provider<SessionManager> sessionManagerProvider, Provider<UpsightContext> upsightProvider) {
        return new ManualTracker_Factory(sessionManagerProvider, upsightProvider);
    }
}
