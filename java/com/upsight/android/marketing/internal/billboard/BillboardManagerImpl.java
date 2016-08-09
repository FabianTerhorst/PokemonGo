package com.upsight.android.marketing.internal.billboard;

import android.content.Intent;
import android.text.TextUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.UpsightBillboard.AttachParameters;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContent.ScopedAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.PurchasesEvent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.RewardsEvent;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import java.util.HashMap;
import java.util.Map;

class BillboardManagerImpl implements UpsightBillboardManager {
    private final MarketingContentStore mContentStore;
    private final Map<String, Billboard> mUnfilledBillboards = new HashMap();
    private final UpsightContext mUpsight;

    BillboardManagerImpl(UpsightContext upsight, MarketingContentStore contentStore, Bus bus) {
        this.mUpsight = upsight;
        this.mContentStore = contentStore;
        bus.register(this);
    }

    public synchronized boolean registerBillboard(Billboard billboard) {
        boolean isSuccessful;
        isSuccessful = false;
        if (billboard != null) {
            String billboardScope = billboard.getScope();
            if (!TextUtils.isEmpty(billboardScope) && billboard.getHandler() != null && this.mUnfilledBillboards.get(billboardScope) == null) {
                isSuccessful = true;
                this.mUnfilledBillboards.put(billboardScope, billboard);
                for (String id : this.mContentStore.getIdsForScope(billboardScope)) {
                    if (tryAttachBillboard(id, billboard)) {
                        break;
                    }
                }
            }
        }
        return isSuccessful;
    }

    public synchronized boolean unregisterBillboard(Billboard billboard) {
        return this.mUnfilledBillboards.remove(billboard.getScope()) != null;
    }

    @Subscribe
    public synchronized void handleAvailabilityEvent(ScopedAvailabilityEvent event) {
        for (String scope : event.getScopes()) {
            if (tryAttachBillboard(event.getId(), (Billboard) this.mUnfilledBillboards.get(scope))) {
                break;
            }
        }
    }

    @Subscribe
    public synchronized void handleActionEvent(RewardsEvent event) {
        MarketingContent content = (MarketingContent) this.mContentStore.get(event.mId);
        if (content != null) {
            Billboard billboard = content.getBoundBillboard();
            if (billboard != null) {
                billboard.getHandler().onRewards(event.mRewards);
            }
        }
    }

    @Subscribe
    public synchronized void handleActionEvent(PurchasesEvent event) {
        MarketingContent content = (MarketingContent) this.mContentStore.get(event.mId);
        if (content != null) {
            Billboard billboard = content.getBoundBillboard();
            if (billboard != null) {
                billboard.getHandler().onPurchases(event.mPurchases);
            }
        }
    }

    private boolean tryAttachBillboard(String id, Billboard billboard) {
        MarketingContent content = (MarketingContent) this.mContentStore.get(id);
        if (billboard == null || content == null || !content.isAvailable() || content.getContentMediator() == null) {
            return false;
        }
        AttachParameters params = billboard.getHandler().onAttach(billboard.getScope());
        if (params == null || params.getActivity() == null) {
            return false;
        }
        this.mUnfilledBillboards.remove(billboard.getScope());
        content.bindBillboard(billboard);
        Intent intent = new Intent(this.mUpsight, BillboardManagementActivity.class).putExtra("marketingContentId", content.getId()).putExtra("marketingContentPreferredStyle", params.getPreferredPresentationStyle()).addFlags(268435456);
        Integer dialogTheme = params.getDialogTheme();
        if (dialogTheme != null) {
            intent.putExtra("marketingContentDialogTheme", dialogTheme.intValue());
        }
        this.mUpsight.startActivity(intent);
        return true;
    }
}
