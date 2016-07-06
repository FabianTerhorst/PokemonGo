package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.Clock;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import rx.Scheduler;

@Module
public final class DeliveryModule {
    @Singleton
    @Provides
    public QueueBuilder provideQueueBuilder(UpsightContext upsight, Clock clock, @Named("dispatcher-threadpool") Scheduler retryExecutor, @Named("dispatcher-batching") Scheduler sendExecutor, SignatureVerifier signatureVerifier, Provider<ResponseParser> responseParserProvider) {
        return new QueueBuilder(upsight, upsight.getCoreComponent().objectMapper(), clock, upsight.getLogger(), retryExecutor, sendExecutor, signatureVerifier, responseParserProvider);
    }

    @Singleton
    @Provides
    public SignatureVerifier provideResponseVerifier(UpsightContext upsight) {
        return new BouncySignatureVerifier(upsight);
    }
}
