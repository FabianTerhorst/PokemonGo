package com.upsight.android.unity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.upsight.android.marketing.UpsightBillboard.AttachParameters;
import com.upsight.android.marketing.UpsightBillboardHandlers.DefaultHandler;
import com.upsight.android.marketing.UpsightPurchase;
import com.upsight.android.marketing.UpsightReward;
import java.util.List;
import org.json.JSONObject;

public class BillboardHandler extends DefaultHandler {
    protected static final String TAG = "UpsightBillboardHandler";
    @Nullable
    private static String mCurrentScope;

    public BillboardHandler(Activity activity) {
        super(activity);
    }

    @Nullable
    public AttachParameters onAttach(@NonNull String scope) {
        AttachParameters params = super.onAttach(scope);
        if (params != null) {
            mCurrentScope = scope;
            UnityBridge.UnitySendMessage("onBillboardAppear", scope);
        }
        return params;
    }

    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
        UnityBridge.UnitySendMessage("onBillboardDismiss", mCurrentScope);
        mCurrentScope = null;
    }

    public void onNextView() {
        super.onNextView();
        Log.i(TAG, "onNextView");
    }

    public void onPurchases(@NonNull List<UpsightPurchase> purchases) {
        super.onPurchases(purchases);
        Log.i(TAG, "onPurchases");
        for (UpsightPurchase p : purchases) {
            try {
                JSONObject json = new JSONObject();
                json.put("productIdentifier", p.getProduct());
                json.put("quantity", p.getQuantity());
                json.put("billboardScope", mCurrentScope);
                UnityBridge.UnitySendMessage("billboardDidReceivePurchase", json.toString());
            } catch (Exception e) {
                Log.i(TAG, "Error creating JSON" + e.getMessage());
            }
        }
    }

    public void onRewards(@NonNull List<UpsightReward> rewards) {
        super.onRewards(rewards);
        Log.i(TAG, "onRewards");
        for (UpsightReward r : rewards) {
            try {
                JSONObject json = new JSONObject();
                json.put("productIdentifier", r.getProduct());
                json.put("quantity", r.getQuantity());
                json.put("signatureData", r.getSignatureData());
                json.put("billboardScope", mCurrentScope);
                UnityBridge.UnitySendMessage("billboardDidReceiveReward", json.toString());
            } catch (Exception e) {
                Log.i(TAG, "Error creating JSON" + e.getMessage());
            }
        }
    }

    @Nullable
    public static String getCurrentScope() {
        return mCurrentScope;
    }
}
