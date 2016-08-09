package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.DataStoreRecord;
import com.upsight.android.analytics.internal.dispatcher.delivery.BatchSender.Request;
import com.upsight.android.analytics.internal.dispatcher.routing.Packet;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.internal.util.PreferencesHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@JsonAdapter(DefaultTypeAdapter.class)
class UpsightRequest {
    private long mInstallTs;
    private final JsonParser mJsonParser;
    private boolean mOptOut = UpsightOptOutStatus.get(this.mUpsight);
    private long mRequestTs;
    private Schema mSchema;
    private Session[] mSessions;
    private UpsightContext mUpsight;

    public static final class DefaultTypeAdapter extends TypeAdapter<UpsightRequest> {
        private static final Gson GSON = new Gson();
        private static final String IDENTIFIERS_KEY = "identifiers";
        private static final String LOCALE_KEY = "locale";
        private static final String OPT_OUT_KEY = "opt_out";
        private static final String REQUEST_TS_KEY = "request_ts";
        private static final String SESSIONS_KEY = "sessions";
        private static final TypeAdapter<Session> SESSION_TYPE_ADAPTER = new DefaultTypeAdapter();

        public void write(JsonWriter writer, UpsightRequest request) throws IOException {
            writer.beginObject();
            for (String key : request.mSchema.availableKeys()) {
                Object value = request.mSchema.getValueFor(key);
                if (value != null) {
                    writer.name(key);
                    Streams.write(GSON.toJsonTree(value), writer);
                }
            }
            writer.name(REQUEST_TS_KEY);
            writer.value(request.mRequestTs);
            writer.name(OPT_OUT_KEY);
            writer.value(request.mOptOut);
            Schema schema = request.mSchema;
            if (schema != null) {
                String nameString = schema.getName();
                if (!TextUtils.isEmpty(nameString)) {
                    writer.name(IDENTIFIERS_KEY);
                    writer.value(nameString);
                }
            }
            Locale locale = Locale.getDefault();
            if (locale != null) {
                String localeString = locale.toString();
                if (!TextUtils.isEmpty(localeString)) {
                    writer.name(LOCALE_KEY);
                    writer.value(localeString);
                }
            }
            writer.name(SESSIONS_KEY);
            writer.beginArray();
            for (Session session : request.mSessions) {
                SESSION_TYPE_ADAPTER.write(writer, session);
            }
            writer.endArray();
            writer.endObject();
        }

        public UpsightRequest read(JsonReader in) throws IOException {
            throw new IOException(UpsightRequest.class.getSimpleName() + " cannot be deserialized");
        }
    }

    public UpsightRequest(UpsightContext upsight, Request request, JsonParser jsonParser, Clock clock) {
        this.mUpsight = upsight;
        this.mJsonParser = jsonParser;
        this.mInstallTs = PreferencesHelper.getLong(upsight, PreferencesHelper.INSTALL_TIMESTAMP_NAME, 0);
        this.mSessions = getSessions(request.batch);
        this.mRequestTs = clock.currentTimeSeconds();
        this.mSchema = request.schema;
    }

    private Session[] getSessions(Batch batch) {
        Map<Long, Session> sessions = new HashMap();
        for (Packet packet : batch.getPackets()) {
            DataStoreRecord event = packet.getRecord();
            Session session = (Session) sessions.get(Long.valueOf(event.getSessionID()));
            if (session == null) {
                session = new Session(event, this.mInstallTs);
                sessions.put(Long.valueOf(event.getSessionID()), session);
            }
            session.addEvent(event, this.mJsonParser);
        }
        return (Session[]) sessions.values().toArray(new Session[sessions.values().size()]);
    }
}
