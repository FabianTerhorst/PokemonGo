package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.upsight.android.analytics.internal.dispatcher.delivery.BatchSender.Config;
import java.net.MalformedURLException;
import java.net.URL;

public class QueueConfig {
    private Config mBatchSenderConfig;
    private Batcher.Config mBatcherConfig;
    private String mEndpointAddress;

    public static class Builder {
        private Config mBatchSenderConfig;
        private Batcher.Config mBatcherConfig;
        private String mEndpointAddress;

        public Builder setEndpointAddress(String endpointAddress) {
            this.mEndpointAddress = endpointAddress;
            return this;
        }

        public Builder setBatchSenderConfig(Config retryConfig) {
            this.mBatchSenderConfig = retryConfig;
            return this;
        }

        public Builder setBatcherConfig(Batcher.Config batcherConfig) {
            this.mBatcherConfig = batcherConfig;
            return this;
        }

        public QueueConfig build() {
            return new QueueConfig();
        }
    }

    private QueueConfig(Builder builder) {
        this.mEndpointAddress = builder.mEndpointAddress;
        this.mBatchSenderConfig = builder.mBatchSenderConfig;
        this.mBatcherConfig = builder.mBatcherConfig;
    }

    public String getEndpointAddress() {
        return this.mEndpointAddress;
    }

    public Config getBatchSenderConfig() {
        return this.mBatchSenderConfig;
    }

    public Batcher.Config getBatcherConfig() {
        return this.mBatcherConfig;
    }

    public boolean isValid() {
        try {
            URL url = new URL(this.mEndpointAddress);
            if (this.mBatcherConfig == null || !this.mBatcherConfig.isValid() || this.mBatchSenderConfig == null || !this.mBatchSenderConfig.isValid()) {
                return false;
            }
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueueConfig that = (QueueConfig) o;
        if (this.mBatchSenderConfig == null ? that.mBatchSenderConfig != null : !this.mBatchSenderConfig.equals(that.mBatchSenderConfig)) {
            return false;
        }
        if (this.mBatcherConfig == null ? that.mBatcherConfig != null : !this.mBatcherConfig.equals(that.mBatcherConfig)) {
            return false;
        }
        if (this.mEndpointAddress != null) {
            if (this.mEndpointAddress.equals(that.mEndpointAddress)) {
                return true;
            }
        } else if (that.mEndpointAddress == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.mEndpointAddress != null) {
            result = this.mEndpointAddress.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.mBatchSenderConfig != null) {
            hashCode = this.mBatchSenderConfig.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.mBatcherConfig != null) {
            i = this.mBatcherConfig.hashCode();
        }
        return hashCode + i;
    }
}
