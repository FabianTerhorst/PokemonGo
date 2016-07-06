package com.upsight.android.analytics.internal.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface JacksonHelper {

    public static class JSONArraySerializer {
        private static ObjectMapper sMapper = new ObjectMapper();

        public static JSONArray fromArrayNode(ArrayNode arrayNode) {
            if (arrayNode == null) {
                return null;
            }
            try {
                return new JSONArray(arrayNode.toString());
            } catch (JSONException e) {
                return null;
            }
        }

        public static ArrayNode toArrayNode(JSONArray jsonArray) {
            ArrayNode arrayNode = null;
            try {
                return (ArrayNode) sMapper.readTree(jsonArray.toString());
            } catch (IOException e) {
                return arrayNode;
            }
        }
    }

    public static class JSONObjectSerializer {
        private static ObjectMapper sMapper = new ObjectMapper();

        public static JSONObject fromObjectNode(ObjectNode objectNode) {
            if (objectNode == null) {
                return null;
            }
            try {
                return new JSONObject(objectNode.toString());
            } catch (JSONException e) {
                return null;
            }
        }

        public static ObjectNode toObjectNode(JSONObject jsonObject) {
            ObjectNode objectNode = null;
            if (jsonObject == null) {
                return objectNode;
            }
            try {
                return (ObjectNode) sMapper.readTree(jsonObject.toString());
            } catch (IOException e) {
                return objectNode;
            }
        }
    }
}
