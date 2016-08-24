package com.upsight.android;

import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import com.upsight.android.googlepushservices.internal.PushConfigManager;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightGooglePushServicesExtension_MembersInjector implements MembersInjector<UpsightGooglePushServicesExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightGooglePushServicesExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<PushConfigManager> mPushConfigManagerProvider;
    private final Provider<UpsightGooglePushServicesApi> mUpsightPushProvider;

    public UpsightGooglePushServicesExtension_MembersInjector(Provider<UpsightGooglePushServicesApi> mUpsightPushProvider, Provider<PushConfigManager> mPushConfigManagerProvider) {
        if ($assertionsDisabled || mUpsightPushProvider != null) {
            this.mUpsightPushProvider = mUpsightPushProvider;
            if ($assertionsDisabled || mPushConfigManagerProvider != null) {
                this.mPushConfigManagerProvider = mPushConfigManagerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightGooglePushServicesExtension> create(Provider<UpsightGooglePushServicesApi> mUpsightPushProvider, Provider<PushConfigManager> mPushConfigManagerProvider) {
        return new UpsightGooglePushServicesExtension_MembersInjector(mUpsightPushProvider, mPushConfigManagerProvider);
    }

    public void injectMembers(UpsightGooglePushServicesExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mUpsightPush = (UpsightGooglePushServicesApi) this.mUpsightPushProvider.get();
        instance.mPushConfigManager = (PushConfigManager) this.mPushConfigManagerProvider.get();
    }

    public static void injectMUpsightPush(UpsightGooglePushServicesExtension instance, Provider<UpsightGooglePushServicesApi> mUpsightPushProvider) {
        instance.mUpsightPush = (UpsightGooglePushServicesApi) mUpsightPushProvider.get();
    }

    public static void injectMPushConfigManager(UpsightGooglePushServicesExtension instance, Provider<PushConfigManager> mPushConfigManagerProvider) {
        instance.mPushConfigManager = (PushConfigManager) mPushConfigManagerProvider.get();
    }
}
