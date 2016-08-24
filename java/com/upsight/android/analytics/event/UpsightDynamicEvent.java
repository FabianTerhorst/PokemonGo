package com.upsight.android.analytics.event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.DynamicIdentifiers;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("com.upsight.dynamic")
public final class UpsightDynamicEvent extends UpsightAnalyticsEvent<JsonElement, JsonElement> implements DynamicIdentifiers {
    private String identifiers;

    public static final class Builder {
        private static final JsonParser JSON_PARSER = new JsonParser();
        private String identifiers;
        private JsonObject publisherData;
        private final String type;
        private JsonObject upsightData;

        private Builder(String type) {
            this.publisherData = new JsonObject();
            this.upsightData = new JsonObject();
            this.type = type;
        }

        public Builder setDynamicIdentifiers(String name) {
            this.identifiers = name;
            return this;
        }

        public Builder putPublisherData(JsonObject jsonNode) {
            this.publisherData = deepCopy(jsonNode);
            return this;
        }

        public Builder putUpsightData(JsonObject jsonNode) {
            this.upsightData = deepCopy(jsonNode);
            return this;
        }

        public final UpsightDynamicEvent record(UpsightContext upsight) {
            UpsightDynamicEvent result = build();
            UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
            if (extension != null) {
                extension.getApi().record(result);
            }
            return result;
        }

        private UpsightDynamicEvent build() {
            return new UpsightDynamicEvent(this.type, this.identifiers, this.upsightData, this.publisherData);
        }

        private JsonObject deepCopy(JsonObject source) {
            return JSON_PARSER.parse(source.toString()).getAsJsonObject();
        }
    }

    public static Builder createBuilder(String type) {
        return new Builder(type);
    }

    UpsightDynamicEvent(String type, String identifiers, JsonElement upsightData, JsonElement publisherData) {
        super(type, upsightData, publisherData);
        this.identifiers = identifiers;
    }

    public String getIdentifiersName() {
        return this.identifiers;
    }

    UpsightDynamicEvent() {
    }
}
