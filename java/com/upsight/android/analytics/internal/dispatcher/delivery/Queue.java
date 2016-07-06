package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.text.TextUtils;
import com.upsight.android.analytics.internal.DataStoreRecord;
import com.upsight.android.analytics.internal.dispatcher.delivery.Batcher.Factory;
import com.upsight.android.analytics.internal.dispatcher.routing.Packet;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema;
import com.upsight.android.analytics.internal.dispatcher.util.Selector;
import java.util.HashMap;
import java.util.Map;

public class Queue {
    private BatchSender mBatchSender;
    private Factory mBatcherFactory;
    private Map<Schema, Batcher> mBatchers = new HashMap();
    private String mName;
    private Selector<Schema> mSchemaSelectorByName;
    private Selector<Schema> mSchemaSelectorByType;

    public static final class Trash extends Queue {
        public static final String NAME = "trash";
        private OnDeliveryListener mOnDeliveryListener;

        public Trash() {
            super(NAME, null, null, null, null);
        }

        public void setOnDeliveryListener(OnDeliveryListener listener) {
            this.mOnDeliveryListener = listener;
        }

        public void setOnResponseListener(OnResponseListener listener) {
        }

        public void enqueuePacket(Packet packet) {
            packet.markTrashed();
            OnDeliveryListener listener = this.mOnDeliveryListener;
            if (listener != null) {
                listener.onDelivery(packet);
            }
        }
    }

    Queue(String name, Selector<Schema> schemaSelectorByName, Selector<Schema> schemaSelectorByType, Factory factory, BatchSender batchSender) {
        this.mName = name;
        this.mSchemaSelectorByName = schemaSelectorByName;
        this.mSchemaSelectorByType = schemaSelectorByType;
        this.mBatcherFactory = factory;
        this.mBatchSender = batchSender;
    }

    public String getName() {
        return this.mName;
    }

    public void setOnDeliveryListener(OnDeliveryListener listener) {
        this.mBatchSender.setDeliveryListener(listener);
    }

    public void setOnResponseListener(OnResponseListener listener) {
        this.mBatchSender.setResponseListener(listener);
    }

    public void enqueuePacket(Packet packet) {
        Schema schema = selectSchema(packet.getRecord());
        Batcher batcher = (Batcher) this.mBatchers.get(schema);
        if (batcher == null) {
            batcher = this.mBatcherFactory.create(schema, this.mBatchSender);
            this.mBatchers.put(schema, batcher);
        }
        batcher.addPacket(packet);
    }

    private Schema selectSchema(DataStoreRecord record) {
        Schema schema = null;
        String dynamicIdentifiers = record.getIdentifiers();
        if (!TextUtils.isEmpty(dynamicIdentifiers)) {
            schema = (Schema) this.mSchemaSelectorByName.select(dynamicIdentifiers);
        }
        if (schema == null) {
            return (Schema) this.mSchemaSelectorByType.select(record.getSourceType());
        }
        return schema;
    }
}
