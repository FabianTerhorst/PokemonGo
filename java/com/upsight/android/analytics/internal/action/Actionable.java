package com.upsight.android.analytics.internal.action;

import com.squareup.otto.Bus;

public abstract class Actionable {
    private ActionMap mActionMap;
    private String mId;

    public static class ActionMapFinishedEvent {
        public final String mId;

        private ActionMapFinishedEvent(String id) {
            this.mId = id;
        }
    }

    protected <T extends Actionable, U extends ActionContext> Actionable(String id, ActionMap<T, U> actionMap) {
        this.mId = id;
        this.mActionMap = actionMap;
    }

    private Actionable() {
    }

    public String getId() {
        return this.mId;
    }

    public void executeActions(String trigger) {
        this.mActionMap.executeActions(trigger, this);
    }

    public void signalActionCompleted(Bus bus) {
        if (this.mActionMap.signalActionCompleted()) {
            bus.post(new ActionMapFinishedEvent(this.mId));
        }
    }

    public void signalActionMapCompleted(Bus bus) {
        if (this.mActionMap.signalActionMapCompleted()) {
            bus.post(new ActionMapFinishedEvent(this.mId));
        }
    }
}
