package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class PushModule_ProvideUpsightContextFactory implements Factory<UpsightContext> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PushModule_ProvideUpsightContextFactory.class.desiredAssertionStatus());
    private final PushModule module;

    public PushModule_ProvideUpsightContextFactory(PushModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public UpsightContext get() {
        return (UpsightContext) Preconditions.checkNotNull(this.module.provideUpsightContext(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightContext> create(PushModule module) {
        return new PushModule_ProvideUpsightContextFactory(module);
    }
}
