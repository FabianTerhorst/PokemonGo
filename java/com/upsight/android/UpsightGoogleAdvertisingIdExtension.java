package com.upsight.android;

import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.googleadvertisingid.UpsightGoogleAdvertisingProviderComponent;
import com.upsight.android.googleadvertisingid.internal.DaggerGoogleAdvertisingProviderComponent;
import com.upsight.android.googleadvertisingid.internal.GoogleAdvertisingProviderModule;
import com.upsight.android.googleadvertisingid.internal.GooglePlayAdvertisingProvider;
import javax.inject.Inject;

public final class UpsightGoogleAdvertisingIdExtension extends UpsightExtension<UpsightGoogleAdvertisingProviderComponent, Void> {
    public static final String EXTENSION_NAME = "com.upsight.extension.googleadvertisingid";
    @Inject
    GooglePlayAdvertisingProvider mAdvertisingIdProvider;

    UpsightGoogleAdvertisingIdExtension() {
    }

    protected UpsightGoogleAdvertisingProviderComponent onResolve(UpsightContext upsight) {
        return DaggerGoogleAdvertisingProviderComponent.builder().googleAdvertisingProviderModule(new GoogleAdvertisingProviderModule(upsight)).build();
    }

    protected void onCreate(UpsightContext upsight) {
        UpsightDataProvider.register(upsight, this.mAdvertisingIdProvider);
    }
}
