package com.upsight.android.internal.persistence.subscription;

import com.upsight.android.persistence.annotation.Created;
import com.upsight.android.persistence.annotation.Removed;
import com.upsight.android.persistence.annotation.Updated;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class ClassSubscriptionReader {
    private final Class<?> mClass;

    ClassSubscriptionReader(Class<?> clazz) {
        this.mClass = clazz;
    }

    public void accept(ClassSubscriptionVisitor visitor) {
        for (Method method : this.mClass.getMethods()) {
            if (isSubscriptionMethod(method)) {
                Class<?> argumentType = method.getParameterTypes()[0];
                if (((Created) method.getAnnotation(Created.class)) != null) {
                    visitor.visitCreatedSubscription(method, argumentType);
                }
                if (((Updated) method.getAnnotation(Updated.class)) != null) {
                    visitor.visitUpdatedSubscription(method, argumentType);
                }
                if (((Removed) method.getAnnotation(Removed.class)) != null) {
                    visitor.visitRemovedSubscription(method, argumentType);
                }
            }
        }
    }

    private boolean isSubscriptionMethod(Method method) {
        if (!method.getReturnType().equals(Void.TYPE)) {
            return false;
        }
        Class<?>[] params = method.getParameterTypes();
        if (params.length == 1 && ((UpsightStorableType) params[0].getAnnotation(UpsightStorableType.class)) != null && Modifier.isPublic(method.getModifiers())) {
            return true;
        }
        return false;
    }
}
