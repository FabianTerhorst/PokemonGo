package com.upsight.android.analytics.internal.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.configuration.UpsightConfiguration;
import com.upsight.android.analytics.internal.session.SessionManager;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class ConfigurationResponseParser {
    private Gson mGson;
    private SessionManager mSessionManager;

    public static class ConfigJson {
        @SerializedName("configuration")
        @Expose
        public JsonElement configuration;
        @SerializedName("type")
        @Expose
        public String type;
    }

    public static class ConfigResponseJson {
        @SerializedName("configurationList")
        @Expose
        public ConfigJson[] configs;
    }

    @Inject
    ConfigurationResponseParser(@Named("config-gson") Gson gson, SessionManager sessionManager) {
        this.mGson = gson;
        this.mSessionManager = sessionManager;
    }

    public Collection<UpsightConfiguration> parse(String resposneJson) throws IOException {
        try {
            ConfigResponseJson rsp = (ConfigResponseJson) this.mGson.fromJson(resposneJson, ConfigResponseJson.class);
            Collection<UpsightConfiguration> res = new LinkedList();
            for (ConfigJson cj : rsp.configs) {
                res.add(UpsightConfiguration.create(cj.type, cj.configuration.toString(), this.mSessionManager.getCurrentSession().getSessionNumber()));
            }
            return res;
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
    }
}
