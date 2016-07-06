package com.upsight.android.managedvariables.type;

import com.upsight.android.UpsightException;
import com.upsight.android.managedvariables.internal.type.ManagedVariable;
import com.upsight.android.persistence.UpsightSubscription;

public abstract class UpsightManagedVariable<T> extends ManagedVariable<T> {

    public interface Listener<T> {
        void onFailure(UpsightException upsightException);

        void onSuccess(T t);
    }

    protected static class NoOpSubscription implements UpsightSubscription {
        protected NoOpSubscription() {
        }

        public boolean isSubscribed() {
            return false;
        }

        public void unsubscribe() {
        }
    }

    protected UpsightManagedVariable(String tag, T defaultValue, T value) {
        super(tag, defaultValue, value);
    }
}
