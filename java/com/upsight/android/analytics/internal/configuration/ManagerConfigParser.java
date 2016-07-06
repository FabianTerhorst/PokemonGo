package com.upsight.android.analytics.internal.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.analytics.internal.configuration.ConfigurationManager.Config;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class ManagerConfigParser {
    private ObjectMapper mMapper;

    public static class ConfigJson {
        @JsonProperty("requestInterval")
        public int requestInterval;
    }

    @Inject
    ManagerConfigParser(@Named("config-mapper") ObjectMapper mapper) {
        this.mMapper = mapper;
    }

    public Config parse(String configJson) throws IOException {
        return new Config(TimeUnit.SECONDS.toMillis((long) ((ConfigJson) this.mMapper.readValue(configJson, ConfigJson.class)).requestInterval));
    }
}
