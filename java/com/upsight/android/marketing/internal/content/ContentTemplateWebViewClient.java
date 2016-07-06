package com.upsight.android.marketing.internal.content;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.otto.Bus;
import com.upsight.android.logger.UpsightLogger;
import java.io.IOException;
import java.util.Iterator;

class ContentTemplateWebViewClient extends WebViewClient {
    private static final String DISPATCH_CALLBACK = "javascript:PlayHaven.nativeAPI.callback(\"%s\", %s, %s, %s)";
    private static final String DISPATCH_CALLBACK_PROTOCOL = "javascript:window.PlayHavenDispatchProtocolVersion=7";
    private static final String DISPATCH_LOAD_CONTEXT = "ph://loadContext";
    private static final String DISPATCH_PARAM_KEY_CALLBACK_ID = "callback";
    private static final String DISPATCH_PARAM_KEY_CONTEXT = "context";
    private static final String DISPATCH_PARAM_KEY_TRIGGER = "trigger";
    private static final String DISPATCH_PARAM_KEY_VIEW_DATA = "view_data";
    private static final String DISPATCH_SCHEME = "ph://";
    private final Bus mBus;
    private boolean mIsTemplateLoaded = false;
    private final UpsightLogger mLogger;
    private final ObjectMapper mMapper;
    private final MarketingContent mMarketingContent;

    public ContentTemplateWebViewClient(MarketingContent marketingContent, Bus bus, ObjectMapper mapper, UpsightLogger logger) {
        this.mMarketingContent = marketingContent;
        this.mBus = bus;
        this.mMapper = mapper;
        this.mLogger = logger;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return handleLoadContextDispatch(view, url) || handleActionDispatch(url) || super.shouldOverrideUrlLoading(view, url);
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (!this.mIsTemplateLoaded) {
            this.mIsTemplateLoaded = true;
            this.mMarketingContent.markLoaded(this.mBus);
        }
    }

    private boolean handleActionDispatch(String url) {
        boolean isHandled = false;
        if (url != null && url.startsWith(DISPATCH_SCHEME)) {
            isHandled = true;
            String context = Uri.parse(url).getQueryParameter(DISPATCH_PARAM_KEY_CONTEXT);
            if (!TextUtils.isEmpty(context)) {
                try {
                    JsonNode contextNode = this.mMapper.readTree(context);
                    if (contextNode.hasNonNull(DISPATCH_PARAM_KEY_TRIGGER)) {
                        JsonNode triggerNode = contextNode.path(DISPATCH_PARAM_KEY_TRIGGER);
                        if (triggerNode.isTextual()) {
                            this.mMarketingContent.executeActions(triggerNode.asText());
                        }
                    } else if (contextNode.hasNonNull(DISPATCH_PARAM_KEY_VIEW_DATA)) {
                        JsonNode viewDataNode = contextNode.path(DISPATCH_PARAM_KEY_VIEW_DATA);
                        Iterator<String> iterator = viewDataNode.fieldNames();
                        while (iterator.hasNext()) {
                            String dataKey = (String) iterator.next();
                            this.mMarketingContent.putExtra(dataKey, viewDataNode.path(dataKey).toString());
                        }
                    }
                } catch (IOException e) {
                    this.mLogger.e(getClass().getSimpleName(), e, "Failed to parse contextNode=" + null, new Object[0]);
                }
            }
        }
        return isHandled;
    }

    @TargetApi(19)
    private boolean handleLoadContextDispatch(WebView view, String url) {
        boolean isHandled = false;
        if (url != null && url.startsWith(DISPATCH_LOAD_CONTEXT)) {
            isHandled = true;
            String callbackId = Uri.parse(url).getQueryParameter(DISPATCH_PARAM_KEY_CALLBACK_ID);
            JsonNode context = this.mMarketingContent.getContentModel().getContext();
            String dispatchCallback = String.format(DISPATCH_CALLBACK, new Object[]{callbackId, context, null, null});
            view.loadUrl(DISPATCH_CALLBACK_PROTOCOL);
            if (VERSION.SDK_INT >= 19) {
                view.evaluateJavascript(dispatchCallback, null);
            } else {
                view.loadUrl(dispatchCallback);
            }
        }
        return isHandled;
    }
}
