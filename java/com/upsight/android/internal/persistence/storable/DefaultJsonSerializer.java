package com.upsight.android.internal.persistence.storable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.upsight.android.UpsightException;
import com.upsight.android.persistence.UpsightStorableSerializer;

public class DefaultJsonSerializer<T> implements UpsightStorableSerializer<T> {
    private final Class<T> mClass;
    private final Gson mGson;

    public DefaultJsonSerializer(Gson gson, Class<T> clazz) {
        this.mGson = gson;
        this.mClass = clazz;
    }

    public String toString(T t) {
        return this.mGson.toJson(t);
    }

    public T fromString(String string) throws UpsightException {
        try {
            return this.mGson.fromJson(string, this.mClass);
        } catch (JsonSyntaxException e) {
            throw new UpsightException(e);
        }
    }
}
