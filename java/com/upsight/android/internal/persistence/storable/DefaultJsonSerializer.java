package com.upsight.android.internal.persistence.storable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.UpsightException;
import com.upsight.android.persistence.UpsightStorableSerializer;
import java.io.IOException;

public class DefaultJsonSerializer<T> implements UpsightStorableSerializer<T> {
    private final Class<T> mClass;
    private final ObjectMapper mObjectMapper;

    public DefaultJsonSerializer(ObjectMapper objectMapper, Class<T> clazz) {
        this.mObjectMapper = objectMapper;
        this.mClass = clazz;
    }

    public String toString(T t) {
        return this.mObjectMapper.valueToTree(t).toString();
    }

    public T fromString(String string) throws UpsightException {
        try {
            return this.mObjectMapper.treeToValue(this.mObjectMapper.readTree(string), this.mClass);
        } catch (IOException e) {
            throw new UpsightException(e);
        }
    }
}
