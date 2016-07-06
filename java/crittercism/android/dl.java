package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.HashMap;
import java.util.Map;

public final class dl extends di {
    public Map a = new HashMap();
    private dw b;
    private au c;
    private boolean d = false;
    private boolean e = false;
    private boolean f = false;
    private boolean g = false;

    public dl(au auVar) {
        this.c = auVar;
        this.b = auVar.l();
    }

    public final void b() {
        this.d = true;
    }

    public final void c() {
        this.e = true;
    }

    public final void d() {
        this.f = true;
    }

    public final void e() {
        this.g = true;
    }

    private synchronized void a(String str, Object obj) {
        this.a.put(str, obj);
    }

    public final void a() {
        boolean z = false;
        boolean b = this.b.b();
        if (this.d) {
            a("optOutStatus", Boolean.valueOf(b));
        }
        if (!b) {
            if (this.e) {
                a("crashedOnLastLoad", Boolean.valueOf(dq.a));
            }
            if (this.f) {
                a("userUUID", this.c.c());
            }
            if (this.g) {
                dt dtVar = az.A().A;
                if (dtVar != null) {
                    String str = "shouldShowRateAppAlert";
                    if (dtVar.a.getBoolean("rateMyAppEnabled", false) && !dtVar.a.getBoolean("hasRatedApp", false)) {
                        int a = dtVar.a();
                        int i = dtVar.a.getInt("rateAfterNumLoads", 5);
                        if (a >= i) {
                            if ((a - i) % dtVar.a.getInt("remindAfterNumLoads", 5) == 0) {
                                z = true;
                            }
                        }
                    }
                    a(str, Boolean.valueOf(z));
                    a(Keys.MESSAGE, dtVar.b());
                    a(Keys.TITLE, dtVar.c());
                }
            }
        }
    }
}
