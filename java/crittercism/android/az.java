package crittercism.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.ConditionVariable;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.os.Process;
import com.crittercism.app.CritterRateMyAppButtons;
import com.crittercism.app.CrittercismConfig;
import com.crittercism.app.Transaction;
import com.crittercism.integrations.PluginException;
import crittercism.android.bx.f;
import crittercism.android.bx.o;
import crittercism.android.bx.p;
import crittercism.android.cs.b;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class az implements au, aw, ax, f {
    static az a;
    public dt A = null;
    int B = 0;
    public boolean C = false;
    private String D = null;
    private bs E;
    private bs F;
    private g G = null;
    private at H;
    private boolean I = false;
    private String J = BuildConfig.FLAVOR;
    public boolean b = false;
    public Context c = null;
    public final ConditionVariable d = new ConditionVariable(false);
    public final ConditionVariable e = new ConditionVariable(false);
    public dw f = new dw();
    bs g;
    bs h;
    bs i;
    bs j;
    bs k;
    bs l;
    bs m;
    bs n;
    bs o;
    cv p = null;
    public dg q = null;
    ExecutorService r = Executors.newCachedThreadPool(new dz());
    public ExecutorService s = Executors.newSingleThreadExecutor(new dz());
    public boolean t = false;
    public bb u;
    protected e v = new e(this.s);
    public dr w;
    dv x = null;
    public bi y;
    public Map z = new HashMap();

    public static /* synthetic */ class AnonymousClass4 {
        public static final /* synthetic */ int[] a = new int[CritterRateMyAppButtons.values().length];

        static {
            try {
                a[CritterRateMyAppButtons.YES.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[CritterRateMyAppButtons.NO.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[CritterRateMyAppButtons.LATER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public class AnonymousClass7 extends di {
        final /* synthetic */ cf a;
        final /* synthetic */ az b;

        public AnonymousClass7(az azVar, cf cfVar) {
            this.b = azVar;
            this.a = cfVar;
        }

        public final void a() {
            this.b.k.a(this.a);
        }
    }

    static class a implements IdleHandler {
        private boolean a;

        private a() {
            this.a = false;
        }

        public final boolean queueIdle() {
            synchronized (this) {
                if (!this.a) {
                    this.a = true;
                    bg.g();
                }
            }
            return true;
        }
    }

    protected az() {
    }

    public static az A() {
        if (a == null) {
            a = new az();
        }
        return a;
    }

    public final void a(Context context, String str, CrittercismConfig crittercismConfig) {
        dx.a("Initializing Crittercism 5.0.8 for App ID " + str);
        bn bnVar = new bn(str);
        this.D = str;
        this.u = new bb(bnVar, crittercismConfig);
        this.c = context;
        this.H = new at(this.c, this.u);
        this.J = context.getPackageName();
        this.w = new dr(context);
        G();
        long j = 60000000000L;
        if (this.t) {
            j = 12000000000L;
        }
        this.p = new cv(j);
        if (!F()) {
            dx.c("Crittercism should be initialized in onCreate() of MainActivity");
        }
        bx.a(this.H);
        bx.a(this.c);
        bx.a(new cc());
        bx.a(new bf(this.c, this.u));
        try {
            this.v.a(this.u.a());
            this.v.b(this.u.getPreserveQueryStringPatterns());
            this.G = new g(this, new URL(this.u.c() + "/api/apm/network"));
            this.v.a(this.G);
            this.v.a((f) this);
            new dy(this.G, "OPTMZ").start();
            if (!h.a(this.c).exists() && this.u.isServiceMonitoringEnabled()) {
                this.I = new i(this.v, new d(this.c)).a();
                new StringBuilder("installedApm = ").append(this.I);
                dx.b();
            }
        } catch (Exception e) {
            new StringBuilder("Exception in startApm: ").append(e.getClass().getName());
            dx.b();
            dx.c();
        }
        this.q = new dg(this.u, context, this, this, this);
        if (!this.t) {
            dx.a(new ec(this, this.s, this.q, this.f));
        }
        UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(defaultUncaughtExceptionHandler instanceof ay)) {
            Thread.setDefaultUncaughtExceptionHandler(new ay(this, defaultUncaughtExceptionHandler));
        }
        if (VERSION.SDK_INT < 14) {
            dx.a("API Level is less than 14. Automatic breadcrumbs are not supported.");
        } else if (this.c instanceof Application) {
            dx.b();
            ((Application) this.c).registerActivityLifecycleCallbacks(new av(this.c, this));
        } else {
            dx.c("Application context not provided. Automatic breadcrumbs will not be recorded.");
        }
        if (!this.t) {
            bg.b(this);
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Looper.myQueue().addIdleHandler(new a());
            }
        }
        new dy(this.q).start();
        this.b = true;
    }

    private static boolean F() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getMethodName().equals("onCreate") || stackTraceElement.getMethodName().equals("onResume")) {
                return true;
            }
        }
        return false;
    }

    private void G() {
        int myUid = Process.myUid();
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) this.c.getSystemService("activity");
        int i = 0;
        for (RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
            int i2;
            if (runningAppProcessInfo.uid == myUid) {
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2;
        }
        if (i <= 1) {
            this.t = false;
            return;
        }
        for (RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.pid == myPid) {
                this.t = true;
                return;
            }
        }
    }

    public final void a(Throwable th) {
        if (this.q == null) {
            dx.b("Unable to handle application crash. Crittercism not yet initialized");
            return;
        }
        this.q.b();
        dq.a(this.c, true);
        if (!this.f.b()) {
            if (this.t) {
                new dj(new cu(this).a(br.SDK_CRASHES.f(), new JSONArray().put(new bk(th, Thread.currentThread().getId()).b())), new dc(new db(this.u.b(), "/android_v2/handle_crashes").a()), null).run();
                return;
            }
            List a = bg.a(this, th instanceof PluginException);
            ch bkVar = new bk(th, Thread.currentThread().getId());
            bkVar.a("crashed_session", this.k);
            if (this.F.b() > 0) {
                bkVar.a("previous_session", this.F);
            }
            bkVar.a(this.l);
            bkVar.b = new bo(this.m).a;
            bkVar.a();
            bkVar.a(a);
            this.j.a(bkVar);
            df dfVar = new df(this.c);
            dfVar.a(this.g, new crittercism.android.da.a(), this.u.e(), "/v0/appload", null, this, new b());
            dfVar.a(this.h, new crittercism.android.da.a(), this.u.b(), "/android_v2/handle_exceptions", null, this, new crittercism.android.cu.a());
            dfVar.a(this.i, new crittercism.android.da.a(), this.u.b(), "/android_v2/handle_ndk_crashes", null, this, new crittercism.android.cu.a());
            dfVar.a(this.j, new crittercism.android.da.a(), this.u.b(), "/android_v2/handle_crashes", null, this, new crittercism.android.cu.a());
            try {
                dfVar.a();
            } catch (InterruptedException e) {
                new StringBuilder("InterruptedException in logCrashException: ").append(e.getMessage());
                dx.b();
                dx.c();
            } catch (Throwable th2) {
                new StringBuilder("Unexpected throwable in logCrashException: ").append(th2.getMessage());
                dx.b();
                dx.c();
            }
        }
    }

    public final void a(String str, URL url, long j, long j2, long j3, int i, Exception exception, long j4) {
        if (str == null) {
            dx.b("Null HTTP request method provided. Endpoint will not be logged.");
            return;
        }
        String toUpperCase = str.toUpperCase(Locale.US);
        Set hashSet = new HashSet();
        hashSet.add("GET");
        hashSet.add("POST");
        hashSet.add("HEAD");
        hashSet.add("PUT");
        hashSet.add("DELETE");
        hashSet.add("TRACE");
        hashSet.add("OPTIONS");
        hashSet.add("CONNECT");
        hashSet.add("PATCH");
        if (!hashSet.contains(toUpperCase)) {
            dx.c("Logging endpoint with invalid HTTP request method: " + str);
        }
        if (url == null) {
            dx.b("Null URL provided. Endpoint will not be logged");
        } else if (j2 < 0 || j3 < 0) {
            dx.b("Invalid byte values. Bytes need to be non-negative. Endpoint will not be logged.");
        } else {
            if (i != 0) {
                if (i < 100 || i >= 600) {
                    dx.c("Logging endpoint with invalid HTTP response code: " + Integer.toString(i));
                }
            } else if (exception == null) {
                dx.c("Logging endpoint with null error and response code of 0.");
            }
            b a = new d(this.c).a();
            if (j < 0) {
                dx.b("Invalid latency. Endpoint will not be logged.");
            } else if (j4 < 0) {
                dx.b("Invalid start time. Endpoint will not be logged.");
            } else {
                c cVar = new c();
                cVar.f = toUpperCase;
                cVar.a(url.toExternalForm());
                cVar.b(j2);
                cVar.d(j3);
                cVar.e = i;
                cVar.j = a;
                cVar.e(j4);
                cVar.f(j4 + j);
                if (bc.b()) {
                    cVar.a(bc.a());
                }
                cVar.a((Throwable) exception);
                this.v.a(cVar, crittercism.android.c.a.LOG_ENDPOINT);
            }
        }
    }

    private String H() {
        try {
            if (this.J == null || this.J.equals(BuildConfig.FLAVOR)) {
                this.J = this.c.getPackageName();
            }
        } catch (Exception e) {
            dx.c("Call to getPackageName() failed.  Please contact us at support@crittercism.com.");
            this.J = new String();
        }
        return this.J;
    }

    public final synchronized void b(final Throwable th) {
        if (th == null) {
            dx.c("Calling logHandledException with a null java.lang.Throwable. Nothing will be reported to Crittercism");
        } else if (this.t) {
            r0 = Thread.currentThread().getId();
            r2 = new di(this) {
                final /* synthetic */ az c;

                public final void a() {
                    if (!this.c.f.b()) {
                        synchronized (this.c.p) {
                            if (this.c.B < 10) {
                                bk bkVar = new bk(th, r0);
                                bkVar.a("current_session", this.c.k);
                                bkVar.a(this.c.l);
                                bkVar.f = "he";
                                if (this.c.p.a()) {
                                    new dj(new cu(az.a).a(br.HAND_EXCS.f(), new JSONArray().put(bkVar.b())), new dc(new db(this.c.u.b(), "/android_v2/handle_exceptions").a()), null).run();
                                    az azVar = this.c;
                                    azVar.B++;
                                    this.c.p.b();
                                }
                            }
                        }
                    }
                }
            };
            if (!this.q.a(r2)) {
                this.s.execute(r2);
            }
        } else {
            r0 = Thread.currentThread().getId();
            r2 = new di(this) {
                final /* synthetic */ az c;

                public final void a() {
                    if (!this.c.f.b()) {
                        ch bkVar = new bk(th, r0);
                        bkVar.a("current_session", this.c.k);
                        bkVar.f = "he";
                        if (this.c.h.a(bkVar)) {
                            az.a.a(new by(bkVar.c, bkVar.d));
                            if (this.c.p.a()) {
                                df dfVar = new df(this.c.c);
                                dfVar.a(this.c.h, new crittercism.android.da.a(), this.c.u.b(), "/android_v2/handle_exceptions", null, az.a, new crittercism.android.cu.a());
                                dfVar.a(this.c.q, this.c.r);
                                this.c.p.b();
                            }
                        }
                    }
                }
            };
            if (!this.q.a(r2)) {
                this.s.execute(r2);
            }
        }
    }

    public final void a(final c cVar) {
        Runnable anonymousClass8 = new di(this) {
            final /* synthetic */ az b;

            public final void a() {
                this.b.l.a(cVar);
            }
        };
        if (!this.q.a(anonymousClass8)) {
            this.s.execute(anonymousClass8);
        }
    }

    public final void a(final ci ciVar) {
        if (!this.f.b()) {
            Runnable anonymousClass9 = new di(this) {
                final /* synthetic */ az b;

                public final void a() {
                    this.b.m.a(ciVar);
                }
            };
            if (!this.q.a(anonymousClass9)) {
                this.s.execute(anonymousClass9);
            }
        }
    }

    public final String a() {
        String str = this.D;
        if (str == null) {
            return BuildConfig.FLAVOR;
        }
        return str;
    }

    public final String c() {
        String str = BuildConfig.FLAVOR;
        if (this.w != null) {
            return this.w.a();
        }
        return str;
    }

    public final String f() {
        return new f().a;
    }

    public final int g() {
        return new o().a.intValue();
    }

    public final int h() {
        return new p().a.intValue();
    }

    public final int e() {
        if (this.f != null) {
            return Integer.valueOf(this.f.a().a).intValue();
        }
        return -1;
    }

    public final void z() {
        if (this.t) {
            this.k = new bs(this.c, br.CURR_BCS).a(this.c);
        } else {
            this.k = new bs(this.c, br.CURR_BCS);
        }
        this.F = new bs(this.c, br.PREV_BCS);
        this.l = new bs(this.c, br.NW_BCS);
        this.m = new bs(this.c, br.SYSTEM_BCS);
        this.g = new bs(this.c, br.APP_LOADS);
        this.h = new bs(this.c, br.HAND_EXCS);
        this.E = new bs(this.c, br.INTERNAL_EXCS);
        this.i = new bs(this.c, br.NDK_CRASHES);
        this.j = new bs(this.c, br.SDK_CRASHES);
        this.n = new bs(this.c, br.STARTED_TXNS);
        this.o = new bs(this.c, br.FINISHED_TXNS);
        if (!this.t) {
            this.x = new dv(this.c, this.D);
        }
    }

    public final bs n() {
        return this.g;
    }

    public final bs o() {
        return this.h;
    }

    public final bs p() {
        return this.E;
    }

    public final bs q() {
        return this.i;
    }

    public final bs r() {
        return this.j;
    }

    public final dw l() {
        return this.f;
    }

    public final bs w() {
        return this.n;
    }

    public final bs x() {
        return this.o;
    }

    public final String a(String str, String str2) {
        SharedPreferences sharedPreferences = this.c.getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(str2, null);
        }
        return null;
    }

    public final void a(String str, String str2, String str3) {
        SharedPreferences sharedPreferences = this.c.getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            Editor edit = sharedPreferences.edit();
            if (edit != null) {
                edit.remove(str2);
                edit.putString(str2, str3);
                edit.commit();
            }
        }
    }

    public final void a(bh bhVar) {
        bi biVar = this.y;
        if (this.y != null) {
            bg.a(bhVar);
            bg.i();
            if (bhVar.a) {
                this.y.a(bhVar.b, TimeUnit.SECONDS);
                this.y.b();
            }
        }
    }

    public final void a(h hVar) {
        if (this.G != null && hVar.a && !hVar.c) {
            dx.a("Enabling OPTMZ");
            this.G.a(hVar.d, TimeUnit.SECONDS);
            this.G.a();
        }
    }

    public final int b(String str, String str2) {
        SharedPreferences sharedPreferences = this.c.getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(str2, 0);
        }
        return 0;
    }

    public final void a(String str, String str2, int i) {
        SharedPreferences sharedPreferences = this.c.getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            Editor edit = sharedPreferences.edit();
            if (edit != null) {
                edit.remove(str2);
                edit.putInt(str2, i);
                edit.commit();
            }
        }
    }

    public final boolean c(String str, String str2) {
        SharedPreferences sharedPreferences = this.c.getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(str2, false);
        }
        return false;
    }

    public final bs s() {
        return this.k;
    }

    public final bs u() {
        return this.F;
    }

    public final bs t() {
        return this.l;
    }

    public final bs v() {
        return this.m;
    }

    public final String b() {
        return this.H.a;
    }

    public final String d() {
        return CrittercismConfig.API_VERSION;
    }

    public final String i() {
        return "Android";
    }

    public final String j() {
        return Build.MODEL;
    }

    public final String k() {
        return VERSION.RELEASE;
    }

    public final boolean B() {
        this.d.block();
        return this.f.b();
    }

    public final void a(String str) {
        dt dtVar = this.A;
        if (this.A != null) {
            this.A.d();
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(Uri.parse(str));
        this.c.startActivity(intent);
    }

    public final void C() {
        dt dtVar = this.A;
        if (this.A != null) {
            this.A.d();
        }
    }

    public final String D() {
        PackageManager packageManager = this.c.getPackageManager();
        String H = H();
        if (H == null || H.length() <= 0) {
            return null;
        }
        dn a = dp.a(packageManager.getInstallerPackageName(H));
        if (a != null) {
            return a.a(H).a();
        }
        dx.c("Could not find app market for this app.  Will try rate-my-app test target in config.");
        return this.u.getRateMyAppTestTarget();
    }

    public final AlertDialog a(Context context, String str, String str2) {
        AlertDialog alertDialog = null;
        Object obj = null;
        if (this.f.b()) {
            dx.b("User has opted out of crittercism.  generateRateMyAppAlertDialog returning null.");
        } else if (!(context instanceof Activity)) {
            dx.b("Context object must be an instance of Activity for AlertDialog to form correctly.  generateRateMyAppAlertDialog returning null.");
        } else if (str2 == null || (str2 != null && str2.length() == 0)) {
            dx.b("Message has to be a non-empty string.  generateRateMyAppAlertDialog returning null.");
        } else if (VERSION.SDK_INT < 5) {
            dx.b("Rate my app not supported below api level 5");
        } else {
            obj = 1;
        }
        if (obj != null) {
            final String D = D();
            if (D == null) {
                dx.b("Cannot create proper URI to open app market.  Returning null.");
            } else {
                Builder builder = new Builder(context);
                builder.setTitle(str).setMessage(str2);
                try {
                    alertDialog = builder.create();
                    alertDialog.setButton(-1, "Yes", new OnClickListener(this) {
                        final /* synthetic */ az b;

                        public final void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                az.A().a(D);
                            } catch (Exception e) {
                                dx.c("YES button failed.  Email support@crittercism.com.");
                            }
                        }
                    });
                    alertDialog.setButton(-2, "No", new OnClickListener(this) {
                        final /* synthetic */ az a;

                        {
                            this.a = r1;
                        }

                        public final void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                az.A().C();
                            } catch (Exception e) {
                                dx.c("NO button failed.  Email support@crittercism.com.");
                            }
                        }
                    });
                    alertDialog.setButton(-3, "Maybe Later", new OnClickListener(this) {
                        final /* synthetic */ az a;

                        {
                            this.a = r1;
                        }

                        public final void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                az.A();
                            } catch (Exception e) {
                                dx.c("MAYBE LATER button failed.  Email support@crittercism.com.");
                            }
                        }
                    });
                } catch (Exception e) {
                    dx.b("Failed to create AlertDialog instance from AlertDialog.Builder.  Did you remember to call Looper.prepare() before calling Crittercism.generateRateMyAppAlertDialog()?");
                }
            }
        }
        return alertDialog;
    }

    public final void a(final JSONObject jSONObject) {
        if (!this.t) {
            Runnable anonymousClass2 = new di(this) {
                final /* synthetic */ az c;

                public final void a() {
                    if (!this.f.b()) {
                        this.x.a(jSONObject);
                        if (this.x.b()) {
                            this.E();
                        }
                    }
                }
            };
            if (!this.q.a(anonymousClass2)) {
                this.s.execute(anonymousClass2);
            }
        }
    }

    public final void E() {
        if (!this.t) {
            Runnable anonymousClass3 = new di(this) {
                final /* synthetic */ az b;

                public final void a() {
                    if (!this.f.b()) {
                        cw cuVar = new cu(this);
                        JSONObject a = this.x.a();
                        cuVar.a.put("metadata", a);
                        new dj(cuVar, new dc(new db(this.b.u.b(), "/android_v2/update_user_metadata").a()), new dd(this.x)).run();
                    }
                }
            };
            if (!this.q.a(anonymousClass3)) {
                this.r.execute(anonymousClass3);
            }
        }
    }

    public final dv y() {
        return this.x;
    }

    public final int b(String str) {
        if (this.t) {
            dx.c("Transactions are not supported for services. Returning default value of -1 for " + str + ".");
            return -1;
        }
        int d;
        synchronized (this.z) {
            Transaction transaction = (Transaction) this.z.get(str);
            if (transaction != null) {
                d = transaction.d();
            } else {
                d = -1;
            }
        }
        return d;
    }

    public final dt m() {
        return this.A;
    }
}
