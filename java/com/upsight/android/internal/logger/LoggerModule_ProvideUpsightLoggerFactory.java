package com.upsight.android.internal.logger;

import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LoggerModule_ProvideUpsightLoggerFactory implements Factory<UpsightLogger> {
    static final /* synthetic */ boolean $assertionsDisabled = (!LoggerModule_ProvideUpsightLoggerFactory.class.desiredAssertionStatus());
    private final Provider<UpsightDataStore> dataStoreProvider;
    private final LoggerModule module;
    private final Provider<LogWriter> writerProvider;

    public LoggerModule_ProvideUpsightLoggerFactory(LoggerModule module, Provider<UpsightDataStore> dataStoreProvider, Provider<LogWriter> writerProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || dataStoreProvider != null) {
                this.dataStoreProvider = dataStoreProvider;
                if ($assertionsDisabled || writerProvider != null) {
                    this.writerProvider = writerProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightLogger get() {
        UpsightLogger provided = this.module.provideUpsightLogger((UpsightDataStore) this.dataStoreProvider.get(), (LogWriter) this.writerProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightLogger> create(LoggerModule module, Provider<UpsightDataStore> dataStoreProvider, Provider<LogWriter> writerProvider) {
        return new LoggerModule_ProvideUpsightLoggerFactory(module, dataStoreProvider, writerProvider);
    }
}
