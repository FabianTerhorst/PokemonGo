package com.upsight.android.internal;

import android.content.Context;
import com.upsight.android.logger.UpsightLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PropertiesModule_ProvideApplicationTokenFactory implements Factory<String> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PropertiesModule_ProvideApplicationTokenFactory.class.desiredAssertionStatus());
    private final Provider<Context> contextProvider;
    private final Provider<UpsightLogger> loggerProvider;
    private final PropertiesModule module;

    public PropertiesModule_ProvideApplicationTokenFactory(PropertiesModule module, Provider<Context> contextProvider, Provider<UpsightLogger> loggerProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || contextProvider != null) {
                this.contextProvider = contextProvider;
                if ($assertionsDisabled || loggerProvider != null) {
                    this.loggerProvider = loggerProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public String get() {
        return (String) Preconditions.checkNotNull(this.module.provideApplicationToken((Context) this.contextProvider.get(), (UpsightLogger) this.loggerProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<String> create(PropertiesModule module, Provider<Context> contextProvider, Provider<UpsightLogger> loggerProvider) {
        return new PropertiesModule_ProvideApplicationTokenFactory(module, contextProvider, loggerProvider);
    }
}
