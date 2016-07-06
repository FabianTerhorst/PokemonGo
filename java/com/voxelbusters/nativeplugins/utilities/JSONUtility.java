package com.voxelbusters.nativeplugins.utilities;

import android.os.Bundle;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtility {
    public static JSONObject getJSONfromBundle(Bundle bundle) {
        JSONObject json = new JSONObject();
        for (String eachKey : bundle.keySet()) {
            try {
                json.put(eachKey, bundle.get(eachKey));
            } catch (JSONException e) {
                e.printStackTrace();
                Debug.error(CommonDefines.JSON_UTILS_TAG, "Exception while entering key " + eachKey);
            }
        }
        return json;
    }

    public static String[] getKeys(JSONObject jsonData) {
        JSONArray jsonArray = jsonData.names();
        String[] keys = new String[jsonArray.length()];
        int count = jsonArray.length();
        for (int i = 0; i < count; i++) {
            try {
                keys[i] = jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return keys;
    }

    public static JSONArray getJSONArray(String jsonArrayString) {
        try {
            return new JSONArray(jsonArrayString);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static JSONArray removeIndex(JSONArray jsonArray, int pos) {
        JSONArray newJsonArray = new JSONArray();
        int i = 0;
        while (i < jsonArray.length()) {
            try {
                if (i != pos) {
                    newJsonArray.put(jsonArray.get(i));
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newJsonArray;
    }

    public static int findString(JSONArray jsonArray, String stringToSearch) {
        for (int i = 0; i < jsonArray.length(); i++) {
            String str = null;
            try {
                str = jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (str != null && str.equals(stringToSearch)) {
                return i;
            }
        }
        return -1;
    }

    public static String getJSONString(HashMap dataMap) {
        return new JSONObject(dataMap).toString();
    }

    public static JSONObject getJSON(String jsonStr) {
        try {
            return new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
