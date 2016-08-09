package com.upsight.android.analytics.internal.dispatcher.schema;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.upsight.android.Upsight;
import com.upsight.android.internal.util.NetworkHelper;

public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Upsight.createContext(context).getCoreComponent().bus().post(new NetworkChangeEvent(NetworkHelper.getActiveNetworkType(context), NetworkHelper.getNetworkOperatorName(context)));
    }
}
