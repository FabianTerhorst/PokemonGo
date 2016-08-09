package com.upsight.android.analytics.internal.dispatcher;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.AnalyticsContext;
import com.upsight.android.analytics.internal.dispatcher.routing.RouterBuilder;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaSelectorBuilder;
import com.upsight.android.analytics.internal.session.SessionManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DispatchModule_ProvideDispatcherFactory implements Factory<Dispatcher> {
    static final /* synthetic */ boolean $assertionsDisabled = (!DispatchModule_ProvideDispatcherFactory.class.desiredAssertionStatus());
    private final Provider<ConfigParser> configParserProvider;
    private final Provider<AnalyticsContext> contextProvider;
    private final DispatchModule module;
    private final Provider<RouterBuilder> routerBuilderProvider;
    private final Provider<SchemaSelectorBuilder> schemaSelectorBuilderProvider;
    private final Provider<SessionManager> sessionManagerProvider;
    private final Provider<UpsightContext> upsightProvider;

    public DispatchModule_ProvideDispatcherFactory(DispatchModule module, Provider<UpsightContext> upsightProvider, Provider<SessionManager> sessionManagerProvider, Provider<AnalyticsContext> contextProvider, Provider<ConfigParser> configParserProvider, Provider<RouterBuilder> routerBuilderProvider, Provider<SchemaSelectorBuilder> schemaSelectorBuilderProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || sessionManagerProvider != null) {
                    this.sessionManagerProvider = sessionManagerProvider;
                    if ($assertionsDisabled || contextProvider != null) {
                        this.contextProvider = contextProvider;
                        if ($assertionsDisabled || configParserProvider != null) {
                            this.configParserProvider = configParserProvider;
                            if ($assertionsDisabled || routerBuilderProvider != null) {
                                this.routerBuilderProvider = routerBuilderProvider;
                                if ($assertionsDisabled || schemaSelectorBuilderProvider != null) {
                                    this.schemaSelectorBuilderProvider = schemaSelectorBuilderProvider;
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
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public Dispatcher get() {
        return (Dispatcher) Preconditions.checkNotNull(this.module.provideDispatcher((UpsightContext) this.upsightProvider.get(), (SessionManager) this.sessionManagerProvider.get(), (AnalyticsContext) this.contextProvider.get(), (ConfigParser) this.configParserProvider.get(), (RouterBuilder) this.routerBuilderProvider.get(), (SchemaSelectorBuilder) this.schemaSelectorBuilderProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Dispatcher> create(DispatchModule module, Provider<UpsightContext> upsightProvider, Provider<SessionManager> sessionManagerProvider, Provider<AnalyticsContext> contextProvider, Provider<ConfigParser> configParserProvider, Provider<RouterBuilder> routerBuilderProvider, Provider<SchemaSelectorBuilder> schemaSelectorBuilderProvider) {
        return new DispatchModule_ProvideDispatcherFactory(module, upsightProvider, sessionManagerProvider, contextProvider, configParserProvider, routerBuilderProvider, schemaSelectorBuilderProvider);
    }
}
