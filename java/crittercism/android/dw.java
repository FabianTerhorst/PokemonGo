package crittercism.android;

import crittercism.android.ds.a;

public final class dw {
    private ds a;
    private du b;

    public final synchronized du a() {
        return this.b;
    }

    public final synchronized boolean b() {
        boolean z;
        z = true;
        if (this.a != null) {
            z = this.a.a();
        }
        return z;
    }

    public final synchronized void a(ax axVar) {
        this.a = a.a(axVar);
        if (!this.a.a()) {
            int b = axVar.b(cq.SESSION_ID_SETTING.a(), cq.SESSION_ID_SETTING.b());
            if (b == 0) {
                b = axVar.b(cq.OLD_SESSION_ID_SETTING.a(), cq.OLD_SESSION_ID_SETTING.b());
            }
            du duVar = new du(b);
            duVar.a++;
            this.b = duVar;
        }
    }
}
