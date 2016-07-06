package com.upsight.android.internal.persistence.storable;

import com.upsight.android.UpsightException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class StorableMethodTypeAccessor<T> implements StorableTypeAccessor<T> {
    private final Method mMethod;

    public StorableMethodTypeAccessor(Method method) {
        this.mMethod = method;
    }

    public String getType(T target) throws UpsightException {
        ReflectiveOperationException e;
        try {
            return (String) this.mMethod.invoke(target, new Object[0]);
        } catch (IllegalAccessException e2) {
            e = e2;
            throw new UpsightException(e);
        } catch (InvocationTargetException e3) {
            e = e3;
            throw new UpsightException(e);
        }
    }

    public String getType() throws UpsightException {
        return null;
    }

    public boolean isDynamic() {
        return true;
    }
}
