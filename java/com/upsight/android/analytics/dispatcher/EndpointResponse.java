package com.upsight.android.analytics.dispatcher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.dispatcher.response")
public final class EndpointResponse {
    @SerializedName("id")
    @Expose
    @UpsightStorableIdentifier
    String id;
    @SerializedName("content")
    @Expose
    private String mContent;
    @SerializedName("type")
    @Expose
    private String mType;

    public static EndpointResponse create(String type, String content) {
        return new EndpointResponse(type, content);
    }

    EndpointResponse() {
    }

    EndpointResponse(String type, String content) {
        this.mType = type;
        this.mContent = content;
    }

    public String getContent() {
        return this.mContent;
    }

    public String getType() {
        return this.mType;
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
        EndpointResponse that = (EndpointResponse) o;
        if (this.id == null || that.id == null || !this.id.equals(that.id)) {
            return false;
        }
        return true;
    }
}
