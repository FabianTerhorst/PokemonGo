package com.upsight.android.internal;

import com.google.gson.JsonParser;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class JsonModule_ProvideJsonParserFactory implements Factory<JsonParser> {
    static final /* synthetic */ boolean $assertionsDisabled = (!JsonModule_ProvideJsonParserFactory.class.desiredAssertionStatus());
    private final JsonModule module;

    public JsonModule_ProvideJsonParserFactory(JsonModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public JsonParser get() {
        return (JsonParser) Preconditions.checkNotNull(this.module.provideJsonParser(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<JsonParser> create(JsonModule module) {
        return new JsonModule_ProvideJsonParserFactory(module);
    }
}
