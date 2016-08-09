package com.upsight.android.internal;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

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
        return (Context) Preconditions.checkNotNull(this.module.provideApplicationContext(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Context> create(ContextModule module) {
        return new ContextModule_ProvideApplicationContextFactory(module);
    }
}
