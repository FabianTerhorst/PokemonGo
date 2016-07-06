package com.upsight.android.analytics.internal.configuration;

import com.upsight.android.UpsightContext;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class ConfigurationModule {
    @Singleton
    @Provides
    public ConfigurationManager provideConfigurationManager(UpsightContext upsight, ConfigurationResponseParser responseParser, ManagerConfigParser managerConfigParser) {
        return new ConfigurationManager(upsight, upsight.getDataStore(), responseParser, managerConfigParser, upsight.getCoreComponent().subscribeOnScheduler(), upsight.getLogger());
    }
}
