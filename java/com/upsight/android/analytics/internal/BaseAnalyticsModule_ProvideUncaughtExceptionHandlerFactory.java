package com.upsight.android.analytics.internal;

import com.upsight.android.internal.util.Opt;
import dagger.internal.Factory;
import java.lang.Thread.UncaughtExceptionHandler;

public final class BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory implements Factory<Opt<UncaughtExceptionHandler>> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory.class.desiredAssertionStatus());
    private final BaseAnalyticsModule module;

    public BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory(BaseAnalyticsModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Opt<UncaughtExceptionHandler> get() {
        Opt<UncaughtExceptionHandler> provided = this.module.provideUncaughtExceptionHandler();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Opt<UncaughtExceptionHandler>> create(BaseAnalyticsModule module) {
        return new BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory(module);
    }
}
