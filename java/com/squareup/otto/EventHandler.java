package com.squareup.otto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class EventHandler {
    private final int hashCode;
    private final Method method;
    private final Object target;
    private boolean valid = true;

    EventHandler(Object target, Method method) {
        if (target == null) {
            throw new NullPointerException("EventHandler target cannot be null.");
        } else if (method == null) {
            throw new NullPointerException("EventHandler method cannot be null.");
        } else {
            this.target = target;
            this.method = method;
            method.setAccessible(true);
            this.hashCode = ((method.hashCode() + 31) * 31) + target.hashCode();
        }
    }

    public boolean isValid() {
        return this.valid;
    }

    public void invalidate() {
        this.valid = false;
    }

    public void handleEvent(Object event) throws InvocationTargetException {
        if (this.valid) {
            try {
                this.method.invoke(this.target, new Object[]{event});
                return;
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException e2) {
                if (e2.getCause() instanceof Error) {
                    throw ((Error) e2.getCause());
                }
                throw e2;
            }
        }
        throw new IllegalStateException(toString() + " has been invalidated and can no longer handle events.");
    }

    public String toString() {
        return "[EventHandler " + this.method + "]";
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EventHandler other = (EventHandler) obj;
        if (this.method.equals(other.method) && this.target == other.target) {
            return true;
        }
        return false;
    }
}
