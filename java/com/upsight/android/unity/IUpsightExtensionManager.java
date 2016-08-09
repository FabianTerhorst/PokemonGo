package com.upsight.android.unity;

import com.upsight.android.UpsightContext;

public interface IUpsightExtensionManager {
    void init(UpsightContext upsightContext);

    void onApplicationPaused();

    void onApplicationResumed();
}
