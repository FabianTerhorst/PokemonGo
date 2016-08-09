package com.upsight.android.analytics.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.configuration")
public final class UpsightConfiguration {
    @SerializedName("id")
    @Expose
    @UpsightStorableIdentifier
    String id;
    @SerializedName("scope")
    @Expose
    private String mScope;
    @SerializedName("session_num_created")
    @Expose
    private int mSessionNumCreated;
    @SerializedName("value")
    @Expose
    private String mValue;

    public static UpsightConfiguration create(String type, String configuration, int currentSessionNum) {
        return new UpsightConfiguration(type, configuration, currentSessionNum);
    }

    UpsightConfiguration(String scope, String configuration, int sessionNumCreated) {
        this.mScope = scope;
        this.mValue = configuration;
        this.mSessionNumCreated = sessionNumCreated;
    }

    UpsightConfiguration() {
    }

    public String getId() {
        return this.id;
    }

    public String getScope() {
        return this.mScope;
    }

    public String getConfiguration() {
        return this.mValue;
    }

    public int getSessionNumberCreated() {
        return this.mSessionNumCreated;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpsightConfiguration that = (UpsightConfiguration) o;
        if (this.id == null || that.id == null || !this.id.equals(that.id)) {
            return false;
        }
        return true;
    }
}
