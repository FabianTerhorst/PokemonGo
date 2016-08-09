package com.upsight.android;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.internal.action.ActionMapResponse;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.UpsightManagedVariablesApi;
import com.upsight.android.managedvariables.UpsightManagedVariablesComponent;
import com.upsight.android.managedvariables.internal.BaseManagedVariablesModule;
import com.upsight.android.managedvariables.internal.DaggerManagedVariablesComponent;
import com.upsight.android.managedvariables.internal.type.UxmBlockProvider;
import com.upsight.android.managedvariables.internal.type.UxmContent;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionFactory;
import com.upsight.android.managedvariables.internal.type.UxmContentFactory;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.persistence.annotation.Created;
import javax.inject.Inject;

public class UpsightManagedVariablesExtension extends UpsightExtension<UpsightManagedVariablesComponent, UpsightManagedVariablesApi> {
    public static final String EXTENSION_NAME = "com.upsight.extension.managedvariables";
    private static final String UPSIGHT_ACTION_MAP = "upsight.action_map";
    private Gson mGson;
    private JsonParser mJsonParser;
    private UpsightLogger mLogger;
    @Inject
    UpsightManagedVariablesApi mManagedVariables;
    @Inject
    UxmBlockProvider mUxmBlockProvider;
    @Inject
    UxmContentFactory mUxmContentFactory;

    UpsightManagedVariablesExtension() {
    }

    protected UpsightManagedVariablesComponent onResolve(UpsightContext upsight) {
        return DaggerManagedVariablesComponent.builder().baseManagedVariablesModule(new BaseManagedVariablesModule(upsight)).build();
    }

    protected void onCreate(UpsightContext upsight) {
        this.mGson = upsight.getCoreComponent().gson();
        this.mJsonParser = upsight.getCoreComponent().jsonParser();
        this.mLogger = upsight.getLogger();
        UpsightDataProvider.register(upsight, this.mUxmBlockProvider);
        upsight.getDataStore().subscribe(this);
    }

    public UpsightManagedVariablesApi getApi() {
        return this.mManagedVariables;
    }

    @Created
    public void onResponse(EndpointResponse endpointResponse) {
        if (UPSIGHT_ACTION_MAP.equals(endpointResponse.getType())) {
            try {
                ActionMapResponse actionMapResponse = (ActionMapResponse) this.mGson.fromJson(this.mJsonParser.parse(endpointResponse.getContent()), ActionMapResponse.class);
                if (UxmContentActionFactory.TYPE.equals(actionMapResponse.getActionFactory())) {
                    UxmContent content = this.mUxmContentFactory.create(actionMapResponse);
                    if (content != null) {
                        content.executeActions(MarketingContent.TRIGGER_CONTENT_RECEIVED);
                    }
                }
            } catch (JsonSyntaxException e) {
                this.mLogger.w(Upsight.LOG_TAG, "Unable to parse action map", e);
            }
        }
    }
}
