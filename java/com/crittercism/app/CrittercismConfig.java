package com.crittercism.app;

import android.os.Build.VERSION;
import crittercism.android.dx;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;

public class CrittercismConfig {
    public static final String API_VERSION = "5.0.8";
    protected String a = "com.crittercism/dumps";
    private String b = null;
    private boolean c = false;
    private boolean d = false;
    private boolean e = true;
    private boolean f = false;
    private boolean g = b();
    private String h = "Developer Reply";
    private String i = null;
    private List j = new LinkedList();
    private List k = new LinkedList();

    @Deprecated
    public CrittercismConfig(JSONObject config) {
        this.b = a(config, "customVersionName", this.b);
        this.d = a(config, "includeVersionCode", this.d);
        this.e = a(config, "installNdk", this.e);
        this.c = a(config, "delaySendingAppLoad", this.c);
        this.f = a(config, "shouldCollectLogcat", this.f);
        this.a = a(config, "nativeDumpPath", this.a);
        this.h = a(config, "notificationTitle", this.h);
        this.g = a(config, "installApm", this.g);
    }

    public CrittercismConfig(CrittercismConfig toCopy) {
        this.b = toCopy.b;
        this.c = toCopy.c;
        this.d = toCopy.d;
        this.e = toCopy.e;
        this.f = toCopy.f;
        this.g = toCopy.g;
        this.a = toCopy.a;
        this.h = toCopy.h;
        setURLBlacklistPatterns(toCopy.j);
        setPreserveQueryStringPatterns(toCopy.k);
        this.i = toCopy.i;
    }

    public List getURLBlacklistPatterns() {
        return new LinkedList(this.j);
    }

    public void setURLBlacklistPatterns(List patterns) {
        this.j.clear();
        if (patterns != null) {
            this.j.addAll(patterns);
        }
    }

    public void setPreserveQueryStringPatterns(List patterns) {
        this.k.clear();
        if (patterns != null) {
            this.k.addAll(patterns);
        }
    }

    public List getPreserveQueryStringPatterns() {
        return new LinkedList(this.k);
    }

    public boolean equals(Object o) {
        if (!(o instanceof CrittercismConfig)) {
            return false;
        }
        CrittercismConfig crittercismConfig = (CrittercismConfig) o;
        if (this.c == crittercismConfig.c && this.f == crittercismConfig.f && isNdkCrashReportingEnabled() == crittercismConfig.isNdkCrashReportingEnabled() && isOptmzEnabled() == crittercismConfig.isOptmzEnabled() && isVersionCodeToBeIncludedInVersionString() == crittercismConfig.isVersionCodeToBeIncludedInVersionString() && a(this.b, crittercismConfig.b) && a(this.h, crittercismConfig.h) && a(this.a, crittercismConfig.a) && this.j.equals(crittercismConfig.j) && this.k.equals(crittercismConfig.k) && a(this.i, crittercismConfig.i)) {
            return true;
        }
        return false;
    }

    protected static boolean a(String str, String str2) {
        if (str == null) {
            return str2 == null;
        } else {
            return str.equals(str2);
        }
    }

    public int hashCode() {
        int i;
        int i2 = 1;
        int hashCode = this.k.hashCode() + ((((((((((a(this.b) + 0) * 31) + a(this.h)) * 31) + a(this.a)) * 31) + a(this.i)) * 31) + this.j.hashCode()) * 31);
        int i3 = ((this.c ? 1 : 0) + 0) << 1;
        if (this.f) {
            i = 1;
        } else {
            i = 0;
        }
        i3 = (i + i3) << 1;
        if (isNdkCrashReportingEnabled()) {
            i = 1;
        } else {
            i = 0;
        }
        i3 = (i + i3) << 1;
        if (isOptmzEnabled()) {
            i = 1;
        } else {
            i = 0;
        }
        i = (i + i3) << 1;
        if (!isVersionCodeToBeIncludedInVersionString()) {
            i2 = 0;
        }
        return Integer.valueOf(i + i2).hashCode() + (hashCode * 31);
    }

    private static int a(String str) {
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }

    private static String a(JSONObject jSONObject, String str, String str2) {
        if (jSONObject.has(str)) {
            try {
                str2 = jSONObject.getString(str);
            } catch (Exception e) {
            }
        }
        return str2;
    }

    private static boolean a(JSONObject jSONObject, String str, boolean z) {
        if (jSONObject.has(str)) {
            try {
                z = jSONObject.getBoolean(str);
            } catch (Exception e) {
            }
        }
        return z;
    }

    public final String getCustomVersionName() {
        return this.b;
    }

    public final void setCustomVersionName(String customVersionName) {
        this.b = customVersionName;
    }

    public final boolean delaySendingAppLoad() {
        return this.c;
    }

    public final void setDelaySendingAppLoad(boolean delaySendingAppLoad) {
        this.c = delaySendingAppLoad;
    }

    public final boolean isVersionCodeToBeIncludedInVersionString() {
        return this.d;
    }

    public final void setVersionCodeToBeIncludedInVersionString(boolean shouldIncludeVersionCode) {
        this.d = shouldIncludeVersionCode;
    }

    public final boolean isNdkCrashReportingEnabled() {
        return this.e;
    }

    public final void setNdkCrashReportingEnabled(boolean installNdk) {
        this.e = installNdk;
    }

    public final boolean isLogcatReportingEnabled() {
        return this.f;
    }

    public final void setLogcatReportingEnabled(boolean shouldCollectLogcat) {
        this.f = shouldCollectLogcat;
    }

    private static final boolean b() {
        return VERSION.SDK_INT >= 10 && VERSION.SDK_INT <= 21;
    }

    public final boolean isServiceMonitoringEnabled() {
        return isOptmzEnabled();
    }

    @Deprecated
    public final boolean isOptmzEnabled() {
        return this.g;
    }

    public final void setServiceMonitoringEnabled(boolean isServiceMonitoringEnabled) {
        setOptmzEnabled(isServiceMonitoringEnabled);
    }

    @Deprecated
    public final void setOptmzEnabled(boolean isOptmzEnabled) {
        if (b() || !isOptmzEnabled) {
            this.g = isOptmzEnabled;
        } else {
            dx.a("OPTMZ is currently only allowed for api levels 10 to 21.  APM will not be installed");
        }
    }

    public List a() {
        return getURLBlacklistPatterns();
    }

    public final void setRateMyAppTestTarget(String rateMyAppTestTarget) {
        this.i = rateMyAppTestTarget;
    }

    public final String getRateMyAppTestTarget() {
        return this.i;
    }
}
