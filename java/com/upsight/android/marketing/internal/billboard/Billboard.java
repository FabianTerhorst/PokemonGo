package com.upsight.android.marketing.internal.billboard;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightMarketingExtension;
import com.upsight.android.marketing.UpsightBillboard;
import com.upsight.android.marketing.UpsightBillboard.Handler;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.internal.content.MarketingContent;

public class Billboard extends UpsightBillboard {
    private UpsightBillboardManager mBillboardManager;
    private MarketingContent mContent = null;
    protected final Handler mHandler;
    protected final String mScope;

    public Billboard(String scope, Handler handler) {
        this.mScope = scope;
        this.mHandler = handler;
    }

    public final UpsightBillboard setUp(UpsightContext upsight) throws IllegalStateException {
        UpsightMarketingApi marketingApi = null;
        UpsightMarketingExtension extension = (UpsightMarketingExtension) upsight.getUpsightExtension(UpsightMarketingExtension.EXTENSION_NAME);
        if (extension != null) {
            marketingApi = extension.getApi();
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.marketing must be registered in your Android Manifest", new Object[0]);
        }
        if (marketingApi != null) {
            this.mBillboardManager = marketingApi;
            if (!this.mBillboardManager.registerBillboard(this)) {
                String billboardClassName = UpsightBillboard.class.getSimpleName();
                throw new IllegalStateException("An active " + billboardClassName + " with the same scope already exists. A billboard remains active until either a content view is attached, or " + billboardClassName + "#destroy() is called.");
            }
        }
        return this;
    }

    public final void destroy() {
        UpsightBillboardManager billboardManager = this.mBillboardManager;
        if (billboardManager != null) {
            billboardManager.unregisterBillboard(this);
            this.mBillboardManager = null;
        }
    }

    String getScope() {
        return this.mScope;
    }

    Handler getHandler() {
        return this.mHandler;
    }

    void setMarketingContent(MarketingContent content) {
        this.mContent = content;
    }

    MarketingContent getMarketingContent() {
        return this.mContent;
    }
}
