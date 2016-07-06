package com.upsight.android.googlepushservices.internal;

import android.app.IntentService;
import com.upsight.android.analytics.internal.session.SessionManager;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class PushClickIntentService_MembersInjector implements MembersInjector<PushClickIntentService> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PushClickIntentService_MembersInjector.class.desiredAssertionStatus());
    private final Provider<SessionManager> mSessionManagerProvider;
    private final MembersInjector<IntentService> supertypeInjector;

    public PushClickIntentService_MembersInjector(MembersInjector<IntentService> supertypeInjector, Provider<SessionManager> mSessionManagerProvider) {
        if ($assertionsDisabled || supertypeInjector != null) {
            this.supertypeInjector = supertypeInjector;
            if ($assertionsDisabled || mSessionManagerProvider != null) {
                this.mSessionManagerProvider = mSessionManagerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public void injectMembers(PushClickIntentService instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        this.supertypeInjector.injectMembers(instance);
        instance.mSessionManager = (SessionManager) this.mSessionManagerProvider.get();
    }

    public static MembersInjector<PushClickIntentService> create(MembersInjector<IntentService> supertypeInjector, Provider<SessionManager> mSessionManagerProvider) {
        return new PushClickIntentService_MembersInjector(supertypeInjector, mSessionManagerProvider);
    }
}
