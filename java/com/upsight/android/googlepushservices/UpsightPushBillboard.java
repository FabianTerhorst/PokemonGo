package com.upsight.android.googlepushservices;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightGooglePushServicesExtension;
import com.upsight.android.marketing.UpsightBillboard;
import com.upsight.android.marketing.UpsightBillboard.Handler;

public class UpsightPushBillboard {

    private static class NoOpBillboard extends UpsightBillboard {
        private NoOpBillboard() {
        }

        protected UpsightBillboard setUp(UpsightContext upsight) throws IllegalStateException {
            return this;
        }

        public void destroy() {
        }
    }

    public static UpsightBillboard create(UpsightContext upsight, Handler handler) throws IllegalArgumentException, IllegalStateException {
        UpsightGooglePushServicesExtension extension = (UpsightGooglePushServicesExtension) upsight.getUpsightExtension(UpsightGooglePushServicesExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().createPushBillboard(upsight, handler);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.googlepushservices must be registered in your Android Manifest", new Object[0]);
        }
        return new NoOpBillboard();
    }
}
