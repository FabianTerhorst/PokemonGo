package com.upsight.android.marketing.internal.content;

import android.text.TextUtils;
import com.upsight.android.analytics.internal.action.ActionMap;
import com.upsight.android.analytics.internal.action.ActionMapResponse;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionFactory;

public final class MarketingContentFactory {
    private static final MarketingContentActionFactory sMarketingContentActionFactory = new MarketingContentActionFactory();
    private MarketingContentActionContext mActionContext;

    public MarketingContentFactory(MarketingContentActionContext actionContext) {
        this.mActionContext = actionContext;
    }

    public MarketingContent create(ActionMapResponse actionMapResponse) {
        String id = actionMapResponse.getActionMapId();
        if (TextUtils.isEmpty(id) || !MarketingContentActionFactory.TYPE.equals(actionMapResponse.getActionFactory())) {
            return null;
        }
        return MarketingContent.create(id, new ActionMap(sMarketingContentActionFactory, this.mActionContext, actionMapResponse.getActionMap()));
    }
}
