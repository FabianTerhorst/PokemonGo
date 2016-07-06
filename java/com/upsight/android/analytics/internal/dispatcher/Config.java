package com.upsight.android.analytics.internal.dispatcher;

import com.upsight.android.analytics.internal.dispatcher.routing.RoutingConfig;
import com.upsight.android.analytics.internal.dispatcher.schema.IdentifierConfig;

class Config {
    private IdentifierConfig mIdentifierConfig;
    private RoutingConfig mRoutingConfig;

    public static class Builder {
        private IdentifierConfig mIdentifierConfig;
        private RoutingConfig mRoutingConfig;

        public Builder setRoutingConfig(RoutingConfig config) {
            this.mRoutingConfig = config;
            return this;
        }

        public Builder setIdentifierConfig(IdentifierConfig config) {
            this.mIdentifierConfig = config;
            return this;
        }

        public Config build() {
            return new Config();
        }
    }

    private Config(Builder builder) {
        this.mRoutingConfig = builder.mRoutingConfig;
        this.mIdentifierConfig = builder.mIdentifierConfig;
    }

    public RoutingConfig getRoutingConfig() {
        return this.mRoutingConfig;
    }

    public IdentifierConfig getIdentifierConfig() {
        return this.mIdentifierConfig;
    }

    public boolean isValid() {
        return this.mRoutingConfig != null && this.mRoutingConfig.isValid();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Config config = (Config) o;
        if (this.mIdentifierConfig == null ? config.mIdentifierConfig != null : !this.mIdentifierConfig.equals(config.mIdentifierConfig)) {
            return false;
        }
        if (this.mRoutingConfig != null) {
            if (this.mRoutingConfig.equals(config.mRoutingConfig)) {
                return true;
            }
        } else if (config.mRoutingConfig == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.mRoutingConfig != null) {
            result = this.mRoutingConfig.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.mIdentifierConfig != null) {
            i = this.mIdentifierConfig.hashCode();
        }
        return i2 + i;
    }
}
