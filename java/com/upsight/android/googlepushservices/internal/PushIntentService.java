package com.upsight.android.googlepushservices.internal;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightGooglePushServicesExtension;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import javax.inject.Inject;
import spacemadness.com.lunarconsole.R;

public final class PushIntentService extends IntentService {
    private static final String ACTION_ACTIVITY = "activity";
    private static final String ACTION_CONTENT_UNIT = "content_id";
    private static final String ACTION_PLACEMENT = "placement";
    private static final Integer INVALID_MSG_ID = Integer.valueOf(0);
    private static final String LOG_TAG = PushIntentService.class.getSimpleName();
    private static final String NOTIFICATION_BUILDER_FACTORY_KEY_NAME = "com.upsight.notification_builder_factory";
    private static final String SERVICE_NAME = "UpsightGcmPushIntentService";
    private static final String URI_HOST = "com.playhaven.android";
    private static final String URI_SCHEME = "playhaven";
    @Inject
    GoogleCloudMessaging mGcm;
    private UpsightPushNotificationBuilderFactory mNotificationBuilderFactory;
    @Inject
    UpsightContext mUpsight;

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
        uri,
        image_url
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
        UpsightGooglePushServicesExtension extension = (UpsightGooglePushServicesExtension) Upsight.createContext(this).getUpsightExtension(UpsightGooglePushServicesExtension.EXTENSION_NAME);
        if (extension != null) {
            ((UpsightGooglePushServicesComponent) extension.getComponent()).inject(this);
            if ("gcm".equals(this.mGcm.getMessageType(intent))) {
                Bundle extras = intent.getExtras();
                if (!(extras.isEmpty() || TextUtils.isEmpty(extras.getString(PushParams.message_id.name())))) {
                    interpretPushEvent(extras);
                }
            }
            PushBroadcastReceiver.completeWakefulIntent(intent);
        }
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
            UpsightLogger logger = this.mUpsight.getLogger();
            PushIds ids = parsePushIds(uri, extras, logger);
            Intent messageIntent = null;
            boolean isDispatchFromForeground = false;
            switch (AnonymousClass1.$SwitchMap$com$upsight$android$googlepushservices$internal$PushIntentService$UriTypes[checkUri(logger, uri).ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    messageIntent = new Intent("android.intent.action.VIEW", uri);
                    isDispatchFromForeground = true;
                    break;
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                    messageIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    isDispatchFromForeground = false;
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                    try {
                        isDispatchFromForeground = true;
                        messageIntent = new Intent(this, Class.forName(uri.getQueryParameter(ACTION_ACTIVITY)));
                        break;
                    } catch (ClassNotFoundException e) {
                        logger.e(LOG_TAG, e, "Could not parse class name", new Object[0]);
                        break;
                    }
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                    messageIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    isDispatchFromForeground = false;
                    break;
            }
            if (messageIntent != null) {
                showNotification(messageIntent, isDispatchFromForeground, ids.campaignId, ids.messageId, ids.contentId, extras.getString(PushParams.title.name()), extras.getString(PushParams.text.name()), extras.getString(PushParams.image_url.name()));
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
            if (uri.getQueryParameter(ACTION_CONTENT_UNIT) != null) {
                return UriTypes.PLACEMENT;
            }
            return UriTypes.DEFAULT;
        }
    }

    UpsightPushNotificationBuilderFactory loadNotificationBuilderFactory() {
        UpsightLogger logger = this.mUpsight.getLogger();
        try {
            Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), 128).metaData;
            if (bundle != null && bundle.containsKey(NOTIFICATION_BUILDER_FACTORY_KEY_NAME)) {
                try {
                    Class<?> clazz = Class.forName(bundle.getString(NOTIFICATION_BUILDER_FACTORY_KEY_NAME));
                    if (UpsightPushNotificationBuilderFactory.class.isAssignableFrom(clazz)) {
                        return (UpsightPushNotificationBuilderFactory) clazz.newInstance();
                    }
                    logger.e(Upsight.LOG_TAG, String.format("Class %s must implement %s!", new Object[]{clazz.getName(), UpsightPushNotificationBuilderFactory.class.getName()}), new Object[0]);
                } catch (ClassNotFoundException e) {
                    logger.e(Upsight.LOG_TAG, String.format("Unexpected error: Class: %s was not found.", new Object[]{customBuilderClassName}), e);
                } catch (InstantiationException e2) {
                    logger.e(Upsight.LOG_TAG, String.format("Unexpected error: Class: %s does not have public access.", new Object[]{customBuilderClassName}), e2);
                } catch (IllegalAccessException e3) {
                    logger.e(Upsight.LOG_TAG, String.format("Unexpected error: Class: %s could not be instantiated.", new Object[]{customBuilderClassName}), e3);
                }
            }
        } catch (NameNotFoundException e4) {
            logger.e(Upsight.LOG_TAG, "Unexpected error: Package name missing!?", e4);
        }
        return new Default();
    }

    private void showNotification(Intent messageIntent, boolean isDispatchFromForeground, Integer campaignId, Integer messageId, Integer contentId, String title, String text, String imageUrl) {
        PendingIntent notifyIntent = PendingIntent.getService(this, messageId.intValue(), PushClickIntentService.newIntent(this, messageIntent, isDispatchFromForeground, campaignId, messageId, contentId), 268435456);
        if (this.mNotificationBuilderFactory == null) {
            this.mNotificationBuilderFactory = loadNotificationBuilderFactory();
        }
        ((NotificationManager) getSystemService("notification")).notify(messageId.intValue(), this.mNotificationBuilderFactory.getNotificationBuilder(this.mUpsight, title, text, imageUrl).setContentIntent(notifyIntent).setAutoCancel(true).build());
    }
}
