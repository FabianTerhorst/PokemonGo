package com.upsight.android;

import android.content.Context;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import com.upsight.android.persistence.UpsightStorableSerializer;
import com.upsight.android.persistence.UpsightSubscription;
import java.util.EnumSet;
import java.util.Set;
import rx.Observable;

class UpsightContextCompat extends UpsightContext {

    private static class NoOpDataStore implements UpsightDataStore {

        private static class NoOpSubscription implements UpsightSubscription {
            private NoOpSubscription() {
            }

            public boolean isSubscribed() {
                return false;
            }

            public void unsubscribe() {
            }
        }

        private NoOpDataStore() {
        }

        public <T> UpsightSubscription fetch(Class<T> cls, Set<String> set, UpsightDataStoreListener<Set<T>> upsightDataStoreListener) {
            return new NoOpSubscription();
        }

        public <T> UpsightSubscription fetch(Class<T> cls, UpsightDataStoreListener<Set<T>> upsightDataStoreListener) {
            return new NoOpSubscription();
        }

        public <T> UpsightSubscription store(T t, UpsightDataStoreListener<T> upsightDataStoreListener) {
            return new NoOpSubscription();
        }

        public <T> UpsightSubscription store(T t) {
            return new NoOpSubscription();
        }

        public <T> UpsightSubscription remove(Class<T> cls, Set<String> set, UpsightDataStoreListener<Set<T>> upsightDataStoreListener) {
            return new NoOpSubscription();
        }

        public <T> UpsightSubscription remove(Class<T> cls, Set<String> set) {
            return new NoOpSubscription();
        }

        public <T> UpsightSubscription remove(T t, UpsightDataStoreListener<T> upsightDataStoreListener) {
            return new NoOpSubscription();
        }

        public <T> UpsightSubscription remove(T t) {
            return new NoOpSubscription();
        }

        public UpsightSubscription subscribe(Object object) {
            return new NoOpSubscription();
        }

        public <T> void setSerializer(Class<T> cls, UpsightStorableSerializer<T> upsightStorableSerializer) {
        }

        public <T> Observable<T> fetchObservable(Class<T> cls) {
            return Observable.empty();
        }

        public <T> Observable<T> fetchObservable(Class<T> cls, String... ids) {
            return Observable.empty();
        }

        public <T> Observable<T> storeObservable(T t) {
            return Observable.empty();
        }

        public <T> Observable<T> removeObservable(T t) {
            return Observable.empty();
        }

        public <T> Observable<T> removeObservable(Class<T> cls, String... ids) {
            return Observable.empty();
        }
    }

    private static class NoOpLogger implements UpsightLogger {
        private NoOpLogger() {
        }

        public void setLogLevel(String logTagRegularExpression, EnumSet<Level> enumSet) {
        }

        public void v(String tag, String message, Object... args) {
        }

        public void v(String tag, Throwable tr, String message, Object... args) {
        }

        public void d(String tag, String message, Object... args) {
        }

        public void d(String tag, Throwable tr, String message, Object... args) {
        }

        public void i(String tag, String message, Object... args) {
        }

        public void i(String tag, Throwable tr, String message, Object... args) {
        }

        public void w(String tag, String message, Object... args) {
        }

        public void w(String tag, Throwable tr, String message, Object... args) {
        }

        public void e(String tag, String message, Object... args) {
        }

        public void e(String tag, Throwable tr, String message, Object... args) {
        }
    }

    UpsightContextCompat(Context context) {
        super(context, null, null, null, null, new NoOpDataStore(), new NoOpLogger());
    }

    public UpsightCoreComponent getCoreComponent() {
        return null;
    }

    public UpsightExtension<?, ?> getUpsightExtension(String extensionName) {
        return null;
    }
}
