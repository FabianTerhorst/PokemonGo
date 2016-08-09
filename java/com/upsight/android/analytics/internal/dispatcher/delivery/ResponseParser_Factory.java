package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.google.gson.Gson;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ResponseParser_Factory implements Factory<ResponseParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ResponseParser_Factory.class.desiredAssertionStatus());
    private final Provider<Gson> gsonProvider;

    public ResponseParser_Factory(Provider<Gson> gsonProvider) {
        if ($assertionsDisabled || gsonProvider != null) {
            this.gsonProvider = gsonProvider;
            return;
        }
        throw new AssertionError();
    }

    public ResponseParser get() {
        return new ResponseParser((Gson) this.gsonProvider.get());
    }

    public static Factory<ResponseParser> create(Provider<Gson> gsonProvider) {
        return new ResponseParser_Factory(gsonProvider);
    }
}
