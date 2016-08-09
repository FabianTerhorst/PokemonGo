package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.internal.association.AssociationManager;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaSelectorBuilder;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class Analytics_Factory implements Factory<Analytics> {
    static final /* synthetic */ boolean $assertionsDisabled = (!Analytics_Factory.class.desiredAssertionStatus());
    private final Provider<AssociationManager> associationManagerProvider;
    private final Provider<UpsightGooglePlayHelper> googlePlayHelperProvider;
    private final Provider<UpsightLifeCycleTracker> lifeCycleTrackerProvider;
    private final Provider<UpsightLocationTracker> locationTrackerProvider;
    private final Provider<UpsightOptOutStatus> optOutStatusProvider;
    private final Provider<SchemaSelectorBuilder> schemaSelectorProvider;
    private final Provider<SessionManager> sessionManagerProvider;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<UpsightUserAttributes> userAttributesProvider;

    public Analytics_Factory(Provider<UpsightContext> upsightProvider, Provider<UpsightLifeCycleTracker> lifeCycleTrackerProvider, Provider<SessionManager> sessionManagerProvider, Provider<SchemaSelectorBuilder> schemaSelectorProvider, Provider<AssociationManager> associationManagerProvider, Provider<UpsightOptOutStatus> optOutStatusProvider, Provider<UpsightLocationTracker> locationTrackerProvider, Provider<UpsightUserAttributes> userAttributesProvider, Provider<UpsightGooglePlayHelper> googlePlayHelperProvider) {
        if ($assertionsDisabled || upsightProvider != null) {
            this.upsightProvider = upsightProvider;
            if ($assertionsDisabled || lifeCycleTrackerProvider != null) {
                this.lifeCycleTrackerProvider = lifeCycleTrackerProvider;
                if ($assertionsDisabled || sessionManagerProvider != null) {
                    this.sessionManagerProvider = sessionManagerProvider;
                    if ($assertionsDisabled || schemaSelectorProvider != null) {
                        this.schemaSelectorProvider = schemaSelectorProvider;
                        if ($assertionsDisabled || associationManagerProvider != null) {
                            this.associationManagerProvider = associationManagerProvider;
                            if ($assertionsDisabled || optOutStatusProvider != null) {
                                this.optOutStatusProvider = optOutStatusProvider;
                                if ($assertionsDisabled || locationTrackerProvider != null) {
                                    this.locationTrackerProvider = locationTrackerProvider;
                                    if ($assertionsDisabled || userAttributesProvider != null) {
                                        this.userAttributesProvider = userAttributesProvider;
                                        if ($assertionsDisabled || googlePlayHelperProvider != null) {
                                            this.googlePlayHelperProvider = googlePlayHelperProvider;
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
                        throw new AssertionError();
                    }
                    throw new AssertionError();
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public Analytics get() {
        return new Analytics((UpsightContext) this.upsightProvider.get(), (UpsightLifeCycleTracker) this.lifeCycleTrackerProvider.get(), (SessionManager) this.sessionManagerProvider.get(), (SchemaSelectorBuilder) this.schemaSelectorProvider.get(), (AssociationManager) this.associationManagerProvider.get(), (UpsightOptOutStatus) this.optOutStatusProvider.get(), (UpsightLocationTracker) this.locationTrackerProvider.get(), (UpsightUserAttributes) this.userAttributesProvider.get(), (UpsightGooglePlayHelper) this.googlePlayHelperProvider.get());
    }

    public static Factory<Analytics> create(Provider<UpsightContext> upsightProvider, Provider<UpsightLifeCycleTracker> lifeCycleTrackerProvider, Provider<SessionManager> sessionManagerProvider, Provider<SchemaSelectorBuilder> schemaSelectorProvider, Provider<AssociationManager> associationManagerProvider, Provider<UpsightOptOutStatus> optOutStatusProvider, Provider<UpsightLocationTracker> locationTrackerProvider, Provider<UpsightUserAttributes> userAttributesProvider, Provider<UpsightGooglePlayHelper> googlePlayHelperProvider) {
        return new Analytics_Factory(upsightProvider, lifeCycleTrackerProvider, sessionManagerProvider, schemaSelectorProvider, associationManagerProvider, optOutStatusProvider, locationTrackerProvider, userAttributesProvider, googlePlayHelperProvider);
    }
}
