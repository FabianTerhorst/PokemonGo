package com.upsight.android.analytics.internal.session;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class SessionModule_ProvidesSessionManagerFactory implements Factory<SessionManager> {
    static final /* synthetic */ boolean $assertionsDisabled = (!SessionModule_ProvidesSessionManagerFactory.class.desiredAssertionStatus());
    private final SessionModule module;
    private final Provider<SessionManagerImpl> sessionManagerProvider;

    public SessionModule_ProvidesSessionManagerFactory(SessionModule module, Provider<SessionManagerImpl> sessionManagerProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || sessionManagerProvider != null) {
                this.sessionManagerProvider = sessionManagerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public SessionManager get() {
        SessionManager provided = this.module.providesSessionManager((SessionManagerImpl) this.sessionManagerProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SessionManager> create(SessionModule module, Provider<SessionManagerImpl> sessionManagerProvider) {
        return new SessionModule_ProvidesSessionManagerFactory(module, sessionManagerProvider);
    }
}
