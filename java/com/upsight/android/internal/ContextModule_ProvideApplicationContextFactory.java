package com.upsight.android.internal;

import android.content.Context;
import dagger.internal.Factory;

public final class ContextModule_ProvideApplicationContextFactory implements Factory<Context> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContextModule_ProvideApplicationContextFactory.class.desiredAssertionStatus());
    private final ContextModule module;

    public ContextModule_ProvideApplicationContextFactory(ContextModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Context get() {
        Context provided = this.module.provideApplicationContext();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Context> create(ContextModule module) {
        return new ContextModule_ProvideApplicationContextFactory(module);
    }
}
