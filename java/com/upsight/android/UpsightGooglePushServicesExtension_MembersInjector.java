package com.upsight.android;

import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightGooglePushServicesExtension_MembersInjector implements MembersInjector<UpsightGooglePushServicesExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightGooglePushServicesExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<UpsightGooglePushServicesApi> mUpsightPushProvider;

    public UpsightGooglePushServicesExtension_MembersInjector(Provider<UpsightGooglePushServicesApi> mUpsightPushProvider) {
        if ($assertionsDisabled || mUpsightPushProvider != null) {
            this.mUpsightPushProvider = mUpsightPushProvider;
            return;
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightGooglePushServicesExtension> create(Provider<UpsightGooglePushServicesApi> mUpsightPushProvider) {
        return new UpsightGooglePushServicesExtension_MembersInjector(mUpsightPushProvider);
    }

    public void injectMembers(UpsightGooglePushServicesExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mUpsightPush = (UpsightGooglePushServicesApi) this.mUpsightPushProvider.get();
    }

    public static void injectMUpsightPush(UpsightGooglePushServicesExtension instance, Provider<UpsightGooglePushServicesApi> mUpsightPushProvider) {
        instance.mUpsightPush = (UpsightGooglePushServicesApi) mUpsightPushProvider.get();
    }
}
