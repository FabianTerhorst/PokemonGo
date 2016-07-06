package com.upsight.android.managedvariables.internal.experience;

import com.upsight.android.UpsightContext;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class UserExperienceModule {
    @Singleton
    @Provides
    UpsightUserExperience provideUserExperience(UpsightContext upsight) {
        return new UserExperience(upsight.getCoreComponent().bus());
    }
}
