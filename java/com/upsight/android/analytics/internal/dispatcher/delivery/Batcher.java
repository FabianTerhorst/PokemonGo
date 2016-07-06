package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.upsight.android.analytics.internal.dispatcher.delivery.BatchSender.Request;
import com.upsight.android.analytics.internal.dispatcher.routing.Packet;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema;
import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;

public class Batcher {
    private static final int DISABLE_AGING_MAX_AGE = 0;
    private Scheduler mAgingExecutor;
    private Action0 mAgingRunnable = new Action0() {
        public void call() {
            Batcher.this.sendCurrentBatch();
        }
    };
    private Subscription mAgingTask;
    private BatchSender mBatchSender;
    private Config mConfig;
    private Batch mCurrentBatch;
    private Schema mSchema;

    public static class Config {
        public final int batchCapacity;
        public final int maxBatchAge;

        public Config(int batchCapacity, int maxBatchAge) {
            this.batchCapacity = batchCapacity;
            this.maxBatchAge = maxBatchAge;
        }

        public boolean isValid() {
            return this.maxBatchAge >= 0 && this.batchCapacity > 0;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Config that = (Config) o;
            if (that.batchCapacity == this.batchCapacity && that.maxBatchAge == this.maxBatchAge) {
                return true;
            }
            return false;
        }
    }

    public interface Factory {
        Batcher create(Schema schema, BatchSender batchSender);
    }

    Batcher(Config config, Schema schema, BatchSender batchSender, Scheduler executor) {
        this.mSchema = schema;
        this.mBatchSender = batchSender;
        this.mConfig = config;
        this.mAgingExecutor = executor;
    }

    public synchronized void addPacket(Packet packet) {
        if (this.mCurrentBatch == null) {
            this.mCurrentBatch = new Batch(this.mConfig.batchCapacity);
            if (this.mConfig.maxBatchAge != 0) {
                this.mAgingTask = this.mAgingExecutor.createWorker().schedule(this.mAgingRunnable, (long) this.mConfig.maxBatchAge, TimeUnit.SECONDS);
            }
        }
        this.mCurrentBatch.addPacket(packet);
        if (this.mCurrentBatch.capacityLeft() == 0) {
            sendCurrentBatch();
        }
    }

    private synchronized void sendCurrentBatch() {
        Batch batch = this.mCurrentBatch;
        if (batch != null) {
            this.mCurrentBatch = null;
            if (this.mAgingTask != null) {
                this.mAgingTask.unsubscribe();
                this.mAgingTask = null;
            }
            this.mBatchSender.submitRequest(new Request(batch, this.mSchema));
        }
    }
}
