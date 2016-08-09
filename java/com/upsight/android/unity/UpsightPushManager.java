package com.upsight.android.unity;

import android.app.Activity;
import android.util.Log;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.googlepushservices.UpsightGooglePushServices;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnRegisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnUnregisterListener;
import com.upsight.android.googlepushservices.UpsightPushBillboard;
import com.upsight.android.marketing.UpsightBillboard;

public class UpsightPushManager implements IUpsightExtensionManager {
    protected static final String TAG = "Upsight-UnityPush";
    private UpsightBillboard mPushBillboard;
    private BillboardHandler mPushBillboardHandler;
    private UpsightContext mUpsight;

    public void init(UpsightContext context) {
        this.mUpsight = context;
        Activity activity = UnityBridge.getActivity();
        if (activity != null) {
            this.mPushBillboardHandler = new BillboardHandler(activity);
            this.mPushBillboard = UpsightPushBillboard.create(this.mUpsight, this.mPushBillboardHandler);
        }
    }

    public void unregisterForPushNotifications() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new Runnable() {
                public void run() {
                    Log.i(UpsightPushManager.TAG, "unregistering for push notifications");
                    UpsightGooglePushServices.unregister(UpsightPushManager.this.mUpsight, new OnUnregisterListener() {
                        public void onSuccess() {
                            Log.e(UpsightPushManager.TAG, "unregistration succeeded");
                        }

                        public void onFailure(UpsightException e) {
                            Log.e(UpsightPushManager.TAG, "unregistration failed: " + e);
                        }
                    });
                }
            });
        }
    }

    public void registerForPushNotifications() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new Runnable() {
                public void run() {
                    Log.i(UpsightPushManager.TAG, "registering for push notifications");
                    UpsightGooglePushServices.register(UpsightPushManager.this.mUpsight, new OnRegisterListener() {
                        public void onSuccess(String arg0) {
                            Log.e(UpsightPushManager.TAG, "registration succeeded");
                        }

                        public void onFailure(UpsightException ex) {
                            Log.e(UpsightPushManager.TAG, "registration failed: " + ex);
                        }
                    });
                }
            });
        }
    }

    public void onApplicationPaused() {
        if (this.mUpsight != null) {
            try {
                if (this.mPushBillboard != null) {
                    this.mPushBillboard.destroy();
                    this.mPushBillboard = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onApplicationResumed() {
        if (this.mUpsight != null) {
            try {
                if (this.mPushBillboard == null) {
                    this.mPushBillboard = UpsightPushBillboard.create(this.mUpsight, this.mPushBillboardHandler);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
