package com.upsight.android.internal.persistence.storable;

import com.upsight.android.UpsightException;
import com.upsight.android.internal.persistence.Storable;
import com.upsight.android.persistence.UpsightStorableSerializer;
import rx.Observable.Operator;
import rx.Subscriber;

class OperatorDeserialize<T> implements Operator<T, Storable> {
    private final StorableInfo<T> mStorableInfo;

    private static class DeserializeSubscriber<T> extends Subscriber<Storable> {
        private final Subscriber<? super T> mChildSubscriber;
        private final StorableInfo<T> mStorableInfo;

        public DeserializeSubscriber(StorableInfo<T> storableInfo, Subscriber<? super T> child) {
            super(child);
            this.mStorableInfo = storableInfo;
            this.mChildSubscriber = child;
        }

        public void onCompleted() {
            this.mChildSubscriber.onCompleted();
        }

        public void onError(Throwable throwable) {
            this.mChildSubscriber.onError(throwable);
        }

        public void onNext(Storable storable) {
            UpsightStorableSerializer<T> serializer = this.mStorableInfo.getDeserializer();
            try {
                if (!this.mChildSubscriber.isUnsubscribed()) {
                    T object = serializer.fromString(storable.getValue());
                    this.mStorableInfo.getIdentifierAccessor().setId(object, storable.getID());
                    this.mChildSubscriber.onNext(object);
                }
            } catch (UpsightException e) {
                this.mChildSubscriber.onError(e);
            }
        }
    }

    public OperatorDeserialize(StorableInfo<T> info) {
        if (info == null) {
            throw new IllegalArgumentException("StorableInfo can not be null.");
        }
        this.mStorableInfo = info;
    }

    public Subscriber<? super Storable> call(Subscriber<? super T> subscriber) {
        return new DeserializeSubscriber(this.mStorableInfo, subscriber);
    }
}
