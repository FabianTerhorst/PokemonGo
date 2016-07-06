package com.upsight.android.analytics.internal.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper mMapper;
    private SessionManager mSessionManager;

    public static class ConfigJson {
        @JsonProperty("configuration")
        public JsonNode configuration;
        @JsonProperty("type")
        public String type;
    }

    public static class ConfigResponseJson {
        @JsonProperty("configurationList")
        public ConfigJson[] configs;
    }

    @Inject
    ConfigurationResponseParser(@Named("config-mapper") ObjectMapper mapper, SessionManager sessionManager) {
        this.mMapper = mapper;
        this.mSessionManager = sessionManager;
    }

    public Collection<UpsightConfiguration> parse(String resposneJson) throws IOException {
        ConfigResponseJson rsp = (ConfigResponseJson) this.mMapper.readValue(resposneJson, ConfigResponseJson.class);
        Collection<UpsightConfiguration> res = new LinkedList();
        for (ConfigJson cj : rsp.configs) {
            res.add(UpsightConfiguration.create(cj.type, this.mMapper.writeValueAsString(cj.configuration), this.mSessionManager.getCurrentSession().getSessionNumber()));
        }
        return res;
    }
}
