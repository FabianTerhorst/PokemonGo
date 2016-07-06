package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.SessionManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PushModule_ProvideSessionManagerFactory implements Factory<SessionManager> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PushModule_ProvideSessionManagerFactory.class.desiredAssertionStatus());
    private final PushModule module;
    private final Provider<UpsightContext> upsightProvider;

    public PushModule_ProvideSessionManagerFactory(PushModule module, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public SessionManager get() {
        SessionManager provided = this.module.provideSessionManager((UpsightContext) this.upsightProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SessionManager> create(PushModule module, Provider<UpsightContext> upsightProvider) {
        return new PushModule_ProvideSessionManagerFactory(module, upsightProvider);
    }
}
