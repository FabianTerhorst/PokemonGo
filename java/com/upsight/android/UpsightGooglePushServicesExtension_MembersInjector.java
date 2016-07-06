package com.upsight.android;

import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightGooglePushServicesExtension_MembersInjector implements MembersInjector<UpsightGooglePushServicesExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightGooglePushServicesExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<UpsightGooglePushServicesApi> mUpsightPushProvider;
    private final MembersInjector<UpsightExtension<UpsightGooglePushServicesComponent, UpsightGooglePushServicesApi>> supertypeInjector;

    public UpsightGooglePushServicesExtension_MembersInjector(MembersInjector<UpsightExtension<UpsightGooglePushServicesComponent, UpsightGooglePushServicesApi>> supertypeInjector, Provider<UpsightGooglePushServicesApi> mUpsightPushProvider) {
        if ($assertionsDisabled || supertypeInjector != null) {
            this.supertypeInjector = supertypeInjector;
            if ($assertionsDisabled || mUpsightPushProvider != null) {
                this.mUpsightPushProvider = mUpsightPushProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public void injectMembers(UpsightGooglePushServicesExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        this.supertypeInjector.injectMembers(instance);
        instance.mUpsightPush = (UpsightGooglePushServicesApi) this.mUpsightPushProvider.get();
    }

    public static MembersInjector<UpsightGooglePushServicesExtension> create(MembersInjector<UpsightExtension<UpsightGooglePushServicesComponent, UpsightGooglePushServicesApi>> supertypeInjector, Provider<UpsightGooglePushServicesApi> mUpsightPushProvider) {
        return new UpsightGooglePushServicesExtension_MembersInjector(supertypeInjector, mUpsightPushProvider);
    }
}
