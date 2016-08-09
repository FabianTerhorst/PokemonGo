package com.upsight.mediation.ads.adapters;

public class FuseMRAIDAdAdapter extends MRaidAdAdapter {
    public static final String NAME = "FuseMRAID";

    public void init() {
        super.init();
        this.baseUrl = "http://www.fuseboxx.com";
    }
}
