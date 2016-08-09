package com.upsight.android;

import com.upsight.android.googleadvertisingid.internal.GooglePlayAdvertisingProvider;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightGoogleAdvertisingIdExtension_MembersInjector implements MembersInjector<UpsightGoogleAdvertisingIdExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightGoogleAdvertisingIdExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<GooglePlayAdvertisingProvider> mAdvertisingIdProvider;

    public UpsightGoogleAdvertisingIdExtension_MembersInjector(Provider<GooglePlayAdvertisingProvider> mAdvertisingIdProvider) {
        if ($assertionsDisabled || mAdvertisingIdProvider != null) {
            this.mAdvertisingIdProvider = mAdvertisingIdProvider;
            return;
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightGoogleAdvertisingIdExtension> create(Provider<GooglePlayAdvertisingProvider> mAdvertisingIdProvider) {
        return new UpsightGoogleAdvertisingIdExtension_MembersInjector(mAdvertisingIdProvider);
    }

    public void injectMembers(UpsightGoogleAdvertisingIdExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mAdvertisingIdProvider = (GooglePlayAdvertisingProvider) this.mAdvertisingIdProvider.get();
    }

    public static void injectMAdvertisingIdProvider(UpsightGoogleAdvertisingIdExtension instance, Provider<GooglePlayAdvertisingProvider> mAdvertisingIdProvider) {
        instance.mAdvertisingIdProvider = (GooglePlayAdvertisingProvider) mAdvertisingIdProvider.get();
    }
}
