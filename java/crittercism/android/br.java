package crittercism.android;

import crittercism.android.bs.a;

public enum br {
    APP_LOADS("app_loads_2", 10, Integer.MAX_VALUE, new a(0), new ca.a(), null),
    HAND_EXCS("exceptions", 5, 50, new a(0), new ca.a(), "exceptions"),
    INTERNAL_EXCS("internal_excs", 3, 3, new a(0), new ca.a(), "exceptions"),
    NDK_CRASHES("ndk_crashes", 5, Integer.MAX_VALUE, new a(0), new ca.a(), "crashes"),
    SDK_CRASHES("sdk_crashes", 5, Integer.MAX_VALUE, new a(0), new ca.a(), "crashes"),
    CURR_BCS("current_bcs", 50, Integer.MAX_VALUE, new a(1), new bz.a(), null),
    NW_BCS("network_bcs", 10, Integer.MAX_VALUE, new a(0), new bz.a(), null),
    PREV_BCS("previous_bcs", 50, Integer.MAX_VALUE, new a(0), new bz.a(), null),
    STARTED_TXNS("started_txns", 50, Integer.MAX_VALUE, new a(0), new bz.a(), null),
    FINISHED_TXNS("finished_txns", Integer.MAX_VALUE, Integer.MAX_VALUE, new a(0), new bz.a(), null),
    SYSTEM_BCS("system_bcs", 100, Integer.MAX_VALUE, new a(0), new bz.a(), null);
    
    private String l;
    private int m;
    private int n;
    private a o;
    private cj p;
    private String q;

    private br(String str, int i, int i2, a aVar, cj cjVar, String str2) {
        this.l = str;
        this.m = i;
        this.n = i2;
        this.o = aVar;
        this.p = cjVar;
        this.q = str2;
    }

    public final String a() {
        return this.l;
    }

    public final int b() {
        return this.m;
    }

    public final a c() {
        return this.o;
    }

    public final cj d() {
        return this.p;
    }

    public final int e() {
        return this.n;
    }

    public final String f() {
        return this.q;
    }
}
