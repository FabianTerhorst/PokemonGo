package com.upsight.android.internal;

import android.content.Context;
import com.upsight.android.UpsightContext;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UpsightContextModule_ProvideUpsightContextFactory implements Factory<UpsightContext> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightContextModule_ProvideUpsightContextFactory.class.desiredAssertionStatus());
    private final Provider<String> appTokenProvider;
    private final Provider<Context> baseContextProvider;
    private final Provider<UpsightDataStore> dataStoreProvider;
    private final Provider<UpsightLogger> loggerProvider;
    private final UpsightContextModule module;
    private final Provider<String> publicKeyProvider;
    private final Provider<String> sdkPluginProvider;

    public UpsightContextModule_ProvideUpsightContextFactory(UpsightContextModule module, Provider<Context> baseContextProvider, Provider<String> sdkPluginProvider, Provider<String> appTokenProvider, Provider<String> publicKeyProvider, Provider<UpsightDataStore> dataStoreProvider, Provider<UpsightLogger> loggerProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || baseContextProvider != null) {
                this.baseContextProvider = baseContextProvider;
                if ($assertionsDisabled || sdkPluginProvider != null) {
                    this.sdkPluginProvider = sdkPluginProvider;
                    if ($assertionsDisabled || appTokenProvider != null) {
                        this.appTokenProvider = appTokenProvider;
                        if ($assertionsDisabled || publicKeyProvider != null) {
                            this.publicKeyProvider = publicKeyProvider;
                            if ($assertionsDisabled || dataStoreProvider != null) {
                                this.dataStoreProvider = dataStoreProvider;
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
                    throw new AssertionError();
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightContext get() {
        return (UpsightContext) Preconditions.checkNotNull(this.module.provideUpsightContext((Context) this.baseContextProvider.get(), (String) this.sdkPluginProvider.get(), (String) this.appTokenProvider.get(), (String) this.publicKeyProvider.get(), (UpsightDataStore) this.dataStoreProvider.get(), (UpsightLogger) this.loggerProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightContext> create(UpsightContextModule module, Provider<Context> baseContextProvider, Provider<String> sdkPluginProvider, Provider<String> appTokenProvider, Provider<String> publicKeyProvider, Provider<UpsightDataStore> dataStoreProvider, Provider<UpsightLogger> loggerProvider) {
        return new UpsightContextModule_ProvideUpsightContextFactory(module, baseContextProvider, sdkPluginProvider, appTokenProvider, publicKeyProvider, dataStoreProvider, loggerProvider);
    }
}
