package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.ConditionVariable;
import com.crittercism.app.CrittercismNDK;
import crittercism.android.cs.b;
import crittercism.android.ct.a;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class dg extends di {
    public ConditionVariable a = new ConditionVariable();
    public bm b = null;
    private ConditionVariable c = new ConditionVariable();
    private bb d;
    private Context e;
    private aw f;
    private ax g;
    private au h;
    private List i = new ArrayList();
    private boolean j = false;
    private boolean k;
    private Exception l = null;

    public dg(bb bbVar, Context context, aw awVar, ax axVar, au auVar) {
        this.d = bbVar;
        this.e = context;
        this.f = awVar;
        this.g = axVar;
        this.h = auVar;
        this.k = false;
    }

    private synchronized void c() {
        this.j = true;
    }

    private synchronized boolean d() {
        return this.j;
    }

    public final synchronized boolean a(Runnable runnable) {
        boolean z;
        if (d()) {
            z = false;
        } else {
            this.i.add(runnable);
            z = true;
        }
        return z;
    }

    private File e() {
        int i = 0;
        File file = new File(this.e.getFilesDir().getAbsolutePath() + "/" + this.d.g());
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return null;
        }
        if (listFiles.length == 1) {
            File file2 = listFiles[0];
            file2.isFile();
            if (file2.isFile()) {
                return file2;
            }
            return null;
        } else if (listFiles.length <= 1) {
            return null;
        } else {
            int length = listFiles.length;
            while (i < length) {
                File file3 = listFiles[i];
                file3.isFile();
                file3.delete();
                i++;
            }
            return null;
        }
    }

    private void a(File file) {
        boolean z = this.k;
        az A = az.A();
        if (!A.t) {
            if (file != null && file.exists()) {
                file.isFile();
            }
            aw awVar = this.f;
            bs s = this.f.s();
            bs t = this.f.t();
            bs u = this.f.u();
            bs v = this.f.v();
            bs q = this.f.q();
            if (file != null) {
                dq.a = true;
                A.e.open();
                q.a(new cd(file, s, u, t, v));
                file.delete();
                this.f.w().a();
            } else {
                A.e.open();
                bg.a(this.f);
            }
            u.a();
            t.a();
            v.a();
            s.a(u);
        }
    }

    private void f() {
        if (!az.A().t) {
            boolean z = this.k;
            bs n = this.f.n();
            bs o = this.f.o();
            bs p = this.f.p();
            bs q = this.f.q();
            bs r = this.f.r();
            dv y = this.f.y();
            this.d.b();
            this.b = new bm(this.h);
            if (!this.d.delaySendingAppLoad()) {
                n.a(this.b);
                df dfVar = new df(this.e);
                dfVar.a(n, new a(), this.d.e(), "/v0/appload", this.d.b(), this.h, new b());
                dfVar.a(o, new da.a(), this.d.b(), "/android_v2/handle_exceptions", null, this.h, new cu.a());
                dfVar.a(q, new da.a(), this.d.b(), "/android_v2/handle_ndk_crashes", null, this.h, new cu.a());
                dfVar.a(r, new da.a(), this.d.b(), "/android_v2/handle_crashes", null, this.h, new cu.a());
                dfVar.a(p, new da.a(), this.d.b(), "/android_v2/handle_exceptions", null, new ba(this.h, this.d), new cu.a());
                dfVar.a();
            }
            if (y.b()) {
                az.A().E();
            }
        }
    }

    public final void a() {
        dx.b();
        File file = new File(this.e.getFilesDir().getAbsolutePath() + "/com.crittercism/pending");
        if (!file.exists() || file.isDirectory()) {
            try {
                eb.a(file);
            } catch (Exception e) {
                new StringBuilder("Exception in run(): ").append(e.getMessage());
                dx.b();
                dx.c();
                this.l = e;
            } finally {
                this.c.open();
            }
        } else {
            dx.b();
        }
        az A = az.A();
        A.w.a();
        dw l = this.h.l();
        A.d.open();
        ax axVar = this.g;
        Context context = this.e;
        l.a(axVar);
        dq.a = dq.a(this.e).booleanValue();
        dq.a(this.e, false);
        if (!l.b()) {
            dt dtVar = new dt(this.e);
            dtVar.a.edit().putInt("numAppLoads", dtVar.a() + 1).commit();
            az.A().A = dtVar;
            l.a().a(this.g, cq.SESSION_ID_SETTING.a(), cq.SESSION_ID_SETTING.b());
        }
        this.k = l.b();
        File e2 = e();
        boolean z;
        if (this.k) {
            z = this.k;
            if (!az.A().t) {
                if (e2 != null && e2.exists()) {
                    e2.isFile();
                }
                new bs(this.e, br.APP_LOADS).a();
                new bs(this.e, br.HAND_EXCS).a();
                new bs(this.e, br.INTERNAL_EXCS).a();
                new bs(this.e, br.NDK_CRASHES).a();
                new bs(this.e, br.SDK_CRASHES).a();
                new bs(this.e, br.CURR_BCS).a();
                new bs(this.e, br.PREV_BCS).a();
                new bs(this.e, br.NW_BCS).a();
                new bs(this.e, br.SYSTEM_BCS).a();
                if (e2 != null) {
                    e2.delete();
                }
            }
            h.b(this.e);
        } else {
            Context context2 = this.e;
            h hVar = new h(context2);
            SharedPreferences sharedPreferences = context2.getSharedPreferences("com.crittercism.optmz.config", 0);
            if (sharedPreferences.contains("interval")) {
                hVar.d = sharedPreferences.getInt("interval", 10);
                if (sharedPreferences.contains("kill")) {
                    hVar.c = sharedPreferences.getBoolean("kill", false);
                    if (sharedPreferences.contains("persist")) {
                        hVar.b = sharedPreferences.getBoolean("persist", false);
                        if (sharedPreferences.contains("enabled")) {
                            hVar.a = sharedPreferences.getBoolean("enabled", false);
                        } else {
                            hVar = null;
                        }
                    } else {
                        hVar = null;
                    }
                } else {
                    hVar = null;
                }
            } else {
                hVar = null;
            }
            if (hVar != null) {
                az.A().a(hVar);
            }
            z = this.k;
            this.f.z();
            if (!az.A().t) {
                bh a = bh.a(this.e);
                try {
                    Runnable biVar = new bi(this.e, this.h, this.f.x(), this.f.s(), this.f.t(), this.f.v(), new URL(this.d.d() + "/api/v1/transactions"));
                    az A2 = az.A();
                    A2.y = biVar;
                    new dy(biVar, "TXN Thread").start();
                    A2.a(a);
                } catch (MalformedURLException e3) {
                    dx.a();
                }
            }
            a(e2);
            this.a.open();
            this.f.s().a(cf.a);
            if (!az.A().t && this.d.isNdkCrashReportingEnabled()) {
                dx.b();
                try {
                    CrittercismNDK.installNdkLib(this.e, this.d.g());
                } catch (Throwable th) {
                    new StringBuilder("Exception installing ndk library: ").append(th.getClass().getName());
                    dx.b();
                }
            }
            f();
        }
        c();
        for (Runnable biVar2 : this.i) {
            biVar2.run();
        }
    }

    public final void b() {
        this.c.block();
    }
}
