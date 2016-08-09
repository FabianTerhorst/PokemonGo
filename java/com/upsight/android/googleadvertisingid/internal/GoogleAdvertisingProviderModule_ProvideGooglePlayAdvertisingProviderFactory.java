package com.upsight.android.googleadvertisingid.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class GoogleAdvertisingProviderModule_ProvideGooglePlayAdvertisingProviderFactory implements Factory<GooglePlayAdvertisingProvider> {
    static final /* synthetic */ boolean $assertionsDisabled = (!GoogleAdvertisingProviderModule_ProvideGooglePlayAdvertisingProviderFactory.class.desiredAssertionStatus());
    private final GoogleAdvertisingProviderModule module;

    public GoogleAdvertisingProviderModule_ProvideGooglePlayAdvertisingProviderFactory(GoogleAdvertisingProviderModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public GooglePlayAdvertisingProvider get() {
        return (GooglePlayAdvertisingProvider) Preconditions.checkNotNull(this.module.provideGooglePlayAdvertisingProvider(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<GooglePlayAdvertisingProvider> create(GoogleAdvertisingProviderModule module) {
        return new GoogleAdvertisingProviderModule_ProvideGooglePlayAdvertisingProviderFactory(module);
    }
}
