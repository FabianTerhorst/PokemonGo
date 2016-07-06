package com.upsight.android.analytics.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.configuration")
public final class UpsightConfiguration {
    @UpsightStorableIdentifier
    @JsonProperty("id")
    String id;
    @JsonProperty("scope")
    private String mScope;
    @JsonProperty("session_num_created")
    private int mSessionNumCreated;
    @JsonProperty("value")
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
