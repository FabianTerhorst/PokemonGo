package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.text.TextUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.DataStoreRecord;
import com.upsight.android.analytics.internal.dispatcher.delivery.BatchSender.Request;
import com.upsight.android.analytics.internal.dispatcher.routing.Packet;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@JsonSerialize(using = RequestSerializer.class)
class UpsightRequest {
    private long mInstallTs;
    private final UpsightLogger mLogger;
    private final ObjectMapper mObjectMapper;
    private boolean mOptOut = UpsightOptOutStatus.get(this.mUpsight);
    private long mRequestTs;
    private Schema mSchema;
    private Session[] mSessions;
    private UpsightContext mUpsight;

    static class RequestSerializer extends JsonSerializer<UpsightRequest> {
        private static final String IDENTIFIERS_KEY = "identifiers";
        private static final String LOCALE_KEY = "locale";
        private static final String OPT_OUT_KEY = "opt_out";
        private static final String REQUEST_TS_KEY = "request_ts";
        private static final String SESSIONS_KEY = "sessions";

        RequestSerializer() {
        }

        public void serialize(UpsightRequest request, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            for (String key : request.mSchema.availableKeys()) {
                Object value = request.mSchema.getValueFor(key);
                if (value != null) {
                    jsonGenerator.writeObjectField(key, value);
                }
            }
            jsonGenerator.writeObjectField(REQUEST_TS_KEY, Long.valueOf(request.mRequestTs));
            jsonGenerator.writeObjectField(OPT_OUT_KEY, Boolean.valueOf(request.mOptOut));
            Schema schema = request.mSchema;
            if (schema != null) {
                String nameString = schema.getName();
                if (!TextUtils.isEmpty(nameString)) {
                    jsonGenerator.writeObjectField(IDENTIFIERS_KEY, nameString);
                }
            }
            Locale locale = Locale.getDefault();
            if (locale != null) {
                String localeString = locale.toString();
                if (!TextUtils.isEmpty(localeString)) {
                    jsonGenerator.writeObjectField(LOCALE_KEY, localeString);
                }
            }
            jsonGenerator.writeArrayFieldStart(SESSIONS_KEY);
            for (Session session : request.mSessions) {
                serializerProvider.defaultSerializeValue(session, jsonGenerator);
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }

    public UpsightRequest(UpsightContext upsight, Request request, ObjectMapper objectMapper, Clock clock, UpsightLogger logger) {
        this.mUpsight = upsight;
        this.mObjectMapper = objectMapper;
        this.mLogger = logger;
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
                session = new Session(event, this.mObjectMapper, this.mLogger, this.mInstallTs);
                sessions.put(Long.valueOf(event.getSessionID()), session);
            }
            session.addEvent(event);
        }
        return (Session[]) sessions.values().toArray(new Session[sessions.values().size()]);
    }
}
