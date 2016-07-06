package com.upsight.android.googlepushservices.internal;

import android.app.IntentService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class PushIntentService_MembersInjector implements MembersInjector<PushIntentService> {
    static final /* synthetic */ boolean $assertionsDisabled = (!PushIntentService_MembersInjector.class.desiredAssertionStatus());
    private final Provider<GoogleCloudMessaging> mGcmProvider;
    private final MembersInjector<IntentService> supertypeInjector;

    public PushIntentService_MembersInjector(MembersInjector<IntentService> supertypeInjector, Provider<GoogleCloudMessaging> mGcmProvider) {
        if ($assertionsDisabled || supertypeInjector != null) {
            this.supertypeInjector = supertypeInjector;
            if ($assertionsDisabled || mGcmProvider != null) {
                this.mGcmProvider = mGcmProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public void injectMembers(PushIntentService instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        this.supertypeInjector.injectMembers(instance);
        instance.mGcm = (GoogleCloudMessaging) this.mGcmProvider.get();
    }

    public static MembersInjector<PushIntentService> create(MembersInjector<IntentService> supertypeInjector, Provider<GoogleCloudMessaging> mGcmProvider) {
        return new PushIntentService_MembersInjector(supertypeInjector, mGcmProvider);
    }
}
