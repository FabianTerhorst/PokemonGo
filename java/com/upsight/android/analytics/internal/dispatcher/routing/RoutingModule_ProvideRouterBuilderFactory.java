package com.upsight.android.analytics.internal.dispatcher.routing;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueBuilder;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RoutingModule_ProvideRouterBuilderFactory implements Factory<RouterBuilder> {
    static final /* synthetic */ boolean $assertionsDisabled = (!RoutingModule_ProvideRouterBuilderFactory.class.desiredAssertionStatus());
    private final RoutingModule module;
    private final Provider<QueueBuilder> queueBuilderProvider;
    private final Provider<UpsightContext> upsightProvider;

    public RoutingModule_ProvideRouterBuilderFactory(RoutingModule module, Provider<UpsightContext> upsightProvider, Provider<QueueBuilder> queueBuilderProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || queueBuilderProvider != null) {
                    this.queueBuilderProvider = queueBuilderProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public RouterBuilder get() {
        RouterBuilder provided = this.module.provideRouterBuilder((UpsightContext) this.upsightProvider.get(), (QueueBuilder) this.queueBuilderProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<RouterBuilder> create(RoutingModule module, Provider<UpsightContext> upsightProvider, Provider<QueueBuilder> queueBuilderProvider) {
        return new RoutingModule_ProvideRouterBuilderFactory(module, upsightProvider, queueBuilderProvider);
    }
}
