package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.Opt;
import dagger.Module;
import dagger.Provides;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public final class BaseAnalyticsModule {
    public static final String OPT_UNCAUGHT_EXCEPTION_HANDLER = "optUncaughtExceptionHandler";
    private final UpsightContext mUpsight;

    public BaseAnalyticsModule(UpsightContext upsight) {
        this.mUpsight = upsight;
    }

    @Singleton
    @Provides
    public UpsightContext provideUpsightContext() {
        return this.mUpsight;
    }

    @Singleton
    @Provides
    public Clock provideClock() {
        return new Clock() {
            public long currentTimeSeconds() {
                return TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            }

            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
    }

    @Provides
    @Singleton
    @Named("optUncaughtExceptionHandler")
    public Opt<UncaughtExceptionHandler> provideUncaughtExceptionHandler() {
        return Opt.absent();
    }

    @Singleton
    @Provides
    public UpsightGooglePlayHelper provideGooglePlayHelper(UpsightContext upsight) {
        return new GooglePlayHelper(upsight, upsight.getCoreComponent().objectMapper());
    }
}
