package crittercism.android;

import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public final class df {
    private Context a;
    private List b = new ArrayList();

    public df(Context context) {
        this.a = context;
    }

    public final synchronized void a(bs bsVar, cz czVar, String str, String str2, String str3, au auVar, cx cxVar) {
        if (bsVar.b() > 0) {
            bs a = bsVar.a(this.a);
            cy a2 = czVar.a(a, bsVar, str3, this.a, auVar);
            this.b.add(new dh(a, bsVar, auVar, new db(str, str2).a(), a2, cxVar));
        }
    }

    public final void a(dg dgVar, ExecutorService executorService) {
        for (Runnable runnable : this.b) {
            if (!dgVar.a(runnable)) {
                executorService.execute(runnable);
            }
        }
    }

    public final void a() {
        ArrayList arrayList = new ArrayList();
        for (di thread : this.b) {
            arrayList.add(new Thread(thread));
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((Thread) it.next()).start();
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            ((Thread) it2.next()).join();
        }
    }
}
