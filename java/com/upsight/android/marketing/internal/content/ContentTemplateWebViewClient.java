package com.upsight.android.marketing.internal.content;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.squareup.otto.Bus;
import com.upsight.android.logger.UpsightLogger;
import java.util.Map.Entry;

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
    private final Gson mGson;
    private boolean mIsTemplateLoaded = false;
    private final JsonParser mJsonParser;
    private final UpsightLogger mLogger;
    private final MarketingContent<MarketingContentModel> mMarketingContent;

    public ContentTemplateWebViewClient(MarketingContent<MarketingContentModel> marketingContent, Bus bus, Gson gson, JsonParser jsoNParser, UpsightLogger logger) {
        this.mMarketingContent = marketingContent;
        this.mBus = bus;
        this.mGson = gson;
        this.mJsonParser = jsoNParser;
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
        if (url != null) {
            if (url.startsWith(DISPATCH_SCHEME)) {
                isHandled = true;
                String context = Uri.parse(url).getQueryParameter(DISPATCH_PARAM_KEY_CONTEXT);
                if (!TextUtils.isEmpty(context)) {
                    try {
                        JsonElement contextElement = this.mJsonParser.parse(context);
                        if (contextElement.isJsonObject()) {
                            JsonObject contextNode = contextElement.getAsJsonObject();
                            JsonElement triggerNode = contextNode.get(DISPATCH_PARAM_KEY_TRIGGER);
                            JsonElement viewDataNode = contextNode.get(DISPATCH_PARAM_KEY_VIEW_DATA);
                            if (triggerNode != null && triggerNode.isJsonPrimitive() && triggerNode.getAsJsonPrimitive().isString()) {
                                this.mMarketingContent.executeActions(triggerNode.getAsString());
                            } else if (viewDataNode != null && viewDataNode.isJsonObject()) {
                                for (Entry<String, JsonElement> entry : viewDataNode.getAsJsonObject().entrySet()) {
                                    JsonElement value = (JsonElement) entry.getValue();
                                    this.mMarketingContent.putExtra((String) entry.getKey(), value != null ? value.toString() : null);
                                }
                            }
                        } else {
                            this.mLogger.e(getClass().getSimpleName(), "Failed to parse context into JsonObject context=" + context, new Object[0]);
                        }
                    } catch (JsonSyntaxException e) {
                        this.mLogger.e(getClass().getSimpleName(), e, "Failed to parse context into JsonElement context=" + context, new Object[0]);
                    }
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
            JsonElement context = ((MarketingContentModel) this.mMarketingContent.getContentModel()).getContext();
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
