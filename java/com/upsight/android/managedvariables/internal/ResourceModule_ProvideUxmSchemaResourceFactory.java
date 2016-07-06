package com.upsight.android.managedvariables.internal;

import dagger.internal.Factory;

public final class ResourceModule_ProvideUxmSchemaResourceFactory implements Factory<Integer> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ResourceModule_ProvideUxmSchemaResourceFactory.class.desiredAssertionStatus());
    private final ResourceModule module;

    public ResourceModule_ProvideUxmSchemaResourceFactory(ResourceModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Integer get() {
        Integer provided = this.module.provideUxmSchemaResource();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Integer> create(ResourceModule module) {
        return new ResourceModule_ProvideUxmSchemaResourceFactory(module);
    }
}
