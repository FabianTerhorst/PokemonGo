package com.upsight.mediation.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.mediation.log.FuseLog;
import com.voxelbusters.nativeplugins.defines.Keys.Billing;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.json.JSONObject;

public class InAppBillingConnection {
    @Nullable
    private Method asInterfaceMethod = null;
    @Nullable
    private Activity boundActivity = null;
    @Nullable
    private ServiceConnection connection = null;
    @Nullable
    private Method getDetailsMethod = null;
    @Nullable
    private Object iabService = null;
    @NonNull
    private Status status = Status.NotConnected;

    public enum Status {
        Unavailable,
        NotConnected,
        Pending,
        Connected
    }

    public InAppBillingConnection() {
        try {
            this.getDetailsMethod = Class.forName("com.android.vending.billing.IInAppBillingService").getMethod("getSkuDetails", new Class[]{Integer.TYPE, String.class, String.class, Bundle.class});
            this.asInterfaceMethod = Class.forName("com.android.vending.billing.IInAppBillingService$Stub").getMethod("asInterface", new Class[]{IBinder.class});
        } catch (Throwable t) {
            FuseLog.public_w("Fuse-InAppBillingConnection", "com.android.vending.billing.IInAppBillingService is NOT available", t);
            this.status = Status.Unavailable;
        }
    }

    public void initConnection(@NonNull Activity activity) {
        if (this.status == Status.NotConnected && this.asInterfaceMethod != null) {
            this.status = Status.Pending;
            this.boundActivity = activity;
            this.connection = new ServiceConnection() {
                public void onServiceDisconnected(ComponentName name) {
                    InAppBillingConnection.this.closeConnection();
                }

                public void onServiceConnected(ComponentName name, IBinder service) {
                    try {
                        InAppBillingConnection.this.iabService = InAppBillingConnection.this.asInterfaceMethod.invoke(null, new Object[]{service});
                        InAppBillingConnection.this.status = Status.Connected;
                    } catch (Throwable t) {
                        FuseLog.e("Fuse-InAppBillingConnection", "Exception thrown while attempting to retrieve IInAppBillingService", t);
                        InAppBillingConnection.this.closeConnection();
                        InAppBillingConnection.this.status = Status.Unavailable;
                    }
                }
            };
            Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.android.vending");
            this.boundActivity.bindService(serviceIntent, this.connection, 1);
        }
    }

    public void closeConnection() {
        if (!(this.connection == null || this.boundActivity == null)) {
            this.boundActivity.unbindService(this.connection);
        }
        this.iabService = null;
        this.connection = null;
        this.boundActivity = null;
        this.status = Status.NotConnected;
    }

    public boolean isConnected() {
        return this.status == Status.Connected;
    }

    public boolean isPending() {
        return this.status == Status.Pending;
    }

    @Nullable
    public String getLocalPriceForProductId(@NonNull final String productId) {
        if (this.status != Status.Connected || this.getDetailsMethod == null || this.boundActivity == null) {
            return null;
        }
        new Bundle().putStringArrayList("ITEM_ID_LIST", new ArrayList<String>() {
        });
        try {
            Bundle returnBundle = (Bundle) this.getDetailsMethod.invoke(this.iabService, new Object[]{Integer.valueOf(3), this.boundActivity.getPackageName(), "inapp", requestBundle});
            if (returnBundle != null && returnBundle.getInt(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE) == 0) {
                ArrayList<String> details = returnBundle.getStringArrayList("DETAILS_LIST");
                if (details != null && details.size() == 1) {
                    JSONObject itemDetails = new JSONObject((String) details.get(0));
                    String sku = itemDetails.getString(Billing.PRODUCT_IDENTIFIER);
                    if (sku != null && sku.equals(productId)) {
                        return itemDetails.getString("price");
                    }
                }
            }
        } catch (Throwable t) {
            FuseLog.e("Fuse-InAppBillingConnection", "Exception thrown while getting SKU Details", t);
        }
        return null;
    }
}
