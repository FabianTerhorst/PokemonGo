package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.session.Session;
import com.upsight.android.analytics.internal.session.SessionInitializer;
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
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return ((UpsightAnalyticsComponent) extension.getComponent()).sessionManager();
        }
        return new SessionManager() {
            public Session getCurrentSession() {
                return null;
            }

            public Session startSession(SessionInitializer sessionInitializer) {
                return null;
            }

            public void stopSession() {
            }
        };
    }

    @Singleton
    @Provides
    public UpsightGooglePushServicesApi provideGooglePushServicesApi(GooglePushServices googlePushServices) {
        return googlePushServices;
    }

    @Singleton
    @Provides
    public PushConfigManager providePushConfigManager(UpsightContext upsight) {
        return new PushConfigManager(upsight);
    }
}
