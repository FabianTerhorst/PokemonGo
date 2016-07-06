package com.upsight.android.internal.persistence.storable;

import com.upsight.android.persistence.UpsightStorableSerializer;

public final class StorableInfo<T> {
    private final StorableIdentifierAccessor mIdentifierAccessor;
    private final UpsightStorableSerializer<T> mSerializer;
    private final StorableTypeAccessor<T> mStorableType;

    public static final <T> StorableInfo<T> create(StorableTypeAccessor<T> typeAccessor, UpsightStorableSerializer<T> serializer, StorableIdentifierAccessor identifierAccessor) {
        return new StorableInfo(typeAccessor, serializer, identifierAccessor);
    }

    StorableInfo(StorableTypeAccessor<T> typeAccessor, UpsightStorableSerializer<T> serializer, StorableIdentifierAccessor identifierAccessor) {
        if (typeAccessor == null) {
            throw new IllegalArgumentException("StorableTypeAccessor type can not be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("UpsightStorableSerializer can not be null.");
        } else if (identifierAccessor == null) {
            throw new IllegalArgumentException("StorableIdentifierAccessor can not be null.");
        } else {
            this.mStorableType = typeAccessor;
            this.mSerializer = serializer;
            this.mIdentifierAccessor = identifierAccessor;
        }
    }

    public UpsightStorableSerializer<T> getDeserializer() {
        return this.mSerializer;
    }

    public StorableTypeAccessor<T> getStorableTypeAccessor() {
        return this.mStorableType;
    }

    public StorableIdentifierAccessor getIdentifierAccessor() {
        return this.mIdentifierAccessor;
    }
}
