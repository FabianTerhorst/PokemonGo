package com.unity3d.player;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import java.util.LinkedList;
import java.util.List;

public final class n implements i {
    private static boolean a(PackageItemInfo packageItemInfo) {
        try {
            return packageItemInfo.metaData.getBoolean("unityplayer.SkipPermissionsDialog");
        } catch (Exception e) {
            return false;
        }
    }

    public final void a(Activity activity, final Runnable runnable) {
        if (activity != null) {
            PackageManager packageManager = activity.getPackageManager();
            try {
                PackageItemInfo activityInfo = packageManager.getActivityInfo(activity.getComponentName(), 128);
                PackageItemInfo applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), 128);
                if (a(activityInfo) || a(applicationInfo)) {
                    runnable.run();
                    return;
                }
            } catch (Exception e) {
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 4096);
                if (packageInfo.requestedPermissions == null) {
                    packageInfo.requestedPermissions = new String[0];
                }
                final List linkedList = new LinkedList();
                for (String str : packageInfo.requestedPermissions) {
                    try {
                        if (packageManager.getPermissionInfo(str, 128).protectionLevel == 1 && activity.checkCallingOrSelfPermission(str) != 0) {
                            linkedList.add(str);
                        }
                    } catch (NameNotFoundException e2) {
                        m.Log(5, "Failed to get permission info for " + str + ", manifest likely missing custom permission declaration");
                        m.Log(5, "Permission " + str + " ignored");
                    }
                }
                if (linkedList.isEmpty()) {
                    runnable.run();
                    return;
                }
                final FragmentManager fragmentManager = activity.getFragmentManager();
                Fragment anonymousClass1 = new Fragment(this) {
                    final /* synthetic */ n d;

                    public final void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
                        if (i == 15881) {
                            int i2 = 0;
                            while (i2 < strArr.length && i2 < iArr.length) {
                                m.Log(4, strArr[i2] + (iArr[i2] == 0 ? " granted" : " denied"));
                                i2++;
                            }
                            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                            beginTransaction.remove(this);
                            beginTransaction.commit();
                            runnable.run();
                        }
                    }

                    public final void onStart() {
                        super.onStart();
                        requestPermissions((String[]) linkedList.toArray(new String[0]), 15881);
                    }
                };
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                beginTransaction.add(0, anonymousClass1);
                beginTransaction.commit();
            } catch (Exception e3) {
                m.Log(6, "Unable to query for permission: " + e3.getMessage());
            }
        }
    }
}
