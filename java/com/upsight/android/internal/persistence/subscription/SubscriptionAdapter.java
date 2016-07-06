package com.upsight.android.internal.persistence.subscription;

import com.upsight.android.persistence.UpsightSubscription;
import rx.Subscription;

class SubscriptionAdapter implements UpsightSubscription {
    private final Subscription mRxSubscription;

    SubscriptionAdapter(Subscription rxSubscription) {
        this.mRxSubscription = rxSubscription;
    }

    public boolean isSubscribed() {
        return !this.mRxSubscription.isUnsubscribed();
    }

    public void unsubscribe() {
        this.mRxSubscription.unsubscribe();
    }
}
