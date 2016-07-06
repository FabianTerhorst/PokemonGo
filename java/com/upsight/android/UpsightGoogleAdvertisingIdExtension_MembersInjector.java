package com.upsight.android;

import com.upsight.android.googleadvertisingid.UpsightGoogleAdvertisingProviderComponent;
import com.upsight.android.googleadvertisingid.internal.GooglePlayAdvertisingProvider;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightGoogleAdvertisingIdExtension_MembersInjector implements MembersInjector<UpsightGoogleAdvertisingIdExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightGoogleAdvertisingIdExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<GooglePlayAdvertisingProvider> mAdvertisingIdProvider;
    private final MembersInjector<UpsightExtension<UpsightGoogleAdvertisingProviderComponent, Void>> supertypeInjector;

    public UpsightGoogleAdvertisingIdExtension_MembersInjector(MembersInjector<UpsightExtension<UpsightGoogleAdvertisingProviderComponent, Void>> supertypeInjector, Provider<GooglePlayAdvertisingProvider> mAdvertisingIdProvider) {
        if ($assertionsDisabled || supertypeInjector != null) {
            this.supertypeInjector = supertypeInjector;
            if ($assertionsDisabled || mAdvertisingIdProvider != null) {
                this.mAdvertisingIdProvider = mAdvertisingIdProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public void injectMembers(UpsightGoogleAdvertisingIdExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        this.supertypeInjector.injectMembers(instance);
        instance.mAdvertisingIdProvider = (GooglePlayAdvertisingProvider) this.mAdvertisingIdProvider.get();
    }

    public static MembersInjector<UpsightGoogleAdvertisingIdExtension> create(MembersInjector<UpsightExtension<UpsightGoogleAdvertisingProviderComponent, Void>> supertypeInjector, Provider<GooglePlayAdvertisingProvider> mAdvertisingIdProvider) {
        return new UpsightGoogleAdvertisingIdExtension_MembersInjector(supertypeInjector, mAdvertisingIdProvider);
    }
}
