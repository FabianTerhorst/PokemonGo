package com.upsight.android.internal;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.squareup.otto.Bus;
import com.upsight.android.UpsightContext;
import com.upsight.android.internal.logger.LogWriter;
import com.upsight.android.internal.logger.LoggerModule;
import com.upsight.android.internal.logger.LoggerModule_ProvideUpsightLoggerFactory;
import com.upsight.android.internal.persistence.PersistenceModule;
import com.upsight.android.internal.persistence.PersistenceModule_ProvideBackgroundDataStoreFactory;
import com.upsight.android.internal.persistence.PersistenceModule_ProvideDataStoreFactory;
import com.upsight.android.internal.persistence.storable.StorableIdFactory;
import com.upsight.android.internal.persistence.storable.StorableInfoCache;
import com.upsight.android.internal.persistence.storable.StorableModule;
import com.upsight.android.internal.persistence.storable.StorableModule_ProvideStorableInfoCacheFactory;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import rx.Scheduler;

public final class DaggerCoreComponent implements CoreComponent {
    static final /* synthetic */ boolean $assertionsDisabled = (!DaggerCoreComponent.class.desiredAssertionStatus());
    private Provider<Context> provideApplicationContextProvider;
    private Provider<String> provideApplicationTokenProvider;
    private Provider<UpsightDataStore> provideBackgroundDataStoreProvider;
    private Provider<Bus> provideBusProvider;
    private Provider<UpsightDataStore> provideDataStoreProvider;
    private Provider<Gson> provideGsonProvider;
    private Provider<JsonParser> provideJsonParserProvider;
    private Provider<LogWriter> provideLogWriterProvider;
    private Provider<Scheduler> provideObserveOnSchedulerProvider;
    private Provider<String> providePublicKeyProvider;
    private Provider<String> provideSdkPluginProvider;
    private Provider<StorableInfoCache> provideStorableInfoCacheProvider;
    private Provider<Scheduler> provideSubscribeOnSchedulerProvider;
    private Provider<StorableIdFactory> provideTypeIdGeneratorProvider;
    private Provider<UpsightContext> provideUpsightContextProvider;
    private Provider<UpsightLogger> provideUpsightLoggerProvider;

    public static final class Builder {
        private ContextModule contextModule;
        private JsonModule jsonModule;
        private LoggerModule loggerModule;
        private PersistenceModule persistenceModule;
        private PropertiesModule propertiesModule;
        private SchedulersModule schedulersModule;
        private StorableModule storableModule;
        private UpsightContextModule upsightContextModule;

        private Builder() {
        }

        public CoreComponent build() {
            if (this.contextModule == null) {
                throw new IllegalStateException(ContextModule.class.getCanonicalName() + " must be set");
            }
            if (this.jsonModule == null) {
                this.jsonModule = new JsonModule();
            }
            if (this.storableModule == null) {
                this.storableModule = new StorableModule();
            }
            if (this.schedulersModule == null) {
                this.schedulersModule = new SchedulersModule();
            }
            if (this.persistenceModule == null) {
                this.persistenceModule = new PersistenceModule();
            }
            if (this.loggerModule == null) {
                this.loggerModule = new LoggerModule();
            }
            if (this.propertiesModule == null) {
                this.propertiesModule = new PropertiesModule();
            }
            if (this.upsightContextModule == null) {
                this.upsightContextModule = new UpsightContextModule();
            }
            return new DaggerCoreComponent();
        }

        @Deprecated
        public Builder coreModule(CoreModule coreModule) {
            Preconditions.checkNotNull(coreModule);
            return this;
        }

        public Builder upsightContextModule(UpsightContextModule upsightContextModule) {
            this.upsightContextModule = (UpsightContextModule) Preconditions.checkNotNull(upsightContextModule);
            return this;
        }

        public Builder contextModule(ContextModule contextModule) {
            this.contextModule = (ContextModule) Preconditions.checkNotNull(contextModule);
            return this;
        }

        public Builder propertiesModule(PropertiesModule propertiesModule) {
            this.propertiesModule = (PropertiesModule) Preconditions.checkNotNull(propertiesModule);
            return this;
        }

        public Builder jsonModule(JsonModule jsonModule) {
            this.jsonModule = (JsonModule) Preconditions.checkNotNull(jsonModule);
            return this;
        }

        public Builder schedulersModule(SchedulersModule schedulersModule) {
            this.schedulersModule = (SchedulersModule) Preconditions.checkNotNull(schedulersModule);
            return this;
        }

        public Builder storableModule(StorableModule storableModule) {
            this.storableModule = (StorableModule) Preconditions.checkNotNull(storableModule);
            return this;
        }

        public Builder persistenceModule(PersistenceModule persistenceModule) {
            this.persistenceModule = (PersistenceModule) Preconditions.checkNotNull(persistenceModule);
            return this;
        }

        public Builder loggerModule(LoggerModule loggerModule) {
            this.loggerModule = (LoggerModule) Preconditions.checkNotNull(loggerModule);
            return this;
        }
    }

    private DaggerCoreComponent(Builder builder) {
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
        this.provideApplicationContextProvider = DoubleCheck.provider(ContextModule_ProvideApplicationContextFactory.create(builder.contextModule));
        this.provideGsonProvider = DoubleCheck.provider(JsonModule_ProvideGsonFactory.create(builder.jsonModule));
        this.provideStorableInfoCacheProvider = DoubleCheck.provider(StorableModule_ProvideStorableInfoCacheFactory.create(builder.storableModule, this.provideGsonProvider));
        this.provideTypeIdGeneratorProvider = DoubleCheck.provider(ContextModule_ProvideTypeIdGeneratorFactory.create(builder.contextModule));
        this.provideSubscribeOnSchedulerProvider = DoubleCheck.provider(SchedulersModule_ProvideSubscribeOnSchedulerFactory.create(builder.schedulersModule));
        this.provideObserveOnSchedulerProvider = DoubleCheck.provider(SchedulersModule_ProvideObserveOnSchedulerFactory.create(builder.schedulersModule));
        this.provideBusProvider = DoubleCheck.provider(ContextModule_ProvideBusFactory.create(builder.contextModule));
        this.provideDataStoreProvider = DoubleCheck.provider(PersistenceModule_ProvideDataStoreFactory.create(builder.persistenceModule, this.provideApplicationContextProvider, this.provideStorableInfoCacheProvider, this.provideTypeIdGeneratorProvider, this.provideSubscribeOnSchedulerProvider, this.provideObserveOnSchedulerProvider, this.provideBusProvider));
        this.provideLogWriterProvider = DoubleCheck.provider(ContextModule_ProvideLogWriterFactory.create(builder.contextModule));
        this.provideUpsightLoggerProvider = DoubleCheck.provider(LoggerModule_ProvideUpsightLoggerFactory.create(builder.loggerModule, this.provideDataStoreProvider, this.provideLogWriterProvider));
        this.provideSdkPluginProvider = DoubleCheck.provider(PropertiesModule_ProvideSdkPluginFactory.create(builder.propertiesModule, this.provideApplicationContextProvider, this.provideUpsightLoggerProvider));
        this.provideApplicationTokenProvider = DoubleCheck.provider(PropertiesModule_ProvideApplicationTokenFactory.create(builder.propertiesModule, this.provideApplicationContextProvider, this.provideUpsightLoggerProvider));
        this.providePublicKeyProvider = DoubleCheck.provider(PropertiesModule_ProvidePublicKeyFactory.create(builder.propertiesModule, this.provideApplicationContextProvider, this.provideUpsightLoggerProvider));
        this.provideUpsightContextProvider = DoubleCheck.provider(UpsightContextModule_ProvideUpsightContextFactory.create(builder.upsightContextModule, this.provideApplicationContextProvider, this.provideSdkPluginProvider, this.provideApplicationTokenProvider, this.providePublicKeyProvider, this.provideDataStoreProvider, this.provideUpsightLoggerProvider));
        this.provideJsonParserProvider = DoubleCheck.provider(JsonModule_ProvideJsonParserFactory.create(builder.jsonModule));
        this.provideBackgroundDataStoreProvider = DoubleCheck.provider(PersistenceModule_ProvideBackgroundDataStoreFactory.create(builder.persistenceModule, this.provideApplicationContextProvider, this.provideSubscribeOnSchedulerProvider, this.provideTypeIdGeneratorProvider, this.provideStorableInfoCacheProvider, this.provideBusProvider));
    }

    public UpsightContext upsightContext() {
        return (UpsightContext) this.provideUpsightContextProvider.get();
    }

    public Context applicationContext() {
        return (Context) this.provideApplicationContextProvider.get();
    }

    public Bus bus() {
        return (Bus) this.provideBusProvider.get();
    }

    public Gson gson() {
        return (Gson) this.provideGsonProvider.get();
    }

    public JsonParser jsonParser() {
        return (JsonParser) this.provideJsonParserProvider.get();
    }

    public Scheduler subscribeOnScheduler() {
        return (Scheduler) this.provideSubscribeOnSchedulerProvider.get();
    }

    public Scheduler observeOnScheduler() {
        return (Scheduler) this.provideObserveOnSchedulerProvider.get();
    }

    public UpsightDataStore backgroundDataStore() {
        return (UpsightDataStore) this.provideBackgroundDataStoreProvider.get();
    }
}
