package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GooglePushServices_Factory implements Factory<GooglePushServices> {
    static final /* synthetic */ boolean $assertionsDisabled = (!GooglePushServices_Factory.class.desiredAssertionStatus());
    private final Provider<PushConfigManager> pushConfigManagerProvider;
    private final Provider<UpsightContext> upsightProvider;

    public GooglePushServices_Factory(Provider<UpsightContext> upsightProvider, Provider<PushConfigManager> pushConfigManagerProvider) {
        if ($assertionsDisabled || upsightProvider != null) {
            this.upsightProvider = upsightProvider;
            if ($assertionsDisabled || pushConfigManagerProvider != null) {
                this.pushConfigManagerProvider = pushConfigManagerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public GooglePushServices get() {
        return new GooglePushServices((UpsightContext) this.upsightProvider.get(), (PushConfigManager) this.pushConfigManagerProvider.get());
    }

    public static Factory<GooglePushServices> create(Provider<UpsightContext> upsightProvider, Provider<PushConfigManager> pushConfigManagerProvider) {
        return new GooglePushServices_Factory(upsightProvider, pushConfigManagerProvider);
    }
}
