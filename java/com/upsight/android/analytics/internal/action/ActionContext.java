package com.upsight.android.analytics.internal.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.otto.Bus;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import rx.Scheduler.Worker;

public class ActionContext {
    public final Bus mBus;
    public final Clock mClock;
    public final UpsightLogger mLogger;
    public final Worker mMainWorker;
    public final ObjectMapper mMapper;
    public final UpsightContext mUpsight;

    public ActionContext(UpsightContext upsight, Bus bus, ObjectMapper mapper, Clock clock, Worker mainWorker, UpsightLogger logger) {
        this.mUpsight = upsight;
        this.mBus = bus;
        this.mMapper = mapper;
        this.mClock = clock;
        this.mMainWorker = mainWorker;
        this.mLogger = logger;
    }
}
