package com.upsight.android.internal.persistence.subscription;

import com.upsight.android.UpsightException;
import com.upsight.android.internal.persistence.subscription.DataStoreEvent.Action;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class SubscriptionHandler {
    private final Action mAction;
    private final Method mMethod;
    private final Object mTarget;
    private final String mType;

    SubscriptionHandler(Object target, Method method, Action action, String type) {
        this.mTarget = target;
        this.mMethod = method;
        this.mAction = action;
        this.mType = type;
    }

    public boolean matches(Action action, String type) {
        return this.mAction.equals(action) && this.mType.equals(type);
    }

    public void handle(DataStoreEvent event) throws UpsightException {
        ReflectiveOperationException e;
        try {
            this.mMethod.invoke(this.mTarget, new Object[]{event.source});
        } catch (InvocationTargetException e2) {
            e = e2;
            throw new UpsightException(e, "Failed to invoke subscription method %s.%s: ", this.mTarget.getClass(), this.mMethod.getName());
        } catch (IllegalAccessException e3) {
            e = e3;
            throw new UpsightException(e, "Failed to invoke subscription method %s.%s: ", this.mTarget.getClass(), this.mMethod.getName());
        }
    }
}
