package com.upsight.android.analytics.internal.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.analytics.internal.session.SessionManagerImpl.Config;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;

class ConfigParser {
    private ObjectMapper mMapper;

    public static class ConfigJson {
        @JsonProperty("session_gap")
        public int session_gap;
    }

    @Inject
    public ConfigParser(@Named("config-mapper") ObjectMapper mapper) {
        this.mMapper = mapper;
    }

    public Config parseConfig(String configContent) throws IOException {
        return new Config((long) ((ConfigJson) this.mMapper.readValue(configContent, ConfigJson.class)).session_gap);
    }
}
