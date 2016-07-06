package com.upsight.android.internal.util;

public class Opt<T> {
    private T mObject;

    public static <T> Opt<T> absent() {
        return new Opt(null);
    }

    public static <T> Opt<T> from(T object) {
        return new Opt(object);
    }

    private Opt(T object) {
        this.mObject = object;
    }

    public boolean isPresent() {
        return this.mObject != null;
    }

    public T get() {
        return this.mObject;
    }
}
