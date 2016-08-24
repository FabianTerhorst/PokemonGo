package com.upsight.android.analytics.internal.dispatcher.schema;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightCoreComponent;
import com.upsight.android.internal.util.NetworkHelper;

public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        UpsightCoreComponent coreComponent = Upsight.createContext(context).getCoreComponent();
        if (coreComponent != null) {
            coreComponent.bus().post(new NetworkChangeEvent(NetworkHelper.getActiveNetworkType(context), NetworkHelper.getNetworkOperatorName(context)));
        }
    }
}
