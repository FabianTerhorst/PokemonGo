package com.upsight.android;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.internal.action.ActionMapResponse;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingComponent;
import com.upsight.android.marketing.internal.BaseMarketingModule;
import com.upsight.android.marketing.internal.DaggerMarketingComponent;
import com.upsight.android.marketing.internal.content.DefaultContentMediator;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionFactory;
import com.upsight.android.marketing.internal.content.MarketingContentFactory;
import com.upsight.android.persistence.annotation.Created;
import java.io.IOException;
import javax.inject.Inject;

public class UpsightMarketingExtension extends UpsightExtension<UpsightMarketingComponent, UpsightMarketingApi> {
    public static final String EXTENSION_NAME = "com.upsight.extension.marketing";
    private static final String UPSIGHT_ACTION_MAP = "upsight.action_map";
    @Inject
    UpsightBillboardManager mBillboardManager;
    @Inject
    DefaultContentMediator mDefaultContentMediator;
    private UpsightLogger mLogger;
    private ObjectMapper mMapper;
    @Inject
    UpsightMarketingApi mMarketing;
    @Inject
    MarketingContentFactory mMarketingContentFactory;

    UpsightMarketingExtension() {
    }

    protected UpsightMarketingComponent onResolve(UpsightContext upsight) {
        return DaggerMarketingComponent.builder().baseMarketingModule(new BaseMarketingModule(upsight)).build();
    }

    protected void onCreate(UpsightContext upsight) {
        this.mMapper = upsight.getCoreComponent().objectMapper();
        this.mLogger = upsight.getLogger();
        this.mBillboardManager.registerContentMediator(this.mDefaultContentMediator);
        upsight.getDataStore().subscribe(this);
    }

    public UpsightMarketingApi getApi() {
        return this.mMarketing;
    }

    @Created
    public void onResponse(EndpointResponse endpointResponse) {
        if (UPSIGHT_ACTION_MAP.equals(endpointResponse.getType())) {
            try {
                ActionMapResponse actionMapResponse = (ActionMapResponse) this.mMapper.treeToValue(this.mMapper.readTree(endpointResponse.getContent()), ActionMapResponse.class);
                if (MarketingContentActionFactory.TYPE.equals(actionMapResponse.getActionFactory())) {
                    MarketingContent content = this.mMarketingContentFactory.create(actionMapResponse);
                    if (content != null) {
                        content.executeActions(MarketingContent.TRIGGER_CONTENT_RECEIVED);
                    }
                }
            } catch (IOException e) {
                this.mLogger.w(Upsight.LOG_TAG, "Unable to parse action map", e);
            }
        }
    }
}
