package com.upsight.android.managedvariables.internal;

import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.internal.type.ManagedVariableManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ManagedVariables_Factory implements Factory<ManagedVariables> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ManagedVariables_Factory.class.desiredAssertionStatus());
    private final Provider<ManagedVariableManager> managedVariableManagerProvider;
    private final Provider<UpsightUserExperience> userExperienceProvider;

    public ManagedVariables_Factory(Provider<ManagedVariableManager> managedVariableManagerProvider, Provider<UpsightUserExperience> userExperienceProvider) {
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

    public ManagedVariables get() {
        return new ManagedVariables((ManagedVariableManager) this.managedVariableManagerProvider.get(), (UpsightUserExperience) this.userExperienceProvider.get());
    }

    public static Factory<ManagedVariables> create(Provider<ManagedVariableManager> managedVariableManagerProvider, Provider<UpsightUserExperience> userExperienceProvider) {
        return new ManagedVariables_Factory(managedVariableManagerProvider, userExperienceProvider);
    }
}
