package com.upsight.android.analytics.internal.dispatcher.routing;

import com.upsight.android.analytics.internal.dispatcher.delivery.Queue.Trash;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RoutingConfig {
    private Map<String, QueueConfig> mQueuesConfigs;
    private Map<String, List<String>> mRoutes;

    public static class Builder {
        private Map<String, QueueConfig> mQueueConfigs = new HashMap();
        private Map<String, List<String>> mRoutes = new HashMap();

        public Builder addQueueConfig(String name, QueueConfig config) {
            if (this.mQueueConfigs.containsKey(name)) {
                throw new IllegalArgumentException("Queue with name " + name + " already exists");
            }
            this.mQueueConfigs.put(name, config);
            return this;
        }

        public Builder addRoute(String filter, List<String> route) {
            if (this.mRoutes.containsKey(filter)) {
                throw new IllegalArgumentException("Filter with name " + filter + " already exists");
            }
            this.mRoutes.put(filter, route);
            return this;
        }

        public RoutingConfig build() {
            return new RoutingConfig();
        }
    }

    private RoutingConfig(Builder builder) {
        this.mQueuesConfigs = builder.mQueueConfigs;
        this.mRoutes = builder.mRoutes;
    }

    public Map<String, QueueConfig> getQueueConfigs() {
        return Collections.unmodifiableMap(this.mQueuesConfigs);
    }

    public Map<String, List<String>> getRouters() {
        return Collections.unmodifiableMap(this.mRoutes);
    }

    public boolean isValid() {
        return areQueueConfigsValid() && areRoutesValid();
    }

    private boolean areQueueConfigsValid() {
        for (QueueConfig dc : this.mQueuesConfigs.values()) {
            if (dc != null) {
                if (!dc.isValid()) {
                }
            }
            return false;
        }
        return true;
    }

    private boolean areRoutesValid() {
        for (List<String> route : this.mRoutes.values()) {
            if (route == null) {
                return false;
            }
            for (String routeStep : route) {
                if (!this.mQueuesConfigs.containsKey(routeStep) && !Trash.NAME.equals(routeStep)) {
                    return false;
                }
            }
            if (new HashSet(route).size() != route.size()) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoutingConfig that = (RoutingConfig) o;
        if (that.mQueuesConfigs.equals(this.mQueuesConfigs) && that.mRoutes.equals(this.mRoutes)) {
            return true;
        }
        return false;
    }
}
