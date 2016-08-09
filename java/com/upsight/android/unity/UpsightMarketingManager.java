package com.upsight.android.unity;

import android.support.annotation.NonNull;
import android.util.Log;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.UpsightBillboard;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpsightMarketingManager implements IUpsightExtensionManager {
    protected static final String TAG = "Upsight-UnityMarketing";
    @NonNull
    private Map<String, BillboardInfo> mBillboardMap = new HashMap();
    @NonNull
    private Set<String> mPreparedBillboards = new HashSet();
    private UpsightContext mUpsight;

    private static class BillboardInfo {
        @NonNull
        public final UpsightBillboard billboard;
        @NonNull
        public final BillboardHandler handler;

        public BillboardInfo(@NonNull UpsightBillboard billboard, @NonNull BillboardHandler handler) {
            this.billboard = billboard;
            this.handler = handler;
        }
    }

    public void init(UpsightContext context) {
        this.mUpsight = context;
    }

    public boolean isContentReadyForBillboardWithScope(@NonNull String scope) {
        boolean z = false;
        if (this.mUpsight != null) {
            try {
                z = UpsightMarketingContentStore.isContentReady(this.mUpsight, scope);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public void prepareBillboard(@NonNull final String scope) {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new Runnable() {
                public void run() {
                    if (!UpsightMarketingManager.this.mBillboardMap.containsKey(scope) && !UpsightMarketingManager.this.mPreparedBillboards.contains(scope)) {
                        BillboardHandler handler = new BillboardHandler(UnityBridge.getActivity());
                        UpsightMarketingManager.this.mBillboardMap.put(scope, new BillboardInfo(UpsightBillboard.create(UpsightMarketingManager.this.mUpsight, scope, handler), handler));
                    }
                }
            });
        }
    }

    public void destroyBillboard(@NonNull final String scope) {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new Runnable() {
                public void run() {
                    Log.i(UpsightMarketingManager.TAG, "Destroying billboard for scope: " + scope);
                    ((BillboardInfo) UpsightMarketingManager.this.mBillboardMap.remove(scope)).billboard.destroy();
                    UpsightMarketingManager.this.mPreparedBillboards.remove(scope);
                }
            });
        }
    }

    public void onApplicationPaused() {
        if (this.mUpsight != null) {
            final String currentScope = BillboardHandler.getCurrentScope();
            UnityBridge.runSafelyOnUiThread(new Runnable() {
                public void run() {
                    if (currentScope != null) {
                        UpsightMarketingManager.this.mBillboardMap.remove(currentScope);
                    }
                    UpsightMarketingManager.this.mPreparedBillboards.addAll(UpsightMarketingManager.this.mBillboardMap.keySet());
                    for (String scope : UpsightMarketingManager.this.mBillboardMap.keySet()) {
                        ((BillboardInfo) UpsightMarketingManager.this.mBillboardMap.get(scope)).billboard.destroy();
                    }
                    UpsightMarketingManager.this.mBillboardMap.clear();
                }
            });
        }
    }

    public void onApplicationResumed() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new Runnable() {
                public void run() {
                    for (String scope : UpsightMarketingManager.this.mPreparedBillboards) {
                        BillboardHandler handler = new BillboardHandler(UnityBridge.getActivity());
                        UpsightMarketingManager.this.mBillboardMap.put(scope, new BillboardInfo(UpsightBillboard.create(UpsightMarketingManager.this.mUpsight, scope, handler), handler));
                    }
                    UpsightMarketingManager.this.mPreparedBillboards.clear();
                }
            });
        }
    }
}
