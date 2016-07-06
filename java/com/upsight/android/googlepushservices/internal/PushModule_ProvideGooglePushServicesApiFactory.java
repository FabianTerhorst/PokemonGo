package com.upsight.android.googlepushservices.internal;

import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PushModule_ProvideGooglePushServicesApiFactory implements Factory<UpsightGooglePushServicesApi> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PushModule_ProvideGooglePushServicesApiFactory.class.desiredAssertionStatus());
    private final Provider<GooglePushServices> googlePushServicesProvider;
    private final PushModule module;

    public PushModule_ProvideGooglePushServicesApiFactory(PushModule module, Provider<GooglePushServices> googlePushServicesProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || googlePushServicesProvider != null) {
                this.googlePushServicesProvider = googlePushServicesProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightGooglePushServicesApi get() {
        UpsightGooglePushServicesApi provided = this.module.provideGooglePushServicesApi((GooglePushServices) this.googlePushServicesProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightGooglePushServicesApi> create(PushModule module, Provider<GooglePushServices> googlePushServicesProvider) {
        return new PushModule_ProvideGooglePushServicesApiFactory(module, googlePushServicesProvider);
    }
}
