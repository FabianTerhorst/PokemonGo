package com.upsight.android.analytics.internal.action;

import com.google.gson.JsonObject;
import com.upsight.android.UpsightException;

public interface ActionFactory<T extends Actionable, U extends ActionContext> {
    public static final String KEY_ACTION_PARAMS = "parameters";
    public static final String KEY_ACTION_TYPE = "action_type";

    Action<T, U> create(U u, JsonObject jsonObject) throws UpsightException;
}
