package com.upsight.android.analytics.event;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONObjectSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public abstract class UpsightAnalyticsEvent<U, P> {
    @UpsightStorableIdentifier
    protected String id;
    @SerializedName("ts")
    @Expose
    protected long mCreationTsMs;
    @SerializedName("pub_data")
    @Expose
    protected P mPublisherData;
    @SerializedName("seq_id")
    @Expose
    protected long mSequenceId;
    @SerializedName("type")
    @Expose
    protected String mType;
    @SerializedName("upsight_data")
    @Expose
    protected U mUpsightData;
    @SerializedName("user_attributes")
    @Expose
    protected JsonObject mUserAttributes;

    public static abstract class Builder<T extends UpsightAnalyticsEvent<U, P>, U, P> {
        protected abstract T build();

        public final T record(UpsightContext upsight) {
            T result = build();
            UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
            if (extension != null) {
                extension.getApi().record(result);
            } else {
                upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
            }
            return result;
        }
    }

    protected UpsightAnalyticsEvent(String type, U upsightData, P publisherData) {
        this.mCreationTsMs = TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        this.mType = type;
        this.mUpsightData = upsightData;
        this.mPublisherData = publisherData;
    }

    protected UpsightAnalyticsEvent() {
    }

    public long getCreationTimestampMs() {
        return this.mCreationTsMs;
    }

    public String getType() {
        return this.mType;
    }

    public String getId() {
        return this.id;
    }

    public P getPublisherData() {
        return this.mPublisherData;
    }

    protected U getUpsightData() {
        return this.mUpsightData;
    }

    public long getSequenceId() {
        return this.mSequenceId;
    }

    public JSONObject getUserAttributes() {
        return JSONObjectSerializer.fromJsonObject(this.mUserAttributes);
    }
}
