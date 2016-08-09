package com.upsight.mediation.ads.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.data.Offer;
import java.util.HashMap;

public abstract class NetworkWrapperFuseInternal extends NetworkWrapper {
    public abstract boolean verifyParameters(HashMap<String, String> hashMap);

    protected void onOpenMRaidUrl(@NonNull String url) {
        this.listener.onOpenMRaidUrl(url);
    }

    public final void sendRequestToBeacon(@Nullable String url) {
        this.listener.sendRequestToBeacon(url);
    }

    public final void onOfferDisplayed(Offer offer) {
        this.listener.onOfferDisplayed(offer);
    }

    public final void onOfferAccepted(Offer offer) {
        this.listener.onOfferAccepted();
    }

    public final void onOfferRejected(Offer offer) {
        this.listener.onOfferRejected();
    }

    protected final void onVastError(int error) {
        this.listener.onVastError(error);
    }

    protected final void onVastProgress(int progress) {
        this.listener.onVastProgress(progress);
    }

    protected final void onVastSkip() {
        this.listener.onVastSkip();
    }

    protected final void onVastReplay() {
        this.listener.onVastReplay();
    }
}
