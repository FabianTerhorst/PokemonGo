package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.text.TextUtils;
import com.google.gson.JsonParser;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.internal.dispatcher.delivery.UpsightEndpoint.Response;
import com.upsight.android.analytics.internal.dispatcher.routing.Packet;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.NetworkHelper;
import com.upsight.android.logger.UpsightLogger;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import rx.Scheduler;
import rx.functions.Action0;

public class BatchSender {
    private Scheduler mBatchSendExecutor;
    private final Clock mClock;
    private Config mConfig;
    private OnDeliveryListener mDeliveryListener;
    private UpsightEndpoint mEndpoint;
    private JsonParser mJsonParser;
    private ReentrantLock mListenersLock = new ReentrantLock();
    private final UpsightLogger mLogger;
    private OnResponseListener mResponseListener;
    private ResponseParser mResponseParser;
    private Scheduler mRetryExecutor;
    private ConcurrentMap<Request, Integer> mTryCounts = new ConcurrentHashMap();
    private UpsightContext mUpsight;

    private class BatchSendTask implements Runnable {
        public static final String NETWORK_ERROR = "Network communication problems";
        private Request mRequest;

        public BatchSendTask(Request request) {
            this.mRequest = request;
        }

        public void run() {
            if (NetworkHelper.isConnected(BatchSender.this.mUpsight)) {
                try {
                    Response resp = BatchSender.this.mEndpoint.send(new UpsightRequest(BatchSender.this.mUpsight, this.mRequest, BatchSender.this.mJsonParser, BatchSender.this.mClock));
                    ResponseParser.Response response = null;
                    if (!TextUtils.isEmpty(resp.body)) {
                        response = BatchSender.this.mResponseParser.parse(resp.body);
                        BatchSender.this.notifyResponseListener(response.responses);
                    }
                    if (resp.isOk()) {
                        BatchSender.this.sendSucceeded(this.mRequest);
                        return;
                    }
                    BatchSender.this.mLogger.e("BatchSender", "Received " + resp.statusCode + " HTTP response code from server", new Object[0]);
                    BatchSender.this.sendFailed(this.mRequest, FailReason.SERVER, response != null ? response.error : null);
                    return;
                } catch (IOException e) {
                    BatchSender.this.sendFailed(this.mRequest, FailReason.NETWORK, NETWORK_ERROR);
                    return;
                }
            }
            BatchSender.this.sendFailed(this.mRequest, FailReason.NETWORK, NETWORK_ERROR);
        }
    }

    public static class Config {
        public final boolean countNetworkFail;
        public final int maxRetryCount;
        public final int retryInterval;

        public Config(boolean countNetworkFail, int retryInterval, int maxRetryCount) {
            this.countNetworkFail = countNetworkFail;
            this.retryInterval = retryInterval;
            this.maxRetryCount = maxRetryCount;
        }

        public boolean isValid() {
            return this.retryInterval > 0 && this.maxRetryCount >= 0;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Config that = (Config) o;
            if (that.countNetworkFail == this.countNetworkFail && that.retryInterval == this.retryInterval && that.maxRetryCount == this.maxRetryCount) {
                return true;
            }
            return false;
        }
    }

    private enum FailReason {
        SERVER,
        NETWORK
    }

    public static final class Request {
        public final Batch batch;
        public final Schema schema;

        public Request(Batch batch, Schema schema) {
            this.batch = batch;
            this.schema = schema;
        }
    }

    private class RetryTask implements Runnable {
        private Request mRequest;

        public RetryTask(Request request) {
            this.mRequest = request;
        }

        public void run() {
            BatchSender.this.submitRequest(this.mRequest);
        }
    }

    BatchSender(UpsightContext upsight, Config config, Scheduler retryExecutor, Scheduler sendExecutor, UpsightEndpoint endpoint, ResponseParser responseParser, JsonParser jsonParser, Clock clock, UpsightLogger logger) {
        this.mUpsight = upsight;
        this.mEndpoint = endpoint;
        this.mConfig = config;
        this.mRetryExecutor = retryExecutor;
        this.mJsonParser = jsonParser;
        this.mBatchSendExecutor = sendExecutor;
        this.mResponseParser = responseParser;
        this.mClock = clock;
        this.mLogger = logger;
    }

    public void setDeliveryListener(OnDeliveryListener deliveryListener) {
        this.mListenersLock.lock();
        try {
            this.mDeliveryListener = deliveryListener;
        } finally {
            this.mListenersLock.unlock();
        }
    }

    private void notifyDeliveryListener(Batch batch) {
        this.mListenersLock.lock();
        try {
            if (this.mDeliveryListener != null) {
                for (Packet packet : batch.getPackets()) {
                    this.mDeliveryListener.onDelivery(packet);
                }
            }
            this.mListenersLock.unlock();
        } catch (Throwable th) {
            this.mListenersLock.unlock();
        }
    }

    public void setResponseListener(OnResponseListener responseListener) {
        this.mListenersLock.lock();
        try {
            this.mResponseListener = responseListener;
        } finally {
            this.mListenersLock.unlock();
        }
    }

    private void notifyResponseListener(Collection<EndpointResponse> responses) {
        this.mListenersLock.lock();
        try {
            if (this.mResponseListener != null) {
                for (EndpointResponse response : responses) {
                    this.mResponseListener.onResponse(response);
                }
            }
            this.mListenersLock.unlock();
        } catch (Throwable th) {
            this.mListenersLock.unlock();
        }
    }

    public void submitRequest(final Request request) {
        this.mBatchSendExecutor.createWorker().schedule(new Action0() {
            public void call() {
                new BatchSendTask(request).run();
            }
        });
    }

    private void sendSucceeded(Request request) {
        this.mTryCounts.remove(request);
        for (Packet packet : request.batch.getPackets()) {
            packet.markDelivered();
        }
        notifyDeliveryListener(request.batch);
    }

    private void sendFailed(final Request request, FailReason failReason, String errorString) {
        Integer tryCount = (Integer) this.mTryCounts.get(request);
        if (tryCount == null) {
            tryCount = Integer.valueOf(this.mConfig.maxRetryCount);
        }
        if (tryCount.intValue() > 0) {
            if (failReason != FailReason.NETWORK || this.mConfig.countNetworkFail) {
                tryCount = Integer.valueOf(tryCount.intValue() - 1);
            }
            this.mTryCounts.put(request, tryCount);
            this.mRetryExecutor.createWorker().schedule(new Action0() {
                public void call() {
                    new RetryTask(request).run();
                }
            }, (long) this.mConfig.retryInterval, TimeUnit.SECONDS);
            return;
        }
        this.mTryCounts.remove(request);
        for (Packet packet : request.batch.getPackets()) {
            packet.failAndRoute(errorString);
        }
        notifyDeliveryListener(request.batch);
    }
}
