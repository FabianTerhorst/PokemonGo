package com.upsight.mediation.mraid;

public interface MRAIDNativeFeatureListener {
    void mraidNativeFeatureCallTel(String str);

    void mraidNativeFeatureCreateCalendarEvent(String str);

    void mraidNativeFeatureOpenBrowser(String str);

    void mraidNativeFeatureOpenMarket(String str);

    void mraidNativeFeaturePlayVideo(String str);

    void mraidNativeFeatureSendSms(String str);

    void mraidNativeFeatureStorePicture(String str);

    void mraidRewardComplete();
}
