package com.upsight.android.analytics.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Action<T extends Actionable, U extends ActionContext> {
    private U mActionContext;
    private JsonObject mParams;
    private String mType;

    public abstract void execute(T t);

    protected Action(U actionContext, String type, JsonObject params) {
        this.mActionContext = actionContext;
        this.mType = type;
        this.mParams = params;
    }

    public U getActionContext() {
        return this.mActionContext;
    }

    public String getType() {
        return this.mType;
    }

    protected String optParamString(String key) {
        if (this.mParams != null) {
            JsonElement element = this.mParams.get(key);
            if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                return element.getAsString();
            }
        }
        return null;
    }

    protected int optParamInt(String key) {
        if (this.mParams != null) {
            JsonElement element = this.mParams.get(key);
            if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsInt();
            }
        }
        return 0;
    }

    protected JsonObject optParamJsonObject(String key) {
        if (this.mParams != null) {
            JsonElement element = this.mParams.get(key);
            if (element != null && element.isJsonObject()) {
                return element.getAsJsonObject();
            }
        }
        return null;
    }

    protected JsonArray optParamJsonArray(String key) {
        if (this.mParams != null) {
            JsonElement element = this.mParams.get(key);
            if (element != null && element.isJsonArray()) {
                return element.getAsJsonArray();
            }
        }
        return null;
    }
}
