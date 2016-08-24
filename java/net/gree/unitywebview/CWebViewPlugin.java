package net.gree.unitywebview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.unity3d.player.UnityPlayer;

public class CWebViewPlugin {
    private static FrameLayout layout = null;
    private boolean canGoBack;
    private boolean canGoForward;
    private WebView mWebView;
    private CWebViewPluginInterface mWebViewPlugin;

    public boolean IsInitialized() {
        return this.mWebView != null;
    }

    public void Init(final String gameObject, boolean transparent) {
        final CWebViewPlugin self = this;
        final Activity a = UnityPlayer.currentActivity;
        final String str = gameObject;
        final boolean z = transparent;
        a.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPlugin.this.mWebView == null) {
                    final WebView webView = new WebView(a);
                    webView.setVisibility(8);
                    webView.setFocusable(true);
                    webView.setFocusableInTouchMode(true);
                    webView.setWebChromeClient(new WebChromeClient());
                    CWebViewPlugin.this.mWebViewPlugin = new CWebViewPluginInterface(self, str);
                    webView.setWebViewClient(new WebViewClient() {
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            webView.loadUrl("about:blank");
                            CWebViewPlugin.this.canGoBack = webView.canGoBack();
                            CWebViewPlugin.this.canGoForward = webView.canGoForward();
                            CWebViewPlugin.this.mWebViewPlugin.call("CallOnError", errorCode + "\t" + description + "\t" + failingUrl);
                        }

                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            CWebViewPlugin.this.canGoBack = webView.canGoBack();
                            CWebViewPlugin.this.canGoForward = webView.canGoForward();
                        }

                        public void onPageFinished(WebView view, String url) {
                            CWebViewPlugin.this.canGoBack = webView.canGoBack();
                            CWebViewPlugin.this.canGoForward = webView.canGoForward();
                            CWebViewPlugin.this.mWebViewPlugin.call("CallOnLoaded", url);
                        }

                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://") || url.startsWith("javascript:")) {
                                return false;
                            }
                            if (url.startsWith("unity:")) {
                                CWebViewPlugin.this.mWebViewPlugin.call("CallFromJS", url.substring(6));
                                return true;
                            }
                            view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                            return true;
                        }
                    });
                    webView.addJavascriptInterface(CWebViewPlugin.this.mWebViewPlugin, "Unity");
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setSupportZoom(false);
                    webSettings.setJavaScriptEnabled(true);
                    if (VERSION.SDK_INT >= 16) {
                        webSettings.setAllowUniversalAccessFromFileURLs(true);
                    }
                    webSettings.setDatabaseEnabled(true);
                    webSettings.setDomStorageEnabled(true);
                    webSettings.setDatabasePath(webView.getContext().getDir("databases", 0).getPath());
                    webSettings.setUseWideViewPort(true);
                    if (z) {
                        webView.setBackgroundColor(0);
                    }
                    if (CWebViewPlugin.layout == null) {
                        CWebViewPlugin.layout = new FrameLayout(a);
                        a.addContentView(CWebViewPlugin.layout, new LayoutParams(-1, -1));
                        CWebViewPlugin.layout.setFocusable(true);
                        CWebViewPlugin.layout.setFocusableInTouchMode(true);
                    }
                    CWebViewPlugin.layout.addView(webView, new FrameLayout.LayoutParams(-1, -1, 0));
                    CWebViewPlugin.this.mWebView = webView;
                }
            }
        });
        final View activityRootView = a.getWindow().getDecorView().getRootView();
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                Display display = a.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                if (activityRootView.getRootView().getHeight() - (r.bottom - r.top) > size.y / 3) {
                    UnityPlayer.UnitySendMessage(gameObject, "SetKeyboardVisible", "true");
                } else {
                    UnityPlayer.UnitySendMessage(gameObject, "SetKeyboardVisible", "false");
                }
            }
        });
    }

    public void Destroy() {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPlugin.this.mWebView != null) {
                    CWebViewPlugin.layout.removeView(CWebViewPlugin.this.mWebView);
                    CWebViewPlugin.this.mWebView = null;
                }
            }
        });
    }

    public void LoadURL(final String url) {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPlugin.this.mWebView != null) {
                    CWebViewPlugin.this.mWebView.loadUrl(url);
                }
            }
        });
    }

    public void EvaluateJS(final String js) {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPlugin.this.mWebView != null) {
                    CWebViewPlugin.this.mWebView.loadUrl("javascript:" + js);
                }
            }
        });
    }

    public void GoBack() {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPlugin.this.mWebView != null) {
                    CWebViewPlugin.this.mWebView.goBack();
                }
            }
        });
    }

    public void GoForward() {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPlugin.this.mWebView != null) {
                    CWebViewPlugin.this.mWebView.goForward();
                }
            }
        });
    }

    public void SetMargins(int left, int top, int right, int bottom) {
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1, 0);
        params.setMargins(left, top, right, bottom);
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPlugin.this.mWebView != null) {
                    CWebViewPlugin.this.mWebView.setLayoutParams(params);
                }
            }
        });
    }

    public void SetVisibility(final boolean visibility) {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPlugin.this.mWebView != null) {
                    if (visibility) {
                        CWebViewPlugin.this.mWebView.setVisibility(0);
                        CWebViewPlugin.layout.requestFocus();
                        CWebViewPlugin.this.mWebView.requestFocus();
                        return;
                    }
                    CWebViewPlugin.this.mWebView.setVisibility(8);
                }
            }
        });
    }
}
