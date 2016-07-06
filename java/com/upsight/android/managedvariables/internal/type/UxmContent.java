package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.analytics.internal.action.ActionMap;
import com.upsight.android.analytics.internal.action.Actionable;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionContext;

public class UxmContent extends Actionable {
    public static final String PREFERENCES_KEY_UXM_BUNDLE_ID = "uxmBundleId";
    public static final String TRIGGER_CONTENT_RECEIVED = "content_received";
    private boolean mShouldApplyBundle;

    public static UxmContent create(String id, ActionMap<UxmContent, UxmContentActionContext> actionMap, boolean shouldApplyBundle) {
        return new UxmContent(id, actionMap, shouldApplyBundle);
    }

    private UxmContent(String id, ActionMap<UxmContent, UxmContentActionContext> actionMap, boolean shouldApplyBundle) {
        super(id, actionMap);
        this.mShouldApplyBundle = shouldApplyBundle;
    }

    public boolean shouldApplyBundle() {
        return this.mShouldApplyBundle;
    }
}
