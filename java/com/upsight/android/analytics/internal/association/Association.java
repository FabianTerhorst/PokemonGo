package com.upsight.android.analytics.internal.association;

import android.text.TextUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.association")
public class Association {
    @UpsightStorableIdentifier
    String id;
    @JsonProperty("timestamp_ms")
    long timestampMs;
    @JsonProperty("upsight_data")
    ObjectNode upsightData;
    @JsonProperty("upsight_data_filter")
    UpsightDataFilter upsightDataFilter;
    @JsonProperty("with")
    String with;

    public static class UpsightDataFilter {
        @JsonProperty("match_key")
        String matchKey;
        @JsonProperty("match_values")
        ArrayNode matchValues;

        public String getMatchKey() {
            return this.matchKey;
        }

        public ArrayNode getMatchValues() {
            return this.matchValues;
        }
    }

    public static Association from(String with, ObjectNode upsightDataFilter, ObjectNode upsightData, ObjectMapper mapper, Clock clock) throws IllegalArgumentException, JsonProcessingException {
        if (TextUtils.isEmpty(with) || upsightDataFilter == null || upsightData == null) {
            throw new IllegalArgumentException("Illegal arguments");
        }
        return new Association(with, (UpsightDataFilter) mapper.treeToValue(upsightDataFilter, UpsightDataFilter.class), upsightData, clock.currentTimeMillis());
    }

    private Association(String with, UpsightDataFilter upsightDataFilter, ObjectNode upsightData, long timestampMs) {
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

    public ObjectNode getUpsightData() {
        return this.upsightData;
    }
}
