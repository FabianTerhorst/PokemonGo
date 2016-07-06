package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.Clock;
import dagger.internal.Factory;
import javax.inject.Provider;
import rx.Scheduler;

public final class DeliveryModule_ProvideQueueBuilderFactory implements Factory<QueueBuilder> {
    static final /* synthetic */ boolean $assertionsDisabled = (!DeliveryModule_ProvideQueueBuilderFactory.class.desiredAssertionStatus());
    private final Provider<Clock> clockProvider;
    private final DeliveryModule module;
    private final Provider<ResponseParser> responseParserProvider;
    private final Provider<Scheduler> retryExecutorProvider;
    private final Provider<Scheduler> sendExecutorProvider;
    private final Provider<SignatureVerifier> signatureVerifierProvider;
    private final Provider<UpsightContext> upsightProvider;

    public DeliveryModule_ProvideQueueBuilderFactory(DeliveryModule module, Provider<UpsightContext> upsightProvider, Provider<Clock> clockProvider, Provider<Scheduler> retryExecutorProvider, Provider<Scheduler> sendExecutorProvider, Provider<SignatureVerifier> signatureVerifierProvider, Provider<ResponseParser> responseParserProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || clockProvider != null) {
                    this.clockProvider = clockProvider;
                    if ($assertionsDisabled || retryExecutorProvider != null) {
                        this.retryExecutorProvider = retryExecutorProvider;
                        if ($assertionsDisabled || sendExecutorProvider != null) {
                            this.sendExecutorProvider = sendExecutorProvider;
                            if ($assertionsDisabled || signatureVerifierProvider != null) {
                                this.signatureVerifierProvider = signatureVerifierProvider;
                                if ($assertionsDisabled || responseParserProvider != null) {
                                    this.responseParserProvider = responseParserProvider;
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
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public QueueBuilder get() {
        QueueBuilder provided = this.module.provideQueueBuilder((UpsightContext) this.upsightProvider.get(), (Clock) this.clockProvider.get(), (Scheduler) this.retryExecutorProvider.get(), (Scheduler) this.sendExecutorProvider.get(), (SignatureVerifier) this.signatureVerifierProvider.get(), this.responseParserProvider);
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<QueueBuilder> create(DeliveryModule module, Provider<UpsightContext> upsightProvider, Provider<Clock> clockProvider, Provider<Scheduler> retryExecutorProvider, Provider<Scheduler> sendExecutorProvider, Provider<SignatureVerifier> signatureVerifierProvider, Provider<ResponseParser> responseParserProvider) {
        return new DeliveryModule_ProvideQueueBuilderFactory(module, upsightProvider, clockProvider, retryExecutorProvider, sendExecutorProvider, signatureVerifierProvider, responseParserProvider);
    }
}
