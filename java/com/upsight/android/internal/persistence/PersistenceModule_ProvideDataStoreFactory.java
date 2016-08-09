package com.upsight.android.internal.persistence;

import android.content.Context;
import com.squareup.otto.Bus;
import com.upsight.android.internal.persistence.storable.StorableIdFactory;
import com.upsight.android.internal.persistence.storable.StorableInfoCache;
import com.upsight.android.persistence.UpsightDataStore;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import rx.Scheduler;

public final class PersistenceModule_ProvideDataStoreFactory implements Factory<UpsightDataStore> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PersistenceModule_ProvideDataStoreFactory.class.desiredAssertionStatus());
    private final Provider<Bus> busProvider;
    private final Provider<Context> contextProvider;
    private final Provider<StorableIdFactory> idFactoryProvider;
    private final Provider<StorableInfoCache> infoCacheProvider;
    private final PersistenceModule module;
    private final Provider<Scheduler> observeOnSchedulerProvider;
    private final Provider<Scheduler> subscribeOnSchedulerProvider;

    public PersistenceModule_ProvideDataStoreFactory(PersistenceModule module, Provider<Context> contextProvider, Provider<StorableInfoCache> infoCacheProvider, Provider<StorableIdFactory> idFactoryProvider, Provider<Scheduler> subscribeOnSchedulerProvider, Provider<Scheduler> observeOnSchedulerProvider, Provider<Bus> busProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || contextProvider != null) {
                this.contextProvider = contextProvider;
                if ($assertionsDisabled || infoCacheProvider != null) {
                    this.infoCacheProvider = infoCacheProvider;
                    if ($assertionsDisabled || idFactoryProvider != null) {
                        this.idFactoryProvider = idFactoryProvider;
                        if ($assertionsDisabled || subscribeOnSchedulerProvider != null) {
                            this.subscribeOnSchedulerProvider = subscribeOnSchedulerProvider;
                            if ($assertionsDisabled || observeOnSchedulerProvider != null) {
                                this.observeOnSchedulerProvider = observeOnSchedulerProvider;
                                if ($assertionsDisabled || busProvider != null) {
                                    this.busProvider = busProvider;
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

    public UpsightDataStore get() {
        return (UpsightDataStore) Preconditions.checkNotNull(this.module.provideDataStore((Context) this.contextProvider.get(), (StorableInfoCache) this.infoCacheProvider.get(), (StorableIdFactory) this.idFactoryProvider.get(), (Scheduler) this.subscribeOnSchedulerProvider.get(), (Scheduler) this.observeOnSchedulerProvider.get(), (Bus) this.busProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightDataStore> create(PersistenceModule module, Provider<Context> contextProvider, Provider<StorableInfoCache> infoCacheProvider, Provider<StorableIdFactory> idFactoryProvider, Provider<Scheduler> subscribeOnSchedulerProvider, Provider<Scheduler> observeOnSchedulerProvider, Provider<Bus> busProvider) {
        return new PersistenceModule_ProvideDataStoreFactory(module, contextProvider, infoCacheProvider, idFactoryProvider, subscribeOnSchedulerProvider, observeOnSchedulerProvider, busProvider);
    }
}
