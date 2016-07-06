package com.unity3d.player;

import android.os.Build.VERSION;

public final class q {
    static final boolean a = (VERSION.SDK_INT >= 11);
    static final boolean b = (VERSION.SDK_INT >= 12);
    static final boolean c = (VERSION.SDK_INT >= 14);
    static final boolean d = (VERSION.SDK_INT >= 16);
    static final boolean e = (VERSION.SDK_INT >= 17);
    static final boolean f = (VERSION.SDK_INT >= 19);
    static final boolean g = (VERSION.SDK_INT >= 21);
    static final boolean h;
    static final f i = (a ? new d() : null);
    static final e j = (b ? new c() : null);
    static final h k = (d ? new l() : null);
    static final g l = (e ? new k() : null);
    static final i m;

    static {
        i iVar = null;
        boolean z = true;
        if (VERSION.SDK_INT < 23) {
            z = false;
        }
        h = z;
        if (h) {
            iVar = new n();
        }
        m = iVar;
    }
}
