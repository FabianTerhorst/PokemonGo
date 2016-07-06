package com.voxelbusters.nativeplugins.features.sharing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.voxelbusters.nativeplugins.defines.Enums.eShareOptions;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Mime;
import com.voxelbusters.nativeplugins.defines.Keys.Package;
import com.voxelbusters.nativeplugins.utilities.ApplicationUtility;
import java.util.ArrayList;
import java.util.HashMap;

public class SharingHelper {
    public static HashMap<String, eShareOptions> packageNameMap;

    static {
        packageNameMap = null;
        packageNameMap = new HashMap();
        packageNameMap.put(Package.FACEBOOK_1, eShareOptions.FB);
        packageNameMap.put(Package.FACEBOOK_2, eShareOptions.FB);
        packageNameMap.put(Package.TWITTER, eShareOptions.TWITTER);
        packageNameMap.put(Package.GOOGLE_PLUS, eShareOptions.GOOGLE_PLUS);
        packageNameMap.put(Package.INSTAGRAM, eShareOptions.INSTAGRAM);
        packageNameMap.put(Package.WHATS_APP, eShareOptions.WHATSAPP);
    }

    public static boolean checkIfPackageMatchesShareOptions(String packageName, String[] shareOptions) {
        eShareOptions shareOption = (eShareOptions) packageNameMap.get(packageName);
        if (shareOptions == null || shareOptions.length <= 0 || shareOption == null) {
            return false;
        }
        for (String each : shareOptions) {
            if (Integer.parseInt(each) == shareOption.ordinal()) {
                return true;
            }
        }
        return false;
    }

    public static Intent[] getPrioritySocialNetworkingIntents(Intent referenceIntent) {
        ArrayList<Intent> list = new ArrayList();
        for (String eachPackage : packageNameMap.keySet()) {
            if (isSocialNetwork(eachPackage)) {
                Intent intent = new Intent(referenceIntent);
                intent.setPackage(eachPackage);
                list.add(intent);
            }
        }
        return (Intent[]) list.toArray(new Intent[list.size()]);
    }

    public static Intent[] getPriorityIntents(Intent sampleIntent) {
        ArrayList<Intent> list = new ArrayList();
        for (String eachPackage : packageNameMap.keySet()) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setPackage(eachPackage);
            list.add(intent);
        }
        return (Intent[]) list.toArray(new Intent[list.size()]);
    }

    public static boolean isSocialNetwork(String packageName) {
        eShareOptions shareOption = (eShareOptions) packageNameMap.get(packageName);
        if (shareOption == null || (eShareOptions.FB != shareOption && eShareOptions.TWITTER != shareOption && eShareOptions.GOOGLE_PLUS != shareOption && eShareOptions.INSTAGRAM != shareOption)) {
            return false;
        }
        return true;
    }

    public static boolean isServiceAvailable(Context context, eShareOptions serviceType) {
        if (serviceType == eShareOptions.FB) {
            if (ApplicationUtility.isIntentAvailable(context, "android.intent.action.SEND", Mime.PLAIN_TEXT, Package.FACEBOOK_1) || ApplicationUtility.isIntentAvailable(context, "android.intent.action.SEND", Mime.PLAIN_TEXT, Package.FACEBOOK_2)) {
                return true;
            }
            return false;
        } else if (serviceType == eShareOptions.TWITTER) {
            return ApplicationUtility.isIntentAvailable(context, "android.intent.action.SEND", Mime.PLAIN_TEXT, Package.TWITTER);
        } else {
            if (serviceType == eShareOptions.WHATSAPP) {
                return ApplicationUtility.isIntentAvailable(context, "android.intent.action.SEND", Mime.PLAIN_TEXT, Package.WHATS_APP);
            }
            if (serviceType == eShareOptions.MESSAGE) {
                Intent intent = new Intent("android.intent.action.SENDTO");
                intent.setData(Uri.parse(Keys.Intent.SCHEME_SEND_TO));
                return ApplicationUtility.isIntentAvailable(context, intent);
            } else if (serviceType == eShareOptions.MAIL) {
                return ApplicationUtility.isIntentAvailable(context, "android.intent.action.SEND", Mime.EMAIL, null);
            } else {
                return false;
            }
        }
    }
}
