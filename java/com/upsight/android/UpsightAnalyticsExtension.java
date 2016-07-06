package com.upsight.android;

import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import com.upsight.android.analytics.UpsightAnalyticsApi;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.event.install.UpsightInstallEvent;
import com.upsight.android.analytics.internal.BaseAnalyticsModule;
import com.upsight.android.analytics.internal.DaggerAnalyticsComponent;
import com.upsight.android.analytics.internal.association.AssociationManager;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.Opt;
import com.upsight.android.internal.util.PreferencesHelper;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.inject.Inject;
import javax.inject.Named;

public class UpsightAnalyticsExtension extends UpsightExtension<UpsightAnalyticsComponent, UpsightAnalyticsApi> {
    public static final String EXTENSION_NAME = "com.upsight.extension.analytics";
    @Inject
    UpsightAnalyticsApi mAnalytics;
    @Inject
    AssociationManager mAssociationManager;
    @Inject
    Clock mClock;
    @Named("optUncaughtExceptionHandler")
    @Inject
    Opt<UncaughtExceptionHandler> mUncaughtExceptionHandler;
    @Inject
    ActivityLifecycleCallbacks mUpsightLifeCycleCallbacks;

    protected UpsightAnalyticsExtension() {
    }

    protected UpsightAnalyticsComponent onResolve(UpsightContext upsight) {
        return DaggerAnalyticsComponent.builder().baseAnalyticsModule(new BaseAnalyticsModule(upsight)).build();
    }

    protected void onCreate(UpsightContext upsight) {
        if (this.mUncaughtExceptionHandler.isPresent()) {
            Thread.setDefaultUncaughtExceptionHandler((UncaughtExceptionHandler) this.mUncaughtExceptionHandler.get());
        }
        ((Application) upsight.getApplicationContext()).registerActivityLifecycleCallbacks(this.mUpsightLifeCycleCallbacks);
        this.mAssociationManager.launch();
    }

    protected void onPostCreate(UpsightContext upsight) {
        if (!PreferencesHelper.contains(upsight, PreferencesHelper.INSTALL_TIMESTAMP_NAME)) {
            PreferencesHelper.putLong(upsight, PreferencesHelper.INSTALL_TIMESTAMP_NAME, this.mClock.currentTimeSeconds());
            UpsightInstallEvent.createBuilder().record(upsight);
        }
    }

    public UpsightAnalyticsApi getApi() {
        return this.mAnalytics;
    }
}
