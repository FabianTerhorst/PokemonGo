package com.upsight.mediation.vast.Postroll;

import android.app.Activity;
import android.view.ViewGroup;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.mraid.MRAIDInterstitial;
import com.upsight.mediation.mraid.MRAIDInterstitialListener;
import com.upsight.mediation.mraid.MRAIDNativeFeatureListener;
import com.upsight.mediation.vast.Postroll.Postroll.Listener;

public class MRaidPostroll implements MRAIDInterstitialListener, MRAIDNativeFeatureListener, Postroll {
    private static final String BASE_URL = "";
    private static final String TAG = "MRaidPostroll";
    private final Activity mActivity;
    private final String mHtml;
    private MRAIDInterstitial mInterstitial;
    private final Listener mListener;
    private boolean mReady = false;
    private int previousOrientation;

    public MRaidPostroll(Activity activity, String html, Listener listener) {
        this.mActivity = activity;
        this.mHtml = html;
        this.mListener = listener;
    }

    public void init() {
        if (this.mInterstitial != null) {
            FuseLog.w(TAG, "Tried to call init on already init'd mraid postroll");
        } else {
            this.mInterstitial = new MRAIDInterstitial(this.mActivity, BASE_URL, this.mHtml, new String[0], this, this);
        }
    }

    public boolean isReady() {
        return this.mReady;
    }

    public void show(ViewGroup parent) {
        this.previousOrientation = this.mActivity.getRequestedOrientation();
        this.mActivity.setRequestedOrientation(4);
        this.mInterstitial.show();
    }

    public void hide() {
    }

    public void mraidInterstitialLoaded(MRAIDInterstitial mraidInterstitial) {
        this.mReady = true;
    }

    public void mraidInterstitialShow(MRAIDInterstitial mraidInterstitial) {
    }

    public void mraidInterstitialHide(MRAIDInterstitial mraidInterstitial) {
        this.mActivity.setRequestedOrientation(this.previousOrientation);
        this.mListener.closeClicked();
    }

    public void mraidInterstitialFailedToLoad(MRAIDInterstitial mraidInterstitial) {
    }

    public void mraidInterstitialAcceptPressed(MRAIDInterstitial mraidInterstitial) {
    }

    public void mraidInterstitialRejectPressed(MRAIDInterstitial mraidInterstitial) {
    }

    public void mraidInterstitialReplayVideoPressed(MRAIDInterstitial mraidInterstitial) {
        this.mActivity.setRequestedOrientation(this.previousOrientation);
        this.mListener.replayedClicked();
    }

    public void mraidNativeFeatureCallTel(String url) {
    }

    public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {
    }

    public void mraidNativeFeaturePlayVideo(String url) {
    }

    public void mraidNativeFeatureOpenBrowser(String url) {
        if (this.mListener != null) {
            this.mListener.infoClicked(false);
            this.mListener.onOpenMRaidUrl(url);
        }
    }

    public void mraidNativeFeatureStorePicture(String url) {
    }

    public void mraidNativeFeatureSendSms(String url) {
    }

    public void mraidNativeFeatureOpenMarket(String url) {
    }

    public void mraidRewardComplete() {
    }
}
