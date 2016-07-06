package com.upsight.android.internal;

import android.content.Context;
import com.upsight.android.UpsightContext;
import com.upsight.android.internal.util.SidHelper;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public final class UpsightContextModule {
    @Singleton
    @Provides
    UpsightContext provideUpsightContext(Context baseContext, @Named("com.upsight.sdk_plugin") String sdkPlugin, @Named("com.upsight.app_token") String appToken, @Named("com.upsight.public_key") String publicKey, UpsightDataStore dataStore, UpsightLogger logger) {
        return new UpsightContext(baseContext, sdkPlugin, appToken, publicKey, SidHelper.getSid(baseContext), dataStore, logger);
    }
}
