package com.upsight.android;

import com.upsight.android.managedvariables.UpsightManagedVariablesApi;
import com.upsight.android.managedvariables.internal.type.UxmBlockProvider;
import com.upsight.android.managedvariables.internal.type.UxmContentFactory;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightManagedVariablesExtension_MembersInjector implements MembersInjector<UpsightManagedVariablesExtension> {
    static final /* synthetic */ boolean $assertionsDisabled = (!UpsightManagedVariablesExtension_MembersInjector.class.desiredAssertionStatus());
    private final Provider<UpsightManagedVariablesApi> mManagedVariablesProvider;
    private final Provider<UxmBlockProvider> mUxmBlockProvider;
    private final Provider<UxmContentFactory> mUxmContentFactoryProvider;

    public UpsightManagedVariablesExtension_MembersInjector(Provider<UpsightManagedVariablesApi> mManagedVariablesProvider, Provider<UxmContentFactory> mUxmContentFactoryProvider, Provider<UxmBlockProvider> mUxmBlockProvider) {
        if ($assertionsDisabled || mManagedVariablesProvider != null) {
            this.mManagedVariablesProvider = mManagedVariablesProvider;
            if ($assertionsDisabled || mUxmContentFactoryProvider != null) {
                this.mUxmContentFactoryProvider = mUxmContentFactoryProvider;
                if ($assertionsDisabled || mUxmBlockProvider != null) {
                    this.mUxmBlockProvider = mUxmBlockProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightManagedVariablesExtension> create(Provider<UpsightManagedVariablesApi> mManagedVariablesProvider, Provider<UxmContentFactory> mUxmContentFactoryProvider, Provider<UxmBlockProvider> mUxmBlockProvider) {
        return new UpsightManagedVariablesExtension_MembersInjector(mManagedVariablesProvider, mUxmContentFactoryProvider, mUxmBlockProvider);
    }

    public void injectMembers(UpsightManagedVariablesExtension instance) {
        if (instance == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        instance.mManagedVariables = (UpsightManagedVariablesApi) this.mManagedVariablesProvider.get();
        instance.mUxmContentFactory = (UxmContentFactory) this.mUxmContentFactoryProvider.get();
        instance.mUxmBlockProvider = (UxmBlockProvider) this.mUxmBlockProvider.get();
    }

    public static void injectMManagedVariables(UpsightManagedVariablesExtension instance, Provider<UpsightManagedVariablesApi> mManagedVariablesProvider) {
        instance.mManagedVariables = (UpsightManagedVariablesApi) mManagedVariablesProvider.get();
    }

    public static void injectMUxmContentFactory(UpsightManagedVariablesExtension instance, Provider<UxmContentFactory> mUxmContentFactoryProvider) {
        instance.mUxmContentFactory = (UxmContentFactory) mUxmContentFactoryProvider.get();
    }

    public static void injectMUxmBlockProvider(UpsightManagedVariablesExtension instance, Provider<UxmBlockProvider> mUxmBlockProvider) {
        instance.mUxmBlockProvider = (UxmBlockProvider) mUxmBlockProvider.get();
    }
}
