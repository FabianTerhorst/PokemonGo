package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.data.Offer;
import com.upsight.mediation.util.InAppBillingConnection;
import java.util.HashMap;

public class IAPOfferAdAdapter extends OfferAdAdapter implements LocalizedPrice {
    @Nullable
    private InAppBillingConnection mInAppBillingConnection;

    public void injectIAPBillingConnection(@NonNull InAppBillingConnection inAppBillingConnection) {
        this.mInAppBillingConnection = inAppBillingConnection;
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> parameters, @Nullable Offer iapOffer, @Nullable Offer vgOffer) {
        if (this.mInAppBillingConnection == null || !this.mInAppBillingConnection.isConnected()) {
            onAdFailedToLoad(AdapterLoadError.PROVIDER_IAP_BILLING_FAILURE);
            return;
        }
        Offer selectedOffer = iapOffer;
        if (selectedOffer == null) {
            onAdFailedToLoad(AdapterLoadError.PROVIDER_NO_FILL);
            return;
        }
        String localPrice = this.mInAppBillingConnection.getLocalPriceForProductId(selectedOffer.itemId);
        if (localPrice == null) {
            onAdFailedToLoad(AdapterLoadError.PROVIER_IAP_BILLING_NOT_FOUND);
            return;
        }
        this.javascriptInjection = "(function(){document.getElementById(\"bonus_price_or_quantity\").innerHTML=\"" + localPrice + "\"})()";
        super.loadAd(activity, parameters, selectedOffer);
    }

    public boolean isAdAvailable(@Nullable Offer iapOffer, @Nullable Offer vgOffer) {
        return super.isAdAvailable(iapOffer);
    }
}
