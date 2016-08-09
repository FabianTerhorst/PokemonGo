package com.upsight.android.marketing;

import com.upsight.android.marketing.internal.billboard.Billboard;

public interface UpsightBillboardManager {
    boolean registerBillboard(Billboard billboard);

    boolean unregisterBillboard(Billboard billboard);
}
