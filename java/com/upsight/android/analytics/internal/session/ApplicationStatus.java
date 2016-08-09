package com.upsight.android.analytics.internal.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.application.status")
public class ApplicationStatus {
    @SerializedName("id")
    @Expose
    @UpsightStorableIdentifier
    String id;
    @SerializedName("state")
    @Expose
    State state;

    public enum State {
        BACKGROUND,
        FOREGROUND
    }

    public ApplicationStatus(State state) {
        this.state = state;
    }

    ApplicationStatus() {
    }

    public State getState() {
        return this.state;
    }
}
