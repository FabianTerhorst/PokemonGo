package com.upsight.android.internal.persistence.storable;

import com.upsight.android.UpsightException;

class StorableStaticTypeAccessor<T> implements StorableTypeAccessor<T> {
    private final String mType;

    public StorableStaticTypeAccessor(String type) {
        this.mType = type;
    }

    public String getType(T t) throws UpsightException {
        return this.mType;
    }

    public String getType() throws UpsightException {
        return this.mType;
    }

    public boolean isDynamic() {
        return false;
    }
}
