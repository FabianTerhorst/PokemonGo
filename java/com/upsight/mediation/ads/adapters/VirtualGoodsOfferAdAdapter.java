package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.data.Offer;
import java.util.HashMap;

public class VirtualGoodsOfferAdAdapter extends OfferAdAdapter {
    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> parameters, Offer iapOffer, Offer vgOffer) {
        super.loadAd(activity, parameters, vgOffer);
    }

    public boolean isAdAvailable(@Nullable Offer iapOffer, @Nullable Offer vgOffer) {
        return super.isAdAvailable(vgOffer);
    }
}
