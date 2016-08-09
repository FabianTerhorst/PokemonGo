package com.upsight.android.googleadvertisingid.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class GoogleAdvertisingProviderModule_ProvideUpsightContextFactory implements Factory<UpsightContext> {
    static final /* synthetic */ boolean $assertionsDisabled = (!GoogleAdvertisingProviderModule_ProvideUpsightContextFactory.class.desiredAssertionStatus());
    private final GoogleAdvertisingProviderModule module;

    public GoogleAdvertisingProviderModule_ProvideUpsightContextFactory(GoogleAdvertisingProviderModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public UpsightContext get() {
        return (UpsightContext) Preconditions.checkNotNull(this.module.provideUpsightContext(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightContext> create(GoogleAdvertisingProviderModule module) {
        return new GoogleAdvertisingProviderModule_ProvideUpsightContextFactory(module);
    }
}
