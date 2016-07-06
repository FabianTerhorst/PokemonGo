package com.upsight.android.analytics.internal.dispatcher.routing;

import com.upsight.android.analytics.internal.DataStoreRecord;

public class Packet {
    private final DataStoreRecord mEvent;
    private final Route mRoute;
    private State mState = State.UNDELIVERED;
    private boolean mTriedOnCurrentStep;

    public enum State {
        UNDELIVERED,
        DELIVERED,
        TRASHED
    }

    Packet(DataStoreRecord event, Route route) {
        this.mEvent = event;
        this.mRoute = route;
    }

    public DataStoreRecord getRecord() {
        return this.mEvent;
    }

    public void markDelivered() {
        this.mState = State.DELIVERED;
    }

    public void markTrashed() {
        this.mState = State.TRASHED;
    }

    public void failAndRoute(String reason) {
        if (State.DELIVERED.equals(this.mState)) {
            throw new IllegalStateException("Packet could not be failed because it was already delivered successfully");
        }
        this.mRoute.failedOnCurrentStep(reason);
        if (this.mRoute.hasMoreSteps()) {
            this.mTriedOnCurrentStep = false;
            this.mRoute.moveToNextStep();
        }
    }

    public State getState() {
        return this.mState;
    }

    public boolean hasMoreOptionsToTry() {
        return this.mRoute.hasMoreSteps() || !this.mTriedOnCurrentStep;
    }

    public void routeToNext() {
        this.mTriedOnCurrentStep = true;
        this.mRoute.getCurrentQueue().enqueuePacket(this);
    }

    public String getDeliveryHistory() {
        String[] stack = this.mRoute.getRoutingStack();
        StringBuilder builder = new StringBuilder();
        for (String stackItem : stack) {
            builder.append(stackItem).append('\n');
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }
}
