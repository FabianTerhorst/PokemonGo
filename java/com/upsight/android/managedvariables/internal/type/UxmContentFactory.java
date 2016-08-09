package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.JsonElement;
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
        JsonElement actionMapNode = actionMapResponse.getActionMap();
        if (actionMapNode == null || !actionMapNode.isJsonArray()) {
            return null;
        }
        Iterator it = actionMapNode.getAsJsonArray().iterator();
        while (it.hasNext()) {
            JsonElement actionsNode = ((JsonElement) it.next()).getAsJsonObject().get(KEY_ACTIONS);
            if (actionsNode != null && actionsNode.isJsonArray()) {
                Iterator it2 = actionsNode.getAsJsonArray().iterator();
                while (it2.hasNext()) {
                    JsonElement typeNode = ((JsonElement) it2.next()).getAsJsonObject().get(KEY_ACTION_TYPE);
                    if (!ACTION_SET_BUNDLE_ID.equals(typeNode.getAsString())) {
                        if (ACTION_MODIFY_VALUE.equals(typeNode.getAsString())) {
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
        return UxmContent.create(id, new ActionMap(sUxmContentActionFactory, this.mActionContext, actionMapNode.getAsJsonArray()), shouldApplyBundle);
    }
}
