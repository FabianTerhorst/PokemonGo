package com.upsight.android.analytics.dispatcher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.dispatcher.delivery.status")
public final class AnalyticsEventDeliveryStatus {
    @SerializedName("id")
    @Expose
    @UpsightStorableIdentifier
    String id;
    @SerializedName("failure_reason")
    @Expose
    private String mFailureReason;
    @SerializedName("source_event_id")
    @Expose
    private String mOriginEventId;
    @SerializedName("status")
    @Expose
    private boolean mStatus;

    public static AnalyticsEventDeliveryStatus fromSuccess(String sourceEventId) {
        return new AnalyticsEventDeliveryStatus(sourceEventId, true, null);
    }

    public static AnalyticsEventDeliveryStatus fromFailure(String sourceEventId, String failureReason) {
        return new AnalyticsEventDeliveryStatus(sourceEventId, false, failureReason);
    }

    AnalyticsEventDeliveryStatus() {
    }

    AnalyticsEventDeliveryStatus(String sourceEventId, boolean wasDelivered, String failureReason) {
        this.mOriginEventId = sourceEventId;
        this.mStatus = wasDelivered;
        this.mFailureReason = failureReason;
    }

    public boolean wasDelivered() {
        return this.mStatus;
    }

    public String getSourceEventId() {
        return this.mOriginEventId;
    }

    public String getFailureReason() {
        return this.mFailureReason;
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
        AnalyticsEventDeliveryStatus that = (AnalyticsEventDeliveryStatus) o;
        if (this.id == null || that.id == null || !this.id.equals(that.id)) {
            return false;
        }
        return true;
    }
}
