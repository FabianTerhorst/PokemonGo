package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class PushModule {
    private final UpsightContext mUpsight;

    public PushModule(UpsightContext upsight) {
        this.mUpsight = upsight;
    }

    @Singleton
    @Provides
    UpsightContext provideUpsightContext() {
        return this.mUpsight;
    }

    @Singleton
    @Provides
    SessionManager provideSessionManager(UpsightContext upsight) {
        return ((UpsightAnalyticsComponent) ((UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME)).getComponent()).sessionManager();
    }

    @Singleton
    @Provides
    public UpsightGooglePushServicesApi provideGooglePushServicesApi(GooglePushServices googlePushServices) {
        return googlePushServices;
    }
}
