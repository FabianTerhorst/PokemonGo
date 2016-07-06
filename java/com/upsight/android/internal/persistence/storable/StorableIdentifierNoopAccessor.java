package com.upsight.android.internal.persistence.storable;

import com.upsight.android.UpsightException;

class StorableIdentifierNoopAccessor implements StorableIdentifierAccessor {
    StorableIdentifierNoopAccessor() {
    }

    public void setId(Object target, String id) throws UpsightException {
    }

    public String getId(Object target) throws UpsightException {
        return null;
    }
}
