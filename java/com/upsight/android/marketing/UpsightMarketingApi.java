package com.upsight.android.marketing;

public interface UpsightMarketingApi extends UpsightBillboardManager {
    boolean isContentReady(String str);

    void registerContentMediator(UpsightContentMediator upsightContentMediator);
}
