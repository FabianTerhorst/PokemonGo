package com.upsight.android;

import android.app.Application.ActivityLifecycleCallbacks;
import com.upsight.android.analytics.UpsightAnalyticsApi;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.association.AssociationManager;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.Opt;
import dagger.MembersInjector;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.inject.Provider;

public final class UpsightAnalyticsExtension_MembersInjector implements MembersInjector<UpsightAnalyticsExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightAnalyticsExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<UpsightAnalyticsApi> mAnalyticsProvider;
    private final Provider<AssociationManager> mAssociationManagerProvider;
    private final Provider<Clock> mClockProvider;
    private final Provider<Opt<UncaughtExceptionHandler>> mUncaughtExceptionHandlerProvider;
    private final Provider<ActivityLifecycleCallbacks> mUpsightLifeCycleCallbacksProvider;
    private final MembersInjector<UpsightExtension<UpsightAnalyticsComponent, UpsightAnalyticsApi>> supertypeInjector;

    public UpsightAnalyticsExtension_MembersInjector(MembersInjector<UpsightExtension<UpsightAnalyticsComponent, UpsightAnalyticsApi>> supertypeInjector, Provider<Opt<UncaughtExceptionHandler>> mUncaughtExceptionHandlerProvider, Provider<UpsightAnalyticsApi> mAnalyticsProvider, Provider<Clock> mClockProvider, Provider<ActivityLifecycleCallbacks> mUpsightLifeCycleCallbacksProvider, Provider<AssociationManager> mAssociationManagerProvider) {
        if ($assertionsDisabled || supertypeInjector != null) {
            this.supertypeInjector = supertypeInjector;
            if ($assertionsDisabled || mUncaughtExceptionHandlerProvider != null) {
                this.mUncaughtExceptionHandlerProvider = mUncaughtExceptionHandlerProvider;
                if ($assertionsDisabled || mAnalyticsProvider != null) {
                    this.mAnalyticsProvider = mAnalyticsProvider;
                    if ($assertionsDisabled || mClockProvider != null) {
                        this.mClockProvider = mClockProvider;
                        if ($assertionsDisabled || mUpsightLifeCycleCallbacksProvider != null) {
                            this.mUpsightLifeCycleCallbacksProvider = mUpsightLifeCycleCallbacksProvider;
                            if ($assertionsDisabled || mAssociationManagerProvider != null) {
                                this.mAssociationManagerProvider = mAssociationManagerProvider;
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

    public void injectMembers(UpsightAnalyticsExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        this.supertypeInjector.injectMembers(instance);
        instance.mUncaughtExceptionHandler = (Opt) this.mUncaughtExceptionHandlerProvider.get();
        instance.mAnalytics = (UpsightAnalyticsApi) this.mAnalyticsProvider.get();
        instance.mClock = (Clock) this.mClockProvider.get();
        instance.mUpsightLifeCycleCallbacks = (ActivityLifecycleCallbacks) this.mUpsightLifeCycleCallbacksProvider.get();
        instance.mAssociationManager = (AssociationManager) this.mAssociationManagerProvider.get();
    }

    public static MembersInjector<UpsightAnalyticsExtension> create(MembersInjector<UpsightExtension<UpsightAnalyticsComponent, UpsightAnalyticsApi>> supertypeInjector, Provider<Opt<UncaughtExceptionHandler>> mUncaughtExceptionHandlerProvider, Provider<UpsightAnalyticsApi> mAnalyticsProvider, Provider<Clock> mClockProvider, Provider<ActivityLifecycleCallbacks> mUpsightLifeCycleCallbacksProvider, Provider<AssociationManager> mAssociationManagerProvider) {
        return new UpsightAnalyticsExtension_MembersInjector(supertypeInjector, mUncaughtExceptionHandlerProvider, mAnalyticsProvider, mClockProvider, mUpsightLifeCycleCallbacksProvider, mAssociationManagerProvider);
    }
}
