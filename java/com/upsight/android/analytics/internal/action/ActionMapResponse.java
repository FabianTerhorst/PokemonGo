package com.upsight.android.analytics.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.action_map")
public final class ActionMapResponse {
    @SerializedName("action_factory")
    @Expose
    String actionFactory;
    @SerializedName("action_map")
    @Expose
    JsonArray actionMap;
    @SerializedName("id")
    @Expose
    String actionMapId;
    @UpsightStorableIdentifier
    String id;

    public String getActionMapId() {
        return this.actionMapId;
    }

    public String getActionFactory() {
        return this.actionFactory;
    }

    public JsonArray getActionMap() {
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
