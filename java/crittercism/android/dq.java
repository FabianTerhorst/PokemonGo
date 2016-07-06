package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public final class dq {
    public static boolean a = false;

    public static Boolean a(Context context) {
        return Boolean.valueOf(context.getSharedPreferences("com.crittercism.usersettings", 0).getBoolean("crashedOnLastLoad", false));
    }

    public static void a(Context context, boolean z) {
        Editor edit = context.getSharedPreferences("com.crittercism.usersettings", 0).edit();
        edit.putBoolean("crashedOnLastLoad", z);
        edit.commit();
    }
}
