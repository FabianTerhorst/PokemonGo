package com.upsight.android.analytics.internal.dispatcher;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.dispatcher.Config.Builder;
import com.upsight.android.analytics.internal.dispatcher.delivery.Queue.Trash;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueBuilder;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueConfig;
import com.upsight.android.analytics.internal.dispatcher.routing.RoutingConfig;
import com.upsight.android.analytics.internal.dispatcher.schema.IdentifierConfig;
import com.upsight.android.logger.UpsightLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
class ConfigParser {
    private static final String LOG_TAG = "Dispatcher";
    private UpsightLogger mLogger;
    private ObjectMapper mMapper;

    public static class Config {
        @JsonProperty("identifier_filters")
        public List<IdentifierFilter> identifierFilters;
        @JsonProperty("identifiers")
        public List<Identifier> identifiers;
        @JsonProperty("queues")
        public List<Queue> queues;
        @JsonProperty("route_filters")
        public List<RouteFilter> routeFilters;
    }

    public static class Identifier {
        @JsonProperty("ids")
        public List<String> ids;
        @JsonProperty("name")
        public String name;
    }

    public static class IdentifierFilter {
        @JsonProperty("filter")
        public String filter;
        @JsonProperty("identifiers")
        public String identifiers;
    }

    public static class Queue {
        @JsonProperty("count_network_fail_retries")
        public boolean countNetworkFail;
        @JsonProperty("host")
        public String host;
        @JsonProperty("max_age")
        public int maxAge;
        @JsonProperty("max_size")
        public int maxSize;
        @JsonProperty("name")
        public String name;
        @JsonProperty("protocol")
        public String protocol;
        @JsonProperty("max_retry_count")
        public int retryCount;
        @JsonProperty("retry_interval")
        public int retryInterval;
        @JsonProperty("url_fmt")
        public String urlFormat;

        public String formatEndpointAddress() {
            return this.urlFormat.replace(QueueBuilder.MACRO_PROTOCOL, this.protocol).replace(QueueBuilder.MACRO_HOST, this.host);
        }
    }

    public static class RouteFilter {
        @JsonProperty("filter")
        public String filter;
        @JsonProperty("queues")
        public List<String> queues;
    }

    @Inject
    public ConfigParser(UpsightContext upsight, @Named("config-mapper") ObjectMapper mapper) {
        this.mMapper = mapper;
        this.mLogger = upsight.getLogger();
    }

    public Config parseConfig(String configContent) throws IOException {
        Config config = (Config) this.mMapper.readValue(configContent, Config.class);
        return new Builder().setRoutingConfig(parseRoutingConfig(config)).setIdentifierConfig(parseIdentifierConfig(config)).build();
    }

    private IdentifierConfig parseIdentifierConfig(Config config) {
        IdentifierConfig.Builder builder = new IdentifierConfig.Builder();
        if (config.identifiers != null) {
            for (Identifier identifier : config.identifiers) {
                builder.addIdentifiers(identifier.name, new HashSet(identifier.ids));
            }
            for (IdentifierFilter filter : config.identifierFilters) {
                builder.addIdentifierFilter(filter.filter, filter.identifiers);
            }
        }
        return builder.build();
    }

    private RoutingConfig parseRoutingConfig(Config config) {
        RoutingConfig.Builder routingBuilder = new RoutingConfig.Builder();
        if (config.queues != null) {
            List<String> queueConfigs = new ArrayList();
            for (Queue queue : config.queues) {
                routingBuilder.addQueueConfig(queue.name, parseQueueConfig(queue));
                queueConfigs.add(queue.name);
            }
            for (RouteFilter filter : config.routeFilters) {
                List<String> validQueues = new ArrayList();
                for (String queue2 : filter.queues) {
                    if (queueConfigs.contains(queue2) || Trash.NAME.equals(queue2)) {
                        validQueues.add(queue2);
                    } else {
                        this.mLogger.w(LOG_TAG, "Dispatcher ignored a route to a non-existent queue: " + queue2 + " in the dispatcher configuration", new Object[0]);
                    }
                }
                if (validQueues.size() > 0) {
                    routingBuilder.addRoute(filter.filter, validQueues);
                }
            }
        }
        return routingBuilder.build();
    }

    private QueueConfig parseQueueConfig(Queue queue) {
        return new QueueConfig.Builder().setEndpointAddress(queue.formatEndpointAddress()).setBatchSenderConfig(new com.upsight.android.analytics.internal.dispatcher.delivery.BatchSender.Config(queue.countNetworkFail, queue.retryInterval, queue.retryCount)).setBatcherConfig(new com.upsight.android.analytics.internal.dispatcher.delivery.Batcher.Config(queue.maxSize, queue.maxAge)).build();
    }
}
