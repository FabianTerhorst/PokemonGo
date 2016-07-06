package com.upsight.android.analytics.internal.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.upsight.android.UpsightException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ActionMap<T extends Actionable, U extends ActionContext> extends HashMap<String, List<Action<T, U>>> {
    private static final String ACTIONS = "actions";
    private static final String TAG = ActionMap.class.getSimpleName();
    private static final String TRIGGER = "trigger";
    private int mActiveActionCount = 0;
    private boolean mIsActionMapCompleted = false;

    public ActionMap(ActionFactory<T, U> actionFactory, U actionContext, JsonNode actionMapJSON) {
        if (actionMapJSON != null && actionMapJSON.isArray()) {
            Iterator<JsonNode> triggerItr = actionMapJSON.elements();
            while (triggerItr.hasNext()) {
                JsonNode triggerObject = (JsonNode) triggerItr.next();
                JsonNode trigger = triggerObject.get(TRIGGER);
                JsonNode actionArray = triggerObject.get(ACTIONS);
                if (trigger != null && trigger.isTextual() && actionArray != null && actionArray.isArray()) {
                    int numActions = actionArray.size();
                    if (numActions > 0) {
                        List<Action<T, U>> actions = new ArrayList(numActions);
                        for (int i = 0; i < numActions; i++) {
                            JsonNode actionJSON = null;
                            try {
                                actionJSON = actionArray.get(i);
                                actions.add(actionFactory.create(actionContext, actionJSON));
                            } catch (UpsightException e) {
                                actionContext.mLogger.e(TAG, e, "Unable to create action from actionJSON=" + actionJSON, new Object[0]);
                            }
                        }
                        if (actions.size() > 0) {
                            put(trigger.asText(), actions);
                        }
                    }
                }
            }
        }
    }

    public synchronized void executeActions(String trigger, T object) {
        List<Action<T, U>> actions = (List) get(trigger);
        if (actions != null) {
            for (Action<T, U> action : actions) {
                this.mActiveActionCount++;
                action.execute(object);
            }
        }
    }

    public synchronized boolean signalActionCompleted() {
        this.mActiveActionCount--;
        return isFinished();
    }

    public synchronized boolean signalActionMapCompleted() {
        this.mIsActionMapCompleted = true;
        return isFinished();
    }

    private boolean isFinished() {
        return this.mIsActionMapCompleted && this.mActiveActionCount == 0;
    }
}
