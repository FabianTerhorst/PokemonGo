package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.text.TextUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.upsight.android.analytics.internal.DataStoreRecord;
import java.io.IOException;

class Session {
    private static final String EVENTS = "events";
    private static final String INSTALL_TS = "install_ts";
    private static final String MSG_CAMPAIGN_ID = "msg_campaign_id";
    private static final String MSG_ID = "msg_id";
    private static final String PAST_SESSION_TIME = "past_session_time";
    private static final String SESSION_NUM = "session_num";
    private static final String SESSION_START = "session_start";
    @SerializedName("events")
    @Expose
    private JsonArray mEvents = new JsonArray();
    @SerializedName("install_ts")
    @Expose
    private long mInstallTs;
    @SerializedName("msg_campaign_id")
    @Expose
    private Integer mMsgCampaignId;
    @SerializedName("msg_id")
    @Expose
    private Integer mMsgId;
    @SerializedName("past_session_time")
    @Expose
    private long mPastSessionTime;
    @SerializedName("session_num")
    @Expose
    private int mSessionNum;
    @SerializedName("session_start")
    @Expose
    private long mSessionStart;

    static final class DefaultTypeAdapter extends TypeAdapter<Session> {
        DefaultTypeAdapter() {
        }

        public void write(JsonWriter out, Session value) throws IOException {
            out.beginObject();
            out.name(Session.SESSION_NUM).value((long) value.mSessionNum);
            out.name(Session.SESSION_START).value(value.mSessionStart);
            out.name(Session.PAST_SESSION_TIME).value(value.mPastSessionTime);
            out.name(Session.MSG_ID).value(value.mMsgId);
            out.name(Session.MSG_CAMPAIGN_ID).value(value.mMsgCampaignId);
            out.name(Session.INSTALL_TS).value(value.mInstallTs);
            out.name(Session.EVENTS);
            out.setSerializeNulls(true);
            Streams.write(value.mEvents, out);
            out.setSerializeNulls(false);
            out.endObject();
        }

        public Session read(JsonReader in) throws IOException {
            throw new IllegalStateException(getClass().getSimpleName() + " does not implement read().");
        }
    }

    public Session(DataStoreRecord record, long installTs) {
        this.mSessionStart = record.getSessionID();
        this.mInstallTs = installTs;
        this.mMsgId = record.getMessageID();
        this.mMsgCampaignId = record.getCampaignID();
        this.mPastSessionTime = record.getPastSessionTime();
        this.mSessionNum = record.getSessionNumber();
    }

    void addEvent(DataStoreRecord record, JsonParser jsonParser) {
        String eventString = record.getSource();
        if (!TextUtils.isEmpty(eventString)) {
            JsonElement event = jsonParser.parse(eventString);
            if (event != null && event.isJsonObject()) {
                this.mEvents.add(event.getAsJsonObject());
            }
        }
    }
}
