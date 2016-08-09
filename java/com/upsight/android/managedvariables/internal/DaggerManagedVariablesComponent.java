package com.upsight.android.managedvariables.internal;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightManagedVariablesExtension;
import com.upsight.android.UpsightManagedVariablesExtension_MembersInjector;
import com.upsight.android.managedvariables.UpsightManagedVariablesApi;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.internal.experience.UserExperienceModule;
import com.upsight.android.managedvariables.internal.experience.UserExperienceModule_ProvideUserExperienceFactory;
import com.upsight.android.managedvariables.internal.type.ManagedVariableManager;
import com.upsight.android.managedvariables.internal.type.UxmBlockProvider;
import com.upsight.android.managedvariables.internal.type.UxmContentFactory;
import com.upsight.android.managedvariables.internal.type.UxmModule;
import com.upsight.android.managedvariables.internal.type.UxmModule_ProvideManagedVariableManagerFactory;
import com.upsight.android.managedvariables.internal.type.UxmModule_ProvideUxmBlockProviderFactory;
import com.upsight.android.managedvariables.internal.type.UxmModule_ProvideUxmContentFactoryFactory;
import com.upsight.android.managedvariables.internal.type.UxmModule_ProvideUxmSchemaFactory;
import com.upsight.android.managedvariables.internal.type.UxmModule_ProvideUxmSchemaGsonFactory;
import com.upsight.android.managedvariables.internal.type.UxmModule_ProvideUxmSchemaJsonParserFactory;
import com.upsight.android.managedvariables.internal.type.UxmModule_ProvideUxmSchemaRawStringFactory;
import com.upsight.android.managedvariables.internal.type.UxmSchema;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import rx.Scheduler;

public final class DaggerManagedVariablesComponent implements ManagedVariablesComponent {
    static final /* synthetic */ boolean $assertionsDisabled = (!DaggerManagedVariablesComponent.class.desiredAssertionStatus());
    private Provider<Scheduler> provideMainSchedulerProvider;
    private Provider<ManagedVariableManager> provideManagedVariableManagerProvider;
    private Provider<UpsightManagedVariablesApi> provideManagedVariablesApiProvider;
    private Provider<UpsightContext> provideUpsightContextProvider;
    private Provider<UpsightUserExperience> provideUserExperienceProvider;
    private Provider<UxmBlockProvider> provideUxmBlockProvider;
    private Provider<UxmContentFactory> provideUxmContentFactoryProvider;
    private Provider<Gson> provideUxmSchemaGsonProvider;
    private Provider<JsonParser> provideUxmSchemaJsonParserProvider;
    private Provider<UxmSchema> provideUxmSchemaProvider;
    private Provider<String> provideUxmSchemaRawStringProvider;
    private Provider<Integer> provideUxmSchemaResourceProvider;
    private MembersInjector<UpsightManagedVariablesExtension> upsightManagedVariablesExtensionMembersInjector;

    public static final class Builder {
        private BaseManagedVariablesModule baseManagedVariablesModule;
        private ResourceModule resourceModule;
        private UserExperienceModule userExperienceModule;
        private UxmModule uxmModule;

        private Builder() {
        }

        public ManagedVariablesComponent build() {
            if (this.baseManagedVariablesModule == null) {
                throw new IllegalStateException(BaseManagedVariablesModule.class.getCanonicalName() + " must be set");
            }
            if (this.uxmModule == null) {
                this.uxmModule = new UxmModule();
            }
            if (this.resourceModule == null) {
                this.resourceModule = new ResourceModule();
            }
            if (this.userExperienceModule == null) {
                this.userExperienceModule = new UserExperienceModule();
            }
            return new DaggerManagedVariablesComponent();
        }

        @Deprecated
        public Builder managedVariablesModule(ManagedVariablesModule managedVariablesModule) {
            Preconditions.checkNotNull(managedVariablesModule);
            return this;
        }

        public Builder resourceModule(ResourceModule resourceModule) {
            this.resourceModule = (ResourceModule) Preconditions.checkNotNull(resourceModule);
            return this;
        }

        public Builder uxmModule(UxmModule uxmModule) {
            this.uxmModule = (UxmModule) Preconditions.checkNotNull(uxmModule);
            return this;
        }

        public Builder userExperienceModule(UserExperienceModule userExperienceModule) {
            this.userExperienceModule = (UserExperienceModule) Preconditions.checkNotNull(userExperienceModule);
            return this;
        }

        public Builder baseManagedVariablesModule(BaseManagedVariablesModule baseManagedVariablesModule) {
            this.baseManagedVariablesModule = (BaseManagedVariablesModule) Preconditions.checkNotNull(baseManagedVariablesModule);
            return this;
        }
    }

    private DaggerManagedVariablesComponent(Builder builder) {
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
        this.provideUpsightContextProvider = DoubleCheck.provider(BaseManagedVariablesModule_ProvideUpsightContextFactory.create(builder.baseManagedVariablesModule));
        this.provideMainSchedulerProvider = DoubleCheck.provider(BaseManagedVariablesModule_ProvideMainSchedulerFactory.create(builder.baseManagedVariablesModule));
        this.provideUxmSchemaGsonProvider = DoubleCheck.provider(UxmModule_ProvideUxmSchemaGsonFactory.create(builder.uxmModule, this.provideUpsightContextProvider));
        this.provideUxmSchemaJsonParserProvider = DoubleCheck.provider(UxmModule_ProvideUxmSchemaJsonParserFactory.create(builder.uxmModule, this.provideUpsightContextProvider));
        this.provideUxmSchemaResourceProvider = DoubleCheck.provider(ResourceModule_ProvideUxmSchemaResourceFactory.create(builder.resourceModule));
        this.provideUxmSchemaRawStringProvider = DoubleCheck.provider(UxmModule_ProvideUxmSchemaRawStringFactory.create(builder.uxmModule, this.provideUpsightContextProvider, this.provideUxmSchemaResourceProvider));
        this.provideUxmSchemaProvider = DoubleCheck.provider(UxmModule_ProvideUxmSchemaFactory.create(builder.uxmModule, this.provideUpsightContextProvider, this.provideUxmSchemaGsonProvider, this.provideUxmSchemaJsonParserProvider, this.provideUxmSchemaRawStringProvider));
        this.provideManagedVariableManagerProvider = DoubleCheck.provider(UxmModule_ProvideManagedVariableManagerFactory.create(builder.uxmModule, this.provideUpsightContextProvider, this.provideMainSchedulerProvider, this.provideUxmSchemaProvider));
        this.provideUserExperienceProvider = DoubleCheck.provider(UserExperienceModule_ProvideUserExperienceFactory.create(builder.userExperienceModule, this.provideUpsightContextProvider));
        this.provideManagedVariablesApiProvider = DoubleCheck.provider(BaseManagedVariablesModule_ProvideManagedVariablesApiFactory.create(builder.baseManagedVariablesModule, this.provideManagedVariableManagerProvider, this.provideUserExperienceProvider));
        this.provideUxmContentFactoryProvider = DoubleCheck.provider(UxmModule_ProvideUxmContentFactoryFactory.create(builder.uxmModule, this.provideUpsightContextProvider, this.provideMainSchedulerProvider, this.provideUserExperienceProvider));
        this.provideUxmBlockProvider = DoubleCheck.provider(UxmModule_ProvideUxmBlockProviderFactory.create(builder.uxmModule, this.provideUpsightContextProvider, this.provideUxmSchemaRawStringProvider, this.provideUxmSchemaProvider));
        this.upsightManagedVariablesExtensionMembersInjector = UpsightManagedVariablesExtension_MembersInjector.create(this.provideManagedVariablesApiProvider, this.provideUxmContentFactoryProvider, this.provideUxmBlockProvider);
    }

    public void inject(UpsightManagedVariablesExtension arg0) {
        this.upsightManagedVariablesExtensionMembersInjector.injectMembers(arg0);
    }

    public UxmSchema uxmSchema() {
        return (UxmSchema) this.provideUxmSchemaProvider.get();
    }
}
