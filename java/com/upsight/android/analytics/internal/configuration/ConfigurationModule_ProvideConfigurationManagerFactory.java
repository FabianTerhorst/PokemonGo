package com.upsight.android.analytics.internal.configuration;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ConfigurationModule_ProvideConfigurationManagerFactory implements Factory<ConfigurationManager> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ConfigurationModule_ProvideConfigurationManagerFactory.class.desiredAssertionStatus());
    private final Provider<ManagerConfigParser> managerConfigParserProvider;
    private final ConfigurationModule module;
    private final Provider<ConfigurationResponseParser> responseParserProvider;
    private final Provider<UpsightContext> upsightProvider;

    public ConfigurationModule_ProvideConfigurationManagerFactory(ConfigurationModule module, Provider<UpsightContext> upsightProvider, Provider<ConfigurationResponseParser> responseParserProvider, Provider<ManagerConfigParser> managerConfigParserProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || responseParserProvider != null) {
                    this.responseParserProvider = responseParserProvider;
                    if ($assertionsDisabled || managerConfigParserProvider != null) {
                        this.managerConfigParserProvider = managerConfigParserProvider;
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

    public ConfigurationManager get() {
        return (ConfigurationManager) Preconditions.checkNotNull(this.module.provideConfigurationManager((UpsightContext) this.upsightProvider.get(), (ConfigurationResponseParser) this.responseParserProvider.get(), (ManagerConfigParser) this.managerConfigParserProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ConfigurationManager> create(ConfigurationModule module, Provider<UpsightContext> upsightProvider, Provider<ConfigurationResponseParser> responseParserProvider, Provider<ManagerConfigParser> managerConfigParserProvider) {
        return new ConfigurationModule_ProvideConfigurationManagerFactory(module, upsightProvider, responseParserProvider, managerConfigParserProvider);
    }
}
