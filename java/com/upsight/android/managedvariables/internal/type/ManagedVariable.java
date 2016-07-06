package com.upsight.android.managedvariables.internal.type;

import java.util.Observable;

public abstract class ManagedVariable<T> extends Observable {
    private T mDefaultValue;
    private String mTag;
    private T mValue;

    protected ManagedVariable(String tag, T defaultValue, T value) {
        this.mTag = tag;
        this.mDefaultValue = defaultValue;
        if (value == null) {
            value = defaultValue;
        }
        this.mValue = value;
    }

    public String getTag() {
        return this.mTag;
    }

    public synchronized T get() {
        return this.mValue;
    }

    synchronized void set(T value) {
        if (value != this.mValue) {
            if (value != null) {
                this.mValue = value;
            } else {
                this.mValue = this.mDefaultValue;
            }
            setChanged();
            notifyObservers();
        }
    }
}
