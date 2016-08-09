package com.upsight.android.googlepushservices.internal;

import com.upsight.android.analytics.internal.session.SessionManager;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class PushClickIntentService_MembersInjector implements MembersInjector<PushClickIntentService> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PushClickIntentService_MembersInjector.class.desiredAssertionStatus());
    private final Provider<SessionManager> mSessionManagerProvider;

    public PushClickIntentService_MembersInjector(Provider<SessionManager> mSessionManagerProvider) {
        if ($assertionsDisabled || mSessionManagerProvider != null) {
            this.mSessionManagerProvider = mSessionManagerProvider;
            return;
        }
        throw new AssertionError();
    }

    public static MembersInjector<PushClickIntentService> create(Provider<SessionManager> mSessionManagerProvider) {
        return new PushClickIntentService_MembersInjector(mSessionManagerProvider);
    }

    public void injectMembers(PushClickIntentService instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mSessionManager = (SessionManager) this.mSessionManagerProvider.get();
    }

    public static void injectMSessionManager(PushClickIntentService instance, Provider<SessionManager> mSessionManagerProvider) {
        instance.mSessionManager = (SessionManager) mSessionManagerProvider.get();
    }
}
