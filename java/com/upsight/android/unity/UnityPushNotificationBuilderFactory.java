package com.upsight.android.unity;

import android.support.v4.app.NotificationCompat.Builder;
import com.upsight.android.UpsightContext;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;

public class UnityPushNotificationBuilderFactory extends Default {
    private static final String NOTIFICATION_ICON_RES_NAME = "upsight_notification_icon";
    private static final String NOTIFICATION_ICON_RES_TYPE = "drawable";

    public Builder getNotificationBuilder(UpsightContext upsight, String title, String message, String imageUrl) {
        int iconResId = upsight.getResources().getIdentifier(NOTIFICATION_ICON_RES_NAME, NOTIFICATION_ICON_RES_TYPE, upsight.getPackageName());
        if (iconResId == 0) {
            iconResId = upsight.getApplicationInfo().icon;
        }
        return super.getNotificationBuilder(upsight, title, message, imageUrl).setSmallIcon(iconResId);
    }
}
