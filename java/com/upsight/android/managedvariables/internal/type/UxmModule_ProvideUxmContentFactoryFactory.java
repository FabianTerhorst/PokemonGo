package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.UpsightContext;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import rx.Scheduler;

public final class UxmModule_ProvideUxmContentFactoryFactory implements Factory<UxmContentFactory> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UxmModule_ProvideUxmContentFactoryFactory.class.desiredAssertionStatus());
    private final UxmModule module;
    private final Provider<Scheduler> schedulerProvider;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<UpsightUserExperience> userExperienceProvider;

    public UxmModule_ProvideUxmContentFactoryFactory(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<Scheduler> schedulerProvider, Provider<UpsightUserExperience> userExperienceProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || schedulerProvider != null) {
                    this.schedulerProvider = schedulerProvider;
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
        throw new AssertionError();
    }

    public UxmContentFactory get() {
        return (UxmContentFactory) Preconditions.checkNotNull(this.module.provideUxmContentFactory((UpsightContext) this.upsightProvider.get(), (Scheduler) this.schedulerProvider.get(), (UpsightUserExperience) this.userExperienceProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UxmContentFactory> create(UxmModule module, Provider<UpsightContext> upsightProvider, Provider<Scheduler> schedulerProvider, Provider<UpsightUserExperience> userExperienceProvider) {
        return new UxmModule_ProvideUxmContentFactoryFactory(module, upsightProvider, schedulerProvider, userExperienceProvider);
    }
}
