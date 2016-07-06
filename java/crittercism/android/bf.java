package crittercism.android;

import android.content.Context;
import android.os.Build.VERSION;
import com.crittercism.app.CrittercismConfig;

public final class bf {
    public boolean a;
    public boolean b;
    public boolean c;

    public bf(Context context, CrittercismConfig crittercismConfig) {
        boolean z = true;
        if (!crittercismConfig.isLogcatReportingEnabled() || (VERSION.SDK_INT < 16 && !a("android.permission.READ_LOGS", context))) {
            z = false;
        }
        this.a = z;
        this.c = a("android.permission.ACCESS_NETWORK_STATE", context);
        this.b = a("android.permission.GET_TASKS", context);
    }

    private static boolean a(String str, Context context) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
    }
}
