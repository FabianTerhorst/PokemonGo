package com.upsight.android.internal;

import com.upsight.android.internal.logger.LogWriter;
import dagger.internal.Factory;

public final class ContextModule_ProvideLogWriterFactory implements Factory<LogWriter> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContextModule_ProvideLogWriterFactory.class.desiredAssertionStatus());
    private final ContextModule module;

    public ContextModule_ProvideLogWriterFactory(ContextModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public LogWriter get() {
        LogWriter provided = this.module.provideLogWriter();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<LogWriter> create(ContextModule module) {
        return new ContextModule_ProvideLogWriterFactory(module);
    }
}
