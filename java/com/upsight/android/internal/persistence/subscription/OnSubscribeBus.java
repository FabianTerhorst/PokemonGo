package com.upsight.android.internal.persistence.subscription;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

class OnSubscribeBus implements OnSubscribe<DataStoreEvent> {
    private final Bus mBus;

    private static class BusAdapter {
        private final Subscriber<? super DataStoreEvent> mChild;

        private BusAdapter(Subscriber<? super DataStoreEvent> child) {
            this.mChild = child;
        }

        @Subscribe
        public void onPersistenceEvent(DataStoreEvent event) {
            if (!this.mChild.isUnsubscribed()) {
                this.mChild.onNext(event);
            }
        }
    }

    OnSubscribeBus(Bus bus) {
        this.mBus = bus;
    }

    public void call(Subscriber<? super DataStoreEvent> subscriber) {
        final BusAdapter adapter = new BusAdapter(subscriber);
        this.mBus.register(adapter);
        subscriber.add(Subscriptions.create(new Action0() {
            public void call() {
                OnSubscribeBus.this.mBus.unregister(adapter);
            }
        }));
    }
}
