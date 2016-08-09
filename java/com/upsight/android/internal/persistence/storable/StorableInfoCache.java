package com.upsight.android.internal.persistence.storable;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.upsight.android.UpsightException;
import com.upsight.android.persistence.UpsightStorableSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public final class StorableInfoCache {
    private final ConcurrentHashMap<Class<?>, StorableIdentifierAccessor> mAccessorMap = new ConcurrentHashMap();
    private final Gson mGson;
    private final ConcurrentHashMap<Class<?>, StorableInfo<?>> mInfoMap = new ConcurrentHashMap();
    private final ConcurrentHashMap<Class<?>, UpsightStorableSerializer<?>> mSerializerMap = new ConcurrentHashMap();

    StorableInfoCache(Gson gson) {
        this.mGson = gson;
    }

    public <T> void setSerializer(Class<T> clazz, UpsightStorableSerializer<T> serializer) {
        this.mSerializerMap.put(clazz, serializer);
    }

    public <T> StorableInfo<T> get(Class<T> clazz) throws UpsightException {
        if (clazz == null) {
            throw new IllegalArgumentException("Class can not be null.");
        }
        StorableInfo<T> info = (StorableInfo) this.mInfoMap.get(clazz);
        if (info == null) {
            UpsightStorableSerializer<T> serializer = resolveSerializer(clazz);
            StorableTypeAccessor<T> typeAccessor = resolveType(clazz);
            info = new StorableInfo(typeAccessor, serializer, resolveIdentifierAccessor(clazz));
            if (!typeAccessor.isDynamic()) {
                this.mInfoMap.put(clazz, info);
            }
        }
        return info;
    }

    private StorableIdentifierAccessor resolveIdentifierAccessor(Class<?> clazz) throws UpsightException {
        StorableIdentifierAccessor accessor = (StorableIdentifierAccessor) this.mAccessorMap.get(clazz);
        if (accessor != null) {
            return accessor;
        }
        Class<?> candidate = clazz;
        while (accessor == null && candidate != null) {
            Field[] declaredFields = candidate.getDeclaredFields();
            int length = declaredFields.length;
            int i = 0;
            while (i < length) {
                Field field = declaredFields[i];
                if (((UpsightStorableIdentifier) field.getAnnotation(UpsightStorableIdentifier.class)) == null) {
                    i++;
                } else if (field.getType().equals(String.class)) {
                    accessor = new StorableFieldIdentifierAccessor(field);
                    candidate = candidate.getSuperclass();
                } else {
                    throw new UpsightException("Field annotated with @%s must be of type String.", UpsightStorableIdentifier.class.getSimpleName());
                }
            }
            candidate = candidate.getSuperclass();
        }
        if (accessor == null) {
            accessor = new StorableIdentifierNoopAccessor();
        }
        this.mAccessorMap.put(clazz, accessor);
        return accessor;
    }

    private <T> UpsightStorableSerializer<T> resolveSerializer(Class<T> clazz) {
        UpsightStorableSerializer<T> serializer = (UpsightStorableSerializer) this.mSerializerMap.get(clazz);
        if (serializer != null) {
            return serializer;
        }
        serializer = new DefaultJsonSerializer(this.mGson, clazz);
        this.mSerializerMap.put(clazz, serializer);
        return serializer;
    }

    private <T> StorableTypeAccessor<T> resolveType(Class<T> clazz) throws UpsightException {
        StorableTypeAccessor<T> storableTypeAccessor = null;
        UpsightStorableType annotation = (UpsightStorableType) clazz.getAnnotation(UpsightStorableType.class);
        if (annotation != null) {
            if (TextUtils.isEmpty(annotation.value())) {
                throw new UpsightException("Class annotated with @%s must define non empty value.", UpsightStorableType.class.getSimpleName());
            }
            storableTypeAccessor = new StorableStaticTypeAccessor(annotation.value());
        }
        for (Method method : clazz.getDeclaredMethods()) {
            annotation = (UpsightStorableType) method.getAnnotation(UpsightStorableType.class);
            if (annotation != null) {
                if (!method.getReturnType().equals(String.class)) {
                    throw new UpsightException("Method annotated with @%s must return empty.", UpsightStorableType.class);
                } else if (method.getParameterTypes().length > 0) {
                    throw new UpsightException("Method annotated with @%s must have no parameters.", UpsightStorableType.class);
                } else if (storableTypeAccessor != null) {
                    throw new UpsightException("@%s can only be defined once in class.", UpsightStorableType.class.getSimpleName());
                } else if (!TextUtils.isEmpty(annotation.value())) {
                    throw new UpsightException("Method annotated with @%s should not define type in annotation but return it.", UpsightStorableType.class.getSimpleName());
                } else if (Modifier.isPublic(method.getModifiers())) {
                    storableTypeAccessor = new StorableMethodTypeAccessor(method);
                } else {
                    throw new UpsightException("Method annotated with @%s must be public.", UpsightStorableType.class.getSimpleName());
                }
            }
        }
        if (storableTypeAccessor != null) {
            return storableTypeAccessor;
        }
        throw new UpsightException("Class must either be annotated or have method annotated with %s.", UpsightStorableType.class.getSimpleName());
    }
}
