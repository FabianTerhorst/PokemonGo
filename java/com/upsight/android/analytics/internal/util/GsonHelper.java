package com.upsight.android.analytics.internal.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface GsonHelper {

    public static class JSONArraySerializer {
        private static JsonParser sJsonParser = new JsonParser();

        public static JSONArray fromJsonArray(JsonArray arrayNode) {
            if (arrayNode == null) {
                return null;
            }
            try {
                return new JSONArray(arrayNode.toString());
            } catch (JSONException e) {
                return null;
            }
        }

        public static JsonArray toJsonArray(JSONArray jsonArray) {
            JsonArray arrayNode = null;
            try {
                arrayNode = sJsonParser.parse(jsonArray.toString()).getAsJsonArray();
            } catch (JsonParseException e) {
            }
            return arrayNode;
        }
    }

    public static class JSONObjectSerializer {
        private static JsonParser sJsonParser = new JsonParser();

        public static JSONObject fromJsonObject(JsonObject objectNode) {
            if (objectNode == null) {
                return null;
            }
            try {
                return new JSONObject(objectNode.toString());
            } catch (JSONException e) {
                return null;
            }
        }

        public static JsonObject toJsonObject(JSONObject jsonObject) {
            JsonObject objectNode = null;
            if (jsonObject != null) {
                try {
                    objectNode = sJsonParser.parse(jsonObject.toString()).getAsJsonObject();
                } catch (JsonParseException e) {
                }
            }
            return objectNode;
        }
    }
}
