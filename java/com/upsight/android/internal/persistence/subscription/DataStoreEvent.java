package com.upsight.android.internal.persistence.subscription;

class DataStoreEvent {
    public final Action action;
    public final Object source;
    public final String sourceType;

    public enum Action {
        Created,
        Updated,
        Removed
    }

    DataStoreEvent(Action action, String sourceType, Object source) {
        this.action = action;
        this.sourceType = sourceType;
        this.source = source;
    }
}
