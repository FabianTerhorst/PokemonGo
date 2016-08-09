package com.upsight.android.analytics.internal;

import android.app.Activity;
import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightAnalyticsApi;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.event.UpsightAnalyticsEvent;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.DataStoreRecord.Action;
import com.upsight.android.analytics.internal.association.AssociationManager;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaSelectorBuilder;
import com.upsight.android.analytics.internal.session.Session;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.analytics.provider.UpsightUserAttributes.Entry;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class Analytics implements UpsightAnalyticsApi {
    private static final String LOG_TAG = Analytics.class.getSimpleName();
    private static final String SEQUENCE_ID_FIELD_NAME = "seq_id";
    private static final String USER_ATTRIBUTES_FIELD_NAME = "user_attributes";
    private final AssociationManager mAssociationManager;
    private final UpsightDataStore mDataStore;
    private final Set<Entry> mDefaultUserAttributes = this.mUserAttributes.getDefault();
    private final UpsightGooglePlayHelper mGooglePlayHelper;
    private final Gson mGson;
    private final UpsightLifeCycleTracker mLifeCycleTracker;
    private final UpsightLocationTracker mLocationTracker;
    private final UpsightLogger mLogger;
    private final UpsightOptOutStatus mOptOutStatus;
    private final SchemaSelectorBuilder mSchemaSelector;
    private final SessionManager mSessionManager;
    private final UpsightContext mUpsight;
    private final UpsightUserAttributes mUserAttributes;

    @Inject
    public Analytics(UpsightContext upsight, UpsightLifeCycleTracker lifeCycleTracker, SessionManager sessionManager, SchemaSelectorBuilder schemaSelector, AssociationManager associationManager, UpsightOptOutStatus optOutStatus, UpsightLocationTracker locationTracker, UpsightUserAttributes userAttributes, UpsightGooglePlayHelper googlePlayHelper) {
        this.mUpsight = upsight;
        this.mDataStore = upsight.getDataStore();
        this.mLifeCycleTracker = lifeCycleTracker;
        this.mSessionManager = sessionManager;
        this.mGson = upsight.getCoreComponent().gson();
        this.mLogger = upsight.getLogger();
        this.mSchemaSelector = schemaSelector;
        this.mAssociationManager = associationManager;
        this.mOptOutStatus = optOutStatus;
        this.mLocationTracker = locationTracker;
        this.mUserAttributes = userAttributes;
        this.mGooglePlayHelper = googlePlayHelper;
    }

    public void record(UpsightAnalyticsEvent event) {
        Session currentSession = this.mSessionManager.getCurrentSession();
        long sessionStart = currentSession.getTimeStamp();
        Integer messageID = currentSession.getMessageID();
        Integer campaignID = currentSession.getCampaignID();
        int sessionNum = currentSession.getSessionNumber();
        long prevTos = currentSession.getPreviousTos();
        JsonObject eventNode = toJsonElement(event);
        appendAssociationData(event.getType(), eventNode);
        DataStoreRecord record = DataStoreRecord.create(Action.Created, sessionStart, messageID, campaignID, sessionNum, prevTos, eventNode.toString(), event.getType());
        if (event instanceof DynamicIdentifiers) {
            record.setIdentifiers(((DynamicIdentifiers) event).getIdentifiersName());
        }
        this.mDataStore.store(record);
    }

    private JsonObject toJsonElement(UpsightAnalyticsEvent event) {
        JsonObject node = this.mGson.toJsonTree(event).getAsJsonObject();
        node.addProperty(SEQUENCE_ID_FIELD_NAME, Long.valueOf(EventSequenceId.getAndIncrement(this.mUpsight)));
        node.add(USER_ATTRIBUTES_FIELD_NAME, getAllAsJsonElement(this.mDefaultUserAttributes));
        return node;
    }

    private void appendAssociationData(String eventType, JsonObject eventNode) {
        this.mAssociationManager.associate(eventType, eventNode);
    }

    public void setOptOutStatus(boolean optOut) {
        this.mOptOutStatus.set(optOut);
    }

    public boolean getOptOutStatus() {
        return this.mOptOutStatus.get();
    }

    public void registerDataProvider(UpsightDataProvider provider) {
        this.mSchemaSelector.registerDataProvider(provider);
    }

    public void trackActivity(Activity activity, ActivityState activityState) {
        this.mLifeCycleTracker.track(activity, activityState, null);
    }

    public void trackLocation(Data locationData) {
        this.mLocationTracker.track(locationData);
    }

    public void purgeLocation() {
        this.mLocationTracker.purge();
    }

    public void putUserAttribute(String key, String value) {
        this.mUserAttributes.put(key, value);
    }

    public void putUserAttribute(String key, Integer value) {
        this.mUserAttributes.put(key, value);
    }

    public void putUserAttribute(String key, Boolean value) {
        this.mUserAttributes.put(key, value);
    }

    public void putUserAttribute(String key, Float value) {
        this.mUserAttributes.put(key, value);
    }

    public void putUserAttribute(String key, Date value) {
        this.mUserAttributes.put(key, value);
    }

    public String getStringUserAttribute(String key) {
        return this.mUserAttributes.getString(key);
    }

    public Integer getIntUserAttribute(String key) {
        return this.mUserAttributes.getInt(key);
    }

    public Boolean getBooleanUserAttribute(String key) {
        return this.mUserAttributes.getBoolean(key);
    }

    public Float getFloatUserAttribute(String key) {
        return this.mUserAttributes.getFloat(key);
    }

    public Date getDatetimeUserAttribute(String key) {
        return this.mUserAttributes.getDatetime(key);
    }

    public Set<Entry> getDefaultUserAttributes() {
        return this.mUserAttributes.getDefault();
    }

    public void trackPurchase(int quantity, String currency, double price, double totalPrice, String product, Intent responseData, UpsightPublisherData publisherData) throws UpsightException {
        this.mGooglePlayHelper.trackPurchase(quantity, currency, price, totalPrice, product, responseData, publisherData);
    }

    private JsonElement getAllAsJsonElement(Set<Entry> defaultUserAttributes) {
        JsonObject o = new JsonObject();
        for (Entry entry : defaultUserAttributes) {
            if (String.class.equals(entry.getType())) {
                o.addProperty(entry.getKey(), PreferencesHelper.getString(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), (String) entry.getDefaultValue()));
            } else if (Integer.class.equals(entry.getType())) {
                o.addProperty(entry.getKey(), Integer.valueOf(PreferencesHelper.getInt(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), ((Integer) entry.getDefaultValue()).intValue())));
            } else if (Boolean.class.equals(entry.getType())) {
                o.addProperty(entry.getKey(), Boolean.valueOf(PreferencesHelper.getBoolean(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), ((Boolean) entry.getDefaultValue()).booleanValue())));
            } else if (Float.class.equals(entry.getType())) {
                o.addProperty(entry.getKey(), Float.valueOf(PreferencesHelper.getFloat(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), ((Float) entry.getDefaultValue()).floatValue())));
            } else if (Date.class.equals(entry.getType())) {
                long datetimeS = PreferencesHelper.getLong(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), TimeUnit.SECONDS.convert(((Date) entry.getDefaultValue()).getTime(), TimeUnit.MILLISECONDS));
                if (datetimeS != UpsightUserAttributes.DATETIME_NULL_S) {
                    o.addProperty(entry.getKey(), Long.valueOf(datetimeS));
                } else {
                    o.addProperty(entry.getKey(), (Long) null);
                }
            }
        }
        return o;
    }
}
