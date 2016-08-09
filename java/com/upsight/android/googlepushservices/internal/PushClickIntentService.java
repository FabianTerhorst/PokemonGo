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
import com.upsight.android.analytics.event.content.UpsightContentUnrenderedEvent;
import com.upsight.android.analytics.internal.session.ApplicationStatus;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.analytics.internal.session.SessionInitializerImpl;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import com.upsight.android.marketing.internal.content.DefaultContentMediator;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

public class PushClickIntentService extends IntentService {
    private static final String BUNDLE_KEY_MESSAGE_INTENT = "messageIntent";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_NAME = "name";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_PARAMETERS = "parameters";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_PARAMETERS_KEY_CONTENT_ID = "content_id";
    private static final String LOG_TAG = PushClickIntentService.class.getSimpleName();
    private static final String PARAM_KEY_IS_DISPATCH_FROM_FOREGROUND = "isDispatchFromForeground";
    private static final String PARAM_KEY_PUSH_CONTENT_ID = "contentId";
    private static final String SERVICE_NAME = "UpsightGcmPushClickIntentService";
    @Inject
    SessionManager mSessionManager;

    static Intent newIntent(Context context, Intent messageIntent, boolean isDispatchFromForeground, Integer campaignId, Integer messageId, Integer contentId) {
        return new Intent(context.getApplicationContext(), PushClickIntentService.class).putExtra(BUNDLE_KEY_MESSAGE_INTENT, appendMessageIntentBundle(messageIntent, isDispatchFromForeground, campaignId, messageId, contentId));
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
        if (extras.containsKey(SessionManager.SESSION_MESSAGE_ID)) {
            Builder clickEvent = UpsightCommClickEvent.createBuilder(Integer.valueOf(extras.getInt(SessionManager.SESSION_MESSAGE_ID)));
            if (extras.containsKey(SessionManager.SESSION_CAMPAIGN_ID)) {
                clickEvent.setMsgCampaignId(Integer.valueOf(extras.getInt(SessionManager.SESSION_CAMPAIGN_ID)));
            }
            clickEvent.record(upsight);
        }
        if (extras.containsKey(PARAM_KEY_PUSH_CONTENT_ID)) {
            JSONObject contentProviderBundle = new JSONObject();
            try {
                contentProviderBundle.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_NAME, DefaultContentMediator.CONTENT_PROVIDER);
                JSONObject parameters = new JSONObject();
                parameters.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_PARAMETERS_KEY_CONTENT_ID, extras.getInt(PARAM_KEY_PUSH_CONTENT_ID));
                contentProviderBundle.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_PARAMETERS, parameters);
                UpsightContentUnrenderedEvent.Builder unrenderedEvent = UpsightContentUnrenderedEvent.createBuilder(contentProviderBundle).setScope("com_upsight_push_scope");
                if (extras.containsKey(SessionManager.SESSION_CAMPAIGN_ID)) {
                    unrenderedEvent.setCampaignId(Integer.valueOf(extras.getInt(SessionManager.SESSION_CAMPAIGN_ID)));
                }
                unrenderedEvent.record(upsight);
            } catch (JSONException e) {
                upsight.getLogger().e(LOG_TAG, e, "Could not construct \"content_provider\" bundle in \"upsight.content.unrendered\"", new Object[0]);
            }
        }
        if (!extras.getBoolean(PARAM_KEY_IS_DISPATCH_FROM_FOREGROUND, false)) {
            ApplicationStatus status = (ApplicationStatus) upsight.getDataStore().fetchObservable(ApplicationStatus.class).toBlocking().first();
            if (!(status == null || State.BACKGROUND.equals(status.getState()))) {
                return;
            }
        }
        startActivity(messageIntent);
    }

    protected static Intent appendMessageIntentBundle(Intent messageIntent, boolean isDispatchFromForeground, Integer campaignId, Integer messageId, Integer contentId) {
        Bundle bundle = new Bundle();
        if (campaignId != null) {
            bundle.putInt(SessionManager.SESSION_CAMPAIGN_ID, campaignId.intValue());
        }
        if (messageId != null) {
            bundle.putInt(SessionManager.SESSION_MESSAGE_ID, messageId.intValue());
        }
        if (contentId != null) {
            bundle.putInt(PARAM_KEY_PUSH_CONTENT_ID, contentId.intValue());
        }
        bundle.putBoolean(PARAM_KEY_IS_DISPATCH_FROM_FOREGROUND, isDispatchFromForeground);
        messageIntent.putExtra(UpsightLifeCycleTracker.STARTED_FROM_PUSH, true);
        messageIntent.addFlags(872415232);
        return messageIntent.putExtra(SessionManager.SESSION_EXTRA, bundle);
    }
}
