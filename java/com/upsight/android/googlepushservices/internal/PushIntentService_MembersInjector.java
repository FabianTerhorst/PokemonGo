package com.upsight.android.googlepushservices.internal;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class PushIntentService_MembersInjector implements MembersInjector<PushIntentService> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PushIntentService_MembersInjector.class.desiredAssertionStatus());
    private final Provider<GoogleCloudMessaging> mGcmProvider;
    private final Provider<UpsightContext> mUpsightProvider;

    public PushIntentService_MembersInjector(Provider<GoogleCloudMessaging> mGcmProvider, Provider<UpsightContext> mUpsightProvider) {
        if ($assertionsDisabled || mGcmProvider != null) {
            this.mGcmProvider = mGcmProvider;
            if ($assertionsDisabled || mUpsightProvider != null) {
                this.mUpsightProvider = mUpsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<PushIntentService> create(Provider<GoogleCloudMessaging> mGcmProvider, Provider<UpsightContext> mUpsightProvider) {
        return new PushIntentService_MembersInjector(mGcmProvider, mUpsightProvider);
    }

    public void injectMembers(PushIntentService instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mGcm = (GoogleCloudMessaging) this.mGcmProvider.get();
        instance.mUpsight = (UpsightContext) this.mUpsightProvider.get();
    }

    public static void injectMGcm(PushIntentService instance, Provider<GoogleCloudMessaging> mGcmProvider) {
        instance.mGcm = (GoogleCloudMessaging) mGcmProvider.get();
    }

    public static void injectMUpsight(PushIntentService instance, Provider<UpsightContext> mUpsightProvider) {
        instance.mUpsight = (UpsightContext) mUpsightProvider.get();
    }
}
