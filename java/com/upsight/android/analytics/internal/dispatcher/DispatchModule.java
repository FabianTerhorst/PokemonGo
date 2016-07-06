package com.upsight.android.analytics.internal.dispatcher;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.AnalyticsContext;
import com.upsight.android.analytics.internal.dispatcher.routing.RouterBuilder;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaSelectorBuilder;
import com.upsight.android.analytics.internal.session.SessionManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class DispatchModule {
    @Singleton
    @Provides
    public Dispatcher provideDispatcher(UpsightContext upsight, SessionManager sessionManager, AnalyticsContext context, ConfigParser configParser, RouterBuilder routerBuilder, SchemaSelectorBuilder schemaSelectorBuilder) {
        return new Dispatcher(context, sessionManager, upsight.getCoreComponent().backgroundDataStore(), configParser, routerBuilder, schemaSelectorBuilder, upsight.getLogger());
    }
}
