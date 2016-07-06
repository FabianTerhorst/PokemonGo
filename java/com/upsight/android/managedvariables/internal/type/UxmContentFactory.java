package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.upsight.android.analytics.internal.action.ActionMap;
import com.upsight.android.analytics.internal.action.ActionMapResponse;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionContext;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionFactory;
import java.util.Iterator;

public final class UxmContentFactory {
    private static final String ACTION_MODIFY_VALUE = "action_modify_value";
    private static final String ACTION_SET_BUNDLE_ID = "action_set_bundle_id";
    private static final String KEY_ACTIONS = "actions";
    private static final String KEY_ACTION_TYPE = "action_type";
    private static final UxmContentActionFactory sUxmContentActionFactory = new UxmContentActionFactory();
    private UxmContentActionContext mActionContext;
    private UpsightUserExperience mUserExperience;

    public UxmContentFactory(UxmContentActionContext actionContext, UpsightUserExperience userExperience) {
        this.mActionContext = actionContext;
        this.mUserExperience = userExperience;
    }

    public UxmContent create(ActionMapResponse actionMapResponse) {
        String id = actionMapResponse.getActionMapId();
        if (TextUtils.isEmpty(id) || !UxmContentActionFactory.TYPE.equals(actionMapResponse.getActionFactory())) {
            return null;
        }
        boolean shouldApplyBundle = false;
        JsonNode actionMapNode = actionMapResponse.getActionMap();
        if (actionMapNode != null && actionMapNode.isArray()) {
            Iterator it = actionMapNode.iterator();
            while (it.hasNext()) {
                JsonNode actionsNode = ((JsonNode) it.next()).findPath(KEY_ACTIONS);
                if (actionsNode != null && actionsNode.isArray()) {
                    Iterator i$ = actionsNode.iterator();
                    while (i$.hasNext()) {
                        JsonNode typeNode = ((JsonNode) i$.next()).findPath(KEY_ACTION_TYPE);
                        if (!ACTION_SET_BUNDLE_ID.equals(typeNode.asText())) {
                            if (ACTION_MODIFY_VALUE.equals(typeNode.asText())) {
                            }
                        }
                        shouldApplyBundle = this.mUserExperience.getHandler().onReceive();
                        continue;
                    }
                    continue;
                }
                if (shouldApplyBundle) {
                    break;
                }
            }
        }
        return UxmContent.create(id, new ActionMap(sUxmContentActionFactory, this.mActionContext, actionMapNode), shouldApplyBundle);
    }
}
