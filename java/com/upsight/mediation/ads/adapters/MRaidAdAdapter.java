package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.mraid.MRAIDInterstitial;
import com.upsight.mediation.mraid.MRAIDInterstitialListener;
import com.upsight.mediation.mraid.MRAIDNativeFeature;
import com.upsight.mediation.mraid.MRAIDNativeFeatureListener;
import com.upsight.mediation.util.StringUtil;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Sharing;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;

public class MRaidAdAdapter extends NetworkWrapperFuseInternal implements MRAIDInterstitialListener, MRAIDNativeFeatureListener {
    private static final int DISPLAY_MRAID_ACTIVITY_REQUEST_CODE = 0;
    public static final String NAME = "MRAID";
    private static final String TAG = "MRaidAdAdapter";
    static MRaidRegistry mRaidRegistry = new MRaidRegistry();
    private WeakReference<MRaidActivity> MRaidActivity;
    protected Activity activity;
    protected int backgroundColor;
    String baseUrl;
    protected String clickBeacon;
    private int closeButtonDelay;
    protected boolean hasReportedClose = false;
    protected String htmlBody;
    protected String impressionBeacon;
    protected MRAIDInterstitial interstitial;
    private boolean isRewarded;
    protected boolean loaded = false;
    private String name;
    protected int registryId;
    private boolean returnToInterstitial;
    private int rewardTimer;
    protected int rotateMode;
    protected boolean shouldPreload;
    private Date startDisplayTime;

    public void init() {
        this.registryId = mRaidRegistry.register(this);
        this.baseUrl = null;
    }

    public String getName() {
        if (this.name == null) {
            this.name = new StringBuilder(NAME).append(": ").append(getID()).toString();
        }
        return this.name;
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> parameters) {
        this.isRewarded = Boolean.parseBoolean((String) parameters.get(NetworkWrapper.IS_REWARDED));
        this.activity = activity;
        this.htmlBody = (String) parameters.get("script");
        if (StringUtil.isNullOrEmpty(this.htmlBody)) {
            onAdFailedToLoad(AdapterLoadError.INVALID_PARAMETERS);
            return;
        }
        this.clickBeacon = (String) parameters.get("beacon-click");
        this.impressionBeacon = (String) parameters.get("beacon-impression");
        this.baseUrl = (String) parameters.get(NetworkWrapper.BASE_URL);
        this.shouldPreload = Boolean.parseBoolean((String) parameters.get(NetworkWrapper.SHOULD_PRELOAD));
        this.returnToInterstitial = Boolean.parseBoolean((String) parameters.get(NetworkWrapper.REWARD_TIMER));
        try {
            this.backgroundColor = Integer.parseInt((String) parameters.get(NetworkWrapper.BACKGROUND_COLOR));
            this.rotateMode = Integer.parseInt((String) parameters.get(NetworkWrapper.ROTATE_MODE));
            this.rewardTimer = Integer.parseInt((String) parameters.get(NetworkWrapper.REWARD_TIMER));
            this.closeButtonDelay = Integer.parseInt((String) parameters.get(NetworkWrapper.REWARD_TIMER));
            if (this.shouldPreload) {
                preloadInterstitial();
                return;
            }
            this.loaded = true;
            onAdLoaded();
        } catch (NumberFormatException e) {
            onAdFailedToLoad(AdapterLoadError.INVALID_PARAMETERS);
        }
    }

    private void preloadInterstitial() {
        this.loaded = false;
        this.interstitial = new MRAIDInterstitial(this.activity, this.baseUrl, this.htmlBody, this.backgroundColor, new String[]{MRAIDNativeFeature.CALENDAR, MRAIDNativeFeature.INLINE_VIDEO, MRAIDNativeFeature.STORE_PICTURE, Sharing.SMS, MRAIDNativeFeature.TEL}, this, this);
        this.interstitial.setOrientationConfig(this.rotateMode);
        this.htmlBody = null;
    }

    public boolean isAdAvailable() {
        boolean isAvailable = this.loaded;
        if (this.shouldPreload) {
            return isAvailable && this.interstitial != null && this.interstitial.isReady;
        } else {
            return isAvailable;
        }
    }

    public void displayAd() {
        if (!isAdAvailable()) {
            onAdFailedToDisplay();
        } else if (this.shouldPreload) {
            displayInterstitial();
        } else {
            preloadInterstitial();
        }
    }

    void displayInterstitial() {
        Intent intent = new Intent(this.activity, MRaidActivity.class);
        intent.putExtra("registryId", this.registryId);
        intent.putExtra("rotate", this.rotateMode);
        intent.setFlags(65536);
        this.activity.startActivity(intent);
        this.hasReportedClose = false;
        this.loaded = false;
    }

    public void mraidInterstitialLoaded(MRAIDInterstitial mraidInterstitial) {
        if (this.shouldPreload) {
            FuseLog.v(TAG, "MRAID Ad Loaded");
            this.loaded = true;
            onAdLoaded();
            return;
        }
        displayInterstitial();
    }

    public void mraidInterstitialShow(MRAIDInterstitial mraidInterstitial) {
        FuseLog.v(TAG, "MRAID Ad Displayed");
        onAdDisplayed();
        if (this.impressionBeacon != null) {
            sendRequestToBeacon(this.impressionBeacon);
        }
        if (this.isRewarded) {
            this.startDisplayTime = new Date();
        }
    }

    public void mraidInterstitialHide(MRAIDInterstitial mraidInterstitial) {
        boolean displayedLongEnoughForReward = true;
        FuseLog.v(TAG, "MRAID Ad Hidden");
        if (!this.hasReportedClose) {
            this.hasReportedClose = true;
            if (this.isRewarded && this.startDisplayTime != null) {
                if (new Date().getTime() - this.startDisplayTime.getTime() <= ((long) this.rewardTimer) || this.rewardTimer <= 0) {
                    displayedLongEnoughForReward = false;
                }
                if (displayedLongEnoughForReward) {
                    onRewardedVideoCompleted();
                }
            }
            onAdClosed();
        }
        MRaidActivity displayedActivity = getMRaidActivity();
        if (displayedActivity != null) {
            displayedActivity.finish();
        }
    }

    public void mraidInterstitialFailedToLoad(MRAIDInterstitial mraidInterstitial) {
        FuseLog.d(TAG, "MRAID Ad Failed to Load");
        onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
    }

    public void mraidInterstitialAcceptPressed(MRAIDInterstitial mraidInterstitial) {
    }

    public void mraidInterstitialRejectPressed(MRAIDInterstitial mraidInterstitial) {
    }

    public void mraidInterstitialReplayVideoPressed(MRAIDInterstitial mraidInterstitial) {
    }

    public void mraidNativeFeatureCallTel(String url) {
        FuseLog.v(TAG, "MRAID Ad Wants to make phone call " + url);
    }

    public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {
        FuseLog.v(TAG, "MRAID Ad Wants to create calendar event: " + eventJSON);
    }

    public void mraidNativeFeaturePlayVideo(String url) {
        FuseLog.v(TAG, "MRAID Ad Wants to play video: " + url);
        MRaidActivity displayedActivity = getMRaidActivity();
        if (displayedActivity != null) {
            Intent intent = new Intent(displayedActivity, MRaidVideoActivity.class);
            intent.putExtra(Keys.URL, url);
            intent.putExtra("cb_ms", this.closeButtonDelay);
            intent.putExtra("rti", this.returnToInterstitial);
            displayedActivity.startActivityForResult(intent, 1);
        }
    }

    public void mraidNativeFeatureOpenBrowser(String url) {
        FuseLog.v(TAG, "Ad Wants to display browser: " + url);
        if (this.clickBeacon != null) {
            sendRequestToBeacon(this.clickBeacon);
        }
        onAdClicked();
        onOpenMRaidUrl(url);
        mraidInterstitialHide(this.interstitial);
    }

    public void mraidNativeFeatureOpenMarket(String url) {
        FuseLog.v(TAG, "Ad Wants to display market: " + url);
        if (this.clickBeacon != null) {
            sendRequestToBeacon(this.clickBeacon);
        }
        onAdClicked();
        onOpenMRaidUrl(url);
        mraidInterstitialHide(this.interstitial);
    }

    public void mraidNativeFeatureStorePicture(String url) {
        FuseLog.v(TAG, "Ad Wants to store a picture: " + url);
    }

    public void mraidNativeFeatureSendSms(String url) {
        FuseLog.v(TAG, "Ad Wants to send SMS: " + url);
    }

    public void mraidRewardComplete() {
        onRewardedVideoCompleted();
    }

    public MRAIDInterstitial getInterstitial() {
        return this.interstitial;
    }

    public void setMRaidActivity(MRaidActivity mRaidActivity) {
        this.MRaidActivity = new WeakReference(mRaidActivity);
    }

    public MRaidActivity getMRaidActivity() {
        if (this.MRaidActivity != null) {
            return (MRaidActivity) this.MRaidActivity.get();
        }
        return null;
    }

    public boolean isDisplaying() {
        MRaidActivity displayedActivity = getMRaidActivity();
        if (displayedActivity != null) {
            return displayedActivity.isVisible;
        }
        return false;
    }

    public void mraidInterstitialFailedToShow() {
        onAdFailedToDisplay();
    }

    public boolean verifyParameters(HashMap<String, String> params) {
        return !StringUtil.isNullOrEmpty((String) params.get("script"));
    }
}
