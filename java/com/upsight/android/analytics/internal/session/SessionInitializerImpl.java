package com.upsight.android.analytics.internal.session;

import android.os.Bundle;

public class SessionInitializerImpl implements SessionInitializer {
    private static final int NO_CMP_ID = Integer.MIN_VALUE;
    private static final int NO_MSG_ID = Integer.MIN_VALUE;
    private static final String SESSION_CAMPAIGN_ID = "campaign_id";
    private static final String SESSION_MESSAGE_ID = "message_id";
    private int mCampaignId;
    private int mMessageId;

    public static SessionInitializer fromPush(Bundle bundle) {
        return new SessionInitializerImpl(bundle.getInt(SESSION_CAMPAIGN_ID, NO_MSG_ID), bundle.getInt(SESSION_MESSAGE_ID, NO_MSG_ID));
    }

    private SessionInitializerImpl(int campaignId, int messageId) {
        this.mCampaignId = campaignId;
        this.mMessageId = messageId;
    }

    public Integer getCampaignID() {
        if (this.mCampaignId == NO_MSG_ID) {
            return null;
        }
        return Integer.valueOf(this.mCampaignId);
    }

    public Integer getMessageID() {
        if (this.mMessageId == NO_MSG_ID) {
            return null;
        }
        return Integer.valueOf(this.mMessageId);
    }
}
