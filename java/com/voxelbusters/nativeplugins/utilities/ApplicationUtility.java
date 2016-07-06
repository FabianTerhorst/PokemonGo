package com.voxelbusters.nativeplugins.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import java.io.File;

public class ApplicationUtility {
    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 100000;

    public static boolean isGooglePlayServicesAvailable(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode == 0) {
            return true;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
        } else {
            Debug.error(CommonDefines.APPLICATION_UTILITY_TAG, "This device does not support Google Play Services.");
        }
        return false;
    }

    public static ApplicationInfo getApplicationInfo(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(getPackageName(context), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            Debug.error(CommonDefines.APPLICATION_UTILITY_TAG, "Package name not found!");
        }
        return appInfo;
    }

    public static String getApplicationName(Context context) {
        return context.getPackageManager().getApplicationLabel(getApplicationInfo(context)).toString();
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getFileProviderAuthoityName(Context context) {
        return getPackageName(context) + ".fileprovider";
    }

    public static Context getApplicationContext(Context context) {
        return context.getApplicationContext();
    }

    public static boolean isIntentAvailable(Context context, String action, String type, String packageName) {
        Intent intent = new Intent(action);
        intent.setType(type);
        if (packageName != null) {
            intent.setPackage(packageName);
        }
        return isIntentAvailable(context, intent);
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }

    public static int getResourceId(Context context, String name, String defType) {
        return context.getResources().getIdentifier(StringUtility.getFileNameWithoutExtension(name), defType, getPackageName(context));
    }

    public static Class<?> GetMainLauncherActivity(Context context) {
        try {
            return Class.forName(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasPermission(Context context, String permissionName) {
        return context.getPackageManager().checkPermission(permissionName, getPackageName(context)) == 0;
    }

    public static File getLocalSaveDirectory(Context context, String dirName) {
        return getSaveDirectory(context, dirName, context.getApplicationContext().getFilesDir());
    }

    public static File getExternalTempDirectoryIfExists(Context context, String dirName) {
        if (hasExternalStorageWritable(context)) {
            return getSaveDirectory(context, dirName, context.getApplicationContext().getExternalCacheDir());
        }
        return getLocalSaveDirectory(context, dirName);
    }

    static File getSaveDirectory(Context context, String dirName, File destinationDir) {
        if (StringUtility.isNullOrEmpty(dirName)) {
            dirName = getApplicationName(context);
        }
        File file = new File(destinationDir, dirName);
        file.mkdirs();
        return file;
    }

    public static boolean hasExternalStorageWritable(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static Object getSystemService(Context context, String serviceName) {
        return context.getSystemService(serviceName);
    }

    public static String getString(Context context, int stringId) {
        return context.getResources().getString(stringId);
    }
}
