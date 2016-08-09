package com.upsight.android.analytics.internal.dispatcher.schema;

class NetworkChangeEvent {
    public final String activeNetworkType;
    public final String networkOperatorName;

    public NetworkChangeEvent(String activeNetworkType, String networkOperatorName) {
        this.activeNetworkType = activeNetworkType;
        this.networkOperatorName = networkOperatorName;
    }
}
