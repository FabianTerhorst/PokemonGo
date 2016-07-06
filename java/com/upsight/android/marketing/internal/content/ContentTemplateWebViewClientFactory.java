package com.upsight.android.marketing.internal.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.otto.Bus;
import com.upsight.android.logger.UpsightLogger;

public class ContentTemplateWebViewClientFactory {
    protected final Bus mBus;
    protected final UpsightLogger mLogger;
    protected final ObjectMapper mMapper;

    public ContentTemplateWebViewClientFactory(Bus bus, ObjectMapper mapper, UpsightLogger logger) {
        this.mBus = bus;
        this.mMapper = mapper;
        this.mLogger = logger;
    }

    public ContentTemplateWebViewClient create(MarketingContent content) {
        return new ContentTemplateWebViewClient(content, this.mBus, this.mMapper, this.mLogger);
    }
}
