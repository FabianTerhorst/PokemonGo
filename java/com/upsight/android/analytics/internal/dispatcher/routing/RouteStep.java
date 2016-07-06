package com.upsight.android.analytics.internal.dispatcher.routing;

import com.upsight.android.analytics.internal.dispatcher.delivery.Queue;

class RouteStep {
    private String mFailureReason;
    private Queue mQueue;

    public RouteStep(RouteStep step) {
        this(step.mQueue);
    }

    public RouteStep(Queue queue) {
        this.mQueue = queue;
    }

    public Queue getQueue() {
        return this.mQueue;
    }

    public String getFailureReason() {
        return this.mFailureReason;
    }

    public void setFailureReason(String failureReason) {
        this.mFailureReason = failureReason;
    }
}
