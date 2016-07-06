package com.upsight.android.internal.persistence.subscription;

import android.util.Log;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightException;
import java.util.Set;
import rx.Subscriber;

class AnnotatedSubscriber extends Subscriber<DataStoreEvent> {
    private final Set<SubscriptionHandler> mHandlers;

    AnnotatedSubscriber(Set<SubscriptionHandler> handlers) {
        this.mHandlers = handlers;
    }

    public void onCompleted() {
    }

    public void onError(Throwable throwable) {
    }

    public void onNext(DataStoreEvent dataStoreEvent) {
        for (SubscriptionHandler handler : this.mHandlers) {
            if (handler.matches(dataStoreEvent.action, dataStoreEvent.sourceType)) {
                try {
                    handler.handle(dataStoreEvent);
                } catch (UpsightException e) {
                    Log.e(Upsight.LOG_TAG, "Failed to handle subscription.", e);
                }
            }
        }
    }
}
