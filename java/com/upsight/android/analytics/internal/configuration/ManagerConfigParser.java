package com.upsight.android.analytics.internal.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.internal.configuration.ConfigurationManager.Config;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class ManagerConfigParser {
    private Gson mGson;

    public static class ConfigJson {
        @SerializedName("requestInterval")
        @Expose
        public int requestInterval;
    }

    @Inject
    ManagerConfigParser(@Named("config-gson") Gson gson) {
        this.mGson = gson;
    }

    public Config parse(String configJson) throws IOException {
        try {
            return new Config(TimeUnit.SECONDS.toMillis((long) ((ConfigJson) this.mGson.fromJson(configJson, ConfigJson.class)).requestInterval));
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
    }
}
