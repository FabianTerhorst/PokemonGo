package com.upsight.android.marketing.internal.content;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.squareup.otto.Bus;
import com.upsight.android.logger.UpsightLogger;

public class ContentTemplateWebViewClientFactory {
    protected final Bus mBus;
    protected final Gson mGson;
    protected final JsonParser mJsonParser;
    protected final UpsightLogger mLogger;

    public ContentTemplateWebViewClientFactory(Bus bus, Gson gson, JsonParser jsonParser, UpsightLogger logger) {
        this.mBus = bus;
        this.mGson = gson;
        this.mJsonParser = jsonParser;
        this.mLogger = logger;
    }

    public ContentTemplateWebViewClient create(MarketingContent content) {
        return new ContentTemplateWebViewClient(content, this.mBus, this.mGson, this.mJsonParser, this.mLogger);
    }
}
