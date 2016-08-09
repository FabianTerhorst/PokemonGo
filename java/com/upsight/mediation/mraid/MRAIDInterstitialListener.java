package com.upsight.mediation.mraid;

public interface MRAIDInterstitialListener {
    void mraidInterstitialAcceptPressed(MRAIDInterstitial mRAIDInterstitial);

    void mraidInterstitialFailedToLoad(MRAIDInterstitial mRAIDInterstitial);

    void mraidInterstitialHide(MRAIDInterstitial mRAIDInterstitial);

    void mraidInterstitialLoaded(MRAIDInterstitial mRAIDInterstitial);

    void mraidInterstitialRejectPressed(MRAIDInterstitial mRAIDInterstitial);

    void mraidInterstitialReplayVideoPressed(MRAIDInterstitial mRAIDInterstitial);

    void mraidInterstitialShow(MRAIDInterstitial mRAIDInterstitial);
}
