package com.upsight.android.googlepushservices.internal;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.text.TextUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightGooglePushServicesExtension;
import com.upsight.android.analytics.event.content.UpsightContentUnrenderedEvent;
import com.upsight.android.analytics.event.content.UpsightContentUnrenderedEvent.Builder;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.internal.content.MarketingContent;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.R;

public final class PushIntentService extends IntentService {
    private static final String ACTION_ACTIVITY = "activity";
    private static final String ACTION_CONTENT_UNIT = "content_id";
    private static final String ACTION_PLACEMENT = "placement";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_NAME = "name";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_PARAMETERS = "parameters";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_PARAMETERS_KEY_CONTENT_ID = "content_id";
    private static final Integer INVALID_MSG_ID = Integer.valueOf(0);
    private static final String LOG_TAG = PushIntentService.class.getSimpleName();
    private static final String SERVICE_NAME = "UpsightGcmPushIntentService";
    private static final String URI_HOST = "com.playhaven.android";
    private static final String URI_SCHEME = "playhaven";
    @Inject
    GoogleCloudMessaging mGcm;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$googlepushservices$internal$PushIntentService$UriTypes = new int[UriTypes.values().length];

        static {
            try {
                $SwitchMap$com$upsight$android$googlepushservices$internal$PushIntentService$UriTypes[UriTypes.CUSTOM.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$googlepushservices$internal$PushIntentService$UriTypes[UriTypes.DEFAULT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$upsight$android$googlepushservices$internal$PushIntentService$UriTypes[UriTypes.ACTIVITY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$upsight$android$googlepushservices$internal$PushIntentService$UriTypes[UriTypes.PLACEMENT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    class PushIds {
        final Integer campaignId;
        final Integer contentId;
        final Integer messageId;

        private PushIds(Integer messageId, Integer campaignId, Integer contentId) {
            this.messageId = messageId;
            this.campaignId = campaignId;
            this.contentId = contentId;
        }
    }

    public enum PushParams {
        message_id,
        msg_campaign_id,
        content_id,
        title,
        text,
        uri
    }

    public enum UriTypes {
        DEFAULT,
        CUSTOM,
        INVALID,
        PLACEMENT,
        ACTIVITY
    }

    public PushIntentService() {
        super(SERVICE_NAME);
    }

    private static Integer parseAsInt(String stringValue, Integer defaultValue, UpsightLogger logger) {
        Integer integerValue = defaultValue;
        if (!TextUtils.isEmpty(stringValue) && TextUtils.isDigitsOnly(stringValue)) {
            try {
                integerValue = Integer.valueOf(Integer.parseInt(stringValue));
            } catch (NumberFormatException e) {
                if (defaultValue == null) {
                    logger.e(LOG_TAG, e, String.format("Could not parse %s. Setting to null.", new Object[]{stringValue}), new Object[0]);
                } else {
                    logger.e(LOG_TAG, e, String.format("Could not parse %s. Setting to %d.", new Object[]{stringValue, defaultValue}), new Object[0]);
                }
            }
        }
        return integerValue;
    }

    protected void onHandleIntent(Intent intent) {
        ((UpsightGooglePushServicesComponent) ((UpsightGooglePushServicesExtension) Upsight.createContext(this).getUpsightExtension(UpsightGooglePushServicesExtension.EXTENSION_NAME)).getComponent()).inject(this);
        if ("gcm".equals(this.mGcm.getMessageType(intent))) {
            Bundle extras = intent.getExtras();
            if (!(extras.isEmpty() || TextUtils.isEmpty(extras.getString(PushParams.message_id.name())))) {
                interpretPushEvent(extras);
            }
        }
        PushBroadcastReceiver.completeWakefulIntent(intent);
    }

    PushIds parsePushIds(Uri uri, Bundle extras, UpsightLogger logger) {
        return new PushIds(parseAsInt(extras.getString(PushParams.message_id.name()), INVALID_MSG_ID, logger), parseAsInt(extras.getString(PushParams.msg_campaign_id.name()), null, logger), parseAsInt(uri.getQueryParameter(PushParams.content_id.name()), null, logger));
    }

    private void interpretPushEvent(Bundle extras) {
        Uri uri = null;
        String uriString = extras.getString(PushParams.uri.name());
        if (!TextUtils.isEmpty(uriString)) {
            uri = Uri.parse(uriString);
        }
        if (uri != null) {
            UpsightContext upsight = Upsight.createContext(this);
            UpsightLogger logger = upsight.getLogger();
            PushIds ids = parsePushIds(uri, extras, logger);
            Intent messageIntent = null;
            switch (AnonymousClass1.$SwitchMap$com$upsight$android$googlepushservices$internal$PushIntentService$UriTypes[checkUri(logger, uri).ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    messageIntent = new Intent("android.intent.action.VIEW", uri);
                    break;
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                    messageIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    break;
                case 3:
                    try {
                        messageIntent = new Intent(this, Class.forName(uri.getQueryParameter(ACTION_ACTIVITY)));
                        break;
                    } catch (ClassNotFoundException e) {
                        logger.e(LOG_TAG, e, "Could not parse class name", new Object[0]);
                        break;
                    }
                case 4:
                    JSONObject contentProviderBundle = new JSONObject();
                    try {
                        contentProviderBundle.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_NAME, MarketingContent.UPSIGHT_CONTENT_PROVIDER);
                        JSONObject parameters = new JSONObject();
                        parameters.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_PARAMETERS_KEY_CONTENT_ID, ids.contentId);
                        contentProviderBundle.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_PARAMETERS, parameters);
                    } catch (JSONException e2) {
                        logger.e(LOG_TAG, e2, "Could not construct \"content_provider\" bundle in \"upsight.content.unrendered\"", new Object[0]);
                    }
                    Builder unrenderedEvent = UpsightContentUnrenderedEvent.createBuilder(contentProviderBundle).setScope("com_upsight_push_scope");
                    if (ids.campaignId != null) {
                        unrenderedEvent.setCampaignId(ids.campaignId);
                    }
                    unrenderedEvent.record(upsight);
                    messageIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    break;
            }
            if (messageIntent != null) {
                showNotification(messageIntent, ids.campaignId, ids.messageId, extras.getString(PushParams.title.name()), extras.getString(PushParams.text.name()));
            }
        }
    }

    public UriTypes checkUri(UpsightLogger logger, Uri uri) {
        String host = uri.getHost();
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(host) || TextUtils.isEmpty(scheme)) {
            logger.e(LOG_TAG, String.format("Invalid URI, host or scheme is null or empty: %s.", new Object[]{uri}), new Object[0]);
            return UriTypes.INVALID;
        } else if (!URI_SCHEME.equals(scheme) || !URI_HOST.equals(host)) {
            try {
                if (getPackageManager().resolveActivity(new Intent().setData(uri), 0) == null) {
                    return UriTypes.INVALID;
                }
                return UriTypes.CUSTOM;
            } catch (Exception e) {
                logger.e(LOG_TAG, String.format("Nothing registered for %s", new Object[]{uri}), new Object[0]);
                return UriTypes.INVALID;
            }
        } else if (uri.getQueryParameter(ACTION_ACTIVITY) != null) {
            return UriTypes.ACTIVITY;
        } else {
            if (uri.getQueryParameter(ACTION_PLACEMENT) != null) {
                return UriTypes.PLACEMENT;
            }
            if (uri.getQueryParameter(CONTENT_UNRENDERED_CONTENT_PROVIDER_PARAMETERS_KEY_CONTENT_ID) != null) {
                return UriTypes.PLACEMENT;
            }
            return UriTypes.DEFAULT;
        }
    }

    private void showNotification(Intent messageIntent, Integer campaignId, Integer messageId, String title, String text) {
        ((NotificationManager) getSystemService("notification")).notify(messageId.intValue(), new NotificationCompat.Builder(this).setAutoCancel(true).setSmallIcon(getApplicationInfo().icon).setContentTitle(title).setContentText(text).setContentIntent(PendingIntent.getService(this, 0, PushClickIntentService.newIntent(this, messageIntent, campaignId, messageId), 268435456)).setStyle(new BigTextStyle().bigText(text)).build());
    }
}
