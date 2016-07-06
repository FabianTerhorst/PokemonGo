package com.upsight.android.analytics.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.DynamicIdentifiers;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("com.upsight.dynamic")
public final class UpsightDynamicEvent extends UpsightAnalyticsEvent<JsonNode, JsonNode> implements DynamicIdentifiers {
    private String identifiers;

    public static final class Builder {
        private String identifiers;
        private JsonNode publisherData;
        private final String type;
        private JsonNode upsightData;

        private Builder(String type) {
            this.publisherData = JsonNodeFactory.instance.objectNode();
            this.upsightData = JsonNodeFactory.instance.objectNode();
            this.type = type;
        }

        public Builder setDynamicIdentifiers(String name) {
            this.identifiers = name;
            return this;
        }

        public Builder putPublisherData(JsonNode jsonNode) {
            this.publisherData = jsonNode.deepCopy();
            return this;
        }

        public Builder putUpsightData(JsonNode jsonNode) {
            this.upsightData = jsonNode.deepCopy();
            return this;
        }

        private UpsightDynamicEvent build() {
            return new UpsightDynamicEvent(this.type, this.identifiers, this.upsightData, this.publisherData);
        }

        public final UpsightDynamicEvent record(UpsightContext upsight) {
            UpsightDynamicEvent result = build();
            ((UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME)).getApi().record(result);
            return result;
        }
    }

    public static Builder createBuilder(String type) {
        return new Builder(type);
    }

    UpsightDynamicEvent(String type, String identifiers, JsonNode upsightData, JsonNode publisherData) {
        super(type, upsightData, publisherData);
        this.identifiers = identifiers;
    }

    public String getIdentifiersName() {
        return this.identifiers;
    }

    UpsightDynamicEvent() {
    }
}
