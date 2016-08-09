package com.upsight.android.managedvariables.internal.type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;

abstract class ManagedVariableModel<T> {
    @UpsightStorableIdentifier
    String id;
    @SerializedName("tag")
    @Expose
    String tag;
    @SerializedName("value")
    @Expose
    T value;

    ManagedVariableModel() {
    }

    public String getTag() {
        return this.tag;
    }

    public T getValue() {
        return this.value;
    }
}
