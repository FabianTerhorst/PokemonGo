package com.upsight.android.googlepushservices.internal;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class GoogleCloudMessagingModule_ProvideGoogleCloudMessagingFactory implements Factory<GoogleCloudMessaging> {
    static final /* synthetic */ boolean $assertionsDisabled = (!GoogleCloudMessagingModule_ProvideGoogleCloudMessagingFactory.class.desiredAssertionStatus());
    private final GoogleCloudMessagingModule module;
    private final Provider<UpsightContext> upsightProvider;

    public GoogleCloudMessagingModule_ProvideGoogleCloudMessagingFactory(GoogleCloudMessagingModule module, Provider<UpsightContext> upsightProvider) {
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

    public GoogleCloudMessaging get() {
        return (GoogleCloudMessaging) Preconditions.checkNotNull(this.module.provideGoogleCloudMessaging((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<GoogleCloudMessaging> create(GoogleCloudMessagingModule module, Provider<UpsightContext> upsightProvider) {
        return new GoogleCloudMessagingModule_ProvideGoogleCloudMessagingFactory(module, upsightProvider);
    }
}
