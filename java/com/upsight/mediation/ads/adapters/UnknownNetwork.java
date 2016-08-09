package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.upsight.mediation.ads.model.AdapterLoadError;
import java.util.HashMap;

public class UnknownNetwork extends NetworkWrapper {
    private static final String UNKNOWN = "Unknown";

    public void init() {
    }

    public String getName() {
        return UNKNOWN;
    }

    public boolean isAdAvailable() {
        return false;
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap) {
        onAdFailedToLoad(AdapterLoadError.PROVIDER_UNRECOGNIZED);
    }

    public void displayAd() {
        onAdFailedToDisplay();
    }
}
