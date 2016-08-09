package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.data.Offer;
import com.upsight.mediation.mraid.MRAIDInterstitial;
import java.util.Date;
import java.util.HashMap;
import spacemadness.com.lunarconsole.BuildConfig;

public abstract class OfferAdAdapter extends MRaidAdAdapter implements OfferBasedNetworkWrapper {
    @Nullable
    protected String javascriptInjection;
    @Nullable
    Offer offer;
    boolean offerAccepted;

    public abstract boolean isAdAvailable(@Nullable Offer offer, @Nullable Offer offer2);

    public abstract void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap, @Nullable Offer offer, @Nullable Offer offer2);

    void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> parameters, @Nullable Offer currentOffer) {
        this.activity = activity;
        this.shouldPreload = true;
        if (currentOffer != null && currentOffer.isExpired(new Date())) {
            onAdFailedToLoad(AdapterLoadError.PROVIDER_NO_FILL);
        } else if (currentOffer == this.offer && this.loaded) {
            onAdLoaded();
        } else {
            this.offer = currentOffer;
            this.loaded = false;
            if (currentOffer == null || currentOffer.consumed) {
                onAdFailedToLoad(AdapterLoadError.PROVIDER_NO_FILL);
                return;
            }
            try {
                this.backgroundColor = Integer.parseInt((String) parameters.get(NetworkWrapper.BACKGROUND_COLOR));
                this.rotateMode = Integer.parseInt((String) parameters.get(NetworkWrapper.ROTATE_MODE));
                this.interstitial = new MRAIDInterstitial(activity, BuildConfig.FLAVOR, this.offer.offerHtml, this.backgroundColor, new String[0], this, null);
                this.interstitial.setOrientationConfig(this.rotateMode);
                this.offerAccepted = false;
            } catch (NumberFormatException e) {
                onAdFailedToLoad(AdapterLoadError.INVALID_PARAMETERS);
            }
        }
    }

    public boolean isAdAvailable(@Nullable Offer offer) {
        return this.offer == offer && offer != null && !offer.isExpired(new Date()) && super.isAdAvailable();
    }

    public void mraidInterstitialShow(MRAIDInterstitial mraidInterstitial) {
        super.mraidInterstitialShow(mraidInterstitial);
        onOfferDisplayed(this.offer);
        if (this.javascriptInjection != null) {
            mraidInterstitial.injectJavaScript(this.javascriptInjection);
        }
    }

    public void mraidInterstitialAcceptPressed(MRAIDInterstitial mraidInterstitial) {
        super.mraidInterstitialAcceptPressed(mraidInterstitial);
        onOfferAccepted(this.offer);
        this.offerAccepted = true;
    }

    public void mraidInterstitialHide(MRAIDInterstitial mraidInterstitial) {
        if (!this.offerAccepted) {
            onOfferRejected(this.offer);
        }
        super.mraidInterstitialHide(mraidInterstitial);
    }
}
