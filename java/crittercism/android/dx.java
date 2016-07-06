package crittercism.android;

import android.util.Log;
import crittercism.android.ec.AnonymousClass1;

public final class dx {
    public static a a = a.UNINITIALIZED;
    private static ec b;

    public enum a {
        UNINITIALIZED,
        ON,
        OFF
    }

    public static void a(ec ecVar) {
        b = ecVar;
    }

    public static void a() {
    }

    public static void b() {
    }

    public static void c() {
    }

    public static void a(String str) {
        Log.i("Crittercism", str);
    }

    public static void b(String str) {
        Log.e("Crittercism", str);
    }

    public static void a(String str, Throwable th) {
        Log.e("Crittercism", str, th);
    }

    public static void c(String str) {
        Log.w("Crittercism", str);
    }

    public static void b(String str, Throwable th) {
        Log.w("Crittercism", str, th);
    }

    public static void a(Throwable th) {
        if (!(th instanceof cp)) {
            try {
                ec ecVar = b;
                if (b != null && a == a.ON) {
                    ecVar = b;
                    Runnable anonymousClass1 = new AnonymousClass1(ecVar, th, Thread.currentThread().getId());
                    if (!ecVar.c.a(anonymousClass1)) {
                        ecVar.b.execute(anonymousClass1);
                    }
                }
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th2) {
            }
        }
    }
}
