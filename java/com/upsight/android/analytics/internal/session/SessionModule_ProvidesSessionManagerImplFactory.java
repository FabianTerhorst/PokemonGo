package com.upsight.android.analytics.internal.session;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SessionModule_ProvidesSessionManagerImplFactory implements Factory<SessionManagerImpl> {
    static final /* synthetic */ boolean $assertionsDisabled = (!SessionModule_ProvidesSessionManagerImplFactory.class.desiredAssertionStatus());
    private final Provider<Clock> clockProvider;
    private final Provider<ConfigParser> configParserProvider;
    private final SessionModule module;
    private final Provider<UpsightContext> upsightProvider;

    public SessionModule_ProvidesSessionManagerImplFactory(SessionModule module, Provider<UpsightContext> upsightProvider, Provider<ConfigParser> configParserProvider, Provider<Clock> clockProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || configParserProvider != null) {
                    this.configParserProvider = configParserProvider;
                    if ($assertionsDisabled || clockProvider != null) {
                        this.clockProvider = clockProvider;
                        return;
                    }
                    throw new AssertionError();
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public SessionManagerImpl get() {
        SessionManagerImpl provided = this.module.providesSessionManagerImpl((UpsightContext) this.upsightProvider.get(), (ConfigParser) this.configParserProvider.get(), (Clock) this.clockProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SessionManagerImpl> create(SessionModule module, Provider<UpsightContext> upsightProvider, Provider<ConfigParser> configParserProvider, Provider<Clock> clockProvider) {
        return new SessionModule_ProvidesSessionManagerImplFactory(module, upsightProvider, configParserProvider, clockProvider);
    }
}
