package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.data.Offer;
import java.util.HashMap;

public interface OfferBasedNetworkWrapper {
    boolean isAdAvailable(@Nullable Offer offer, @Nullable Offer offer2);

    void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap, @Nullable Offer offer, @Nullable Offer offer2);
}
