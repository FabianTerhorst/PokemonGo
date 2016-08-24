package net.gree.unitywebview;

import android.webkit.JavascriptInterface;
import com.unity3d.player.UnityPlayer;

/* compiled from: CWebViewPlugin */
class CWebViewPluginInterface {
    private String mGameObject;
    private CWebViewPlugin mPlugin;

    public CWebViewPluginInterface(CWebViewPlugin plugin, String gameObject) {
        this.mPlugin = plugin;
        this.mGameObject = gameObject;
    }

    @JavascriptInterface
    public void call(String message) {
        call("CallFromJS", message);
    }

    public void call(final String method, final String message) {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (CWebViewPluginInterface.this.mPlugin.IsInitialized()) {
                    UnityPlayer.UnitySendMessage(CWebViewPluginInterface.this.mGameObject, method, message);
                }
            }
        });
    }
}
