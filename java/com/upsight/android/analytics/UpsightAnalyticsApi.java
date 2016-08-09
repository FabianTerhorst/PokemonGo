package com.upsight.android.analytics;

import android.app.Activity;
import android.content.Intent;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.event.UpsightAnalyticsEvent;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.analytics.provider.UpsightUserAttributes.Entry;
import java.util.Date;
import java.util.Set;

public interface UpsightAnalyticsApi {
    Boolean getBooleanUserAttribute(String str);

    Date getDatetimeUserAttribute(String str);

    Set<Entry> getDefaultUserAttributes();

    Float getFloatUserAttribute(String str);

    Integer getIntUserAttribute(String str);

    boolean getOptOutStatus();

    String getStringUserAttribute(String str);

    void purgeLocation();

    void putUserAttribute(String str, Boolean bool);

    void putUserAttribute(String str, Float f);

    void putUserAttribute(String str, Integer num);

    void putUserAttribute(String str, String str2);

    void putUserAttribute(String str, Date date);

    void record(UpsightAnalyticsEvent upsightAnalyticsEvent);

    void registerDataProvider(UpsightDataProvider upsightDataProvider);

    void setOptOutStatus(boolean z);

    void trackActivity(Activity activity, ActivityState activityState);

    void trackLocation(Data data);

    void trackPurchase(int i, String str, double d, double d2, String str2, Intent intent, UpsightPublisherData upsightPublisherData) throws UpsightException;
}
