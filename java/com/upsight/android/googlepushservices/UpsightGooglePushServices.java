package com.upsight.android.googlepushservices;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.UpsightGooglePushServicesExtension;

public abstract class UpsightGooglePushServices {

    public interface OnRegisterListener {
        void onFailure(UpsightException upsightException);

        void onSuccess(String str);
    }

    public interface OnUnregisterListener {
        void onFailure(UpsightException upsightException);

        void onSuccess();
    }

    public static void register(UpsightContext upsight, OnRegisterListener listener) {
        UpsightGooglePushServicesExtension extension = (UpsightGooglePushServicesExtension) upsight.getUpsightExtension(UpsightGooglePushServicesExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().register(listener);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.googlepushservices must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static void unregister(UpsightContext upsight, OnUnregisterListener listener) {
        UpsightGooglePushServicesExtension extension = (UpsightGooglePushServicesExtension) upsight.getUpsightExtension(UpsightGooglePushServicesExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().unregister(listener);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.googlepushservices must be registered in your Android Manifest", new Object[0]);
        }
    }
}
