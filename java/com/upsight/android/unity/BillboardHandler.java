package com.upsight.android.unity;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import com.upsight.android.marketing.UpsightBillboard.Dimensions;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import com.upsight.android.marketing.UpsightBillboardHandlers.DefaultHandler;
import com.upsight.android.marketing.UpsightPurchase;
import com.upsight.android.marketing.UpsightReward;
import java.util.List;
import java.util.Set;
import org.json.JSONObject;

public class BillboardHandler extends DefaultHandler {
    protected static final String TAG = "UpsightBillboardHandler";
    private String mCurrentScope;
    private UpsightPlugin mPlugin;

    public BillboardHandler(Activity activity, UpsightPlugin plugin) {
        super(activity);
        this.mPlugin = plugin;
    }

    public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> dimensions) {
        this.mCurrentScope = scope;
        ViewGroup viewGroup = super.onAttach(scope, presentation, dimensions);
        if (viewGroup != null) {
            this.mPlugin.setHasActiveBillboard(true);
            this.mPlugin.UnitySendMessage("onBillboardAppear", scope);
        }
        return viewGroup;
    }

    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
        this.mPlugin.UnitySendMessage("onBillboardDismiss", this.mCurrentScope);
        this.mPlugin.removeBillboardFromMap(this.mCurrentScope);
        this.mPlugin.setHasActiveBillboard(false);
    }

    public void onNextView() {
        super.onNextView();
        Log.i(TAG, "onNextView");
    }

    public void onPurchases(List<UpsightPurchase> purchases) {
        super.onPurchases(purchases);
        Log.i(TAG, "onPurchases");
        for (UpsightPurchase p : purchases) {
            try {
                JSONObject json = new JSONObject();
                json.put("productIdentifier", p.getProduct());
                json.put("quantity", p.getQuantity());
                json.put("billboardScope", this.mCurrentScope);
                this.mPlugin.UnitySendMessage("billboardDidReceivePurchase", json.toString());
            } catch (Exception e) {
                Log.i(TAG, "Error creating JSON" + e.getMessage());
            }
        }
    }

    public void onRewards(List<UpsightReward> rewards) {
        super.onRewards(rewards);
        Log.i(TAG, "onRewards");
        for (UpsightReward r : rewards) {
            try {
                JSONObject json = new JSONObject();
                json.put("productIdentifier", r.getProduct());
                json.put("quantity", r.getQuantity());
                json.put("signatureData", r.getSignatureData());
                json.put("billboardScope", this.mCurrentScope);
                this.mPlugin.UnitySendMessage("billboardDidReceiveReward", json.toString());
            } catch (Exception e) {
                Log.i(TAG, "Error creating JSON" + e.getMessage());
            }
        }
    }
}
