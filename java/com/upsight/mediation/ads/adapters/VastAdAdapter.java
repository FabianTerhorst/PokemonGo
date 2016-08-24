package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.util.StringUtil;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.VASTPlayer.VASTPlayerListener;
import com.voxelbusters.nativeplugins.utilities.FileUtility;
import java.util.HashMap;
import spacemadness.com.lunarconsole.R;

public class VastAdAdapter extends NetworkWrapperFuseInternal implements VASTPlayerListener {
    public static final String NAME = "VAST";
    private static final String TAG = "VastAdAdapter";
    private Activity activity;
    private VASTPlayer interstitial;
    private boolean isRewarded = false;

    @CallSuper
    public void init() {
    }

    public String getName() {
        return new StringBuilder(NAME).append(": ").append(getID()).toString();
    }

    public boolean verifyParameters(HashMap<String, String> params) {
        boolean invalid;
        if (StringUtil.isNullOrEmpty((String) params.get(NetworkWrapper.MAX_FILE_SIZE)) || StringUtil.isNullOrEmpty((String) params.get("script"))) {
            invalid = true;
        } else {
            invalid = false;
        }
        if (invalid) {
            return false;
        }
        return true;
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> parameters) {
        this.isRewarded = Boolean.parseBoolean((String) parameters.get(NetworkWrapper.IS_REWARDED));
        String callToAction = (String) parameters.get("cta");
        if (callToAction == null || callToAction.length() == 0) {
            callToAction = "Learn More";
        }
        String maxVastFileSize = (String) parameters.get(NetworkWrapper.MAX_FILE_SIZE);
        if (StringUtil.isNullOrEmpty(maxVastFileSize)) {
            onAdFailedToLoad(AdapterLoadError.INVALID_PARAMETERS);
            return;
        }
        int closeButtonDelay;
        String pr;
        boolean postroll;
        String svs;
        boolean shouldValidateSchema;
        String endCardHtml;
        int downloadTimeout;
        String vastScript;
        try {
            closeButtonDelay = Integer.parseInt((String) parameters.get(NetworkWrapper.CLOSE_BUTTON_DELAY));
        } catch (NumberFormatException e) {
            closeButtonDelay = -1;
            log("Could not parse close button delay");
            pr = (String) parameters.get("postroll");
            if (pr != null) {
                if (pr.equals("1")) {
                    postroll = true;
                    svs = (String) parameters.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
                    if (svs == null) {
                    }
                    endCardHtml = (String) parameters.get("endcard_script");
                    downloadTimeout = -1;
                    downloadTimeout = Integer.parseInt((String) parameters.get(NetworkWrapper.VAST_CACHE_TO));
                    vastScript = (String) parameters.get("script");
                    if (vastScript != null) {
                        try {
                            this.interstitial = new VASTPlayer(activity, this, postroll, endCardHtml, (long) closeButtonDelay, this.isRewarded, maxVastFileSize, shouldValidateSchema, callToAction, downloadTimeout);
                            if (URLUtil.isValidUrl(vastScript)) {
                                this.interstitial.loadVastResponseViaXML(vastScript);
                                return;
                            } else {
                                this.interstitial.loadVastResponseViaURL(vastScript);
                                return;
                            }
                        } catch (Throwable ex) {
                            logError("Vast failed to load to to unexpected error", ex);
                        }
                    }
                    onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
                }
            }
            postroll = false;
            svs = (String) parameters.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
            if (svs == null) {
            }
            endCardHtml = (String) parameters.get("endcard_script");
            downloadTimeout = -1;
            downloadTimeout = Integer.parseInt((String) parameters.get(NetworkWrapper.VAST_CACHE_TO));
            vastScript = (String) parameters.get("script");
            if (vastScript != null) {
                this.interstitial = new VASTPlayer(activity, this, postroll, endCardHtml, (long) closeButtonDelay, this.isRewarded, maxVastFileSize, shouldValidateSchema, callToAction, downloadTimeout);
                if (URLUtil.isValidUrl(vastScript)) {
                    this.interstitial.loadVastResponseViaXML(vastScript);
                    return;
                } else {
                    this.interstitial.loadVastResponseViaURL(vastScript);
                    return;
                }
            }
            onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
        } catch (NullPointerException e2) {
            closeButtonDelay = -1;
            log("Could not parse close button delay");
            pr = (String) parameters.get("postroll");
            if (pr != null) {
                if (pr.equals("1")) {
                    postroll = true;
                    svs = (String) parameters.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
                    if (svs == null) {
                    }
                    endCardHtml = (String) parameters.get("endcard_script");
                    downloadTimeout = -1;
                    downloadTimeout = Integer.parseInt((String) parameters.get(NetworkWrapper.VAST_CACHE_TO));
                    vastScript = (String) parameters.get("script");
                    if (vastScript != null) {
                        this.interstitial = new VASTPlayer(activity, this, postroll, endCardHtml, (long) closeButtonDelay, this.isRewarded, maxVastFileSize, shouldValidateSchema, callToAction, downloadTimeout);
                        if (URLUtil.isValidUrl(vastScript)) {
                            this.interstitial.loadVastResponseViaURL(vastScript);
                            return;
                        } else {
                            this.interstitial.loadVastResponseViaXML(vastScript);
                            return;
                        }
                    }
                    onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
                }
            }
            postroll = false;
            svs = (String) parameters.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
            if (svs == null) {
            }
            endCardHtml = (String) parameters.get("endcard_script");
            downloadTimeout = -1;
            downloadTimeout = Integer.parseInt((String) parameters.get(NetworkWrapper.VAST_CACHE_TO));
            vastScript = (String) parameters.get("script");
            if (vastScript != null) {
                this.interstitial = new VASTPlayer(activity, this, postroll, endCardHtml, (long) closeButtonDelay, this.isRewarded, maxVastFileSize, shouldValidateSchema, callToAction, downloadTimeout);
                if (URLUtil.isValidUrl(vastScript)) {
                    this.interstitial.loadVastResponseViaXML(vastScript);
                    return;
                } else {
                    this.interstitial.loadVastResponseViaURL(vastScript);
                    return;
                }
            }
            onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
        }
        pr = (String) parameters.get("postroll");
        if (pr != null) {
            if (pr.equals("1")) {
                postroll = true;
                svs = (String) parameters.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
                shouldValidateSchema = svs == null && Boolean.parseBoolean(svs);
                endCardHtml = (String) parameters.get("endcard_script");
                downloadTimeout = -1;
                downloadTimeout = Integer.parseInt((String) parameters.get(NetworkWrapper.VAST_CACHE_TO));
                vastScript = (String) parameters.get("script");
                if (vastScript != null) {
                    this.interstitial = new VASTPlayer(activity, this, postroll, endCardHtml, (long) closeButtonDelay, this.isRewarded, maxVastFileSize, shouldValidateSchema, callToAction, downloadTimeout);
                    if (URLUtil.isValidUrl(vastScript)) {
                        this.interstitial.loadVastResponseViaURL(vastScript);
                        return;
                    } else {
                        this.interstitial.loadVastResponseViaXML(vastScript);
                        return;
                    }
                }
                onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
            }
        }
        postroll = false;
        svs = (String) parameters.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
        if (svs == null) {
        }
        endCardHtml = (String) parameters.get("endcard_script");
        downloadTimeout = -1;
        try {
            downloadTimeout = Integer.parseInt((String) parameters.get(NetworkWrapper.VAST_CACHE_TO));
        } catch (NumberFormatException e3) {
        }
        vastScript = (String) parameters.get("script");
        if (vastScript != null) {
            this.interstitial = new VASTPlayer(activity, this, postroll, endCardHtml, (long) closeButtonDelay, this.isRewarded, maxVastFileSize, shouldValidateSchema, callToAction, downloadTimeout);
            if (URLUtil.isValidUrl(vastScript)) {
                this.interstitial.loadVastResponseViaXML(vastScript);
                return;
            } else {
                this.interstitial.loadVastResponseViaURL(vastScript);
                return;
            }
        }
        onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
    }

    public boolean isAdAvailable() {
        return this.interstitial != null && this.interstitial.isLoaded();
    }

    public void displayAd() {
        if (isAdAvailable()) {
            this.interstitial.play();
        } else {
            onAdFailedToDisplay();
        }
    }

    public void vastReady() {
        log("Ad loaded");
        onAdLoaded();
    }

    public void vastError(int error) {
        log("Error: " + error);
        onVastError(error);
        switch (error) {
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
            case VASTPlayer.ERROR_FILE_NOT_FOUND /*401*/:
            case VASTPlayer.ERROR_NO_COMPATIBLE_MEDIA_FILE /*403*/:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_LOAD_NOT_STARTED);
                return;
            case FileUtility.IMAGE_QUALITY /*100*/:
            case VASTPlayer.ERROR_SCHEMA_VALIDATION /*101*/:
            case VASTPlayer.ERROR_UNSUPPORTED_VERSION /*102*/:
            case VASTPlayer.ERROR_GENERAL_WRAPPER /*300*/:
            case VASTPlayer.ERROR_EXCEEDED_WRAPPER_LIMIT /*302*/:
            case VASTPlayer.ERROR_GENERAL_LINEAR /*400*/:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
                return;
            case VASTPlayer.ERROR_WRAPPER_TIMEOUT /*301*/:
            case VASTPlayer.ERROR_VIDEO_TIMEOUT /*402*/:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_TIMED_OUT);
                return;
            case VASTPlayer.ERROR_NO_VAST_IN_WRAPPER /*303*/:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_UNDEFINED);
                return;
            case VASTPlayer.ERROR_VIDEO_PLAYBACK /*405*/:
                onAdFailedToDisplay();
                return;
            default:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_UNRECOGNIZED);
                return;
        }
    }

    public void vastDisplay() {
        onAdDisplayed();
    }

    public void vastClick() {
        onAdClicked();
    }

    public void vastComplete() {
        onAdCompleted();
    }

    public void vastDismiss() {
        onAdClosed();
    }

    public void vastSkip() {
        onAdSkipped();
        onVastSkip();
    }

    public void vastProgress(int progress) {
        onVastProgress(progress);
    }

    public void vastRewardedVideoComplete() {
        onRewardedVideoCompleted();
    }

    public void vastReplay() {
        onVastReplay();
    }
}
