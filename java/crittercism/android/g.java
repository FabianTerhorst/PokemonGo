package crittercism.android;

import android.os.ConditionVariable;
import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public final class g implements f, Runnable {
    private List a;
    private URL b;
    private long c;
    private ConditionVariable d;
    private au e;
    private ConditionVariable f;
    private volatile boolean g;
    private final Object h;
    private int i;
    private volatile long j;

    public g(au auVar, URL url) {
        this(auVar, url, (byte) 0);
    }

    private g(au auVar, URL url, byte b) {
        this.a = new LinkedList();
        this.b = null;
        this.c = System.currentTimeMillis();
        this.d = new ConditionVariable(false);
        this.f = new ConditionVariable(false);
        this.g = false;
        this.h = new Object();
        this.i = 50;
        this.j = 10000;
        this.e = auVar;
        this.b = url;
        this.i = 50;
        this.j = 10000;
    }

    public final void run() {
        while (!this.g) {
            this.f.block();
            this.d.block();
            if (!this.g) {
                try {
                    if (b() > 0) {
                        Thread.sleep(b());
                    }
                } catch (InterruptedException e) {
                }
                this.c = System.currentTimeMillis();
                HttpURLConnection c = c();
                if (c == null) {
                    this.g = true;
                    dx.b("Disabling APM due to failure instantiating connection");
                    return;
                }
                List list;
                synchronized (this.h) {
                    list = this.a;
                    this.a = new LinkedList();
                    this.d.close();
                }
                try {
                    a a = a.a(this.e, list);
                    if (a == null) {
                        this.g = true;
                        dx.b("Disabling APM due to failure building request");
                        return;
                    }
                    a(c, a.a);
                } catch (Exception e2) {
                    Log.e("Crittercism", "Exited APM send task due to: \n" + e2);
                    return;
                }
            }
            return;
        }
    }

    public final void a() {
        this.f.open();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long b() {
        /*
        r8 = this;
        r0 = 0;
        r2 = r8.j;
        r4 = java.lang.System.currentTimeMillis();
        r6 = r8.c;
        r4 = r4 - r6;
        r6 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r6 <= 0) goto L_0x0017;
    L_0x000f:
        r2 = r2 - r4;
        r4 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
        if (r4 >= 0) goto L_0x0017;
    L_0x0014:
        r2 = r8.j;
        return r0;
    L_0x0017:
        r0 = r2;
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.g.b():long");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.net.HttpURLConnection c() {
        /*
        r8 = this;
        r4 = 0;
        r1 = r8.b;	 Catch:{ IOException -> 0x004d, GeneralSecurityException -> 0x0067 }
        r1 = r1.openConnection();	 Catch:{ IOException -> 0x004d, GeneralSecurityException -> 0x0067 }
        r1 = (java.net.HttpURLConnection) r1;	 Catch:{ IOException -> 0x004d, GeneralSecurityException -> 0x0067 }
        r2 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;
        r1.setConnectTimeout(r2);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = "User-Agent";
        r3 = "5.0.8";
        r1.setRequestProperty(r2, r3);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = "Content-Type";
        r3 = "application/json";
        r1.setRequestProperty(r2, r3);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = 1;
        r1.setDoOutput(r2);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = "POST";
        r1.setRequestMethod(r2);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = r1 instanceof javax.net.ssl.HttpsURLConnection;	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        if (r2 == 0) goto L_0x004c;
    L_0x0029:
        r0 = r1;
        r0 = (javax.net.ssl.HttpsURLConnection) r0;	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = r0;
        r3 = "TLS";
        r3 = javax.net.ssl.SSLContext.getInstance(r3);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r3.init(r5, r6, r7);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r3 = r3.getSocketFactory();	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        if (r3 == 0) goto L_0x004c;
    L_0x003f:
        r5 = r3 instanceof crittercism.android.ab;	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        if (r5 == 0) goto L_0x0049;
    L_0x0043:
        r3 = (crittercism.android.ab) r3;	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r3 = r3.a();	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
    L_0x0049:
        r2.setSSLSocketFactory(r3);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
    L_0x004c:
        return r1;
    L_0x004d:
        r1 = move-exception;
        r2 = r1;
        r1 = r4;
    L_0x0050:
        r3 = new java.lang.StringBuilder;
        r4 = "Failed to instantiate URLConnection to APM server: ";
        r3.<init>(r4);
        r2 = r2.getMessage();
        r2 = r3.append(r2);
        r2 = r2.toString();
        crittercism.android.dx.b(r2);
        goto L_0x004c;
    L_0x0067:
        r1 = move-exception;
        r2 = new java.lang.StringBuilder;
        r3 = "Failed to instantiate URLConnection to APM server: ";
        r2.<init>(r3);
        r1 = r1.getMessage();
        r1 = r2.append(r1);
        r1 = r1.toString();
        crittercism.android.dx.b(r1);
        r1 = r4;
        goto L_0x004c;
    L_0x0080:
        r2 = move-exception;
        goto L_0x0050;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.g.c():java.net.HttpURLConnection");
    }

    private static boolean a(HttpURLConnection httpURLConnection, JSONObject jSONObject) {
        try {
            httpURLConnection.getOutputStream().write(jSONObject.toString().getBytes("UTF8"));
            int responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            if (responseCode == 202) {
                return true;
            }
            return false;
        } catch (IOException e) {
            new StringBuilder("Request failed for ").append(httpURLConnection.getURL().toExternalForm());
            dx.a();
            return false;
        } catch (Exception e2) {
            new StringBuilder("Request failed for ").append(httpURLConnection.getURL().toExternalForm());
            dx.a();
            return false;
        }
    }

    private boolean d() {
        return !this.g && this.a.size() < this.i;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a(crittercism.android.c r5) {
        /*
        r4 = this;
        r0 = 0;
        r1 = r4.d();
        if (r1 != 0) goto L_0x0008;
    L_0x0007:
        return;
    L_0x0008:
        r1 = r4.h;
        monitor-enter(r1);
        r2 = r4.d();	 Catch:{ all -> 0x0013 }
        if (r2 != 0) goto L_0x0016;
    L_0x0011:
        monitor-exit(r1);	 Catch:{ all -> 0x0013 }
        goto L_0x0007;
    L_0x0013:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
    L_0x0016:
        r2 = r4.a;	 Catch:{ all -> 0x0013 }
        r2.add(r5);	 Catch:{ all -> 0x0013 }
        r2 = r4.b;	 Catch:{ all -> 0x0013 }
        r2 = r2.getHost();	 Catch:{ all -> 0x0013 }
        r3 = r5.a();	 Catch:{ all -> 0x0013 }
        r2 = r3.contains(r2);	 Catch:{ all -> 0x0013 }
        if (r2 == 0) goto L_0x0034;
    L_0x002b:
        if (r0 == 0) goto L_0x0032;
    L_0x002d:
        r0 = r4.d;	 Catch:{ all -> 0x0013 }
        r0.open();	 Catch:{ all -> 0x0013 }
    L_0x0032:
        monitor-exit(r1);	 Catch:{ all -> 0x0013 }
        goto L_0x0007;
    L_0x0034:
        r2 = r5.f;	 Catch:{ all -> 0x0013 }
        if (r2 == 0) goto L_0x0044;
    L_0x0038:
        r2 = r2.toLowerCase();	 Catch:{ all -> 0x0013 }
        r3 = "connect";
        r2 = r2.equals(r3);	 Catch:{ all -> 0x0013 }
        if (r2 != 0) goto L_0x002b;
    L_0x0044:
        r0 = 1;
        goto L_0x002b;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.g.a(crittercism.android.c):void");
    }

    public final void a(int i, TimeUnit timeUnit) {
        this.j = timeUnit.toMillis((long) i);
    }
}
