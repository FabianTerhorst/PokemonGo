package com.upsight.android.analytics.internal.provider;

import com.upsight.android.analytics.provider.UpsightUserAttributes;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ProviderModule_ProvidesUpsightUserAttributesFactory implements Factory<UpsightUserAttributes> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ProviderModule_ProvidesUpsightUserAttributesFactory.class.desiredAssertionStatus());
    private final ProviderModule module;
    private final Provider<UserAttributes> userAttributesProvider;

    public ProviderModule_ProvidesUpsightUserAttributesFactory(ProviderModule module, Provider<UserAttributes> userAttributesProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || userAttributesProvider != null) {
                this.userAttributesProvider = userAttributesProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightUserAttributes get() {
        UpsightUserAttributes provided = this.module.providesUpsightUserAttributes((UserAttributes) this.userAttributesProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightUserAttributes> create(ProviderModule module, Provider<UserAttributes> userAttributesProvider) {
        return new ProviderModule_ProvidesUpsightUserAttributesFactory(module, userAttributesProvider);
    }
}
