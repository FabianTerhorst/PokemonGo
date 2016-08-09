package com.upsight.android.analytics.event;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Map.Entry;

@JsonAdapter(DefaultTypeAdapter.class)
public class UpsightPublisherData {
    private final JsonObject mDataMap;

    public static class Builder {
        private final JsonObject mDataMap;

        public Builder() {
            this.mDataMap = new JsonObject();
        }

        Builder(JsonObject objectNode) {
            this.mDataMap = objectNode;
        }

        public Builder put(String key, boolean value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.addProperty(key, Boolean.valueOf(value));
            }
            return this;
        }

        public Builder put(String key, int value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.addProperty(key, Integer.valueOf(value));
            }
            return this;
        }

        public Builder put(String key, long value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.addProperty(key, Long.valueOf(value));
            }
            return this;
        }

        public Builder put(String key, float value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.addProperty(key, Float.valueOf(value));
            }
            return this;
        }

        public Builder put(String key, double value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.addProperty(key, Double.valueOf(value));
            }
            return this;
        }

        public Builder put(String key, char value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.addProperty(key, String.valueOf(value));
            }
            return this;
        }

        public Builder put(String key, CharSequence value) {
            if (!(TextUtils.isEmpty(key) || value == null)) {
                this.mDataMap.addProperty(key, value.toString());
            }
            return this;
        }

        public Builder put(UpsightPublisherData data) {
            if (data != null) {
                for (Entry<String, JsonElement> field : data.mDataMap.entrySet()) {
                    this.mDataMap.add((String) field.getKey(), (JsonElement) field.getValue());
                }
            }
            return this;
        }

        public UpsightPublisherData build() {
            return new UpsightPublisherData();
        }
    }

    public static final class DefaultTypeAdapter extends TypeAdapter<UpsightPublisherData> {
        private static final JsonParser JSON_PARSER = new JsonParser();

        public void write(JsonWriter out, UpsightPublisherData value) throws IOException {
            Streams.write(value.mDataMap, out);
        }

        public UpsightPublisherData read(JsonReader in) throws IOException {
            return new Builder(JSON_PARSER.parse(in).getAsJsonObject()).build();
        }
    }

    private UpsightPublisherData(Builder builder) {
        this.mDataMap = builder.mDataMap;
    }

    public String getData(String key) {
        return this.mDataMap.get(key).toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpsightPublisherData eventData = (UpsightPublisherData) o;
        if (this.mDataMap != null) {
            if (this.mDataMap.equals(eventData.mDataMap)) {
                return true;
            }
        } else if (eventData.mDataMap == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.mDataMap != null ? this.mDataMap.hashCode() : 0;
    }
}
