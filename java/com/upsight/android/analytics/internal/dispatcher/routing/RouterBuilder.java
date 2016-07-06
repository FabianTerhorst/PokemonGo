package com.upsight.android.analytics.internal.dispatcher.routing;

import com.upsight.android.analytics.internal.dispatcher.delivery.Queue;
import com.upsight.android.analytics.internal.dispatcher.delivery.Queue.Trash;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueBuilder;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueConfig;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema;
import com.upsight.android.analytics.internal.dispatcher.util.ByFilterSelector;
import com.upsight.android.analytics.internal.dispatcher.util.Selector;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import rx.Scheduler;

public class RouterBuilder {
    private QueueBuilder mQueueBuilder;
    private Scheduler mScheduler;

    RouterBuilder(Scheduler scheduler, QueueBuilder queueBuilder) {
        this.mScheduler = scheduler;
        this.mQueueBuilder = queueBuilder;
    }

    public Router build(RoutingConfig config, Selector<Schema> schemaSelectorByName, Selector<Schema> schemaSelectorByType, RoutingListener listener) {
        Map<String, Queue> queues = buildQueues(config, schemaSelectorByName, schemaSelectorByType);
        Router router = new Router(this.mScheduler, new ByFilterSelector(buildRoutes(config, queues)), listener);
        for (Queue queue : queues.values()) {
            queue.setOnDeliveryListener(router);
            queue.setOnResponseListener(router);
        }
        return router;
    }

    private Map<String, Queue> buildQueues(RoutingConfig config, Selector<Schema> schemaSelectorByName, Selector<Schema> schemaSelectorByType) {
        Map<String, Queue> queues = new HashMap();
        Queue trash = new Trash();
        queues.put(trash.getName(), trash);
        for (Entry<String, QueueConfig> queue : config.getQueueConfigs().entrySet()) {
            queues.put(queue.getKey(), this.mQueueBuilder.build((String) queue.getKey(), (QueueConfig) queue.getValue(), schemaSelectorByName, schemaSelectorByType));
        }
        return queues;
    }

    private Map<String, Route> buildRoutes(RoutingConfig config, Map<String, Queue> queues) {
        Map<String, Route> routes = new HashMap();
        for (Entry<String, List<String>> routeConf : config.getRouters().entrySet()) {
            List route = new LinkedList();
            for (String queue : (List) routeConf.getValue()) {
                route.add(new RouteStep((Queue) queues.get(queue)));
            }
            routes.put(routeConf.getKey(), new Route(route));
        }
        return routes;
    }
}
