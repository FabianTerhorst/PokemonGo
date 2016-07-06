package com.crittercism.app;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.os.Build.VERSION;
import com.crittercism.integrations.PluginException;
import com.voxelbusters.nativeplugins.defines.Keys.Ui;
import crittercism.android.az;
import crittercism.android.az.AnonymousClass4;
import crittercism.android.az.AnonymousClass7;
import crittercism.android.bc;
import crittercism.android.bg;
import crittercism.android.bn.a;
import crittercism.android.cf;
import crittercism.android.cs.b;
import crittercism.android.di;
import crittercism.android.dk;
import crittercism.android.dq;
import crittercism.android.dt;
import crittercism.android.dx;
import java.lang.reflect.Array;
import java.net.URL;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.R;

public class Crittercism {
    private Crittercism() {
    }

    public static void performRateMyAppButtonAction(CritterRateMyAppButtons critterRateMyAppButtons) {
        try {
            if (az.A().f.b()) {
                dx.c("User has opted out of crittercism.  performRateMyAppButtonAction exiting.");
                return;
            }
            az A = az.A();
            if (VERSION.SDK_INT < 5) {
                dx.c("Rate my app not supported below api level 5");
                return;
            }
            String D = A.D();
            if (D == null) {
                dx.b("Cannot create proper URI to open app market.  Returning null.");
                return;
            }
            switch (AnonymousClass4.a[critterRateMyAppButtons.ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    try {
                        A.a(D);
                        return;
                    } catch (Exception e) {
                        dx.c("performRateMyAppButtonAction(CritterRateMyAppButtons.YES) failed.  Email support@crittercism.com.");
                        dx.c();
                        return;
                    }
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                    try {
                        A.C();
                        return;
                    } catch (Exception e2) {
                        dx.c("performRateMyAppButtonAction(CritterRateMyAppButtons.NO) failed.  Email support@crittercism.com.");
                        return;
                    }
                default:
                    return;
            }
        } catch (ThreadDeath e3) {
            throw e3;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static AlertDialog generateRateMyAppAlertDialog(Context context, String str, String str2) {
        try {
            return az.A().a(context, str, str2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return null;
        }
    }

    public static AlertDialog generateRateMyAppAlertDialog(Context context) {
        AlertDialog alertDialog = null;
        try {
            String b;
            String c;
            az A = az.A();
            dt dtVar = A.A;
            if (A.A != null) {
                b = A.A.b();
                c = A.A.c();
            } else {
                c = null;
                b = null;
            }
            alertDialog = A.a(context, c, b);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
        return alertDialog;
    }

    public static synchronized void initialize(Context context, String str, CrittercismConfig crittercismConfig) {
        synchronized (Crittercism.class) {
            if (str == null) {
                try {
                    a(String.class.getCanonicalName());
                } catch (a e) {
                    throw new IllegalArgumentException("Crittercism cannot be initialized. " + e.getMessage());
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.a(th);
                }
            } else if (context == null) {
                a(Context.class.getCanonicalName());
            } else if (crittercismConfig == null) {
                a(CrittercismConfig.class.getCanonicalName());
            } else if (!az.A().b) {
                long nanoTime = System.nanoTime();
                az.A().a(context, str, crittercismConfig);
                new StringBuilder("Crittercism finished initializing in ").append((System.nanoTime() - nanoTime) / 1000000).append("ms");
                dx.b();
            }
        }
        return;
    }

    public static synchronized void initialize(Context context, String appID) {
        synchronized (Crittercism.class) {
            initialize(context, appID, new CrittercismConfig());
        }
    }

    private static void a(String str) {
        dx.b("Crittercism cannot be initialized", new NullPointerException(str + " was null"));
    }

    public static void sendAppLoadData() {
        try {
            CrittercismConfig crittercismConfig = az.A().u;
            if (crittercismConfig == null) {
                b("sendAppLoadData");
            } else if (!crittercismConfig.delaySendingAppLoad()) {
                dx.a("sendAppLoadData() will only send data to Crittercism if \"delaySendingAppLoad\" is set to true in the configuration settings you include in the init call.");
            } else if (!az.A().f.b()) {
                az A = az.A();
                if (!A.u.delaySendingAppLoad()) {
                    dx.c("CrittercismConfig instance not set to delay sending app loads.");
                } else if (!A.t && !A.C) {
                    A.C = true;
                    Runnable anonymousClass1 = new di(A) {
                        final /* synthetic */ az a;

                        {
                            this.a = r1;
                        }

                        public final void a() {
                            if (!this.a.f.b()) {
                                ch chVar = this.a.q.b;
                                if (chVar != null) {
                                    this.a.g.a(chVar);
                                }
                                df dfVar = new df(this.a.c);
                                dfVar.a(this.a.g, new crittercism.android.ct.a(), this.a.u.e(), "/v0/appload", this.a.u.b(), az.a, new b());
                                dfVar.a(this.a.h, new crittercism.android.da.a(), this.a.u.b(), "/android_v2/handle_exceptions", null, az.a, new crittercism.android.cu.a());
                                dfVar.a(this.a.i, new crittercism.android.da.a(), this.a.u.b(), "/android_v2/handle_ndk_crashes", null, az.a, new crittercism.android.cu.a());
                                dfVar.a(this.a.j, new crittercism.android.da.a(), this.a.u.b(), "/android_v2/handle_crashes", null, az.a, new crittercism.android.cu.a());
                                dfVar.a(this.a.q, this.a.r);
                            }
                        }
                    };
                    if (!A.q.a(anonymousClass1)) {
                        A.s.execute(anonymousClass1);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void logHandledException(Throwable th) {
        try {
            if (!az.A().b) {
                b("logHandledException");
            } else if (!az.A().f.b()) {
                az.A().b(th);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            dx.a(th2);
        }
    }

    public static void _logHandledException(String name, String msg, String stack) {
        try {
            new StringBuilder("_logHandledException(name, msg, stack) called: ").append(name).append(" ").append(msg).append(" ").append(stack);
            dx.b();
            if (name == null || msg == null || stack == null) {
                dx.c("Calling logHandledException with null parameter(s). Nothing will be reported to Crittercism");
            } else {
                logHandledException(new PluginException(name, msg, stack));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void _logHandledException(String name, String msg, String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        try {
            new StringBuilder("_logHandledException(name, msg, classes, methods, files, lines) called: ").append(name);
            dx.b();
            if (name == null || msg == null || classStackElems == null) {
                dx.c("Calling logHandledException with null parameter(s). Nothing will be reported to Crittercism");
            } else {
                logHandledException(new PluginException(name, msg, classStackElems, methodStackElems, fileStackElems, lineNumberStackElems));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void logNetworkRequest(String method, URL url, long responseTime, long bytesRead, long bytesSent, int responseCode, Exception error) {
        try {
            long currentTimeMillis = System.currentTimeMillis() - responseTime;
            if (!az.A().b) {
                b("logEndpoint");
            } else if (!az.A().f.b()) {
                az.A().a(method, url, responseTime, bytesRead, bytesSent, responseCode, error, currentTimeMillis);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static boolean didCrashOnLastLoad() {
        boolean z = false;
        try {
            az A = az.A();
            if (!A.b) {
                b("didCrashOnLoad");
            } else if (!A.B()) {
                A.e.block();
                z = dq.a;
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
        return z;
    }

    @Deprecated
    public static void _logCrashException(Throwable exception) {
        try {
            new StringBuilder("_logCrashException(Throwable) called with throwable: ").append(exception.getMessage());
            dx.b();
            if (az.A().b) {
                az.A().a(exception);
            } else {
                b("_logCrashException");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void _logCrashException(String name, String msg, String stack) {
        if (name == null || msg == null || stack == null) {
            try {
                dx.b("Unable to handle application crash. Missing parameters");
                return;
            } catch (Throwable th) {
                dx.a(th);
                return;
            }
        }
        new StringBuilder("_logCrashException(name, msg, stack) called: ").append(name).append(" ").append(msg).append(" ").append(stack);
        dx.b();
        _logCrashException(new PluginException(name, msg, stack));
    }

    public static void _logCrashException(String msg, String stack) {
        if (msg == null || stack == null) {
            try {
                dx.b("Unable to handle application crash. Missing parameters");
                return;
            } catch (Throwable th) {
                dx.a(th);
                return;
            }
        }
        new StringBuilder("_logCrashException(msg, stack) called: ").append(msg).append(" ").append(stack);
        dx.b();
        _logCrashException(new PluginException(msg, stack));
    }

    public static void _logCrashException(String name, String msg, String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        try {
            new StringBuilder("_logCrashException(String, String, String[], String[], String[], int[]) called: ").append(name).append(" ").append(msg);
            dx.b();
            if (name == null || msg == null || classStackElems == null || methodStackElems == null || fileStackElems == null || lineNumberStackElems == null) {
                dx.b("Unable to handle application crash. Missing parameters");
                return;
            }
            if (a(classStackElems, methodStackElems, fileStackElems, lineNumberStackElems)) {
                _logCrashException(new PluginException(name, msg, classStackElems, methodStackElems, fileStackElems, lineNumberStackElems));
            } else {
                dx.b("Unable to handle application crash. Missing stack elements");
            }
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private static boolean a(Object... objArr) {
        if (objArr.length <= 0 || objArr[0] == null) {
            return false;
        }
        int length = Array.getLength(objArr[0]);
        for (int i = 1; i < objArr.length; i++) {
            if (objArr[i] == null) {
                return false;
            }
            if (Array.getLength(objArr[i]) != length) {
                return false;
            }
        }
        return true;
    }

    public static boolean getOptOutStatus() {
        boolean z = false;
        try {
            az A = az.A();
            if (A.b) {
                z = A.B();
            } else {
                b("getOptOutStatus");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
        return z;
    }

    public static void setOptOutStatus(boolean z) {
        try {
            if (az.A().b) {
                az A = az.A();
                Runnable dkVar = new dk(A.c, A, z);
                if (!A.q.a(dkVar)) {
                    A.s.execute(dkVar);
                    return;
                }
                return;
            }
            b("setOptOutStatus");
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void setMetadata(JSONObject jSONObject) {
        try {
            if (az.A().b) {
                az.A().a(jSONObject);
            } else {
                b("setMetadata");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void setUsername(String str) {
        try {
            if (!az.A().b) {
                b("setUsername");
            } else if (str == null) {
                dx.c("Crittercism.setUsername() given invalid parameter: null");
            } else {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.putOpt(Ui.USER_NAME, str);
                    az.A().a(jSONObject);
                } catch (Throwable e) {
                    dx.b("Crittercism.setUsername()", e);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable e3) {
            dx.a(e3);
        }
    }

    public static void leaveBreadcrumb(String str) {
        try {
            if (!az.A().b) {
                b("leaveBreadcrumb");
            } else if (str == null) {
                dx.b("Cannot leave null breadcrumb", new NullPointerException());
            } else {
                az A = az.A();
                if (!A.f.b()) {
                    Runnable anonymousClass7 = new AnonymousClass7(A, new cf(str, cf.a.NORMAL));
                    if (!A.q.a(anonymousClass7)) {
                        new StringBuilder("SENDING ").append(str).append(" TO EXECUTOR");
                        dx.b();
                        A.s.execute(anonymousClass7);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void beginTransaction(String str) {
        try {
            az A = az.A();
            if (A.t) {
                dx.c("Transactions are not supported for services. Ignoring Crittercism.beginTransaction() call for " + str + ".");
                return;
            }
            Transaction a = Transaction.a(str);
            if (a instanceof bg) {
                synchronized (A.z) {
                    Transaction transaction = (Transaction) A.z.remove(str);
                    if (transaction != null) {
                        ((bg) transaction).h();
                    }
                    if (A.z.size() > 50) {
                        dx.c("Crittercism only supports a maximum of 50 concurrent transactions. Ignoring Crittercism.beginTransaction() call for " + str + ".");
                        return;
                    }
                    A.z.put(str, a);
                    a.a();
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void endTransaction(String str) {
        try {
            az A = az.A();
            if (A.t) {
                dx.c("Transactions are not supported for services. Ignoring Crittercism.endTransaction() call for " + str + ".");
                return;
            }
            Transaction transaction;
            synchronized (A.z) {
                transaction = (Transaction) A.z.remove(str);
            }
            if (transaction != null) {
                transaction.b();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void failTransaction(String str) {
        try {
            az A = az.A();
            if (A.t) {
                dx.c("Transactions are not supported for services. Ignoring Crittercism.failTransaction() call for " + str + ".");
                return;
            }
            Transaction transaction;
            synchronized (A.z) {
                transaction = (Transaction) A.z.remove(str);
            }
            if (transaction != null) {
                transaction.c();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static void setTransactionValue(String str, int i) {
        try {
            az A = az.A();
            if (A.t) {
                dx.c("Transactions are not supported for services. Ignoring Crittercism.setTransactionValue() call for " + str + ".");
                return;
            }
            synchronized (A.z) {
                Transaction transaction = (Transaction) A.z.get(str);
                if (transaction != null) {
                    transaction.a(i);
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public static int getTransactionValue(String str) {
        try {
            return az.A().b(str);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return -1;
        }
    }

    public static void updateLocation(Location curLocation) {
        if (!az.A().b) {
            b("updateLocation");
        } else if (curLocation == null) {
            dx.b("Cannot leave null location", new NullPointerException());
        } else {
            bc.a(curLocation);
        }
    }

    private static void b(String str) {
        dx.b("Must initialize Crittercism before calling " + Crittercism.class.getName() + "." + str + "().  Request is being ignored...", new IllegalStateException());
    }
}
