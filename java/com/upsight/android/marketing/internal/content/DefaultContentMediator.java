package com.upsight.android.marketing.internal.content;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.upsight.android.marketing.UpsightContentMediator;

public final class DefaultContentMediator implements UpsightContentMediator {
    DefaultContentMediator() {
    }

    public boolean isAvailable() {
        return true;
    }

    public String getContentProvider() {
        return MarketingContent.UPSIGHT_CONTENT_PROVIDER;
    }

    public void displayContent(MarketingContent content, ViewGroup viewGroup) {
        viewGroup.addView(content.getContentView(), new LayoutParams(-1, -1));
    }

    public void hideContent(MarketingContent content, ViewGroup viewGroup) {
        viewGroup.removeAllViews();
    }
}
