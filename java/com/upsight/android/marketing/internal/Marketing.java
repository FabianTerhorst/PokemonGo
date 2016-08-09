package com.upsight.android.marketing.internal;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.billboard.Billboard;
import com.upsight.android.marketing.internal.content.MarketingContentMediatorManager;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class Marketing implements UpsightMarketingApi {
    private UpsightBillboardManager mBillboardManager;
    private MarketingContentMediatorManager mContentMediationManager;
    private UpsightMarketingContentStore mMarketingContentStore;

    @Inject
    public Marketing(UpsightBillboardManager billboardManager, UpsightMarketingContentStore marketingContentStore, MarketingContentMediatorManager contentMediatorManager) {
        this.mBillboardManager = billboardManager;
        this.mMarketingContentStore = marketingContentStore;
        this.mContentMediationManager = contentMediatorManager;
    }

    public boolean registerBillboard(Billboard billboard) {
        return this.mBillboardManager.registerBillboard(billboard);
    }

    public boolean unregisterBillboard(Billboard billboard) {
        return this.mBillboardManager.unregisterBillboard(billboard);
    }

    public void registerContentMediator(UpsightContentMediator contentMediator) {
        this.mContentMediationManager.register(contentMediator);
    }

    public boolean isContentReady(String scope) {
        return this.mMarketingContentStore.isContentReady(scope);
    }
}
