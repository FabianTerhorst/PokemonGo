package com.upsight.android.analytics.internal;

import android.app.Service;
import com.upsight.android.analytics.internal.configuration.ConfigurationManager;
import com.upsight.android.analytics.internal.dispatcher.Dispatcher;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class DispatcherService_MembersInjector implements MembersInjector<DispatcherService> {
    static final /* synthetic */ boolean $assertionsDisabled = (!DispatcherService_MembersInjector.class.desiredAssertionStatus());
    private final Provider<ConfigurationManager> mConfigurationManagerProvider;
    private final Provider<Dispatcher> mDispatcherProvider;
    private final MembersInjector<Service> supertypeInjector;

    public DispatcherService_MembersInjector(MembersInjector<Service> supertypeInjector, Provider<ConfigurationManager> mConfigurationManagerProvider, Provider<Dispatcher> mDispatcherProvider) {
        if ($assertionsDisabled || supertypeInjector != null) {
            this.supertypeInjector = supertypeInjector;
            if ($assertionsDisabled || mConfigurationManagerProvider != null) {
                this.mConfigurationManagerProvider = mConfigurationManagerProvider;
                if ($assertionsDisabled || mDispatcherProvider != null) {
                    this.mDispatcherProvider = mDispatcherProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public void injectMembers(DispatcherService instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        this.supertypeInjector.injectMembers(instance);
        instance.mConfigurationManager = (ConfigurationManager) this.mConfigurationManagerProvider.get();
        instance.mDispatcher = (Dispatcher) this.mDispatcherProvider.get();
    }

    public static MembersInjector<DispatcherService> create(MembersInjector<Service> supertypeInjector, Provider<ConfigurationManager> mConfigurationManagerProvider, Provider<Dispatcher> mDispatcherProvider) {
        return new DispatcherService_MembersInjector(supertypeInjector, mConfigurationManagerProvider, mDispatcherProvider);
    }
}
