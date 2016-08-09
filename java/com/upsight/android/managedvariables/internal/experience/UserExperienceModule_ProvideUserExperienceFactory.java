package com.upsight.android.managedvariables.internal.experience;

import com.upsight.android.UpsightContext;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UserExperienceModule_ProvideUserExperienceFactory implements Factory<UpsightUserExperience> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UserExperienceModule_ProvideUserExperienceFactory.class.desiredAssertionStatus());
    private final UserExperienceModule module;
    private final Provider<UpsightContext> upsightProvider;

    public UserExperienceModule_ProvideUserExperienceFactory(UserExperienceModule module, Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightUserExperience get() {
        return (UpsightUserExperience) Preconditions.checkNotNull(this.module.provideUserExperience((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightUserExperience> create(UserExperienceModule module, Provider<UpsightContext> upsightProvider) {
        return new UserExperienceModule_ProvideUserExperienceFactory(module, upsightProvider);
    }
}
