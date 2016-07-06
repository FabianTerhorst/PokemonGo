package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.persistence.annotation.UpsightStorableType;

class ManagedInt extends UpsightManagedInt {
    static final String MODEL_TYPE = "com.upsight.uxm.integer";

    @UpsightStorableType("com.upsight.uxm.integer")
    static class Model extends ManagedVariableModel<Integer> {
        Model() {
        }
    }

    ManagedInt(String tag, Integer defaultValue, Integer value) {
        super(tag, defaultValue, value);
    }
}
