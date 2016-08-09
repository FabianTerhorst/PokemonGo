package com.upsight.mediation.mraid;

public interface MRAIDViewListener {
    void mraidReplayVideoPressed(MRAIDView mRAIDView);

    void mraidViewAcceptPressed(MRAIDView mRAIDView);

    void mraidViewClose(MRAIDView mRAIDView);

    void mraidViewExpand(MRAIDView mRAIDView);

    void mraidViewFailedToLoad(MRAIDView mRAIDView);

    void mraidViewLoaded(MRAIDView mRAIDView);

    void mraidViewRejectPressed(MRAIDView mRAIDView);

    boolean mraidViewResize(MRAIDView mRAIDView, int i, int i2, int i3, int i4);
}
