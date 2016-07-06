package com.nianticlabs.nia.iap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.lang.ref.WeakReference;

public class PurchaseActivity extends Activity {
    public static final int REQUEST_PURCHASE_RESULT = 10009;
    private static String TAG = "PurchaseActivity";
    private GoogleInAppBillingProvider billingProvider;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.billingProvider = null;
        WeakReference<GoogleInAppBillingProvider> weakGoogleInAppBillingProvider = GoogleInAppBillingProvider.getInstance();
        if (weakGoogleInAppBillingProvider != null) {
            this.billingProvider = (GoogleInAppBillingProvider) weakGoogleInAppBillingProvider.get();
        }
        if (this.billingProvider == null) {
            throw new RuntimeException("Unable to locate GoogleInAppBillingProvider");
        }
    }

    protected void onResume() {
        super.onResume();
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != 0) {
            Log.e(TAG, "Google Play Services not available: " + resultCode);
            finish();
            return;
        }
        this.billingProvider.startBuyIntent(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PURCHASE_RESULT /*10009*/:
                this.billingProvider.forwardedOnActivityResult(resultCode, data);
                break;
            default:
                Log.e(TAG, "Unandled requestCode: " + requestCode);
                break;
        }
        finish();
    }
}
