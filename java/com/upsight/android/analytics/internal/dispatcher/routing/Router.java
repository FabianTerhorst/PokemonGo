package com.upsight.android.analytics.internal.dispatcher.routing;

import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.internal.DataStoreRecord;
import com.upsight.android.analytics.internal.dispatcher.delivery.OnDeliveryListener;
import com.upsight.android.analytics.internal.dispatcher.delivery.OnResponseListener;
import com.upsight.android.analytics.internal.dispatcher.routing.Packet.State;
import com.upsight.android.analytics.internal.dispatcher.util.ByFilterSelector;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import spacemadness.com.lunarconsole.R;

public class Router implements OnDeliveryListener, OnResponseListener {
    private final AtomicInteger mEventsInRouting = new AtomicInteger();
    private boolean mIsFinishRequested;
    private final ByFilterSelector<Route> mRouteSelector;
    private final RoutingListener mRoutingListener;
    private final Worker mWorker;

    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$analytics$internal$dispatcher$routing$Packet$State = new int[State.values().length];

        static {
            try {
                $SwitchMap$com$upsight$android$analytics$internal$dispatcher$routing$Packet$State[State.UNDELIVERED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$analytics$internal$dispatcher$routing$Packet$State[State.DELIVERED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$upsight$android$analytics$internal$dispatcher$routing$Packet$State[State.TRASHED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    Router(Scheduler scheduler, ByFilterSelector<Route> routeSelector, RoutingListener routingListener) {
        this.mWorker = scheduler.createWorker();
        this.mRouteSelector = routeSelector;
        this.mRoutingListener = routingListener;
    }

    public boolean routeEvent(DataStoreRecord record) {
        if (this.mIsFinishRequested) {
            throw new IllegalStateException("Router is requested to finish routing");
        }
        Route route = (Route) this.mRouteSelector.select(record.getSourceType());
        if (route == null || route.getStepsCount() == 0) {
            return false;
        }
        new Packet(record, new Route(route)).routeToNext();
        this.mEventsInRouting.incrementAndGet();
        return true;
    }

    public void finishRouting() {
        this.mIsFinishRequested = true;
        if (this.mEventsInRouting.get() == 0) {
            this.mRoutingListener.onRoutingFinished(this);
        }
    }

    public void onDelivery(final Packet packet) {
        this.mWorker.schedule(new Action0() {
            public void call() {
                switch (AnonymousClass3.$SwitchMap$com$upsight$android$analytics$internal$dispatcher$routing$Packet$State[packet.getState().ordinal()]) {
                    case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                        if (packet.hasMoreOptionsToTry()) {
                            packet.routeToNext();
                            return;
                        }
                        Router.this.mRoutingListener.onDelivery(packet.getRecord(), false, false, packet.getDeliveryHistory());
                        Router.this.finishPacket();
                        return;
                    case R.styleable.LoadingImageView_circleCrop /*2*/:
                        Router.this.mRoutingListener.onDelivery(packet.getRecord(), true, false, null);
                        Router.this.finishPacket();
                        return;
                    case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                        Router.this.mRoutingListener.onDelivery(packet.getRecord(), false, true, packet.getDeliveryHistory());
                        Router.this.finishPacket();
                        return;
                    default:
                        return;
                }
            }
        });
    }

    public void onResponse(final EndpointResponse response) {
        this.mWorker.schedule(new Action0() {
            public void call() {
                Router.this.mRoutingListener.onResponse(response);
            }
        });
    }

    private void finishPacket() {
        int eventsInRouting = this.mEventsInRouting.decrementAndGet();
        if (this.mIsFinishRequested && eventsInRouting == 0) {
            this.mRoutingListener.onRoutingFinished(this);
        }
    }
}
