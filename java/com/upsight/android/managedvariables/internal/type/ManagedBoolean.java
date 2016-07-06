package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.persistence.annotation.UpsightStorableType;

class ManagedBoolean extends UpsightManagedBoolean {
    static final String MODEL_TYPE = "com.upsight.uxm.boolean";

    @UpsightStorableType("com.upsight.uxm.boolean")
    static class Model extends ManagedVariableModel<Boolean> {
        Model() {
        }
    }

    ManagedBoolean(String tag, Boolean defaultValue, Boolean value) {
        super(tag, defaultValue, value);
    }
}
