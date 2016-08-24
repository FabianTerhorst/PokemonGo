package com.upsight.android.googlepushservices.internal;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.configuration.UpsightConfiguration;
import com.upsight.android.googlepushservices.R;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import rx.Observable;
import rx.functions.Func1;

public class PushConfigManager {
    private static final String LOG_TAG = PushConfigManager.class.getSimpleName();
    public static final String PUSH_CONFIGURATION_SUBTYPE = "upsight.configuration.push";
    private UpsightDataStore mDataStore = this.mUpsight.getDataStore();
    private Gson mGson;
    private UpsightLogger mLogger = this.mUpsight.getLogger();
    private UpsightContext mUpsight;

    public static final class Config {
        @SerializedName("auto_register")
        @Expose
        public boolean autoRegister;
        @SerializedName("push_token_ttl")
        @Expose
        public long pushTokenTtl;

        private boolean isValid() {
            return this.pushTokenTtl >= 0;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Config that = (Config) o;
            if (that.pushTokenTtl == this.pushTokenTtl && that.autoRegister == this.autoRegister) {
                return true;
            }
            return false;
        }
    }

    PushConfigManager(UpsightContext upsight) {
        this.mUpsight = upsight;
        this.mGson = upsight.getCoreComponent().gson();
    }

    public Observable<Config> fetchCurrentConfigObservable() throws IOException {
        return this.mDataStore.fetchObservable(UpsightConfiguration.class).filter(new Func1<UpsightConfiguration, Boolean>() {
            public Boolean call(UpsightConfiguration upsightConfiguration) {
                return Boolean.valueOf(PushConfigManager.PUSH_CONFIGURATION_SUBTYPE.equals(upsightConfiguration.getScope()));
            }
        }).map(new Func1<UpsightConfiguration, Config>() {
            public Config call(UpsightConfiguration upsightConfiguration) {
                return PushConfigManager.this.parseConfiguration(upsightConfiguration.getConfiguration());
            }
        }).filter(new Func1<Config, Boolean>() {
            public Boolean call(Config config) {
                boolean z = config != null && config.isValid();
                return Boolean.valueOf(z);
            }
        }).firstOrDefault(parseConfiguration(IOUtils.toString(this.mUpsight.getResources().openRawResource(R.raw.push_config))));
    }

    private Config parseConfiguration(String jsonConfig) {
        Config config = null;
        try {
            return (Config) this.mGson.fromJson(jsonConfig, Config.class);
        } catch (JsonSyntaxException e) {
            this.mLogger.e(LOG_TAG, "Could not parse incoming config", e);
            return config;
        }
    }
}
