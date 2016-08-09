package com.upsight.mediation.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.data.APIVersion;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HashMapCaster {
    private final HashMap<String, String> map;

    public HashMapCaster(HashMap<String, String> map) {
        this.map = map;
    }

    @Nullable
    public String get(String key) {
        return (String) this.map.get(key);
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        String val = (String) this.map.get(key);
        return (val == null || val.length() <= 0) ? defaultValue : Integer.parseInt((String) this.map.get(key));
    }

    public boolean getBool(String key) {
        String val = (String) this.map.get(key);
        return val != null ? val.equals("1") : false;
    }

    public int[] getIntArray(String key) {
        String[] sArray;
        String value = (String) this.map.get(key);
        if (value.length() > 0) {
            sArray = value.split(",");
        } else {
            sArray = new String[0];
        }
        int[] iArray = new int[sArray.length];
        for (int j = 0; j < iArray.length; j++) {
            iArray[j] = Integer.parseInt(sArray[j]);
        }
        return iArray;
    }

    public long getLong(String key) {
        String val = (String) this.map.get(key);
        return (val == null || val.length() <= 0) ? -1 : Long.parseLong(val);
    }

    @Nullable
    public APIVersion getAPIVersion(String key) {
        String val = (String) this.map.get(key);
        if (val == null || val.length() == 0) {
            return null;
        }
        return new APIVersion(val);
    }

    @NonNull
    public ArrayList<APIVersion> getAPIVersions(String key) {
        String[] sArray;
        int i = 0;
        ArrayList<APIVersion> versions = new ArrayList();
        String value = (String) this.map.get(key);
        if (value.length() > 0) {
            sArray = value.split(",");
        } else {
            sArray = new String[0];
        }
        int length = sArray.length;
        while (i < length) {
            versions.add(new APIVersion(sArray[i]));
            i++;
        }
        return versions;
    }

    public float getFloat(String key) {
        return getFloat(key, -1.0f);
    }

    @Nullable
    public Date getDate(String key) {
        String value = (String) this.map.get(key);
        if (value == null || value.length() == 0) {
            return null;
        }
        return new Date(1000 * Long.parseLong(value));
    }

    public float getFloat(String key, float defaultVal) {
        String value = (String) this.map.get(key);
        return (value == null || value.length() <= 0) ? defaultVal : Float.parseFloat(value);
    }
}
