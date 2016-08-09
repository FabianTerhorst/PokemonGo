package com.upsight.android.managedvariables.internal;

import com.upsight.android.managedvariables.UpsightManagedVariablesApi;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.internal.type.ManagedVariableManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class BaseManagedVariablesModule_ProvideManagedVariablesApiFactory implements Factory<UpsightManagedVariablesApi> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseManagedVariablesModule_ProvideManagedVariablesApiFactory.class.desiredAssertionStatus());
    private final Provider<ManagedVariableManager> managedVariableManagerProvider;
    private final BaseManagedVariablesModule module;
    private final Provider<UpsightUserExperience> userExperienceProvider;

    public BaseManagedVariablesModule_ProvideManagedVariablesApiFactory(BaseManagedVariablesModule module, Provider<ManagedVariableManager> managedVariableManagerProvider, Provider<UpsightUserExperience> userExperienceProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || managedVariableManagerProvider != null) {
                this.managedVariableManagerProvider = managedVariableManagerProvider;
                if ($assertionsDisabled || userExperienceProvider != null) {
                    this.userExperienceProvider = userExperienceProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightManagedVariablesApi get() {
        return (UpsightManagedVariablesApi) Preconditions.checkNotNull(this.module.provideManagedVariablesApi((ManagedVariableManager) this.managedVariableManagerProvider.get(), (UpsightUserExperience) this.userExperienceProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightManagedVariablesApi> create(BaseManagedVariablesModule module, Provider<ManagedVariableManager> managedVariableManagerProvider, Provider<UpsightUserExperience> userExperienceProvider) {
        return new BaseManagedVariablesModule_ProvideManagedVariablesApiFactory(module, managedVariableManagerProvider, userExperienceProvider);
    }
}
