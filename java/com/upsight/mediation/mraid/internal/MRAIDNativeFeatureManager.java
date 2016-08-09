package com.upsight.mediation.mraid.internal;

import android.content.Context;
import android.os.Build.VERSION;
import com.upsight.mediation.mraid.MRAIDNativeFeature;
import com.voxelbusters.nativeplugins.defines.Keys.Sharing;
import java.util.List;

public class MRAIDNativeFeatureManager {
    private static final String TAG = "MRAIDNativeFeatureManager";
    private Context context;
    private List<String> supportedNativeFeatures;

    public MRAIDNativeFeatureManager(Context context, List<String> supportedNativeFeatures) {
        this.context = context;
        this.supportedNativeFeatures = supportedNativeFeatures;
    }

    public boolean isCalendarSupported() {
        boolean retval = this.supportedNativeFeatures.contains(MRAIDNativeFeature.CALENDAR) && VERSION.SDK_INT >= 14 && this.context.checkCallingOrSelfPermission("android.permission.WRITE_CALENDAR") == 0;
        MRAIDLog.v(TAG, "isCalendarSupported " + retval);
        return retval;
    }

    public boolean isInlineVideoSupported() {
        boolean retval = this.supportedNativeFeatures.contains(MRAIDNativeFeature.INLINE_VIDEO);
        MRAIDLog.v(TAG, "isInlineVideoSupported " + retval);
        return retval;
    }

    public boolean isSmsSupported() {
        boolean retval = this.supportedNativeFeatures.contains(Sharing.SMS) && this.context.checkCallingOrSelfPermission("android.permission.SEND_SMS") == 0;
        MRAIDLog.v(TAG, "isSmsSupported " + retval);
        return retval;
    }

    public boolean isStorePictureSupported() {
        boolean retval = this.supportedNativeFeatures.contains(MRAIDNativeFeature.STORE_PICTURE);
        MRAIDLog.v(TAG, "isStorePictureSupported " + retval);
        return retval;
    }

    public boolean isTelSupported() {
        boolean retval = this.supportedNativeFeatures.contains(MRAIDNativeFeature.TEL) && this.context.checkCallingOrSelfPermission("android.permission.CALL_PHONE") == 0;
        MRAIDLog.v(TAG, "isTelSupported " + retval);
        return retval;
    }
}
