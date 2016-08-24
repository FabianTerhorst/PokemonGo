package com.upsight.android.googlepushservices.internal;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightGooglePushServicesExtension;
import com.upsight.android.UpsightGooglePushServicesExtension_MembersInjector;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerGooglePushServicesComponent implements GooglePushServicesComponent {
    static final /* synthetic */ boolean $assertionsDisabled = (!DaggerGooglePushServicesComponent.class.desiredAssertionStatus());
    private Provider<GooglePushServices> googlePushServicesProvider;
    private Provider<GoogleCloudMessaging> provideGoogleCloudMessagingProvider;
    private Provider<UpsightGooglePushServicesApi> provideGooglePushServicesApiProvider;
    private Provider<PushConfigManager> providePushConfigManagerProvider;
    private Provider<SessionManager> provideSessionManagerProvider;
    private Provider<UpsightContext> provideUpsightContextProvider;
    private MembersInjector<PushClickIntentService> pushClickIntentServiceMembersInjector;
    private MembersInjector<PushIntentService> pushIntentServiceMembersInjector;
    private MembersInjector<UpsightGooglePushServicesExtension> upsightGooglePushServicesExtensionMembersInjector;

    public static final class Builder {
        private GoogleCloudMessagingModule googleCloudMessagingModule;
        private PushModule pushModule;

        private Builder() {
        }

        public GooglePushServicesComponent build() {
            if (this.pushModule == null) {
                throw new IllegalStateException(PushModule.class.getCanonicalName() + " must be set");
            }
            if (this.googleCloudMessagingModule == null) {
                this.googleCloudMessagingModule = new GoogleCloudMessagingModule();
            }
            return new DaggerGooglePushServicesComponent();
        }

        @Deprecated
        public Builder googlePushServicesModule(GooglePushServicesModule googlePushServicesModule) {
            Preconditions.checkNotNull(googlePushServicesModule);
            return this;
        }

        public Builder pushModule(PushModule pushModule) {
            this.pushModule = (PushModule) Preconditions.checkNotNull(pushModule);
            return this;
        }

        public Builder googleCloudMessagingModule(GoogleCloudMessagingModule googleCloudMessagingModule) {
            this.googleCloudMessagingModule = (GoogleCloudMessagingModule) Preconditions.checkNotNull(googleCloudMessagingModule);
            return this;
        }
    }

    private DaggerGooglePushServicesComponent(Builder builder) {
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
        this.provideUpsightContextProvider = DoubleCheck.provider(PushModule_ProvideUpsightContextFactory.create(builder.pushModule));
        this.providePushConfigManagerProvider = DoubleCheck.provider(PushModule_ProvidePushConfigManagerFactory.create(builder.pushModule, this.provideUpsightContextProvider));
        this.googlePushServicesProvider = DoubleCheck.provider(GooglePushServices_Factory.create(this.provideUpsightContextProvider, this.providePushConfigManagerProvider));
        this.provideGooglePushServicesApiProvider = DoubleCheck.provider(PushModule_ProvideGooglePushServicesApiFactory.create(builder.pushModule, this.googlePushServicesProvider));
        this.upsightGooglePushServicesExtensionMembersInjector = UpsightGooglePushServicesExtension_MembersInjector.create(this.provideGooglePushServicesApiProvider, this.providePushConfigManagerProvider);
        this.provideGoogleCloudMessagingProvider = DoubleCheck.provider(GoogleCloudMessagingModule_ProvideGoogleCloudMessagingFactory.create(builder.googleCloudMessagingModule, this.provideUpsightContextProvider));
        this.pushIntentServiceMembersInjector = PushIntentService_MembersInjector.create(this.provideGoogleCloudMessagingProvider, this.provideUpsightContextProvider);
        this.provideSessionManagerProvider = DoubleCheck.provider(PushModule_ProvideSessionManagerFactory.create(builder.pushModule, this.provideUpsightContextProvider));
        this.pushClickIntentServiceMembersInjector = PushClickIntentService_MembersInjector.create(this.provideSessionManagerProvider);
    }

    public void inject(UpsightGooglePushServicesExtension arg0) {
        this.upsightGooglePushServicesExtensionMembersInjector.injectMembers(arg0);
    }

    public void inject(PushIntentService pushIntentService) {
        this.pushIntentServiceMembersInjector.injectMembers(pushIntentService);
    }

    public void inject(PushClickIntentService pushClickIntentService) {
        this.pushClickIntentServiceMembersInjector.injectMembers(pushClickIntentService);
    }
}
