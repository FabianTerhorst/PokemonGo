package com.upsight.android.googlepushservices.internal;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightGooglePushServicesExtension;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.event.comm.UpsightCommClickEvent;
import com.upsight.android.analytics.event.comm.UpsightCommClickEvent.Builder;
import com.upsight.android.analytics.internal.session.ApplicationStatus;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.analytics.internal.session.SessionInitializerImpl;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import javax.inject.Inject;

public class PushClickIntentService extends IntentService {
    private static final String BUNDLE_KEY_MESSAGE_INTENT = "messageIntent";
    private static final Integer NO_CMP_ID = Integer.valueOf(Integer.MIN_VALUE);
    private static final Integer NO_MSG_ID = Integer.valueOf(Integer.MIN_VALUE);
    private static final String SERVICE_NAME = "UpsightGcmPushClickIntentService";
    @Inject
    SessionManager mSessionManager;

    static Intent newIntent(Context context, Intent messageIntent, Integer campaignId, Integer messageId) {
        return new Intent(context.getApplicationContext(), PushClickIntentService.class).putExtra(BUNDLE_KEY_MESSAGE_INTENT, appendMessageIntentBundle(messageIntent, campaignId, messageId));
    }

    public PushClickIntentService() {
        super(SERVICE_NAME);
    }

    protected void onHandleIntent(Intent intent) {
        UpsightContext upsight = Upsight.createContext(this);
        ((UpsightGooglePushServicesComponent) ((UpsightGooglePushServicesExtension) upsight.getUpsightExtension(UpsightGooglePushServicesExtension.EXTENSION_NAME)).getComponent()).inject(this);
        Intent messageIntent = (Intent) intent.getParcelableExtra(BUNDLE_KEY_MESSAGE_INTENT);
        Bundle extras = messageIntent.getBundleExtra(SessionManager.SESSION_EXTRA);
        SessionManager sessionManager = this.mSessionManager;
        if (State.BACKGROUND.name().equals(((ApplicationStatus) upsight.getDataStore().fetchObservable(ApplicationStatus.class).toBlocking().first()).getState().name())) {
            sessionManager.startSession(SessionInitializerImpl.fromPush(extras));
        }
        Builder eventBuilder = UpsightCommClickEvent.createBuilder(Integer.valueOf(extras.getInt(SessionManager.SESSION_MESSAGE_ID, NO_MSG_ID.intValue())));
        Integer campaignId = Integer.valueOf(extras.getInt(SessionManager.SESSION_CAMPAIGN_ID, NO_CMP_ID.intValue()));
        if (!campaignId.equals(NO_CMP_ID)) {
            eventBuilder.setMsgCampaignId(campaignId);
        }
        eventBuilder.record(Upsight.createContext(this));
        startActivity(messageIntent);
    }

    protected static Intent appendMessageIntentBundle(Intent messageIntent, Integer campaignId, Integer messageId) {
        Bundle bundle = new Bundle();
        if (campaignId != null) {
            bundle.putInt(SessionManager.SESSION_CAMPAIGN_ID, campaignId.intValue());
        }
        if (messageId != null) {
            bundle.putInt(SessionManager.SESSION_MESSAGE_ID, messageId.intValue());
        }
        messageIntent.putExtra(UpsightLifeCycleTracker.STARTED_FROM_PUSH, true);
        messageIntent.addFlags(268435456);
        return messageIntent.putExtra(SessionManager.SESSION_EXTRA, bundle);
    }
}
