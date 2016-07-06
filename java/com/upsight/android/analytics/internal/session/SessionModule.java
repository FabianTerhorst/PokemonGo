package com.upsight.android.analytics.internal.session;

import com.upsight.android.UpsightContext;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class SessionModule {
    @Singleton
    @Provides
    public SessionManager providesSessionManager(SessionManagerImpl sessionManager) {
        return sessionManager;
    }

    @Singleton
    @Provides
    public SessionManagerImpl providesSessionManagerImpl(UpsightContext upsight, ConfigParser configParser, Clock clock) {
        return new SessionManagerImpl(upsight.getCoreComponent().applicationContext(), upsight, upsight.getDataStore(), upsight.getLogger(), configParser, clock);
    }
}
