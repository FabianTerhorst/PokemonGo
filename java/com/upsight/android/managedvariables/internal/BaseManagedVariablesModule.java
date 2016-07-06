package com.upsight.android.managedvariables.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.managedvariables.UpsightManagedVariablesApi;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.internal.type.ManagedVariableManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Module
public final class BaseManagedVariablesModule {
    public static final String SCHEDULER_MAIN = "main";
    private final UpsightContext mUpsight;

    public BaseManagedVariablesModule(UpsightContext upsight) {
        this.mUpsight = upsight;
    }

    @Singleton
    @Provides
    UpsightContext provideUpsightContext() {
        return this.mUpsight;
    }

    @Singleton
    @Provides
    UpsightManagedVariablesApi provideManagedVariablesApi(ManagedVariableManager managedVariableManager, UpsightUserExperience userExperience) {
        return new ManagedVariables(managedVariableManager, userExperience);
    }

    @Singleton
    @Provides
    @Named("main")
    Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
