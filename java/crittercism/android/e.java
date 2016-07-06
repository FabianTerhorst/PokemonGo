package crittercism.android;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public final class e {
    List a;
    final Set b;
    final Set c;
    private Executor d;

    class a implements Runnable {
        final /* synthetic */ e a;
        private c b;

        private a(e eVar, c cVar) {
            this.a = eVar;
            this.b = cVar;
        }

        public final void run() {
            if (!a(this.b)) {
                String a = this.b.a();
                if (a(a)) {
                    int indexOf = a.indexOf("?");
                    if (indexOf != -1) {
                        this.b.a(a.substring(0, indexOf));
                    }
                }
                synchronized (this.a.a) {
                    for (f a2 : this.a.a) {
                        a2.a(this.b);
                    }
                }
            }
        }

        private boolean a(c cVar) {
            String a = cVar.a();
            synchronized (this.a.b) {
                for (String contains : this.a.b) {
                    if (a.contains(contains)) {
                        return true;
                    }
                }
                return false;
            }
        }

        private boolean a(String str) {
            synchronized (this.a.c) {
                for (String contains : this.a.c) {
                    if (str.contains(contains)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private e(Executor executor, List list, List list2) {
        this.a = new LinkedList();
        this.b = new HashSet();
        this.c = new HashSet();
        this.d = executor;
        a(list);
        b(list2);
    }

    public e(Executor executor) {
        this(executor, new LinkedList(), new LinkedList());
    }

    public final void a(f fVar) {
        synchronized (this.a) {
            this.a.add(fVar);
        }
    }

    public final void a(List list) {
        synchronized (this.b) {
            this.b.addAll(list);
            this.b.remove(null);
        }
    }

    public final void b(List list) {
        synchronized (this.c) {
            this.c.addAll(list);
            this.c.remove(null);
        }
    }

    @Deprecated
    public final void a(c cVar) {
        a(cVar, crittercism.android.c.a.LEGACY_JAVANET);
    }

    public final void a(c cVar, crittercism.android.c.a aVar) {
        if (!cVar.b) {
            cVar.b = true;
            cVar.c = aVar;
            this.d.execute(new a(cVar));
        }
    }
}
