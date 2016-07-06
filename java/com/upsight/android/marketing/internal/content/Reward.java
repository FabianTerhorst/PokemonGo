package com.upsight.android.marketing.internal.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upsight.android.analytics.internal.util.JacksonHelper.JSONObjectSerializer;
import com.upsight.android.marketing.UpsightReward;
import java.io.IOException;
import org.json.JSONObject;

public final class Reward implements UpsightReward {
    @JsonProperty("product")
    String product;
    @JsonProperty("quantity")
    int quantity;
    @JsonProperty("signature_data")
    ObjectNode signatureData;

    static UpsightReward from(JsonNode json, ObjectMapper mapper) throws IOException {
        return (UpsightReward) mapper.treeToValue(json, Reward.class);
    }

    Reward() {
    }

    public String getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public JSONObject getSignatureData() {
        return JSONObjectSerializer.fromObjectNode(this.signatureData);
    }
}
