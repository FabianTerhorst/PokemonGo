package com.upsight.android.analytics.internal;

import android.app.Application.ActivityLifecycleCallbacks;
import com.google.gson.Gson;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightAnalyticsExtension_MembersInjector;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightAnalyticsApi;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.internal.association.AssociationManager;
import com.upsight.android.analytics.internal.association.AssociationModule;
import com.upsight.android.analytics.internal.association.AssociationModule_ProvideAssociationManagerFactory;
import com.upsight.android.analytics.internal.configuration.ConfigurationManager;
import com.upsight.android.analytics.internal.configuration.ConfigurationModule;
import com.upsight.android.analytics.internal.configuration.ConfigurationModule_ProvideConfigurationManagerFactory;
import com.upsight.android.analytics.internal.configuration.ConfigurationResponseParser;
import com.upsight.android.analytics.internal.configuration.ConfigurationResponseParser_Factory;
import com.upsight.android.analytics.internal.configuration.ManagerConfigParser;
import com.upsight.android.analytics.internal.configuration.ManagerConfigParser_Factory;
import com.upsight.android.analytics.internal.dispatcher.DispatchModule;
import com.upsight.android.analytics.internal.dispatcher.DispatchModule_ProvideDispatcherFactory;
import com.upsight.android.analytics.internal.dispatcher.Dispatcher;
import com.upsight.android.analytics.internal.dispatcher.delivery.DeliveryModule;
import com.upsight.android.analytics.internal.dispatcher.delivery.DeliveryModule_ProvideQueueBuilderFactory;
import com.upsight.android.analytics.internal.dispatcher.delivery.DeliveryModule_ProvideResponseVerifierFactory;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueBuilder;
import com.upsight.android.analytics.internal.dispatcher.delivery.ResponseParser_Factory;
import com.upsight.android.analytics.internal.dispatcher.delivery.SignatureVerifier;
import com.upsight.android.analytics.internal.dispatcher.routing.RouterBuilder;
import com.upsight.android.analytics.internal.dispatcher.routing.RoutingModule;
import com.upsight.android.analytics.internal.dispatcher.routing.RoutingModule_ProvideRouterBuilderFactory;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaModule;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaModule_ProvideSchemaSelectorBuilderFactory;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaSelectorBuilder;
import com.upsight.android.analytics.internal.provider.LocationTracker_Factory;
import com.upsight.android.analytics.internal.provider.OptOutStatus_Factory;
import com.upsight.android.analytics.internal.provider.ProviderModule;
import com.upsight.android.analytics.internal.provider.ProviderModule_ProvidesOptOutStatusFactory;
import com.upsight.android.analytics.internal.provider.ProviderModule_ProvidesUpsightLocationTrackerFactory;
import com.upsight.android.analytics.internal.provider.ProviderModule_ProvidesUpsightUserAttributesFactory;
import com.upsight.android.analytics.internal.provider.UserAttributes_Factory;
import com.upsight.android.analytics.internal.session.ActivityLifecycleTracker;
import com.upsight.android.analytics.internal.session.ActivityLifecycleTracker_Factory;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.analytics.internal.session.ConfigParser_Factory;
import com.upsight.android.analytics.internal.session.LifecycleTrackerModule;
import com.upsight.android.analytics.internal.session.LifecycleTrackerModule_ProvideManualTrackerFactory;
import com.upsight.android.analytics.internal.session.LifecycleTrackerModule_ProvideUpsightLifeCycleCallbacksFactory;
import com.upsight.android.analytics.internal.session.ManualTracker_Factory;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.analytics.internal.session.SessionManagerImpl;
import com.upsight.android.analytics.internal.session.SessionModule;
import com.upsight.android.analytics.internal.session.SessionModule_ProvidesSessionManagerFactory;
import com.upsight.android.analytics.internal.session.SessionModule_ProvidesSessionManagerImplFactory;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.internal.util.Opt;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.MembersInjectors;
import dagger.internal.Preconditions;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.inject.Provider;
import rx.Scheduler;

public final class DaggerAnalyticsComponent implements AnalyticsComponent {
    static final /* synthetic */ boolean $assertionsDisabled = (!DaggerAnalyticsComponent.class.desiredAssertionStatus());
    private Provider<ActivityLifecycleTracker> activityLifecycleTrackerProvider;
    private Provider<AnalyticsContext> analyticsContextProvider;
    private Provider<Analytics> analyticsProvider;
    private Provider configParserProvider;
    private Provider configParserProvider2;
    private Provider<ConfigurationResponseParser> configurationResponseParserProvider;
    private MembersInjector<DispatcherService> dispatcherServiceMembersInjector;
    private Provider locationTrackerProvider;
    private Provider<ManagerConfigParser> managerConfigParserProvider;
    private Provider manualTrackerProvider;
    private Provider optOutStatusProvider;
    private Provider<AssociationManager> provideAssociationManagerProvider;
    private Provider<Clock> provideClockProvider;
    private Provider<Gson> provideConfigGsonProvider;
    private Provider<ConfigurationManager> provideConfigurationManagerProvider;
    private Provider<Dispatcher> provideDispatcherProvider;
    private Provider<UpsightGooglePlayHelper> provideGooglePlayHelperProvider;
    private Provider<UpsightLifeCycleTracker> provideManualTrackerProvider;
    private Provider<QueueBuilder> provideQueueBuilderProvider;
    private Provider<SignatureVerifier> provideResponseVerifierProvider;
    private Provider<RouterBuilder> provideRouterBuilderProvider;
    private Provider<Scheduler> provideSchedulingExecutorProvider;
    private Provider<SchemaSelectorBuilder> provideSchemaSelectorBuilderProvider;
    private Provider<Scheduler> provideSendingExecutorProvider;
    private Provider<Opt<UncaughtExceptionHandler>> provideUncaughtExceptionHandlerProvider;
    private Provider<UpsightAnalyticsApi> provideUpsightAnalyticsApiProvider;
    private Provider<UpsightContext> provideUpsightContextProvider;
    private Provider<ActivityLifecycleCallbacks> provideUpsightLifeCycleCallbacksProvider;
    private Provider<UpsightOptOutStatus> providesOptOutStatusProvider;
    private Provider<SessionManagerImpl> providesSessionManagerImplProvider;
    private Provider<SessionManager> providesSessionManagerProvider;
    private Provider<UpsightLocationTracker> providesUpsightLocationTrackerProvider;
    private Provider<UpsightUserAttributes> providesUpsightUserAttributesProvider;
    private Provider responseParserProvider;
    private MembersInjector<UpsightAnalyticsExtension> upsightAnalyticsExtensionMembersInjector;
    private Provider userAttributesProvider;

    public static final class Builder {
        private AnalyticsApiModule analyticsApiModule;
        private AnalyticsSchedulersModule analyticsSchedulersModule;
        private AssociationModule associationModule;
        private BaseAnalyticsModule baseAnalyticsModule;
        private ConfigGsonModule configGsonModule;
        private ConfigurationModule configurationModule;
        private DeliveryModule deliveryModule;
        private DispatchModule dispatchModule;
        private LifecycleTrackerModule lifecycleTrackerModule;
        private ProviderModule providerModule;
        private RoutingModule routingModule;
        private SchemaModule schemaModule;
        private SessionModule sessionModule;

        private Builder() {
        }

        public AnalyticsComponent build() {
            if (this.baseAnalyticsModule == null) {
                throw new IllegalStateException(BaseAnalyticsModule.class.getCanonicalName() + " must be set");
            }
            if (this.configGsonModule == null) {
                this.configGsonModule = new ConfigGsonModule();
            }
            if (this.sessionModule == null) {
                this.sessionModule = new SessionModule();
            }
            if (this.lifecycleTrackerModule == null) {
                this.lifecycleTrackerModule = new LifecycleTrackerModule();
            }
            if (this.schemaModule == null) {
                this.schemaModule = new SchemaModule();
            }
            if (this.associationModule == null) {
                this.associationModule = new AssociationModule();
            }
            if (this.providerModule == null) {
                this.providerModule = new ProviderModule();
            }
            if (this.analyticsApiModule == null) {
                this.analyticsApiModule = new AnalyticsApiModule();
            }
            if (this.configurationModule == null) {
                this.configurationModule = new ConfigurationModule();
            }
            if (this.analyticsSchedulersModule == null) {
                this.analyticsSchedulersModule = new AnalyticsSchedulersModule();
            }
            if (this.deliveryModule == null) {
                this.deliveryModule = new DeliveryModule();
            }
            if (this.routingModule == null) {
                this.routingModule = new RoutingModule();
            }
            if (this.dispatchModule == null) {
                this.dispatchModule = new DispatchModule();
            }
            return new DaggerAnalyticsComponent();
        }

        @Deprecated
        public Builder analyticsModule(AnalyticsModule analyticsModule) {
            Preconditions.checkNotNull(analyticsModule);
            return this;
        }

        public Builder analyticsApiModule(AnalyticsApiModule analyticsApiModule) {
            this.analyticsApiModule = (AnalyticsApiModule) Preconditions.checkNotNull(analyticsApiModule);
            return this;
        }

        public Builder analyticsSchedulersModule(AnalyticsSchedulersModule analyticsSchedulersModule) {
            this.analyticsSchedulersModule = (AnalyticsSchedulersModule) Preconditions.checkNotNull(analyticsSchedulersModule);
            return this;
        }

        public Builder configGsonModule(ConfigGsonModule configGsonModule) {
            this.configGsonModule = (ConfigGsonModule) Preconditions.checkNotNull(configGsonModule);
            return this;
        }

        public Builder dispatchModule(DispatchModule dispatchModule) {
            this.dispatchModule = (DispatchModule) Preconditions.checkNotNull(dispatchModule);
            return this;
        }

        public Builder deliveryModule(DeliveryModule deliveryModule) {
            this.deliveryModule = (DeliveryModule) Preconditions.checkNotNull(deliveryModule);
            return this;
        }

        public Builder routingModule(RoutingModule routingModule) {
            this.routingModule = (RoutingModule) Preconditions.checkNotNull(routingModule);
            return this;
        }

        public Builder schemaModule(SchemaModule schemaModule) {
            this.schemaModule = (SchemaModule) Preconditions.checkNotNull(schemaModule);
            return this;
        }

        public Builder configurationModule(ConfigurationModule configurationModule) {
            this.configurationModule = (ConfigurationModule) Preconditions.checkNotNull(configurationModule);
            return this;
        }

        public Builder sessionModule(SessionModule sessionModule) {
            this.sessionModule = (SessionModule) Preconditions.checkNotNull(sessionModule);
            return this;
        }

        public Builder lifecycleTrackerModule(LifecycleTrackerModule lifecycleTrackerModule) {
            this.lifecycleTrackerModule = (LifecycleTrackerModule) Preconditions.checkNotNull(lifecycleTrackerModule);
            return this;
        }

        public Builder providerModule(ProviderModule providerModule) {
            this.providerModule = (ProviderModule) Preconditions.checkNotNull(providerModule);
            return this;
        }

        public Builder associationModule(AssociationModule associationModule) {
            this.associationModule = (AssociationModule) Preconditions.checkNotNull(associationModule);
            return this;
        }

        public Builder baseAnalyticsModule(BaseAnalyticsModule baseAnalyticsModule) {
            this.baseAnalyticsModule = (BaseAnalyticsModule) Preconditions.checkNotNull(baseAnalyticsModule);
            return this;
        }
    }

    private DaggerAnalyticsComponent(Builder builder) {
        if ($assertionsDisabled || builder != null) {
            initialize(builder);
            return;
        }
        throw new AssertionError();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void initialize(Builder builder) {
        this.provideUncaughtExceptionHandlerProvider = DoubleCheck.provider(BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory.create(builder.baseAnalyticsModule));
        this.provideUpsightContextProvider = DoubleCheck.provider(BaseAnalyticsModule_ProvideUpsightContextFactory.create(builder.baseAnalyticsModule));
        this.provideConfigGsonProvider = DoubleCheck.provider(ConfigGsonModule_ProvideConfigGsonFactory.create(builder.configGsonModule, this.provideUpsightContextProvider));
        this.configParserProvider = ConfigParser_Factory.create(this.provideConfigGsonProvider);
        this.provideClockProvider = DoubleCheck.provider(BaseAnalyticsModule_ProvideClockFactory.create(builder.baseAnalyticsModule));
        this.providesSessionManagerImplProvider = DoubleCheck.provider(SessionModule_ProvidesSessionManagerImplFactory.create(builder.sessionModule, this.provideUpsightContextProvider, this.configParserProvider, this.provideClockProvider));
        this.providesSessionManagerProvider = DoubleCheck.provider(SessionModule_ProvidesSessionManagerFactory.create(builder.sessionModule, this.providesSessionManagerImplProvider));
        this.manualTrackerProvider = DoubleCheck.provider(ManualTracker_Factory.create(MembersInjectors.noOp(), this.providesSessionManagerProvider, this.provideUpsightContextProvider));
        this.provideManualTrackerProvider = DoubleCheck.provider(LifecycleTrackerModule_ProvideManualTrackerFactory.create(builder.lifecycleTrackerModule, this.manualTrackerProvider));
        this.provideSchemaSelectorBuilderProvider = DoubleCheck.provider(SchemaModule_ProvideSchemaSelectorBuilderFactory.create(builder.schemaModule, this.provideUpsightContextProvider));
        this.provideAssociationManagerProvider = DoubleCheck.provider(AssociationModule_ProvideAssociationManagerFactory.create(builder.associationModule, this.provideUpsightContextProvider, this.provideClockProvider));
        this.optOutStatusProvider = DoubleCheck.provider(OptOutStatus_Factory.create(MembersInjectors.noOp(), this.provideUpsightContextProvider));
        this.providesOptOutStatusProvider = DoubleCheck.provider(ProviderModule_ProvidesOptOutStatusFactory.create(builder.providerModule, this.optOutStatusProvider));
        this.locationTrackerProvider = DoubleCheck.provider(LocationTracker_Factory.create(MembersInjectors.noOp(), this.provideUpsightContextProvider));
        this.providesUpsightLocationTrackerProvider = DoubleCheck.provider(ProviderModule_ProvidesUpsightLocationTrackerFactory.create(builder.providerModule, this.locationTrackerProvider));
        this.userAttributesProvider = DoubleCheck.provider(UserAttributes_Factory.create(MembersInjectors.noOp(), this.provideUpsightContextProvider));
        this.providesUpsightUserAttributesProvider = DoubleCheck.provider(ProviderModule_ProvidesUpsightUserAttributesFactory.create(builder.providerModule, this.userAttributesProvider));
        this.provideGooglePlayHelperProvider = DoubleCheck.provider(BaseAnalyticsModule_ProvideGooglePlayHelperFactory.create(builder.baseAnalyticsModule, this.provideUpsightContextProvider));
        this.analyticsProvider = DoubleCheck.provider(Analytics_Factory.create(this.provideUpsightContextProvider, this.provideManualTrackerProvider, this.providesSessionManagerProvider, this.provideSchemaSelectorBuilderProvider, this.provideAssociationManagerProvider, this.providesOptOutStatusProvider, this.providesUpsightLocationTrackerProvider, this.providesUpsightUserAttributesProvider, this.provideGooglePlayHelperProvider));
        this.provideUpsightAnalyticsApiProvider = DoubleCheck.provider(AnalyticsApiModule_ProvideUpsightAnalyticsApiFactory.create(builder.analyticsApiModule, this.analyticsProvider));
        this.activityLifecycleTrackerProvider = ActivityLifecycleTracker_Factory.create(this.manualTrackerProvider);
        this.provideUpsightLifeCycleCallbacksProvider = DoubleCheck.provider(LifecycleTrackerModule_ProvideUpsightLifeCycleCallbacksFactory.create(builder.lifecycleTrackerModule, this.activityLifecycleTrackerProvider));
        this.upsightAnalyticsExtensionMembersInjector = UpsightAnalyticsExtension_MembersInjector.create(this.provideUncaughtExceptionHandlerProvider, this.provideUpsightAnalyticsApiProvider, this.provideClockProvider, this.provideUpsightLifeCycleCallbacksProvider, this.provideAssociationManagerProvider);
        this.configurationResponseParserProvider = DoubleCheck.provider(ConfigurationResponseParser_Factory.create(this.provideConfigGsonProvider, this.providesSessionManagerProvider));
        this.managerConfigParserProvider = DoubleCheck.provider(ManagerConfigParser_Factory.create(this.provideConfigGsonProvider));
        this.provideConfigurationManagerProvider = DoubleCheck.provider(ConfigurationModule_ProvideConfigurationManagerFactory.create(builder.configurationModule, this.provideUpsightContextProvider, this.configurationResponseParserProvider, this.managerConfigParserProvider));
        this.analyticsContextProvider = AnalyticsContext_Factory.create(MembersInjectors.noOp(), this.provideUpsightContextProvider);
        this.configParserProvider2 = DoubleCheck.provider(com.upsight.android.analytics.internal.dispatcher.ConfigParser_Factory.create(this.provideUpsightContextProvider, this.provideConfigGsonProvider));
        this.provideSendingExecutorProvider = DoubleCheck.provider(AnalyticsSchedulersModule_ProvideSendingExecutorFactory.create(builder.analyticsSchedulersModule));
        this.provideSchedulingExecutorProvider = DoubleCheck.provider(AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory.create(builder.analyticsSchedulersModule));
        this.provideResponseVerifierProvider = DoubleCheck.provider(DeliveryModule_ProvideResponseVerifierFactory.create(builder.deliveryModule, this.provideUpsightContextProvider));
        this.responseParserProvider = ResponseParser_Factory.create(this.provideConfigGsonProvider);
        this.provideQueueBuilderProvider = DoubleCheck.provider(DeliveryModule_ProvideQueueBuilderFactory.create(builder.deliveryModule, this.provideUpsightContextProvider, this.provideClockProvider, this.provideSendingExecutorProvider, this.provideSchedulingExecutorProvider, this.provideResponseVerifierProvider, this.responseParserProvider));
        this.provideRouterBuilderProvider = DoubleCheck.provider(RoutingModule_ProvideRouterBuilderFactory.create(builder.routingModule, this.provideUpsightContextProvider, this.provideQueueBuilderProvider));
        this.provideDispatcherProvider = DoubleCheck.provider(DispatchModule_ProvideDispatcherFactory.create(builder.dispatchModule, this.provideUpsightContextProvider, this.providesSessionManagerProvider, this.analyticsContextProvider, this.configParserProvider2, this.provideRouterBuilderProvider, this.provideSchemaSelectorBuilderProvider));
        this.dispatcherServiceMembersInjector = DispatcherService_MembersInjector.create(this.provideConfigurationManagerProvider, this.provideDispatcherProvider);
    }

    public void inject(UpsightAnalyticsExtension arg0) {
        this.upsightAnalyticsExtensionMembersInjector.injectMembers(arg0);
    }

    public Clock clock() {
        return (Clock) this.provideClockProvider.get();
    }

    public SessionManager sessionManager() {
        return (SessionManager) this.providesSessionManagerProvider.get();
    }

    public void inject(DispatcherService dispatcherService) {
        this.dispatcherServiceMembersInjector.injectMembers(dispatcherService);
    }
}
