package com.upsight.android.analytics.internal.dispatcher.routing;

import com.upsight.android.analytics.internal.dispatcher.delivery.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Route {
    private int mCurrentStepIndex;
    private List<RouteStep> mSteps;

    public Route(Route route) {
        this(route.mSteps);
    }

    public Route(List<RouteStep> steps) {
        this.mCurrentStepIndex = 0;
        this.mSteps = new ArrayList(steps.size());
        for (RouteStep step : steps) {
            this.mSteps.add(new RouteStep(step));
        }
    }

    public void failedOnCurrentStep(String reason) {
        ((RouteStep) this.mSteps.get(this.mCurrentStepIndex)).setFailureReason(reason);
    }

    public String[] getRoutingStack() {
        List<String> res = new LinkedList();
        for (RouteStep step : this.mSteps) {
            res.add(step.getQueue().getName() + " - " + (step.getFailureReason() == null ? "delivered" : step.getFailureReason()));
            if (step.getFailureReason() == null) {
                break;
            }
        }
        return (String[]) res.toArray(new String[res.size()]);
    }

    public Queue getCurrentQueue() {
        return ((RouteStep) this.mSteps.get(this.mCurrentStepIndex)).getQueue();
    }

    public int getStepsCount() {
        return this.mSteps.size();
    }

    public boolean hasMoreSteps() {
        return this.mCurrentStepIndex < this.mSteps.size() + -1;
    }

    public void moveToNextStep() {
        if (hasMoreSteps()) {
            this.mCurrentStepIndex++;
            return;
        }
        throw new IllegalStateException("There are no more steps to move on");
    }
}
