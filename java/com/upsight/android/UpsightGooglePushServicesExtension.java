package com.upsight.android;

import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnRegisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import com.upsight.android.googlepushservices.internal.DaggerGooglePushServicesComponent;
import com.upsight.android.googlepushservices.internal.PushModule;
import javax.inject.Inject;

public class UpsightGooglePushServicesExtension extends UpsightExtension<UpsightGooglePushServicesComponent, UpsightGooglePushServicesApi> {
    public static final String EXTENSION_NAME = "com.upsight.extension.googlepushservices";
    @Inject
    UpsightGooglePushServicesApi mUpsightPush;

    UpsightGooglePushServicesExtension() {
    }

    protected UpsightGooglePushServicesComponent onResolve(UpsightContext upsight) {
        return DaggerGooglePushServicesComponent.builder().pushModule(new PushModule(upsight)).build();
    }

    public UpsightGooglePushServicesApi getApi() {
        return this.mUpsightPush;
    }

    protected void onPostCreate(UpsightContext upsight) {
        this.mUpsightPush.register(new OnRegisterListener() {
            public void onSuccess(String registrationId) {
            }

            public void onFailure(UpsightException e) {
            }
        });
    }
}
