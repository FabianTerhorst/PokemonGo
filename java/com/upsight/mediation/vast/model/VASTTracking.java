package com.upsight.mediation.vast.model;

import com.upsight.mediation.vast.util.Assets;

public class VASTTracking {
    private boolean consumed;
    private TRACKING_EVENTS_TYPE event;
    private String offset;
    private boolean offsetRelative;
    private long parsedOffset;
    private String value;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TRACKING_EVENTS_TYPE getEvent() {
        return this.event;
    }

    public void setEvent(TRACKING_EVENTS_TYPE event) {
        this.event = event;
    }

    public void setOffset(String offset) {
        this.offset = offset;
        if (offset.endsWith("%")) {
            this.parsedOffset = Long.parseLong(offset.substring(0, offset.indexOf("%")));
            this.offsetRelative = true;
            return;
        }
        this.parsedOffset = Assets.parseOffset(offset);
        this.offsetRelative = false;
    }

    public long getParsedOffset() {
        return this.parsedOffset;
    }

    public boolean isOffsetRelative() {
        return this.offsetRelative;
    }

    public boolean isConsumed() {
        return this.consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public String toString() {
        return "Tracking [event=" + this.event + ", value=" + this.value + " offset=" + this.offset + "]";
    }
}
