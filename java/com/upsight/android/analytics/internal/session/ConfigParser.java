package com.upsight.android.analytics.internal.session;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.internal.session.SessionManagerImpl.Config;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;

class ConfigParser {
    private Gson mGson;

    public static class ConfigJson {
        @SerializedName("session_gap")
        @Expose
        public int session_gap;
    }

    @Inject
    public ConfigParser(@Named("config-gson") Gson gson) {
        this.mGson = gson;
    }

    public Config parseConfig(String configContent) throws IOException {
        try {
            return new Config((long) ((ConfigJson) this.mGson.fromJson(configContent, ConfigJson.class)).session_gap);
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
    }
}
