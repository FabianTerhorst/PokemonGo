package com.upsight.android;

import android.app.Application.ActivityLifecycleCallbacks;
import com.upsight.android.analytics.UpsightAnalyticsApi;
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

    public UpsightAnalyticsExtension_MembersInjector(Provider<Opt<UncaughtExceptionHandler>> mUncaughtExceptionHandlerProvider, Provider<UpsightAnalyticsApi> mAnalyticsProvider, Provider<Clock> mClockProvider, Provider<ActivityLifecycleCallbacks> mUpsightLifeCycleCallbacksProvider, Provider<AssociationManager> mAssociationManagerProvider) {
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

    public static MembersInjector<UpsightAnalyticsExtension> create(Provider<Opt<UncaughtExceptionHandler>> mUncaughtExceptionHandlerProvider, Provider<UpsightAnalyticsApi> mAnalyticsProvider, Provider<Clock> mClockProvider, Provider<ActivityLifecycleCallbacks> mUpsightLifeCycleCallbacksProvider, Provider<AssociationManager> mAssociationManagerProvider) {
        return new UpsightAnalyticsExtension_MembersInjector(mUncaughtExceptionHandlerProvider, mAnalyticsProvider, mClockProvider, mUpsightLifeCycleCallbacksProvider, mAssociationManagerProvider);
    }

    public void injectMembers(UpsightAnalyticsExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mUncaughtExceptionHandler = (Opt) this.mUncaughtExceptionHandlerProvider.get();
        instance.mAnalytics = (UpsightAnalyticsApi) this.mAnalyticsProvider.get();
        instance.mClock = (Clock) this.mClockProvider.get();
        instance.mUpsightLifeCycleCallbacks = (ActivityLifecycleCallbacks) this.mUpsightLifeCycleCallbacksProvider.get();
        instance.mAssociationManager = (AssociationManager) this.mAssociationManagerProvider.get();
    }

    public static void injectMUncaughtExceptionHandler(UpsightAnalyticsExtension instance, Provider<Opt<UncaughtExceptionHandler>> mUncaughtExceptionHandlerProvider) {
        instance.mUncaughtExceptionHandler = (Opt) mUncaughtExceptionHandlerProvider.get();
    }

    public static void injectMAnalytics(UpsightAnalyticsExtension instance, Provider<UpsightAnalyticsApi> mAnalyticsProvider) {
        instance.mAnalytics = (UpsightAnalyticsApi) mAnalyticsProvider.get();
    }

    public static void injectMClock(UpsightAnalyticsExtension instance, Provider<Clock> mClockProvider) {
        instance.mClock = (Clock) mClockProvider.get();
    }

    public static void injectMUpsightLifeCycleCallbacks(UpsightAnalyticsExtension instance, Provider<ActivityLifecycleCallbacks> mUpsightLifeCycleCallbacksProvider) {
        instance.mUpsightLifeCycleCallbacks = (ActivityLifecycleCallbacks) mUpsightLifeCycleCallbacksProvider.get();
    }

    public static void injectMAssociationManager(UpsightAnalyticsExtension instance, Provider<AssociationManager> mAssociationManagerProvider) {
        instance.mAssociationManager = (AssociationManager) mAssociationManagerProvider.get();
    }
}
