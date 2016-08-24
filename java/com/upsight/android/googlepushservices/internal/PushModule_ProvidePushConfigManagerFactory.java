package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PushModule_ProvidePushConfigManagerFactory implements Factory<PushConfigManager> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PushModule_ProvidePushConfigManagerFactory.class.desiredAssertionStatus());
    private final PushModule module;
    private final Provider<UpsightContext> upsightProvider;

    public PushModule_ProvidePushConfigManagerFactory(PushModule module, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public PushConfigManager get() {
        return (PushConfigManager) Preconditions.checkNotNull(this.module.providePushConfigManager((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<PushConfigManager> create(PushModule module, Provider<UpsightContext> upsightProvider) {
        return new PushModule_ProvidePushConfigManagerFactory(module, upsightProvider);
    }
}
