package com.upsight.android.internal.util;

import android.util.Log;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightException;
import com.upsight.android.persistence.UpsightDataStoreListener;

public final class LoggingListener<T> implements UpsightDataStoreListener<T> {
    public void onSuccess(T t) {
    }

    public void onFailure(UpsightException exception) {
        Log.e(Upsight.LOG_TAG, "Uncaught Exception within Upsight SDK.", exception);
    }
}
