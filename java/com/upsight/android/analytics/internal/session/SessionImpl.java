package com.upsight.android.analytics.internal.session;

import android.content.Context;
import com.upsight.android.internal.util.PreferencesHelper;

public class SessionImpl implements Session {
    private static final String PREFERENCES_KEY_CURRENT_SESSION_DURATION = "current_session_duration";
    private static final String PREFERENCES_KEY_PAST_SESSION_TIME = "past_session_time";
    private static final String PREFERENCES_KEY_SESSION_NUM = "session_num";
    private static final String PREFERENCES_KEY_SESSION_START_TS = "session_start_ts";
    private static final int SESSION_NUM_BASE_OFFSET = 0;
    private final Integer mCampaignId;
    private final long mInitialSessionStartTs;
    private long mLastKnownSessionTs;
    private final Integer mMessageId;
    private final long mPastSessionTime;
    private final int mSessionNum;

    private SessionImpl(Integer campaignID, Integer messageId, int sessionNum, long initialSessionStartTs, long pastSessionTime) {
        this.mCampaignId = campaignID;
        this.mMessageId = messageId;
        this.mSessionNum = sessionNum;
        this.mInitialSessionStartTs = initialSessionStartTs;
        this.mPastSessionTime = pastSessionTime;
    }

    public static Session create(Context context, Clock clock, Integer campaignID, Integer messageId) {
        int sessionNum = PreferencesHelper.getInt(context, PREFERENCES_KEY_SESSION_NUM, Integer.MIN_VALUE);
        long initialSessionStartTs = PreferencesHelper.getLong(context, PREFERENCES_KEY_SESSION_START_TS, Long.MIN_VALUE);
        if (sessionNum == Integer.MIN_VALUE || initialSessionStartTs == Long.MIN_VALUE) {
            return incrementAndCreate(context, clock, campaignID, messageId);
        }
        return new SessionImpl(campaignID, messageId, sessionNum, initialSessionStartTs, PreferencesHelper.getLong(context, PREFERENCES_KEY_PAST_SESSION_TIME, 0));
    }

    public static Session incrementAndCreate(Context context, Clock clock, Integer campaignID, Integer messageId) {
        int sessionNum = PreferencesHelper.getInt(context, PREFERENCES_KEY_SESSION_NUM, 0) + 1;
        long currentTime = clock.currentTimeSeconds();
        PreferencesHelper.putInt(context, PREFERENCES_KEY_SESSION_NUM, sessionNum);
        PreferencesHelper.putLong(context, PREFERENCES_KEY_SESSION_START_TS, currentTime);
        long pastSessionTime = PreferencesHelper.getLong(context, PREFERENCES_KEY_PAST_SESSION_TIME, 0) + PreferencesHelper.getLong(context, PREFERENCES_KEY_CURRENT_SESSION_DURATION, 0);
        PreferencesHelper.putLong(context, PREFERENCES_KEY_CURRENT_SESSION_DURATION, 0);
        PreferencesHelper.putLong(context, PREFERENCES_KEY_PAST_SESSION_TIME, pastSessionTime);
        return new SessionImpl(campaignID, messageId, sessionNum, currentTime, pastSessionTime);
    }

    public synchronized Integer getCampaignID() {
        return this.mCampaignId;
    }

    public synchronized Integer getMessageID() {
        return this.mMessageId;
    }

    public synchronized int getSessionNumber() {
        return this.mSessionNum;
    }

    public synchronized long getTimeStamp() {
        return this.mInitialSessionStartTs;
    }

    public long getPreviousTos() {
        return this.mPastSessionTime;
    }

    public void updateDuration(Context context, long lastKnownTime) {
        PreferencesHelper.putLong(context, PREFERENCES_KEY_CURRENT_SESSION_DURATION, lastKnownTime - this.mInitialSessionStartTs);
    }
}
