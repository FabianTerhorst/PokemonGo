package com.upsight.android.analytics.internal.association;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import java.io.IOException;

@UpsightStorableType("upsight.association")
public class Association {
    @Expose
    @UpsightStorableIdentifier
    String id;
    @SerializedName("timestamp_ms")
    @Expose
    long timestampMs;
    @SerializedName("upsight_data")
    @Expose
    JsonObject upsightData;
    @SerializedName("upsight_data_filter")
    @Expose
    UpsightDataFilter upsightDataFilter;
    @SerializedName("with")
    @Expose
    String with;

    public static class UpsightDataFilter {
        @SerializedName("match_key")
        @Expose
        String matchKey;
        @SerializedName("match_values")
        @Expose
        JsonArray matchValues;

        public String getMatchKey() {
            return this.matchKey;
        }

        public JsonArray getMatchValues() {
            return this.matchValues;
        }
    }

    public static Association from(String with, JsonObject upsightDataFilter, JsonObject upsightData, Gson gson, Clock clock) throws IllegalArgumentException, IOException {
        if (TextUtils.isEmpty(with) || upsightDataFilter == null || upsightData == null) {
            throw new IllegalArgumentException("Illegal arguments");
        }
        try {
            return new Association(with, (UpsightDataFilter) gson.fromJson(upsightDataFilter, UpsightDataFilter.class), upsightData, clock.currentTimeMillis());
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
    }

    private Association(String with, UpsightDataFilter upsightDataFilter, JsonObject upsightData, long timestampMs) {
        this.with = with;
        this.upsightDataFilter = upsightDataFilter;
        this.upsightData = upsightData;
        this.timestampMs = timestampMs;
    }

    Association() {
    }

    public String getId() {
        return this.id;
    }

    public long getTimestampMs() {
        return this.timestampMs;
    }

    public String getWith() {
        return this.with;
    }

    public UpsightDataFilter getUpsightDataFilter() {
        return this.upsightDataFilter;
    }

    public JsonObject getUpsightData() {
        return this.upsightData;
    }
}
