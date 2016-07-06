package com.upsight.android.analytics.internal.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Action<T extends Actionable, U extends ActionContext> {
    private U mActionContext;
    private JsonNode mParams;
    private String mType;

    public abstract void execute(T t);

    protected Action(U actionContext, String type, JsonNode params) {
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
            JsonNode jsonNode = this.mParams.get(key);
            if (jsonNode != null && jsonNode.isTextual()) {
                return jsonNode.asText();
            }
        }
        return null;
    }

    protected int optParamInt(String key) {
        if (this.mParams != null) {
            JsonNode jsonNode = this.mParams.get(key);
            if (jsonNode != null && jsonNode.isInt()) {
                return jsonNode.asInt();
            }
        }
        return 0;
    }

    protected ObjectNode optParamJsonObject(String key) {
        if (this.mParams != null) {
            JsonNode jsonObject = this.mParams.get(key);
            if (jsonObject != null && jsonObject.isObject()) {
                return (ObjectNode) jsonObject;
            }
        }
        return null;
    }

    protected ArrayNode optParamJsonArray(String key) {
        if (this.mParams != null) {
            JsonNode jsonArray = this.mParams.get(key);
            if (jsonArray != null && jsonArray.isArray()) {
                return (ArrayNode) jsonArray;
            }
        }
        return null;
    }
}
