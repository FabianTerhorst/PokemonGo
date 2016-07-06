package com.upsight.android.internal.logger;

import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class LoggerModule {
    @Singleton
    @Provides
    UpsightLogger provideUpsightLogger(UpsightDataStore dataStore, LogWriter writer) {
        return Logger.create(dataStore, writer);
    }
}
