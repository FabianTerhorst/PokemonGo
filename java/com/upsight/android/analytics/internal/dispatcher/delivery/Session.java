package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upsight.android.analytics.internal.DataStoreRecord;
import com.upsight.android.logger.UpsightLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Session {
    private Set<DataStoreRecord> mEvents = new HashSet();
    private long mInstallTs;
    private final UpsightLogger mLogger;
    private Integer mMsgCampaignId;
    private Integer mMsgId;
    private final ObjectMapper mObjectMapper;
    private long mPastSessionTime;
    private int mSessionNum;
    private long mSessionStart;

    public Session(DataStoreRecord record, ObjectMapper mapper, UpsightLogger logger, long installTs) {
        this.mSessionStart = record.getSessionID();
        this.mObjectMapper = mapper;
        this.mLogger = logger;
        this.mInstallTs = installTs;
        this.mMsgId = record.getMessageID();
        this.mMsgCampaignId = record.getCampaignID();
        this.mPastSessionTime = record.getPastSessionTime();
        this.mSessionNum = record.getSessionNumber();
    }

    public void addEvent(DataStoreRecord event) {
        this.mEvents.add(event);
    }

    @JsonProperty("events")
    public ObjectNode[] getEvents() {
        List<ObjectNode> res = new ArrayList(this.mEvents.size());
        for (DataStoreRecord record : this.mEvents) {
            try {
                JsonNode jsonNode = this.mObjectMapper.readTree(record.getSource());
                if (jsonNode.isObject()) {
                    res.add((ObjectNode) jsonNode);
                }
            } catch (IOException e) {
                this.mLogger.e(getClass().getSimpleName(), e, "Error parsing JSON object.", new Object[0]);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return (ObjectNode[]) res.toArray(new ObjectNode[this.mEvents.size()]);
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    @JsonProperty("msg_id")
    public Integer getMsgId() {
        return this.mMsgId;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    @JsonProperty("msg_campaign_id")
    public Integer getMsgCampaignId() {
        return this.mMsgCampaignId;
    }

    @JsonProperty("past_session_time")
    public long getPastSessionTime() {
        return this.mPastSessionTime;
    }

    @JsonProperty("session_num")
    public int getSessionNum() {
        return this.mSessionNum;
    }

    @JsonProperty("session_start")
    public long getSessionStart() {
        return this.mSessionStart;
    }

    @JsonProperty("install_ts")
    public long getInstallTs() {
        return this.mInstallTs;
    }
}
