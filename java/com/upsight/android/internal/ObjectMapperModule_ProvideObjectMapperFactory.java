package com.upsight.android.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.internal.Factory;

public final class ObjectMapperModule_ProvideObjectMapperFactory implements Factory<ObjectMapper> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ObjectMapperModule_ProvideObjectMapperFactory.class.desiredAssertionStatus());
    private final ObjectMapperModule module;

    public ObjectMapperModule_ProvideObjectMapperFactory(ObjectMapperModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public ObjectMapper get() {
        ObjectMapper provided = this.module.provideObjectMapper();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ObjectMapper> create(ObjectMapperModule module) {
        return new ObjectMapperModule_ProvideObjectMapperFactory(module);
    }
}
