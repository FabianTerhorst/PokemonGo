package com.upsight.android.analytics.internal;

import com.upsight.android.analytics.internal.configuration.ConfigurationManager;
import com.upsight.android.analytics.internal.dispatcher.Dispatcher;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class DispatcherService_MembersInjector implements MembersInjector<DispatcherService> {
    static final /* synthetic */ boolean $assertionsDisabled = (!DispatcherService_MembersInjector.class.desiredAssertionStatus());
    private final Provider<ConfigurationManager> mConfigurationManagerProvider;
    private final Provider<Dispatcher> mDispatcherProvider;

    public DispatcherService_MembersInjector(Provider<ConfigurationManager> mConfigurationManagerProvider, Provider<Dispatcher> mDispatcherProvider) {
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

    public static MembersInjector<DispatcherService> create(Provider<ConfigurationManager> mConfigurationManagerProvider, Provider<Dispatcher> mDispatcherProvider) {
        return new DispatcherService_MembersInjector(mConfigurationManagerProvider, mDispatcherProvider);
    }

    public void injectMembers(DispatcherService instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mConfigurationManager = (ConfigurationManager) this.mConfigurationManagerProvider.get();
        instance.mDispatcher = (Dispatcher) this.mDispatcherProvider.get();
    }

    public static void injectMConfigurationManager(DispatcherService instance, Provider<ConfigurationManager> mConfigurationManagerProvider) {
        instance.mConfigurationManager = (ConfigurationManager) mConfigurationManagerProvider.get();
    }

    public static void injectMDispatcher(DispatcherService instance, Provider<Dispatcher> mDispatcherProvider) {
        instance.mDispatcher = (Dispatcher) mDispatcherProvider.get();
    }
}
