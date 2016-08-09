package com.upsight.android.internal.persistence.subscription;

import com.squareup.otto.Bus;
import com.upsight.android.internal.persistence.subscription.DataStoreEvent.Action;
import com.upsight.android.persistence.UpsightSubscription;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class Subscriptions {
    public static <T> Action1<T> publishCreated(final Bus bus, final String type) {
        return new Action1<T>() {
            public void call(T t) {
                bus.post(new DataStoreEvent(Action.Created, type, t));
            }
        };
    }

    public static <T> Action1<T> publishUpdated(final Bus bus, final String type) {
        return new Action1<T>() {
            public void call(T t) {
                bus.post(new DataStoreEvent(Action.Updated, type, t));
            }
        };
    }

    public static <T> Action1<T> publishRemoved(final Bus bus, final String type) {
        return new Action1<T>() {
            public void call(T t) {
                bus.post(new DataStoreEvent(Action.Removed, type, t));
            }
        };
    }

    public static Observable<DataStoreEvent> toObservable(Bus bus) {
        return Observable.create(new OnSubscribeBus(bus)).onBackpressureBuffer();
    }

    public static UpsightSubscription from(Subscription subscription) {
        return new SubscriptionAdapter(subscription);
    }

    public static AnnotatedSubscriber create(Object target) {
        SubscriptionHandlerVisitor visitor = new SubscriptionHandlerVisitor(target);
        new ClassSubscriptionReader(target.getClass()).accept(visitor);
        return new AnnotatedSubscriber(visitor.getHandlers());
    }

    private Subscriptions() {
    }
}
