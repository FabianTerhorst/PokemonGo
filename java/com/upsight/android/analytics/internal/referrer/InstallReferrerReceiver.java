package com.upsight.android.analytics.internal.referrer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.text.TextUtils;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.event.install.UpsightInstallAttributionEvent;
import com.upsight.android.internal.util.PreferencesHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

public class InstallReferrerReceiver extends BroadcastReceiver {
    private static final String ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String EXTRA_REFERRER = "referrer";
    public static final String REFERRER_PARAM_KEY_CAMPAIGN = "utm_campaign";
    public static final String REFERRER_PARAM_KEY_CONTENT = "utm_content";
    public static final String REFERRER_PARAM_KEY_MEDIUM = "utm_medium";
    public static final String REFERRER_PARAM_KEY_SOURCE = "utm_source";
    public static final String REFERRER_PARAM_KEY_TERM = "utm_term";
    public static final String SHARED_PREFERENCES_KEY_REFERRER = "referrer";

    public void onReceive(Context context, Intent intent) {
        Exception e;
        UpsightContext upsight = Upsight.createContext(context);
        if (ACTION_INSTALL_REFERRER.equals(intent.getAction()) && !PreferencesHelper.contains(upsight, SHARED_PREFERENCES_KEY_REFERRER)) {
            try {
                JSONObject params = parseReferrerParams(intent.getStringExtra(SHARED_PREFERENCES_KEY_REFERRER));
                PreferencesHelper.putString(upsight, SHARED_PREFERENCES_KEY_REFERRER, params.toString());
                String source = params.optString(REFERRER_PARAM_KEY_SOURCE);
                String campaign = params.optString(REFERRER_PARAM_KEY_CAMPAIGN);
                String creative = params.optString(REFERRER_PARAM_KEY_CONTENT);
                if (!TextUtils.isEmpty(source) || !TextUtils.isEmpty(campaign) || !TextUtils.isEmpty(creative)) {
                    UpsightInstallAttributionEvent.createBuilder().setAttributionSource(source).setAttributionCampaign(campaign).setAttributionCreative(creative).record(upsight);
                }
            } catch (UnsupportedEncodingException e2) {
                e = e2;
                upsight.getLogger().e(Upsight.LOG_TAG, "Failed to parse referrer parameters from com.android.vending.INSTALL_REFERRER", e);
            } catch (JSONException e3) {
                e = e3;
                upsight.getLogger().e(Upsight.LOG_TAG, "Failed to parse referrer parameters from com.android.vending.INSTALL_REFERRER", e);
            }
        }
    }

    JSONObject parseReferrerParams(String paramString) throws UnsupportedEncodingException, JSONException {
        JSONObject params = new JSONObject();
        if (!TextUtils.isEmpty(paramString)) {
            UrlQuerySanitizer parser = new UrlQuerySanitizer();
            parser.setAllowUnregisteredParamaters(true);
            parser.parseQuery(URLDecoder.decode(paramString, CHARSET_UTF8).trim());
            for (String key : parser.getParameterSet()) {
                params.put(key, parser.getValue(key));
            }
        }
        return params;
    }
}
