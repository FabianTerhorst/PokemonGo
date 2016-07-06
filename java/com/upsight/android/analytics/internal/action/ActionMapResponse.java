package com.upsight.android.analytics.internal.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.action_map")
public final class ActionMapResponse {
    @JsonProperty("action_factory")
    String actionFactory;
    @JsonProperty("action_map")
    JsonNode actionMap;
    @JsonProperty("id")
    String actionMapId;
    @UpsightStorableIdentifier
    String id;

    public String getActionMapId() {
        return this.actionMapId;
    }

    public String getActionFactory() {
        return this.actionFactory;
    }

    public JsonNode getActionMap() {
        return this.actionMap;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionMapResponse that = (ActionMapResponse) o;
        if (this.id != null) {
            if (this.id.equals(that.id)) {
                return true;
            }
        } else if (that.id == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}
