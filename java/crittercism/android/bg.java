package crittercism.android;

import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import com.crittercism.app.Transaction;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bg extends Transaction implements ch {
    private static ExecutorService b = Executors.newSingleThreadExecutor(new dz());
    private static ScheduledExecutorService c = Executors.newScheduledThreadPool(1, new dz());
    private static List o = new LinkedList();
    private static volatile long p = 0;
    private static volatile long q = 0;
    private static final int[] r = new int[]{32, 544, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 8224};
    private static bg s = null;
    private static bh t = new bh();
    private String d;
    private long e = -1;
    private int f = -1;
    private long g;
    private long h;
    private long i;
    private a j;
    private Map k;
    private String l;
    private long m;
    private ScheduledFuture n = null;

    enum a {
        CREATED,
        STARTED,
        SUCCESS,
        SLOW,
        FAILED,
        TIMEOUT,
        CRASHED,
        ABORTED,
        INTERRUPTED
    }

    public static void a(bh bhVar) {
        t = bhVar;
    }

    public bg(az azVar, String str) {
        int i = -1;
        if (str.length() > 255) {
            dx.c("Transaction name exceeds 255 characters! Truncating to first 255 characters.");
            this.d = str.substring(0, 255);
        } else {
            this.d = str;
        }
        this.j = a.CREATED;
        this.k = new HashMap();
        this.a = azVar;
        this.l = cg.a.a();
        this.e = -1;
        JSONObject optJSONObject = t.d.optJSONObject(str);
        if (optJSONObject != null) {
            i = optJSONObject.optInt(GameServices.SCORE_VALUE, -1);
        }
        this.f = i;
    }

    private bg(bg bgVar) {
        this.d = bgVar.d;
        this.e = bgVar.e;
        this.f = bgVar.f;
        this.g = bgVar.g;
        this.h = bgVar.h;
        this.j = bgVar.j;
        this.k = bgVar.k;
        this.l = bgVar.l;
        this.i = bgVar.i;
        this.m = bgVar.m;
    }

    public bg(JSONArray jSONArray) {
        this.d = jSONArray.getString(0);
        this.j = a.values()[jSONArray.getInt(1)];
        this.e = (long) ((int) (jSONArray.getDouble(2) * 1000.0d));
        this.f = jSONArray.optInt(3, -1);
        this.k = new HashMap();
        JSONObject jSONObject = jSONArray.getJSONObject(4);
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            this.k.put(str, jSONObject.getString(str));
        }
        this.g = ed.a.a(jSONArray.getString(5));
        this.h = ed.a.a(jSONArray.getString(6));
        this.i = (long) (jSONArray.optDouble(7, 0.0d) * Math.pow(10.0d, 9.0d));
        this.l = cg.a.a();
    }

    public static void f() {
        p = System.nanoTime();
        List<bg> linkedList = new LinkedList();
        synchronized (o) {
            linkedList.addAll(o);
        }
        if (s != null && q == 0) {
            synchronized (s) {
                bg bgVar = s;
                bgVar.i += p - s.m;
            }
        }
        for (bg bgVar2 : linkedList) {
            synchronized (bgVar2) {
                if (bgVar2.j == a.STARTED) {
                    if (bgVar2.n != null && bgVar2.n.isCancelled()) {
                        bgVar2.a(bgVar2.e - TimeUnit.MILLISECONDS.convert(bgVar2.i, TimeUnit.NANOSECONDS));
                    } else if (bgVar2.n == null) {
                        bgVar2.a(bgVar2.e);
                    }
                }
            }
        }
    }

    private static boolean l() {
        return p > q;
    }

    public static void a(final az azVar) {
        q = System.nanoTime();
        final List<bg> linkedList = new LinkedList();
        synchronized (o) {
            linkedList.addAll(o);
        }
        for (bg bgVar : linkedList) {
            synchronized (bgVar) {
                if (bgVar.j == a.STARTED) {
                    if (bgVar.m < p) {
                        bgVar.i += q - p;
                    } else if (bgVar.m <= q) {
                        bgVar.i += q - bgVar.m;
                    }
                }
                bgVar.r();
            }
        }
        Object futureTask = new FutureTask(new di() {
            public final void a() {
                for (bg bgVar : linkedList) {
                    synchronized (bgVar) {
                        if (bgVar.j == a.STARTED) {
                            azVar.n.b(bgVar);
                        }
                    }
                }
            }
        }, null);
        synchronized (b) {
            b.execute(futureTask);
        }
        try {
            futureTask.get();
        } catch (Throwable e) {
            dx.a(e);
        } catch (Throwable e2) {
            dx.a(e2);
        }
    }

    public static List a(final az azVar, boolean z) {
        List linkedList = new LinkedList();
        synchronized (o) {
            linkedList.addAll(o);
        }
        long currentTimeMillis = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        for (int size = linkedList.size() - 1; size >= 0; size--) {
            bg bgVar = (bg) linkedList.get(size);
            synchronized (bgVar) {
                if (bgVar.j == a.STARTED) {
                    bgVar.h = currentTimeMillis;
                    bgVar.j = a.CRASHED;
                    if (l()) {
                        bgVar.i = (nanoTime - Math.max(p, bgVar.m)) + bgVar.i;
                    }
                } else {
                    linkedList.remove(size);
                }
                bgVar.r();
            }
        }
        Object futureTask = new FutureTask(new di() {
            public final void a() {
                ea eaVar = ea.TXN_CRASH_ALL_FAULT;
                azVar.n.a();
            }
        }, null);
        synchronized (b) {
            b.execute(futureTask);
            if (z) {
                azVar.z.clear();
            } else {
                b.shutdown();
            }
        }
        try {
            futureTask.get();
        } catch (Throwable e) {
            dx.a(e);
        } catch (Throwable e2) {
            dx.a(e2);
        }
        return linkedList;
    }

    public static void a(aw awVar) {
        try {
            bs w = awVar.w();
            List<bq> c = w.c();
            long currentTimeMillis = System.currentTimeMillis();
            for (bq bqVar : c) {
                JSONArray jSONArray = (JSONArray) ((bz) bqVar).a();
                if (jSONArray != null) {
                    try {
                        ch bgVar = new bg(jSONArray);
                        bgVar.h = currentTimeMillis;
                        bgVar.j = a.ABORTED;
                        awVar.x().a(bgVar);
                    } catch (Throwable e) {
                        dx.a(e);
                    } catch (Throwable e2) {
                        dx.a(e2);
                    }
                }
            }
            w.a();
        } catch (ThreadDeath e3) {
            throw e3;
        } catch (Throwable e22) {
            dx.a(e22);
        }
    }

    public static void b(final az azVar) {
        try {
            bg bgVar = new bg(azVar, "App Load");
            s = bgVar;
            synchronized (bgVar) {
                long m = m();
                if (m != -1) {
                    s.j = a.STARTED;
                    s.g = System.currentTimeMillis() - (SystemClock.elapsedRealtime() - m);
                    bg bgVar2 = s;
                    m = TimeUnit.NANOSECONDS.convert(m, TimeUnit.MILLISECONDS);
                    bgVar2.m = System.nanoTime() - (TimeUnit.NANOSECONDS.convert(SystemClock.elapsedRealtime(), TimeUnit.MILLISECONDS) - m);
                    s.e = t.a(s.d);
                    synchronized (o) {
                        o.add(s);
                    }
                    bgVar2 = new bg(s);
                    Runnable anonymousClass3 = new di() {
                        public final void a() {
                            azVar.q.a.block();
                            azVar.n.a(bgVar2);
                        }
                    };
                    synchronized (b) {
                        b.execute(anonymousClass3);
                        s.a(s.e);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private static long m() {
        long[] jArr = new long[1];
        String str = "/proc/" + Process.myPid() + "/stat";
        try {
            return !((Boolean) Process.class.getDeclaredMethod("readProcFile", new Class[]{String.class, int[].class, String[].class, long[].class, float[].class}).invoke(null, new Object[]{str, r, null, jArr, null})).booleanValue() ? -1 : jArr[0] * 10;
        } catch (Throwable e) {
            dx.a(e);
            return -1;
        } catch (Throwable e2) {
            dx.a(e2);
            return -1;
        } catch (Throwable e22) {
            dx.a(e22);
            return -1;
        } catch (Throwable e222) {
            dx.a(e222);
            return -1;
        }
    }

    public static void g() {
        try {
            if (s != null) {
                s.b();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public final void a() {
        try {
            n();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private synchronized void n() {
        if (this.j == a.CREATED) {
            this.j = a.STARTED;
            this.g = System.currentTimeMillis();
            this.m = System.nanoTime();
            this.e = t.a(this.d);
            synchronized (o) {
                o.add(this);
            }
            final bg bgVar = new bg(this);
            Runnable anonymousClass4 = new di(this) {
                final /* synthetic */ bg b;

                public final void a() {
                    this.b.a.q.a.block();
                    this.b.a.n.a(bgVar);
                }
            };
            synchronized (b) {
                b.execute(anonymousClass4);
                a(this.e);
            }
        } else {
            dx.b("Transaction " + this.d + " has already been started.", new IllegalStateException("Transaction has already started"));
        }
    }

    public final void b() {
        try {
            o();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private synchronized void o() {
        a(a.SUCCESS);
    }

    public final void c() {
        try {
            p();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private synchronized void p() {
        a(a.FAILED);
    }

    public final void h() {
        try {
            q();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private synchronized void q() {
        a(a.INTERRUPTED);
    }

    private void a(a aVar) {
        if (!(aVar == a.SUCCESS || aVar == a.FAILED)) {
            a aVar2 = a.INTERRUPTED;
        }
        if (this.j == a.STARTED) {
            r();
            b(aVar);
        } else if (this.j != a.TIMEOUT) {
            dx.b("Transaction " + this.d + " is not running. Either it has not been started or it has been stopped.", new IllegalStateException("Transaction is not running"));
        }
    }

    public static void i() {
        List<bg> linkedList = new LinkedList();
        synchronized (o) {
            linkedList.addAll(o);
        }
        for (bg bgVar : linkedList) {
            synchronized (bgVar) {
                if (bgVar.j == a.STARTED) {
                    bgVar.e = t.a(bgVar.d);
                    bgVar.r();
                    bgVar.a(bgVar.e);
                }
            }
        }
    }

    private void a(long j) {
        if (l()) {
            this.n = c.schedule(new di(this) {
                final /* synthetic */ bg a;

                {
                    this.a = r1;
                }

                public final void a() {
                    this.a.s();
                }
            }, j, TimeUnit.MILLISECONDS);
        }
    }

    private synchronized void r() {
        if (this.n != null) {
            this.n.cancel(false);
        }
    }

    private synchronized void s() {
        if (this.j == a.STARTED) {
            b(a.TIMEOUT);
        }
    }

    private void b(a aVar) {
        this.j = aVar;
        this.h = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        if (l()) {
            this.i = (nanoTime - Math.max(p, this.m)) + this.i;
        }
        synchronized (o) {
            o.remove(this);
        }
        final bg bgVar = new bg(this);
        Runnable anonymousClass6 = new di(this) {
            final /* synthetic */ bg b;

            public final void a() {
                if (bgVar.j != a.SUCCESS) {
                    Runnable anonymousClass1 = new Runnable(this) {
                        final /* synthetic */ AnonymousClass6 a;

                        {
                            this.a = r1;
                        }

                        public final void run() {
                        }
                    };
                    Executor executor = this.b.a.s;
                    Object futureTask = new FutureTask(anonymousClass1, null);
                    executor.execute(futureTask);
                    try {
                        futureTask.get();
                    } catch (Throwable e) {
                        dx.a(e);
                    } catch (Throwable e2) {
                        dx.a(e2);
                    }
                }
                this.b.a.q.a.block();
                this.b.a.n.a(this.b.l);
                this.b.a.o.a(bgVar);
            }
        };
        synchronized (b) {
            b.execute(anonymousClass6);
        }
    }

    public final void a(int i) {
        try {
            b(i);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private synchronized void b(int i) {
        if (i < 0) {
            dx.c("Ignoring Transaction.setValue(int) call. Negative parameter provided.");
        } else if (this.j == a.CREATED) {
            this.f = i;
        } else if (this.j == a.STARTED) {
            this.f = i;
            final bg bgVar = new bg(this);
            Runnable anonymousClass7 = new di(this) {
                final /* synthetic */ bg b;

                public final void a() {
                    this.b.a.q.a.block();
                    this.b.a.n.a(bgVar);
                }
            };
            synchronized (b) {
                b.execute(anonymousClass7);
            }
        } else {
            dx.b("Transaction " + this.d + " no longer in progress. Ignoring setValue(int) call.", new IllegalStateException("Transaction no longer in progress"));
        }
    }

    public final int d() {
        try {
            return t();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return -1;
        }
    }

    private synchronized int t() {
        return this.f;
    }

    public final JSONArray j() {
        Object obj;
        JSONArray put = new JSONArray().put(this.d).put(this.j.ordinal()).put(((double) this.e) / 1000.0d);
        if (this.f == -1) {
            obj = JSONObject.NULL;
        } else {
            obj = Integer.valueOf(this.f);
        }
        JSONArray put2 = put.put(obj).put(new JSONObject(this.k)).put(ed.a.a(new Date(this.g))).put(ed.a.a(new Date(this.h)));
        if (VERSION.SDK_INT >= 14) {
            put2.put(((double) Math.round((((double) this.i) / Math.pow(10.0d, 9.0d)) * 1000.0d)) / 1000.0d);
        } else {
            put2.put(JSONObject.NULL);
        }
        return put2;
    }

    public final String e() {
        return this.l;
    }

    public final void a(OutputStream outputStream) {
        JSONArray jSONArray = null;
        try {
            jSONArray = j();
        } catch (JSONException e) {
        }
        if (jSONArray != null) {
            outputStream.write(jSONArray.toString().getBytes());
        }
    }

    public final a k() {
        return this.j;
    }
}
