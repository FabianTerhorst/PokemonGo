package com.upsight.android.internal;

import com.upsight.android.internal.persistence.storable.StorableIdFactory;
import dagger.internal.Factory;

public final class ContextModule_ProvideTypeIdGeneratorFactory implements Factory<StorableIdFactory> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ContextModule_ProvideTypeIdGeneratorFactory.class.desiredAssertionStatus());
    private final ContextModule module;

    public ContextModule_ProvideTypeIdGeneratorFactory(ContextModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public StorableIdFactory get() {
        StorableIdFactory provided = this.module.provideTypeIdGenerator();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<StorableIdFactory> create(ContextModule module) {
        return new ContextModule_ProvideTypeIdGeneratorFactory(module);
    }
}
