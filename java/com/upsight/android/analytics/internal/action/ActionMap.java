package com.upsight.android.analytics.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.upsight.android.UpsightException;
import com.upsight.android.logger.UpsightLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ActionMap<T extends Actionable, U extends ActionContext> extends HashMap<String, List<Action<T, U>>> {
    private static final String ACTIONS = "actions";
    private static final String LOG_TEMPLATE_ACTION = "  -> %1$s";
    private static final String LOG_TEMPLATE_TRIGGER = "%1$s on %2$s:";
    private static final String TAG = ActionMap.class.getSimpleName();
    private static final String TRIGGER = "trigger";
    private int mActiveActionCount = 0;
    private boolean mIsActionMapCompleted = false;
    private UpsightLogger mLogger;

    public ActionMap(ActionFactory<T, U> actionFactory, U actionContext, JsonArray actionMapJSON) {
        this.mLogger = actionContext.mLogger;
        if (actionMapJSON != null && actionMapJSON.isJsonArray()) {
            Iterator<JsonElement> triggerItr = actionMapJSON.getAsJsonArray().iterator();
            while (triggerItr.hasNext()) {
                JsonObject triggerObject = ((JsonElement) triggerItr.next()).getAsJsonObject();
                JsonElement trigger = triggerObject.get(TRIGGER);
                JsonElement actionArray = triggerObject.get(ACTIONS);
                if (trigger != null && trigger.isJsonPrimitive() && trigger.getAsJsonPrimitive().isString() && actionArray != null && actionArray.isJsonArray()) {
                    int numActions = actionArray.getAsJsonArray().size();
                    if (numActions > 0) {
                        List<Action<T, U>> actions = new ArrayList(numActions);
                        for (int i = 0; i < numActions; i++) {
                            JsonObject actionJSON = null;
                            try {
                                actionJSON = actionArray.getAsJsonArray().get(i).getAsJsonObject();
                                actions.add(actionFactory.create(actionContext, actionJSON));
                            } catch (UpsightException e) {
                                actionContext.mLogger.e(TAG, e, "Unable to create action from actionJSON=" + actionJSON, new Object[0]);
                            }
                        }
                        if (actions.size() > 0) {
                            put(trigger.getAsString(), actions);
                        }
                    }
                }
            }
        }
    }

    public synchronized void executeActions(String trigger, T object) {
        this.mLogger.i(TAG, LOG_TEMPLATE_TRIGGER, trigger, object.getId());
        List<Action<T, U>> actions = (List) get(trigger);
        if (actions != null) {
            for (Action<T, U> action : actions) {
                this.mLogger.i(TAG, LOG_TEMPLATE_ACTION, action.getType());
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
