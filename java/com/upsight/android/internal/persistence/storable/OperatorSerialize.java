package com.upsight.android.internal.persistence.storable;

import com.upsight.android.UpsightException;
import com.upsight.android.internal.persistence.Storable;
import com.upsight.android.persistence.UpsightStorableSerializer;
import rx.Observable.Operator;
import rx.Subscriber;

class OperatorSerialize<T> implements Operator<Storable, T> {
    private final StorableIdFactory mIDFactory;
    private final StorableInfo<T> mStorableInfo;

    private static class DeserializeSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super Storable> mChildSubscriber;
        private final StorableIdFactory mIdFactory;
        private final StorableInfo<T> mStorableInfo;

        public DeserializeSubscriber(StorableInfo<T> storableInfo, StorableIdFactory idFactory, Subscriber<? super Storable> child) {
            super(child);
            this.mStorableInfo = storableInfo;
            this.mChildSubscriber = child;
            this.mIdFactory = idFactory;
        }

        public void onCompleted() {
            this.mChildSubscriber.onCompleted();
        }

        public void onError(Throwable throwable) {
            this.mChildSubscriber.onError(throwable);
        }

        public void onNext(T upsightStorable) {
            UpsightStorableSerializer<T> serializer = this.mStorableInfo.getDeserializer();
            try {
                if (!this.mChildSubscriber.isUnsubscribed()) {
                    this.mChildSubscriber.onNext(Storable.create(this.mStorableInfo.getIdentifierAccessor().getId(upsightStorable), this.mStorableInfo.getStorableTypeAccessor().getType(upsightStorable), serializer.toString(upsightStorable)));
                }
            } catch (UpsightException e) {
                this.mChildSubscriber.onError(e);
            }
        }
    }

    public OperatorSerialize(StorableInfo<T> info, StorableIdFactory idFactory) {
        if (info == null) {
            throw new IllegalArgumentException("StorableInfo can not be null.");
        }
        this.mStorableInfo = info;
        this.mIDFactory = idFactory;
    }

    public Subscriber<? super T> call(Subscriber<? super Storable> subscriber) {
        return new DeserializeSubscriber(this.mStorableInfo, this.mIDFactory, subscriber);
    }
}
