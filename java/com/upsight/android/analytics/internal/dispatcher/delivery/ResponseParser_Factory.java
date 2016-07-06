package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ResponseParser_Factory implements Factory<ResponseParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ResponseParser_Factory.class.desiredAssertionStatus());
    private final Provider<ObjectMapper> mapperProvider;

    public ResponseParser_Factory(Provider<ObjectMapper> mapperProvider) {
        if ($assertionsDisabled || mapperProvider != null) {
            this.mapperProvider = mapperProvider;
            return;
        }
        throw new AssertionError();
    }

    public ResponseParser get() {
        return new ResponseParser((ObjectMapper) this.mapperProvider.get());
    }

    public static Factory<ResponseParser> create(Provider<ObjectMapper> mapperProvider) {
        return new ResponseParser_Factory(mapperProvider);
    }
}
