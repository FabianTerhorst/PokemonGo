package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DeliveryModule_ProvideResponseVerifierFactory implements Factory<SignatureVerifier> {
    static final /* synthetic */ boolean $assertionsDisabled = (!DeliveryModule_ProvideResponseVerifierFactory.class.desiredAssertionStatus());
    private final DeliveryModule module;
    private final Provider<UpsightContext> upsightProvider;

    public DeliveryModule_ProvideResponseVerifierFactory(DeliveryModule module, Provider<UpsightContext> upsightProvider) {
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

    public SignatureVerifier get() {
        SignatureVerifier provided = this.module.provideResponseVerifier((UpsightContext) this.upsightProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SignatureVerifier> create(DeliveryModule module, Provider<UpsightContext> upsightProvider) {
        return new DeliveryModule_ProvideResponseVerifierFactory(module, upsightProvider);
    }
}
