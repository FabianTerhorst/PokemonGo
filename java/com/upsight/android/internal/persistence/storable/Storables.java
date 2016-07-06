package com.upsight.android.internal.persistence.storable;

import com.upsight.android.internal.persistence.Storable;
import rx.Observable.Operator;

public final class Storables {
    private Storables() {
    }

    public static <T> Operator<Storable, T> serialize(StorableInfo<T> info, StorableIdFactory idFactory) {
        return new OperatorSerialize(info, idFactory);
    }

    public static <T> Operator<T, Storable> deserialize(StorableInfo<T> info) {
        return new OperatorDeserialize(info);
    }
}
