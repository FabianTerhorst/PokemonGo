package com.upsight.mediation.vast.util;

import android.text.TextUtils;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.VASTPlayer;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTools {
    private static final String TAG = HttpTools.class.getName();

    public static void httpGetURL(final String type, final String url, final VASTPlayer currentPlayer) {
        if (!TextUtils.isEmpty(url)) {
            new Thread() {
                public void run() {
                    HttpURLConnection conn = null;
                    try {
                        FuseLog.v(HttpTools.TAG, "connection to URL:" + url);
                        URL httpUrl = new URL(url);
                        HttpURLConnection.setFollowRedirects(true);
                        conn = (HttpURLConnection) httpUrl.openConnection();
                        conn.setConnectTimeout(Default.HTTP_REQUEST_TIMEOUT_MS);
                        conn.setRequestProperty("Connection", "close");
                        conn.setRequestMethod("GET");
                        int code = conn.getResponseCode();
                        FuseLog.v(HttpTools.TAG, "response code:" + code + ", for URL:" + url);
                        if (code < 200 && code > 226 && type.equals("impression")) {
                            currentPlayer.listener.vastError(VASTPlayer.ERROR_UNDEFINED);
                        }
                        if (conn != null) {
                            try {
                                conn.disconnect();
                            } catch (Exception e) {
                                FuseLog.w(HttpTools.TAG, e.toString());
                            }
                        }
                    } catch (Exception e2) {
                        FuseLog.w(HttpTools.TAG, url + ": " + e2.getMessage() + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR + e2.toString());
                        if (type.equals("impression")) {
                            currentPlayer.listener.vastError(VASTPlayer.ERROR_UNDEFINED);
                        }
                        if (conn != null) {
                            try {
                                conn.disconnect();
                            } catch (Exception e22) {
                                FuseLog.w(HttpTools.TAG, e22.toString());
                            }
                        }
                    } catch (Throwable th) {
                        if (conn != null) {
                            try {
                                conn.disconnect();
                            } catch (Exception e222) {
                                FuseLog.w(HttpTools.TAG, e222.toString());
                            }
                        }
                    }
                }
            }.start();
        }
    }
}
