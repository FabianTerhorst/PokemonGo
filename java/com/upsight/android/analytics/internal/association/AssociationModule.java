package com.upsight.android.analytics.internal.association;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.Clock;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class AssociationModule {
    @Singleton
    @Provides
    public AssociationManager provideAssociationManager(UpsightContext upsight, Clock clock) {
        return new AssociationManagerImpl(upsight.getDataStore(), clock);
    }
}
