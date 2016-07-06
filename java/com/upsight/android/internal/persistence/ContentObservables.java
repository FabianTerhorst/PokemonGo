package com.upsight.android.internal.persistence;

import android.content.Context;
import rx.Observable;

final class ContentObservables {
    private ContentObservables() {
    }

    public static Observable<Storable> insert(Context context, Storable storable) {
        return Observable.create(new OnSubscribeInsert(context, storable));
    }

    public static Observable<Storable> update(Context context, Storable storable) {
        return Observable.create(new OnSubscribeUpdate(context, storable));
    }

    public static Observable<Storable> remove(Context context, Storable storable) {
        return Observable.create(new OnSubscribeRemove(context, storable));
    }

    public static Observable<Storable> fetch(Context context, String type) {
        return Observable.create(new OnSubscribeFetchByType(context, type)).onBackpressureBuffer();
    }

    public static Observable<Storable> fetch(Context context, String type, String[] ids) {
        return Observable.create(new OnSubscribeFetchById(context, type, ids)).onBackpressureBuffer();
    }
}
