package com.upsight.android.googleadvertisingid.internal;

import com.upsight.android.UpsightGoogleAdvertisingIdExtension;
import com.upsight.android.UpsightGoogleAdvertisingIdExtension_MembersInjector;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerGoogleAdvertisingProviderComponent implements GoogleAdvertisingProviderComponent {
    static final /* synthetic */ boolean $assertionsDisabled = (!DaggerGoogleAdvertisingProviderComponent.class.desiredAssertionStatus());
    private Provider<GooglePlayAdvertisingProvider> provideGooglePlayAdvertisingProvider;
    private MembersInjector<UpsightGoogleAdvertisingIdExtension> upsightGoogleAdvertisingIdExtensionMembersInjector;

    public static final class Builder {
        private GoogleAdvertisingProviderModule googleAdvertisingProviderModule;

        private Builder() {
        }

        public GoogleAdvertisingProviderComponent build() {
            if (this.googleAdvertisingProviderModule != null) {
                return new DaggerGoogleAdvertisingProviderComponent();
            }
            throw new IllegalStateException(GoogleAdvertisingProviderModule.class.getCanonicalName() + " must be set");
        }

        public Builder googleAdvertisingProviderModule(GoogleAdvertisingProviderModule googleAdvertisingProviderModule) {
            this.googleAdvertisingProviderModule = (GoogleAdvertisingProviderModule) Preconditions.checkNotNull(googleAdvertisingProviderModule);
            return this;
        }
    }

    private DaggerGoogleAdvertisingProviderComponent(Builder builder) {
        if ($assertionsDisabled || builder != null) {
            initialize(builder);
            return;
        }
        throw new AssertionError();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void initialize(Builder builder) {
        this.provideGooglePlayAdvertisingProvider = DoubleCheck.provider(GoogleAdvertisingProviderModule_ProvideGooglePlayAdvertisingProviderFactory.create(builder.googleAdvertisingProviderModule));
        this.upsightGoogleAdvertisingIdExtensionMembersInjector = UpsightGoogleAdvertisingIdExtension_MembersInjector.create(this.provideGooglePlayAdvertisingProvider);
    }

    public void inject(UpsightGoogleAdvertisingIdExtension arg0) {
        this.upsightGoogleAdvertisingIdExtensionMembersInjector.injectMembers(arg0);
    }
}
