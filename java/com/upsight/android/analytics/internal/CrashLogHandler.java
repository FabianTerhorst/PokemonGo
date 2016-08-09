package com.upsight.android.analytics.internal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.UpsightAnalyticsApi;
import com.upsight.android.analytics.event.UpsightAnalyticsEvent;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

class CrashLogHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    private UpsightAnalyticsApi mUpsightAnalytics;

    @UpsightStorableType("upsight.crash_log")
    public static class CrashLogEvent extends UpsightAnalyticsEvent<UpsightData, UpsightPublisherData> {

        public static class Builder extends com.upsight.android.analytics.event.UpsightAnalyticsEvent.Builder<CrashLogEvent, UpsightData, UpsightPublisherData> {
            private String crashID;
            private UpsightPublisherData publisherData = new com.upsight.android.analytics.event.UpsightPublisherData.Builder().build();
            private String stacktrace;

            public Builder(String stacktrace) {
                this.stacktrace = stacktrace;
            }

            public Builder with(UpsightPublisherData data) {
                this.publisherData = data;
                return this;
            }

            public Builder setCrashId(String crashID) {
                this.crashID = crashID;
                return this;
            }

            public CrashLogEvent build() {
                return new CrashLogEvent("upsight.crashlog", new UpsightData(this), this.publisherData);
            }
        }

        public static class UpsightData {
            @SerializedName("crashID")
            @Expose
            String crashID;
            @SerializedName("stacktrace")
            @Expose
            String stacktrace;

            protected UpsightData(Builder builder) {
                this.stacktrace = builder.stacktrace;
                this.crashID = builder.crashID;
            }

            protected UpsightData() {
            }

            public String getStacktrace() {
                return this.stacktrace;
            }

            public String getCrashID() {
                return this.crashID;
            }
        }

        protected CrashLogEvent(String type, UpsightData upsightData, UpsightPublisherData publisherData) {
            super(type, upsightData, publisherData);
        }

        protected CrashLogEvent() {
        }
    }

    public CrashLogHandler(UpsightAnalyticsApi analytics) {
        this.mUpsightAnalytics = analytics;
    }

    public void uncaughtException(Thread t, Throwable e) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        for (Throwable cause = e; cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        String stacktrace = result.toString();
        printWriter.close();
        sendToServer(stacktrace);
        this.mDefaultExceptionHandler.uncaughtException(t, e);
    }

    private void sendToServer(String stacktrace) {
        this.mUpsightAnalytics.record(new Builder(stacktrace).build());
    }
}
