package com.upsight.android.analytics.internal.action;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import rx.Scheduler.Worker;

public class ActionContext {
    public final Bus mBus;
    public final Clock mClock;
    public final Gson mGson;
    public final UpsightLogger mLogger;
    public final Worker mMainWorker;
    public final UpsightContext mUpsight;

    public ActionContext(UpsightContext upsight, Bus bus, Gson gson, Clock clock, Worker mainWorker, UpsightLogger logger) {
        this.mUpsight = upsight;
        this.mBus = bus;
        this.mGson = gson;
        this.mClock = clock;
        this.mMainWorker = mainWorker;
        this.mLogger = logger;
    }
}
