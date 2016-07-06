package com.upsight.android.marketing.internal.billboard;

import android.text.TextUtils;
import android.view.ViewGroup;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.analytics.internal.session.ApplicationStatus;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.marketing.UpsightBillboard.Dimensions;
import com.upsight.android.marketing.UpsightBillboard.Dimensions.LayoutOrientation;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContent.ScopedAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContent.ScopelessAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.DestroyEvent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.PurchasesEvent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.RewardsEvent;
import com.upsight.android.marketing.internal.content.MarketingContentModel.Presentation;
import com.upsight.android.marketing.internal.content.MarketingContentModel.Presentation.DialogLayout;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.annotation.Created;
import com.upsight.android.persistence.annotation.Updated;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class BillboardManagerImpl implements UpsightBillboardManager {
    private final MarketingContentStore mContentStore;
    private final Map<String, Billboard> mFilledBillboards = new HashMap();
    private final Map<String, UpsightContentMediator> mMediators = new HashMap();
    private final Map<String, Billboard> mUnfilledBillboards = new HashMap();

    BillboardManagerImpl(UpsightDataStore dataStore, MarketingContentStore contentStore, Bus bus) {
        this.mContentStore = contentStore;
        bus.register(this);
        dataStore.subscribe(this);
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

    public boolean registerContentMediator(UpsightContentMediator mediator) {
        if (mediator == null || TextUtils.isEmpty(mediator.getContentProvider())) {
            return false;
        }
        return this.mMediators.put(mediator.getContentProvider(), mediator) == mediator;
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
    public synchronized void handleAvailabilityEvent(ScopelessAvailabilityEvent event) {
        if (!TextUtils.isEmpty(event.getParentId())) {
            Billboard billboard = (Billboard) this.mFilledBillboards.get(event.getParentId());
            if (billboard != null) {
                MarketingContent content = billboard.getMarketingContent();
                MarketingContent nextContent = (MarketingContent) this.mContentStore.get(event.getId());
                if (!(content == null || nextContent == null || !nextContent.isAvailable())) {
                    UpsightContentMediator mediator = (UpsightContentMediator) this.mMediators.get(content.getContentProvider());
                    UpsightContentMediator nextMediator = (UpsightContentMediator) this.mMediators.get(nextContent.getContentProvider());
                    if (mediator != null && mediator.isAvailable() && nextMediator != null && nextMediator.isAvailable()) {
                        nextContent.markConsumed();
                        billboard.getHandler().onNextView();
                        ViewGroup billboardRootView = (ViewGroup) billboard.getMarketingContent().getContentView().getParent();
                        mediator.hideContent(content, billboardRootView);
                        nextMediator.displayContent(nextContent, billboardRootView);
                        billboard.setMarketingContent(nextContent);
                        this.mFilledBillboards.remove(event.getParentId());
                        this.mFilledBillboards.put(event.getId(), billboard);
                        nextContent.executeActions(MarketingContent.TRIGGER_CONTENT_DISPLAYED);
                    }
                }
            }
        }
    }

    @Subscribe
    public synchronized void handleActionEvent(RewardsEvent event) {
        Billboard billboard = (Billboard) this.mFilledBillboards.get(event.mId);
        if (billboard != null) {
            billboard.getHandler().onRewards(event.mRewards);
        }
    }

    @Subscribe
    public synchronized void handleActionEvent(PurchasesEvent event) {
        Billboard billboard = (Billboard) this.mFilledBillboards.get(event.mId);
        if (billboard != null) {
            billboard.getHandler().onPurchases(event.mPurchases);
        }
    }

    @Subscribe
    public synchronized void handleActionEvent(DestroyEvent event) {
        detachBillboard(event.mId);
    }

    @Updated
    @Created
    public void onApplicationStatus(ApplicationStatus appStatus) {
        if (appStatus.getState() == State.BACKGROUND) {
            for (String id : this.mFilledBillboards.keySet()) {
                MarketingContent content = (MarketingContent) this.mContentStore.get(id);
                if (content != null) {
                    content.executeActions(MarketingContent.TRIGGER_APP_BACKGROUNDED);
                }
            }
        }
    }

    private boolean tryAttachBillboard(String id, Billboard billboard) {
        MarketingContent content = (MarketingContent) this.mContentStore.get(id);
        if (billboard == null || billboard.getMarketingContent() != null || content == null || !content.isAvailable()) {
            return false;
        }
        UpsightContentMediator mediator = (UpsightContentMediator) this.mMediators.get(content.getContentProvider());
        if (mediator == null || !mediator.isAvailable()) {
            return false;
        }
        ViewGroup billboardRootView = billboard.getHandler().onAttach(billboard.getScope(), getPresentationStyle(content), getDimensions(content));
        if (billboardRootView == null) {
            return false;
        }
        content.markConsumed();
        mediator.displayContent(content, billboardRootView);
        billboard.setMarketingContent(content);
        this.mUnfilledBillboards.remove(billboard.getScope());
        this.mFilledBillboards.put(id, billboard);
        content.executeActions(MarketingContent.TRIGGER_CONTENT_DISPLAYED);
        return true;
    }

    private synchronized void detachBillboard(String id) {
        Billboard billboard = (Billboard) this.mFilledBillboards.get(id);
        if (billboard != null) {
            billboard.getHandler().onDetach();
            this.mFilledBillboards.remove(id);
        }
    }

    private PresentationStyle getPresentationStyle(MarketingContent content) {
        String transition = content.getContentModel().getPresentationStyle();
        if (Presentation.STYLE_DIALOG.equals(transition)) {
            return PresentationStyle.Dialog;
        }
        if (Presentation.STYLE_FULLSCREEN.equals(transition)) {
            return PresentationStyle.Fullscreen;
        }
        return PresentationStyle.None;
    }

    private Set<Dimensions> getDimensions(MarketingContent content) {
        Set<Dimensions> dimensions = new HashSet();
        DialogLayout layouts = content.getContentModel().getDialogLayouts();
        if (layouts != null && layouts.portrait != null && layouts.portrait.w > 0 && layouts.portrait.h > 0) {
            dimensions.add(new Dimensions(LayoutOrientation.Portrait, layouts.portrait.w, layouts.portrait.h));
        }
        if (layouts != null && layouts.landscape != null && layouts.landscape.w > 0 && layouts.landscape.h > 0) {
            dimensions.add(new Dimensions(LayoutOrientation.Landscape, layouts.landscape.w, layouts.landscape.h));
        }
        return dimensions;
    }
}
