package com.upsight.android.analytics.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.datastore.record")
public final class DataStoreRecord {
    @JsonProperty("action")
    Action action;
    @JsonProperty("campaign_id")
    Integer campaignID;
    @UpsightStorableIdentifier
    @JsonProperty
    String id;
    @JsonProperty("identifiers")
    String identifiers;
    @JsonProperty("message_id")
    Integer messageID;
    @JsonProperty("past_session_time")
    long pastSessionTime;
    @JsonProperty("session_id")
    long sessionID;
    @JsonProperty("session_num")
    int sessionNumber;
    @JsonProperty("source")
    String source;
    @JsonProperty("source_type")
    String sourceType;

    public enum Action {
        Created,
        Updated,
        Removed
    }

    public static DataStoreRecord create(Action action, long sessionID, Integer messageID, Integer campaignID, int sessionNumber, long pastSessionTime, String source, String sourceType) {
        return new DataStoreRecord(action, sessionID, messageID, campaignID, sessionNumber, pastSessionTime, source, sourceType);
    }

    public static DataStoreRecord create(Action action, long sessionID, int sessionNumber, long pastSessionTime, String source, String sourceType) {
        return create(action, sessionID, null, null, sessionNumber, pastSessionTime, source, sourceType);
    }

    DataStoreRecord() {
    }

    private DataStoreRecord(Action action, long sessionID, Integer messageID, Integer campaignID, int sessionNumber, long pastSessionTime, String source, String sourceType) {
        this.action = action;
        this.sessionID = sessionID;
        this.messageID = messageID;
        this.campaignID = campaignID;
        this.source = source;
        this.sourceType = sourceType;
        this.sessionNumber = sessionNumber;
        this.pastSessionTime = pastSessionTime;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataStoreRecord that = (DataStoreRecord) o;
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

    public String getID() {
        return this.id;
    }

    public long getSessionID() {
        return this.sessionID;
    }

    public Integer getMessageID() {
        return this.messageID;
    }

    public Integer getCampaignID() {
        return this.campaignID;
    }

    public int getSessionNumber() {
        return this.sessionNumber;
    }

    public long getPastSessionTime() {
        return this.pastSessionTime;
    }

    public Action getAction() {
        return this.action;
    }

    public String getSource() {
        return this.source;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }

    public String getIdentifiers() {
        return this.identifiers;
    }
}
