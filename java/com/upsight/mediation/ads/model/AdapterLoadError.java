package com.upsight.mediation.ads.model;

import java.util.Locale;

public enum AdapterLoadError {
    PROVIDER_NO_FILL(0),
    PROVIDER_TIMED_OUT(1),
    PROVIDER_ADAPTER_ERROR(2),
    PROVIDER_UNRECOGNIZED(3),
    PROVIDER_UNDEFINED(4),
    PROVIDER_LOAD_NOT_STARTED(5),
    PROVIDER_STILL_LOADING(1),
    PROVIDER_NO_ACTIVITY(6),
    INVALID_PARAMETERS(7),
    PROVIDER_IAP_BILLING_FAILURE(8),
    PROVIER_IAP_BILLING_NOT_FOUND(9);
    
    public final int id;

    private AdapterLoadError(int id) {
        this.id = id;
    }

    public String toJSONString() {
        return String.format(Locale.US, "{ \"AdapterLoadError\" : { \"%s\" : %d} }", new Object[]{super.toString(), Integer.valueOf(this.id)});
    }
}
