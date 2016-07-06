package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import java.util.concurrent.ExecutorService;
import org.json.JSONException;

public final class ec {
    aw a;
    ExecutorService b;
    dg c;
    dw d;

    class AnonymousClass1 implements Runnable {
        final /* synthetic */ Throwable a;
        final /* synthetic */ long b;
        final /* synthetic */ ec c;

        AnonymousClass1(ec ecVar, Throwable th, long j) {
            this.c = ecVar;
            this.a = th;
            this.b = j;
        }

        public final void run() {
            try {
                if (!this.c.d.b()) {
                    ch bkVar = new bk(this.a, this.b);
                    bkVar.f = "he";
                    try {
                        bkVar.g.put("app_version", CrittercismConfig.API_VERSION);
                    } catch (JSONException e) {
                    }
                    bkVar.g.remove("logcat");
                    this.c.a.p().a(bkVar);
                }
            } catch (ThreadDeath e2) {
            } catch (Throwable th) {
                ec ecVar = this.c;
                Throwable th2 = this.a;
            }
        }
    }

    public ec(aw awVar, ExecutorService executorService, dg dgVar, dw dwVar) {
        this.a = awVar;
        this.b = executorService;
        this.c = dgVar;
        this.d = dwVar;
    }
}
