package com.upsight.mediation.mraid;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.squareup.otto.Bus;
import com.upsight.mediation.mraid.internal.MRAIDHtmlProcessor;
import com.upsight.mediation.mraid.internal.MRAIDLog;
import com.upsight.mediation.mraid.internal.MRAIDNativeFeatureManager;
import com.upsight.mediation.mraid.internal.MRAIDParser;
import com.upsight.mediation.mraid.properties.MRAIDOrientationProperties;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Billing.Validation;
import com.voxelbusters.nativeplugins.defines.Keys.Mime;
import com.voxelbusters.nativeplugins.defines.Keys.Sharing;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;
import spacemadness.com.lunarconsole.BuildConfig;

@SuppressLint({"ViewConstructor"})
public class MRAIDView extends RelativeLayout {
    private static final int CLOSE_REGION_SIZE = 50;
    private static final int STATE_DEFAULT = 1;
    private static final int STATE_EXPANDED = 2;
    private static final int STATE_HIDDEN = 4;
    private static final int STATE_LOADING = 0;
    private static final int STATE_RESIZED = 3;
    private static final String TAG = "MRAIDView";
    public static final String VERSION = "1.0";
    private int backgroundColor;
    private String baseUrl;
    private ImageButton closeRegion;
    private int contentViewTop;
    private Context context;
    private Rect currentPosition;
    private WebView currentWebView;
    private Rect defaultPosition;
    private DisplayMetrics displayMetrics;
    private RelativeLayout expandedView;
    private GestureDetector gestureDetector;
    private Handler handler;
    private String htmlData;
    private boolean isActionBarShowing;
    private boolean isClosing;
    private boolean isExpandingFromDefault;
    private boolean isExpandingPart2;
    private boolean isForcingFullScreen;
    private final boolean isInterstitial;
    private boolean isLaidOut;
    private boolean isPageFinished;
    private boolean isViewable;
    private MRAIDViewListener listener;
    private Size maxSize;
    private String mraidJs;
    private MRAIDWebChromeClient mraidWebChromeClient;
    private MRAIDWebViewClient mraidWebViewClient;
    private MRAIDNativeFeatureListener nativeFeatureListener;
    private MRAIDNativeFeatureManager nativeFeatureManager;
    private MRAIDOrientationProperties orientationProperties;
    private int origTitleBarVisibility;
    private MRAIDResizeProperties resizeProperties;
    private RelativeLayout resizedView;
    private int rotateMode;
    private Size screenSize;
    private int state;
    private View titleBar;
    private boolean useCustomClose;
    private WebView webView;
    private WebView webViewPart2;

    private class MRAIDWebChromeClient extends WebChromeClient {
        private MRAIDWebChromeClient() {
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            if (!cm.message().contains("Uncaught ReferenceError")) {
                MRAIDLog.i("JS console", cm.message() + (cm.sourceId() == null ? BuildConfig.FLAVOR : " at " + cm.sourceId()) + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR + cm.lineNumber());
            }
            return true;
        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            MRAIDLog.v("JS alert", message);
            return handlePopups(result);
        }

        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            MRAIDLog.v("JS confirm", message);
            return handlePopups(result);
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            MRAIDLog.v("JS prompt", message);
            return handlePopups(result);
        }

        private boolean handlePopups(JsResult result) {
            result.cancel();
            return true;
        }
    }

    private class MRAIDWebViewClient extends WebViewClient {
        private MRAIDWebViewClient() {
        }

        public void onPageFinished(WebView view, String url) {
            MRAIDLog.v(MRAIDView.TAG, "onPageFinished: " + url);
            super.onPageFinished(view, url);
            if (MRAIDView.this.state == 0) {
                MRAIDView.this.isPageFinished = true;
                MRAIDView.this.injectJavaScript("mraid.setPlacementType('" + (MRAIDView.this.isInterstitial ? "interstitial" : "inline") + "');");
                MRAIDView.this.setSupportedServices();
                if (MRAIDView.this.isLaidOut) {
                    MRAIDView.this.setScreenSize();
                    MRAIDView.this.setMaxSize();
                    MRAIDView.this.setCurrentPosition();
                    MRAIDView.this.setDefaultPosition();
                    MRAIDView.this.state = MRAIDView.STATE_DEFAULT;
                    MRAIDView.this.fireStateChangeEvent();
                    MRAIDView.this.fireReadyEvent();
                    if (MRAIDView.this.isViewable) {
                        MRAIDView.this.fireViewableChangeEvent();
                    }
                }
                if (MRAIDView.this.listener != null) {
                    MRAIDView.this.listener.mraidViewLoaded(MRAIDView.this);
                }
            }
            if (MRAIDView.this.isExpandingPart2) {
                MRAIDView.this.isExpandingPart2 = false;
                MRAIDView.this.handler.post(new Runnable() {
                    public void run() {
                        MRAIDView.this.injectJavaScript("mraid.setPlacementType('" + (MRAIDView.this.isInterstitial ? "interstitial" : "inline") + "');");
                        MRAIDView.this.setSupportedServices();
                        MRAIDView.this.setScreenSize();
                        MRAIDView.this.setDefaultPosition();
                        MRAIDLog.v(MRAIDView.TAG, "calling fireStateChangeEvent 2");
                        MRAIDView.this.fireStateChangeEvent();
                        MRAIDView.this.fireReadyEvent();
                        if (MRAIDView.this.isViewable) {
                            MRAIDView.this.fireViewableChangeEvent();
                        }
                    }
                });
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            MRAIDLog.v(MRAIDView.TAG, "onReceivedError: " + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            MRAIDLog.v(MRAIDView.TAG, "shouldOverrideUrlLoading: " + url);
            if (url.startsWith("mraid://")) {
                MRAIDView.this.parseCommandUrl(url);
                return true;
            } else if (url.startsWith("sms:")) {
                if (MRAIDView.this.nativeFeatureListener == null || !MRAIDView.this.nativeFeatureManager.isSmsSupported()) {
                    return true;
                }
                MRAIDView.this.nativeFeatureListener.mraidNativeFeatureSendSms(url);
                return true;
            } else if (url.startsWith("tel:")) {
                if (MRAIDView.this.nativeFeatureListener == null || !MRAIDView.this.nativeFeatureManager.isTelSupported()) {
                    return true;
                }
                MRAIDView.this.nativeFeatureListener.mraidNativeFeatureCallTel(url);
                return true;
            } else if (!url.startsWith("market:")) {
                return super.shouldOverrideUrlLoading(view, url);
            } else {
                if (MRAIDView.this.nativeFeatureListener == null) {
                    return true;
                }
                MRAIDView.this.nativeFeatureListener.mraidNativeFeatureOpenMarket(url);
                return true;
            }
        }
    }

    private final class Size {
        public int height;
        public int width;

        private Size() {
        }
    }

    public MRAIDView(Context context, String baseUrl, String data, String[] supportedNativeFeatures, MRAIDViewListener listener, MRAIDNativeFeatureListener nativeFeatureListener) {
        this(context, baseUrl, data, STATE_LOADING, supportedNativeFeatures, listener, nativeFeatureListener, false);
    }

    protected MRAIDView(Context context, String baseUrl, String data, int backgroundColor, String[] supportedNativeFeatures, MRAIDViewListener listener, MRAIDNativeFeatureListener nativeFeatureListener, boolean isInterstitial) {
        super(context);
        this.mraidJs = MRaidJS.value;
        this.context = context;
        this.baseUrl = baseUrl;
        this.isInterstitial = isInterstitial;
        this.backgroundColor = backgroundColor;
        this.state = STATE_LOADING;
        this.isViewable = false;
        this.useCustomClose = false;
        this.orientationProperties = new MRAIDOrientationProperties();
        this.resizeProperties = new MRAIDResizeProperties();
        this.nativeFeatureManager = new MRAIDNativeFeatureManager(context, Arrays.asList(supportedNativeFeatures));
        this.listener = listener;
        this.nativeFeatureListener = nativeFeatureListener;
        this.displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(this.displayMetrics);
        this.currentPosition = new Rect();
        this.defaultPosition = new Rect();
        this.maxSize = new Size();
        this.screenSize = new Size();
        this.gestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }
        });
        this.handler = new Handler(Looper.getMainLooper());
        this.mraidWebChromeClient = new MRAIDWebChromeClient();
        this.mraidWebViewClient = new MRAIDWebViewClient();
        this.webView = createWebView();
        this.currentWebView = this.webView;
        addView(this.webView);
        this.htmlData = MRAIDHtmlProcessor.processRawHtml(this.mraidJs + data);
        this.webView.loadDataWithBaseURL(baseUrl, this.htmlData, Mime.HTML_TEXT, "UTF-8", null);
    }

    public void setOrientationConfig(int rotateMode) {
        this.rotateMode = rotateMode;
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private WebView createWebView() {
        WebView wv = new WebView(this.context) {
            private static final String TAG = "MRAIDView-WebView";

            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                MRAIDView.this.onLayoutWebView(this, changed, left, top, right, bottom);
            }

            public void onConfigurationChanged(Configuration newConfig) {
                super.onConfigurationChanged(newConfig);
                MRAIDLog.v(TAG, "onConfigurationChanged " + (newConfig.orientation == MRAIDView.STATE_DEFAULT ? "portrait" : "landscape"));
                if (MRAIDView.this.isInterstitial) {
                    ((Activity) MRAIDView.this.context).getWindowManager().getDefaultDisplay().getMetrics(MRAIDView.this.displayMetrics);
                }
            }

            protected void onVisibilityChanged(View changedView, int visibility) {
                super.onVisibilityChanged(changedView, visibility);
                MRAIDLog.v(TAG, "onVisibilityChanged " + MRAIDView.getVisibilityString(visibility));
                if (MRAIDView.this.isInterstitial) {
                    MRAIDView.this.setViewable(visibility);
                }
            }

            protected void onWindowVisibilityChanged(int visibility) {
                super.onWindowVisibilityChanged(visibility);
                int actualVisibility = getVisibility();
                MRAIDLog.v(TAG, "onWindowVisibilityChanged " + MRAIDView.getVisibilityString(visibility) + " (actual " + MRAIDView.getVisibilityString(actualVisibility) + ")");
                if (MRAIDView.this.isInterstitial) {
                    MRAIDView.this.setViewable(actualVisibility);
                }
            }
        };
        wv.setLayoutParams(new LayoutParams(-1, -1));
        wv.setBackgroundColor(this.backgroundColor);
        wv.setScrollContainer(false);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);
        wv.setScrollBarStyle(33554432);
        wv.setFocusableInTouchMode(true);
        wv.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MRAIDView.STATE_LOADING /*0*/:
                    case MRAIDView.STATE_DEFAULT /*1*/:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                            break;
                        }
                        break;
                }
                return false;
            }
        });
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        if (VERSION.SDK_INT < 19) {
            wv.getSettings().setDatabasePath(getContext().getFilesDir() + wv.getContext().getPackageName() + "/databases/");
        }
        wv.setWebChromeClient(this.mraidWebChromeClient);
        wv.setWebViewClient(this.mraidWebViewClient);
        return wv;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.gestureDetector.onTouchEvent(event)) {
            event.setAction(STATE_RESIZED);
        }
        return super.onTouchEvent(event);
    }

    public void updateContext(Context context) {
        Activity currentActivity = this.context;
        Activity newActivity = (Activity) context;
        this.context = context;
    }

    private void parseCommandUrl(String commandUrl) {
        MRAIDLog.v(TAG, "parseCommandUrl " + commandUrl);
        Map<String, String> commandMap = new MRAIDParser().parseCommandUrl(commandUrl);
        String command = (String) commandMap.get("command");
        String[] commandsWithNoParam = new String[]{"close", "resize", Validation.SUCCESS, "rewardComplete", "replay"};
        String[] commandsWithString = new String[]{"createCalendarEvent", "expand", "open", "playVideo", MRAIDNativeFeature.STORE_PICTURE, "useCustomClose"};
        String[] commandsWithMap = new String[STATE_EXPANDED];
        commandsWithMap[STATE_LOADING] = "setOrientationProperties";
        commandsWithMap[STATE_DEFAULT] = "setResizeProperties";
        try {
            if (Arrays.asList(commandsWithNoParam).contains(command)) {
                getClass().getDeclaredMethod(command, new Class[STATE_LOADING]).invoke(this, new Object[STATE_LOADING]);
            } else if (Arrays.asList(commandsWithString).contains(command)) {
                String key;
                r10 = getClass();
                r11 = new Class[STATE_DEFAULT];
                r11[STATE_LOADING] = String.class;
                method = r10.getDeclaredMethod(command, r11);
                if (command.equals("createCalendarEvent")) {
                    key = "eventJSON";
                } else if (command.equals("useCustomClose")) {
                    key = "useCustomClose";
                } else {
                    key = Keys.URL;
                }
                r10 = new Object[STATE_DEFAULT];
                r10[STATE_LOADING] = (String) commandMap.get(key);
                method.invoke(this, r10);
            } else if (Arrays.asList(commandsWithMap).contains(command)) {
                r10 = getClass();
                r11 = new Class[STATE_DEFAULT];
                r11[STATE_LOADING] = Map.class;
                method = r10.getDeclaredMethod(command, r11);
                r10 = new Object[STATE_DEFAULT];
                r10[STATE_LOADING] = commandMap;
                method.invoke(this, r10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close(final boolean sendCallback) {
        MRAIDLog.v("MRAIDView-JS callback", "close " + this.state);
        this.handler.post(new Runnable() {
            public void run() {
                if (MRAIDView.this.state == 0) {
                    return;
                }
                if ((MRAIDView.this.state == MRAIDView.STATE_DEFAULT && !MRAIDView.this.isInterstitial) || MRAIDView.this.state == MRAIDView.STATE_HIDDEN) {
                    return;
                }
                if (MRAIDView.this.state == MRAIDView.STATE_DEFAULT || MRAIDView.this.state == MRAIDView.STATE_EXPANDED) {
                    MRAIDView.this.closeFromExpanded(sendCallback);
                } else if (MRAIDView.this.state == MRAIDView.STATE_RESIZED) {
                    MRAIDView.this.closeFromResized(sendCallback);
                }
            }
        });
    }

    private void close() {
        close(true);
    }

    private void rewardComplete() {
        MRAIDLog.v("MRAIDView-JS callback", "rewardComplete " + this.state);
        this.handler.post(new Runnable() {
            public void run() {
                if (MRAIDView.this.nativeFeatureListener != null) {
                    MRAIDView.this.nativeFeatureListener.mraidRewardComplete();
                }
            }
        });
    }

    private void createCalendarEvent(String eventJSON) {
        MRAIDLog.v("MRAIDView-JS callback", "createCalendarEvent " + eventJSON);
        if (this.nativeFeatureListener != null) {
            this.nativeFeatureListener.mraidNativeFeatureCreateCalendarEvent(eventJSON);
        }
    }

    private void expand(String url) {
        MRAIDLog.v("MRAIDView-JS callback", "expand " + (url != null ? url : "(1-part)"));
        if (this.isInterstitial && this.state != 0) {
            return;
        }
        if (!this.isInterstitial && this.state != STATE_DEFAULT && this.state != STATE_RESIZED) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            if (this.isInterstitial || this.state == STATE_DEFAULT) {
                removeView(this.webView);
            } else if (this.state == STATE_RESIZED) {
                removeResizeView();
            }
            expandHelper();
            return;
        }
        try {
            url = URLDecoder.decode(url, "UTF-8");
            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = this.baseUrl + url;
            }
            final String finalUrl = url;
            new Thread(new Runnable() {
                public void run() {
                    final String content = MRAIDView.this.getStringFromUrl(finalUrl);
                    if (TextUtils.isEmpty(content)) {
                        MRAIDLog.i("Could not load part 2 expanded content for URL: " + finalUrl);
                    } else {
                        ((Activity) MRAIDView.this.context).runOnUiThread(new Runnable() {
                            public void run() {
                                if (MRAIDView.this.state == MRAIDView.STATE_RESIZED) {
                                    MRAIDView.this.removeResizeView();
                                    MRAIDView.this.addView(MRAIDView.this.webView);
                                }
                                MRAIDView.this.webView.setWebChromeClient(null);
                                MRAIDView.this.webView.setWebViewClient(null);
                                MRAIDView.this.webViewPart2 = MRAIDView.this.createWebView();
                                MRAIDView.this.injectMraidJs(MRAIDView.this.webViewPart2);
                                MRAIDView.this.webViewPart2.loadDataWithBaseURL(MRAIDView.this.baseUrl, content, Mime.HTML_TEXT, "UTF-8", null);
                                MRAIDView.this.currentWebView = MRAIDView.this.webViewPart2;
                                MRAIDView.this.isExpandingPart2 = true;
                                MRAIDView.this.expandHelper();
                            }
                        });
                    }
                }
            }, "2-part-content").start();
        } catch (UnsupportedEncodingException e) {
        }
    }

    private void open(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            MRAIDLog.v("MRAIDView-JS callback", "open " + url);
            if (this.nativeFeatureListener == null) {
                return;
            }
            if (url.startsWith(Sharing.SMS)) {
                this.nativeFeatureListener.mraidNativeFeatureSendSms(url);
            } else if (url.startsWith(MRAIDNativeFeature.TEL)) {
                this.nativeFeatureListener.mraidNativeFeatureCallTel(url);
            } else {
                this.nativeFeatureListener.mraidNativeFeatureOpenBrowser(url);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void playVideo(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            MRAIDLog.v("MRAIDView-JS callback", "playVideo " + url);
            if (this.nativeFeatureListener != null) {
                this.nativeFeatureListener.mraidNativeFeaturePlayVideo(url);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void resize() {
        MRAIDLog.v("MRAIDView-JS callback", "resize");
        if (this.listener != null) {
            if (this.listener.mraidViewResize(this, this.resizeProperties.width, this.resizeProperties.height, this.resizeProperties.offsetX, this.resizeProperties.offsetY)) {
                this.state = STATE_RESIZED;
                if (this.resizedView == null) {
                    this.resizedView = new RelativeLayout(this.context);
                    removeAllViews();
                    this.resizedView.addView(this.webView);
                    if (!this.useCustomClose) {
                        addCloseRegion(this.resizedView);
                    }
                    ((FrameLayout) getRootView().findViewById(16908290)).addView(this.resizedView);
                }
                if (!this.useCustomClose) {
                    setCloseRegionPosition(this.resizedView);
                }
                setResizedViewSize();
                setResizedViewPosition();
                this.handler.post(new Runnable() {
                    public void run() {
                        MRAIDView.this.fireStateChangeEvent();
                    }
                });
            }
        }
    }

    private void setOrientationProperties(Map<String, String> properties) {
        boolean allowOrientationChange = Boolean.parseBoolean((String) properties.get("allowOrientationChange"));
        String forceOrientation = (String) properties.get("forceOrientation");
        MRAIDLog.v("MRAIDView-JS callback", "setOrientationProperties " + allowOrientationChange + " " + forceOrientation);
        if (this.orientationProperties.allowOrientationChange != allowOrientationChange || this.orientationProperties.forceOrientation != MRAIDOrientationProperties.forceOrientationFromString(forceOrientation)) {
            this.orientationProperties.allowOrientationChange = allowOrientationChange;
            this.orientationProperties.forceOrientation = MRAIDOrientationProperties.forceOrientationFromString(forceOrientation);
            if (this.isInterstitial || this.state == STATE_EXPANDED) {
                applyOrientationProperties();
            }
        }
    }

    private void setResizeProperties(Map<String, String> properties) {
        int width = Integer.parseInt((String) properties.get("width"));
        int height = Integer.parseInt((String) properties.get("height"));
        int offsetX = Integer.parseInt((String) properties.get("offsetX"));
        int offsetY = Integer.parseInt((String) properties.get("offsetY"));
        String customClosePosition = (String) properties.get("customClosePosition");
        boolean allowOffscreen = Boolean.parseBoolean((String) properties.get("allowOffscreen"));
        MRAIDLog.v("MRAIDView-JS callback", "setResizeProperties " + width + " " + height + " " + offsetX + " " + offsetY + " " + customClosePosition + " " + allowOffscreen);
        this.resizeProperties.width = width;
        this.resizeProperties.height = height;
        this.resizeProperties.offsetX = offsetX;
        this.resizeProperties.offsetY = offsetY;
        this.resizeProperties.customClosePosition = MRAIDResizeProperties.customClosePositionFromString(customClosePosition);
        this.resizeProperties.allowOffscreen = allowOffscreen;
    }

    private void storePicture(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            MRAIDLog.v("MRAIDView-JS callback", "storePicture " + url);
            if (this.nativeFeatureListener != null) {
                this.nativeFeatureListener.mraidNativeFeatureStorePicture(url);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void useCustomClose(String useCustomCloseString) {
        MRAIDLog.v("MRAIDView-JS callback", "useCustomClose " + useCustomCloseString);
        this.useCustomClose = Boolean.parseBoolean(useCustomCloseString);
        if (!this.useCustomClose) {
            return;
        }
        if (this.expandedView != null) {
            removeCloseRegion(this.expandedView);
        } else if (this.resizedView != null) {
            removeCloseRegion(this.resizedView);
        }
    }

    private String getStringFromUrl(String url) {
        if (url.startsWith("file:///")) {
            return getStringFromFileUrl(url);
        }
        String content = null;
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            int responseCode = conn.getResponseCode();
            MRAIDLog.v(TAG, "response code " + responseCode);
            if (responseCode == 200) {
                MRAIDLog.v(TAG, "getContentLength " + conn.getContentLength());
                is = conn.getInputStream();
                byte[] buf = new byte[1500];
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int count = is.read(buf);
                    if (count == -1) {
                        break;
                    }
                    sb.append(new String(buf, STATE_LOADING, count));
                }
                content = sb.toString();
                MRAIDLog.v(TAG, "getStringFromUrl ok, length=" + content.length());
            }
            conn.disconnect();
            if (is == null) {
                return content;
            }
            try {
                is.close();
                return content;
            } catch (IOException e) {
                return content;
            }
        } catch (IOException e2) {
            MRAIDLog.i(TAG, "getStringFromUrl failed " + e2.getLocalizedMessage());
            if (is == null) {
                return null;
            }
            try {
                is.close();
                return null;
            } catch (IOException e3) {
                return null;
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        }
    }

    private String getStringFromFileUrl(String fileURL) {
        StringBuffer mLine = new StringBuffer(BuildConfig.FLAVOR);
        String[] urlElements = fileURL.split("/");
        if (urlElements[STATE_RESIZED].equals("android_asset")) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.context.getAssets().open(urlElements[STATE_HIDDEN])));
                String line = reader.readLine();
                mLine.append(line);
                while (line != null) {
                    line = reader.readLine();
                    mLine.append(line);
                }
                reader.close();
            } catch (IOException e) {
                MRAIDLog.i("Error fetching file: " + e.getMessage());
            }
            return mLine.toString();
        }
        MRAIDLog.i("Unknown location to fetch file content");
        return BuildConfig.FLAVOR;
    }

    protected void showAsInterstitial() {
        expand(null);
    }

    private void expandHelper() {
        if (!this.isInterstitial) {
            this.state = STATE_EXPANDED;
        }
        applyOrientationProperties();
        forceFullScreen();
        this.expandedView = new RelativeLayout(this.context);
        this.expandedView.addView(this.currentWebView);
        if (!this.useCustomClose) {
            addCloseRegion(this.expandedView);
            setCloseRegionPosition(this.expandedView);
        }
        ((Activity) this.context).addContentView(this.expandedView, new LayoutParams(-1, -1));
        this.isExpandingFromDefault = true;
    }

    private void setResizedViewSize() {
        MRAIDLog.v(TAG, "setResizedViewSize");
        int widthInDip = this.resizeProperties.width;
        int heightInDip = this.resizeProperties.height;
        Log.d(TAG, "setResizedViewSize " + widthInDip + "x" + heightInDip);
        this.resizedView.setLayoutParams(new FrameLayout.LayoutParams((int) TypedValue.applyDimension(STATE_DEFAULT, (float) widthInDip, this.displayMetrics), (int) TypedValue.applyDimension(STATE_DEFAULT, (float) heightInDip, this.displayMetrics)));
    }

    private void setResizedViewPosition() {
        MRAIDLog.v(TAG, "setResizedViewPosition");
        if (this.resizedView != null) {
            int widthInDip = this.resizeProperties.width;
            int heightInDip = this.resizeProperties.height;
            int offsetXInDip = this.resizeProperties.offsetX;
            int width = (int) TypedValue.applyDimension(STATE_DEFAULT, (float) widthInDip, this.displayMetrics);
            int height = (int) TypedValue.applyDimension(STATE_DEFAULT, (float) heightInDip, this.displayMetrics);
            int x = this.defaultPosition.left + ((int) TypedValue.applyDimension(STATE_DEFAULT, (float) offsetXInDip, this.displayMetrics));
            int y = this.defaultPosition.top + ((int) TypedValue.applyDimension(STATE_DEFAULT, (float) this.resizeProperties.offsetY, this.displayMetrics));
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.resizedView.getLayoutParams();
            params.leftMargin = x;
            params.topMargin = y;
            this.resizedView.setLayoutParams(params);
            if (x != this.currentPosition.left || y != this.currentPosition.top || width != this.currentPosition.width() || height != this.currentPosition.height()) {
                this.currentPosition.left = x;
                this.currentPosition.top = y;
                this.currentPosition.right = x + width;
                this.currentPosition.bottom = y + height;
                setCurrentPosition();
            }
        }
    }

    private void closeFromExpanded(final boolean sendCallback) {
        if (this.state == STATE_DEFAULT && this.isInterstitial) {
            this.state = STATE_HIDDEN;
            pauseWebView(this.currentWebView);
        } else if (this.state == STATE_EXPANDED || this.state == STATE_RESIZED) {
            this.state = STATE_DEFAULT;
        }
        this.isClosing = true;
        this.expandedView.removeAllViews();
        ((FrameLayout) ((Activity) this.context).findViewById(16908290)).removeView(this.expandedView);
        this.expandedView = null;
        this.closeRegion = null;
        restoreOriginalOrientation();
        restoreOriginalScreenState();
        if (this.webViewPart2 == null) {
            addView(this.webView);
        } else {
            this.webViewPart2.setWebChromeClient(null);
            this.webViewPart2.setWebViewClient(null);
            this.webViewPart2 = null;
            this.webView.setWebChromeClient(this.mraidWebChromeClient);
            this.webView.setWebViewClient(this.mraidWebViewClient);
            this.currentWebView = this.webView;
        }
        this.handler.post(new Runnable() {
            public void run() {
                MRAIDView.this.fireStateChangeEvent();
                if (MRAIDView.this.listener != null && sendCallback) {
                    MRAIDView.this.listener.mraidViewClose(MRAIDView.this);
                }
            }
        });
    }

    private void closeFromResized(final boolean sendCallback) {
        this.state = STATE_DEFAULT;
        this.isClosing = true;
        removeResizeView();
        addView(this.webView);
        this.handler.post(new Runnable() {
            public void run() {
                MRAIDView.this.fireStateChangeEvent();
                if (MRAIDView.this.listener != null && sendCallback) {
                    MRAIDView.this.listener.mraidViewClose(MRAIDView.this);
                }
            }
        });
    }

    private void removeResizeView() {
        this.resizedView.removeAllViews();
        ((FrameLayout) ((Activity) this.context).findViewById(16908290)).removeView(this.resizedView);
        this.resizedView = null;
        this.closeRegion = null;
    }

    private void forceFullScreen() {
        MRAIDLog.v(TAG, "forceFullScreen");
        Activity activity = this.context;
        this.origTitleBarVisibility = -9;
        try {
            if (Activity.class.getMethod("getActionBar", new Class[STATE_LOADING]) != null) {
                ActionBar actionBar = activity.getActionBar();
                if (actionBar != null) {
                    this.isActionBarShowing = actionBar.isShowing();
                    actionBar.hide();
                }
            }
        } catch (Exception e) {
        }
        MRAIDLog.v(TAG, "isActionBarShowing " + this.isActionBarShowing);
        MRAIDLog.v(TAG, "origTitleBarVisibility " + getVisibilityString(this.origTitleBarVisibility));
        this.isForcingFullScreen = false;
    }

    private void restoreOriginalScreenState() {
        Activity activity = this.context;
        if (this.isActionBarShowing) {
            activity.getActionBar().show();
        } else if (this.titleBar != null) {
            this.titleBar.setVisibility(this.origTitleBarVisibility);
        }
    }

    private static String getVisibilityString(int visibility) {
        switch (visibility) {
            case STATE_LOADING /*0*/:
                return "VISIBLE";
            case STATE_HIDDEN /*4*/:
                return "INVISIBLE";
            case 8:
                return "GONE";
            default:
                return "UNKNOWN";
        }
    }

    private void addCloseRegion(View view) {
        this.closeRegion = new ImageButton(this.context);
        this.closeRegion.setBackgroundColor(STATE_LOADING);
        this.closeRegion.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MRAIDView.this.close();
            }
        });
        this.closeRegion.setVisibility(STATE_HIDDEN);
        this.closeRegion.postDelayed(new Runnable() {
            public void run() {
                MRAIDView.this.closeRegion.setVisibility(MRAIDView.STATE_LOADING);
            }
        }, 1000);
        if (view == this.expandedView && !this.useCustomClose) {
            Drawable closeButtonNormalDrawable = MRaidDrawables.getDrawableForImage(this.context, "/assets/drawable/close_button_normal.png", "close_button_normal", -16777216);
            Drawable closeButtonPressedDrawable = MRaidDrawables.getDrawableForImage(this.context, "/assets/drawable/close_button_pressed.png", "close_button_pressed", -16777216);
            StateListDrawable states = new StateListDrawable();
            int[] iArr = new int[STATE_DEFAULT];
            iArr[STATE_LOADING] = -16842919;
            states.addState(iArr, closeButtonNormalDrawable);
            iArr = new int[STATE_DEFAULT];
            iArr[STATE_LOADING] = 16842919;
            states.addState(iArr, closeButtonPressedDrawable);
            this.closeRegion.setImageDrawable(states);
            this.closeRegion.setScaleType(ScaleType.CENTER_CROP);
        }
        ((ViewGroup) view).addView(this.closeRegion);
    }

    private void removeCloseRegion(View view) {
        ((ViewGroup) view).removeView(this.closeRegion);
    }

    private void setCloseRegionPosition(View view) {
        int size = (int) TypedValue.applyDimension(STATE_DEFAULT, 50.0f, this.displayMetrics);
        LayoutParams params = new LayoutParams(size, size);
        if (view != this.expandedView) {
            if (view == this.resizedView) {
                switch (this.resizeProperties.customClosePosition) {
                    case STATE_LOADING /*0*/:
                    case STATE_HIDDEN /*4*/:
                        params.addRule(9);
                        break;
                    case STATE_DEFAULT /*1*/:
                    case STATE_RESIZED /*3*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                        params.addRule(14);
                        break;
                    case STATE_EXPANDED /*2*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                        params.addRule(11);
                        break;
                }
                switch (this.resizeProperties.customClosePosition) {
                    case STATE_LOADING /*0*/:
                    case STATE_DEFAULT /*1*/:
                    case STATE_EXPANDED /*2*/:
                        params.addRule(10);
                        break;
                    case STATE_RESIZED /*3*/:
                        params.addRule(15);
                        break;
                    case STATE_HIDDEN /*4*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                        params.addRule(12);
                        break;
                    default:
                        break;
                }
            }
        }
        params.addRule(10);
        params.addRule(11);
        this.closeRegion.setLayoutParams(params);
    }

    private void injectMraidJs(WebView wv) {
        MRAIDLog.v(TAG, "injectMraidJs ok " + this.mraidJs.length());
        wv.loadUrl("javascript:" + this.mraidJs);
    }

    public void injectJavaScript(String js) {
        if (!TextUtils.isEmpty(js)) {
            this.currentWebView.loadUrl("javascript:" + js);
        }
    }

    private void fireReadyEvent() {
        MRAIDLog.v(TAG, "fireReadyEvent");
        injectJavaScript("mraid.fireReadyEvent();");
    }

    @SuppressLint({"DefaultLocale"})
    private void fireStateChangeEvent() {
        MRAIDLog.v(TAG, "fireStateChangeEvent");
        injectJavaScript("mraid.fireStateChangeEvent('" + new String[]{"loading", Bus.DEFAULT_IDENTIFIER, "expanded", "resized", "hidden"}[this.state] + "');");
    }

    private void fireViewableChangeEvent() {
        MRAIDLog.v(TAG, "fireViewableChangeEvent");
        injectJavaScript("mraid.fireViewableChangeEvent(" + this.isViewable + ");");
    }

    private int px2dip(int pixels) {
        return (pixels * 160) / this.displayMetrics.densityDpi;
    }

    private void setCurrentPosition() {
        int x = this.currentPosition.left;
        int y = this.currentPosition.top;
        int width = this.currentPosition.width();
        int height = this.currentPosition.height();
        MRAIDLog.v(TAG, "setCurrentPosition [" + x + "," + y + "] (" + width + "x" + height + ")");
        injectJavaScript("mraid.setCurrentPosition(" + px2dip(x) + "," + px2dip(y) + "," + px2dip(width) + "," + px2dip(height) + ");");
    }

    private void setDefaultPosition() {
        int x = this.defaultPosition.left;
        int y = this.defaultPosition.top;
        int width = this.defaultPosition.width();
        int height = this.defaultPosition.height();
        MRAIDLog.v(TAG, "setDefaultPosition [" + x + "," + y + "] (" + width + "x" + height + ")");
        injectJavaScript("mraid.setDefaultPosition(" + px2dip(x) + "," + px2dip(y) + "," + px2dip(width) + "," + px2dip(height) + ");");
    }

    private void setMaxSize() {
        MRAIDLog.v(TAG, "setMaxSize");
        int width = this.maxSize.width;
        int height = this.maxSize.height;
        MRAIDLog.v(TAG, "setMaxSize " + width + "x" + height);
        injectJavaScript("mraid.setMaxSize(" + px2dip(width) + "," + px2dip(height) + ");");
    }

    private void setScreenSize() {
        MRAIDLog.v(TAG, "setScreenSize");
        int width = this.screenSize.width;
        int height = this.screenSize.height;
        MRAIDLog.v(TAG, "setScreenSize " + width + "x" + height);
        injectJavaScript("mraid.setScreenSize(" + px2dip(width) + "," + px2dip(height) + ");");
    }

    private void setSupportedServices() {
        MRAIDLog.v(TAG, "setSupportedServices");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.CALENDAR, " + this.nativeFeatureManager.isCalendarSupported() + ");");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.INLINEVIDEO, " + this.nativeFeatureManager.isInlineVideoSupported() + ");");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.SMS, " + this.nativeFeatureManager.isSmsSupported() + ");");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.STOREPICTURE, " + this.nativeFeatureManager.isStorePictureSupported() + ");");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.TEL, " + this.nativeFeatureManager.isTelSupported() + ");");
    }

    private void pauseWebView(WebView webView) {
        MRAIDLog.v(TAG, "pauseWebView " + webView.toString());
        if (VERSION.SDK_INT >= 11) {
            webView.onPause();
        } else {
            webView.loadUrl("about:blank");
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MRAIDLog.v(TAG, "onConfigurationChanged " + (newConfig.orientation == STATE_DEFAULT ? "portrait" : "landscape"));
        ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(this.displayMetrics);
    }

    protected void onAttachedToWindow() {
        MRAIDLog.v(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        MRAIDLog.v(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        MRAIDLog.v(TAG, "onVisibilityChanged " + getVisibilityString(visibility));
        setViewable(visibility);
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        int actualVisibility = getVisibility();
        MRAIDLog.v(TAG, "onWindowVisibilityChanged " + getVisibilityString(visibility) + " (actual " + getVisibilityString(actualVisibility) + ")");
        setViewable(actualVisibility);
    }

    private void setViewable(int visibility) {
        boolean isCurrentlyViewable = visibility == 0;
        if (isCurrentlyViewable != this.isViewable) {
            this.isViewable = isCurrentlyViewable;
            if (this.isPageFinished && this.isLaidOut) {
                fireViewableChangeEvent();
            }
        }
    }

    @SuppressLint({"DrawAllocation"})
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        MRAIDLog.v(TAG, "onLayout (" + this.state + ") " + changed + " " + left + " " + top + " " + right + " " + bottom);
        if (this.isForcingFullScreen) {
            MRAIDLog.v(TAG, "onLayout ignored");
            return;
        }
        if (this.state == STATE_EXPANDED || this.state == STATE_RESIZED) {
            calculateScreenSize();
            calculateMaxSize();
        }
        if (this.isClosing) {
            this.isClosing = false;
            this.currentPosition = new Rect(this.defaultPosition);
            setCurrentPosition();
        } else {
            calculatePosition(false);
        }
        if (this.state == STATE_RESIZED && changed) {
            this.handler.post(new Runnable() {
                public void run() {
                    MRAIDView.this.setResizedViewPosition();
                }
            });
        }
        this.isLaidOut = true;
        if (this.state == 0 && this.isPageFinished) {
            this.state = STATE_DEFAULT;
            fireStateChangeEvent();
            fireReadyEvent();
            if (this.isViewable) {
                fireViewableChangeEvent();
            }
        }
    }

    private void onLayoutWebView(WebView wv, boolean changed, int left, int top, int right, int bottom) {
        boolean isCurrent;
        if (wv == this.currentWebView) {
            isCurrent = true;
        } else {
            isCurrent = false;
        }
        MRAIDLog.v(TAG, "onLayoutWebView " + (wv == this.webView ? "1 " : "2 ") + isCurrent + " (" + this.state + ") " + changed + " " + left + " " + top + " " + right + " " + bottom);
        if (!isCurrent) {
            MRAIDLog.v(TAG, "onLayoutWebView ignored, not current");
        } else if (this.isForcingFullScreen) {
            MRAIDLog.v(TAG, "onLayoutWebView ignored, isForcingFullScreen");
            this.isForcingFullScreen = false;
        } else {
            if (this.state == 0 || this.state == STATE_DEFAULT) {
                calculateScreenSize();
                calculateMaxSize();
            }
            if (!this.isClosing) {
                calculatePosition(true);
                if (this.isInterstitial && !this.defaultPosition.equals(this.currentPosition)) {
                    this.defaultPosition = new Rect(this.currentPosition);
                    setDefaultPosition();
                }
            }
            if (this.isExpandingFromDefault) {
                this.isExpandingFromDefault = false;
                if (this.isInterstitial) {
                    this.state = STATE_DEFAULT;
                    this.isLaidOut = true;
                }
                if (!this.isExpandingPart2) {
                    MRAIDLog.v(TAG, "calling fireStateChangeEvent 1");
                    fireStateChangeEvent();
                }
                if (this.isInterstitial) {
                    fireReadyEvent();
                    if (this.isViewable) {
                        fireViewableChangeEvent();
                    }
                }
                if (this.listener != null) {
                    this.listener.mraidViewExpand(this);
                }
            }
        }
    }

    private void calculateScreenSize() {
        boolean isPortrait = true;
        if (getResources().getConfiguration().orientation != STATE_DEFAULT) {
            isPortrait = false;
        }
        MRAIDLog.v(TAG, "calculateScreenSize orientation " + (isPortrait ? "portrait" : "landscape"));
        int width = this.displayMetrics.widthPixels;
        int height = this.displayMetrics.heightPixels;
        MRAIDLog.v(TAG, "calculateScreenSize screen size " + width + "x" + height);
        if (width != this.screenSize.width || height != this.screenSize.height) {
            this.screenSize.width = width;
            this.screenSize.height = height;
            if (this.isPageFinished) {
                setScreenSize();
            }
        }
    }

    private void calculateMaxSize() {
        Rect frame = new Rect();
        Window window = ((Activity) this.context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(frame);
        MRAIDLog.v(TAG, "calculateMaxSize frame [" + frame.left + "," + frame.top + "][" + frame.right + "," + frame.bottom + "] (" + frame.width() + "x" + frame.height() + ")");
        int statusHeight = frame.top;
        this.contentViewTop = window.findViewById(16908290).getTop();
        int titleHeight = this.contentViewTop - statusHeight;
        MRAIDLog.v(TAG, "calculateMaxSize statusHeight " + statusHeight);
        MRAIDLog.v(TAG, "calculateMaxSize titleHeight " + titleHeight);
        MRAIDLog.v(TAG, "calculateMaxSize contentViewTop " + this.contentViewTop);
        int width = frame.width();
        int height = this.screenSize.height - this.contentViewTop;
        MRAIDLog.v(TAG, "calculateMaxSize max size " + width + "x" + height);
        if (width != this.maxSize.width || height != this.maxSize.height) {
            this.maxSize.width = width;
            this.maxSize.height = height;
            if (this.isPageFinished) {
                setMaxSize();
            }
        }
    }

    private void calculatePosition(boolean isCurrentWebView) {
        View view;
        int[] location = new int[STATE_EXPANDED];
        if (isCurrentWebView) {
            view = this.currentWebView;
        } else {
            view = this;
        }
        String name = isCurrentWebView ? "current" : Bus.DEFAULT_IDENTIFIER;
        view.getLocationOnScreen(location);
        int x = location[STATE_LOADING];
        int y = location[STATE_DEFAULT];
        MRAIDLog.v(TAG, "calculatePosition " + name + " locationOnScreen [" + x + "," + y + "]");
        MRAIDLog.v(TAG, "calculatePosition " + name + " contentViewTop " + this.contentViewTop);
        if (y < this.contentViewTop) {
            MRAIDLog.v(TAG, "calculatePosition " + name + " y < contentViewTop, returning");
            return;
        }
        y -= this.contentViewTop;
        int width = view.getWidth();
        int height = view.getHeight();
        MRAIDLog.v(TAG, "calculatePosition " + name + " position [" + x + "," + y + "] (" + width + "x" + height + ")");
        Rect position = isCurrentWebView ? this.currentPosition : this.defaultPosition;
        if (x != position.left || y != position.top || width != position.width() || height != position.height()) {
            if (isCurrentWebView) {
                this.currentPosition = new Rect(x, y, x + width, y + height);
            } else {
                this.defaultPosition = new Rect(x, y, x + width, y + height);
            }
            if (!this.isPageFinished) {
                return;
            }
            if (isCurrentWebView) {
                setCurrentPosition();
            } else {
                setDefaultPosition();
            }
        }
    }

    private static String getOrientationString(int orientation) {
        switch (orientation) {
            case -1:
                return "UNSPECIFIED";
            case STATE_LOADING /*0*/:
                return "LANDSCAPE";
            case STATE_DEFAULT /*1*/:
                return "PORTRAIT";
            default:
                return "UNKNOWN";
        }
    }

    private void applyOrientationProperties() {
    }

    private void restoreOriginalOrientation() {
    }

    private void success() {
        this.listener.mraidViewAcceptPressed(this);
        close();
    }

    private void replay() {
        close(false);
        this.listener.mraidReplayVideoPressed(this);
    }
}
