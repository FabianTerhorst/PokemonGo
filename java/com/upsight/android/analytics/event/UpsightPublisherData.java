package com.upsight.android.analytics.event;

import android.text.TextUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

@JsonDeserialize(using = Deserializer.class)
@JsonSerialize(using = Serializer.class)
public class UpsightPublisherData {
    private final ObjectNode mDataMap;

    public static class Builder {
        private static final ObjectMapper sObjectMapper = new ObjectMapper();
        private final ObjectNode mDataMap;

        public Builder() {
            this.mDataMap = sObjectMapper.createObjectNode();
        }

        Builder(ObjectNode objectNode) {
            this.mDataMap = objectNode;
        }

        public Builder put(String key, boolean value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.put(key, value);
            }
            return this;
        }

        public Builder put(String key, int value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.put(key, value);
            }
            return this;
        }

        public Builder put(String key, long value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.put(key, value);
            }
            return this;
        }

        public Builder put(String key, float value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.put(key, value);
            }
            return this;
        }

        public Builder put(String key, double value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.put(key, value);
            }
            return this;
        }

        public Builder put(String key, char value) {
            if (!TextUtils.isEmpty(key)) {
                this.mDataMap.put(key, String.valueOf(value));
            }
            return this;
        }

        public Builder put(String key, CharSequence value) {
            if (!(TextUtils.isEmpty(key) || value == null)) {
                this.mDataMap.put(key, value.toString());
            }
            return this;
        }

        public Builder put(UpsightPublisherData data) {
            if (data != null) {
                Iterator<Entry<String, JsonNode>> iterator = data.mDataMap.fields();
                while (iterator.hasNext()) {
                    Entry<String, JsonNode> field = (Entry) iterator.next();
                    this.mDataMap.replace((String) field.getKey(), (JsonNode) field.getValue());
                }
            }
            return this;
        }

        public UpsightPublisherData build() {
            return new UpsightPublisherData();
        }
    }

    public static class Deserializer extends JsonDeserializer<UpsightPublisherData> {
        public UpsightPublisherData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return new Builder((ObjectNode) jsonParser.readValueAs(ObjectNode.class)).build();
        }
    }

    public static final class Serializer extends JsonSerializer<UpsightPublisherData> {
        public void serialize(UpsightPublisherData data, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            serializerProvider.defaultSerializeValue(data.mDataMap, jsonGenerator);
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
