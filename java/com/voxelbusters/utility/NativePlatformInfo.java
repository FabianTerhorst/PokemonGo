package com.voxelbusters.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.voxelbusters.common.Configuration;

public class NativePlatformInfo {
    public static String getPackageName() {
        PackageInfo info = getPackageInfo();
        Log.e("Utility", info.toString());
        if (info != null) {
            return info.packageName;
        }
        return null;
    }

    public static String getPackageVersionName() {
        PackageInfo info = getPackageInfo();
        if (info != null) {
            return info.versionName;
        }
        return null;
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        Context applicationContext = Configuration.getContext().getApplicationContext();
        try {
            info = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }
}
