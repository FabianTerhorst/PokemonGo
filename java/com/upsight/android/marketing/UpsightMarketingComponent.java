package com.upsight.android.marketing;

import com.upsight.android.UpsightExtension.BaseComponent;
import com.upsight.android.UpsightMarketingExtension;
import com.upsight.android.marketing.internal.billboard.BillboardDialogFragment;
import com.upsight.android.marketing.internal.billboard.BillboardManagementActivity;

public interface UpsightMarketingComponent extends BaseComponent<UpsightMarketingExtension> {
    void inject(BillboardDialogFragment billboardDialogFragment);

    void inject(BillboardManagementActivity billboardManagementActivity);
}
