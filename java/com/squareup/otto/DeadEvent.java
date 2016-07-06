package com.squareup.otto;

public class DeadEvent {
    public final Object event;
    public final Object source;

    public DeadEvent(Object source, Object event) {
        this.source = source;
        this.event = event;
    }
}
