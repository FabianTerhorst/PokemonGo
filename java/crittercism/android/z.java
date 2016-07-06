package crittercism.android;

import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public final class z extends SSLContextSpi {
    private static Method[] a = new Method[7];
    private static boolean b;
    private SSLContextSpi c;
    private e d;
    private d e;

    static {
        b = false;
        try {
            a[0] = SSLContextSpi.class.getDeclaredMethod("engineCreateSSLEngine", new Class[0]);
            a[1] = SSLContextSpi.class.getDeclaredMethod("engineCreateSSLEngine", new Class[]{String.class, Integer.TYPE});
            a[2] = SSLContextSpi.class.getDeclaredMethod("engineGetClientSessionContext", new Class[0]);
            a[3] = SSLContextSpi.class.getDeclaredMethod("engineGetServerSessionContext", new Class[0]);
            a[4] = SSLContextSpi.class.getDeclaredMethod("engineGetServerSocketFactory", new Class[0]);
            a[5] = SSLContextSpi.class.getDeclaredMethod("engineGetSocketFactory", new Class[0]);
            a[6] = SSLContextSpi.class.getDeclaredMethod("engineInit", new Class[]{KeyManager[].class, TrustManager[].class, SecureRandom.class});
            j.a(a);
            z zVar = new z(new z(), null, null);
            zVar.engineCreateSSLEngine();
            zVar.engineCreateSSLEngine(null, 0);
            zVar.engineGetClientSessionContext();
            zVar.engineGetServerSessionContext();
            zVar.engineGetServerSocketFactory();
            zVar.engineGetSocketFactory();
            zVar.engineInit(null, null, null);
            b = true;
        } catch (Throwable th) {
            dx.c();
            b = false;
        }
    }

    private z(SSLContextSpi sSLContextSpi, e eVar, d dVar) {
        this.c = sSLContextSpi;
        this.d = eVar;
        this.e = dVar;
    }

    public static z a(SSLContextSpi sSLContextSpi, e eVar, d dVar) {
        if (b) {
            return new z(sSLContextSpi, eVar, dVar);
        }
        return null;
    }

    private z() {
    }

    public static boolean a() {
        return b;
    }

    private Object a(int i, Object... objArr) {
        Throwable e;
        if (this.c == null) {
            return null;
        }
        try {
            return a[i].invoke(this.c, objArr);
        } catch (Throwable e2) {
            throw new ck(e2);
        } catch (Throwable e22) {
            throw new ck(e22);
        } catch (Throwable e222) {
            Throwable th = e222;
            e222 = th.getTargetException();
            if (e222 == null) {
                throw new ck(th);
            } else if (e222 instanceof Exception) {
                throw ((Exception) e222);
            } else if (e222 instanceof Error) {
                throw ((Error) e222);
            } else {
                throw new ck(th);
            }
        } catch (Throwable e2222) {
            throw new ck(e2222);
        }
    }

    private Object b(int i, Object... objArr) {
        try {
            return a(i, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e2) {
            throw new ck(e2);
        }
    }

    private Object a(Object... objArr) {
        try {
            return a(6, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (KeyManagementException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new ck(e3);
        }
    }

    protected final SSLEngine engineCreateSSLEngine() {
        return (SSLEngine) b(0, new Object[0]);
    }

    protected final SSLEngine engineCreateSSLEngine(String host, int port) {
        return (SSLEngine) b(1, host, Integer.valueOf(port));
    }

    protected final SSLSessionContext engineGetClientSessionContext() {
        return (SSLSessionContext) b(2, new Object[0]);
    }

    protected final SSLSessionContext engineGetServerSessionContext() {
        return (SSLSessionContext) b(3, new Object[0]);
    }

    protected final SSLServerSocketFactory engineGetServerSocketFactory() {
        return (SSLServerSocketFactory) b(4, new Object[0]);
    }

    protected final SSLSocketFactory engineGetSocketFactory() {
        SSLSocketFactory sSLSocketFactory = (SSLSocketFactory) b(5, new Object[0]);
        if (sSLSocketFactory == null) {
            return sSLSocketFactory;
        }
        try {
            return new ab(sSLSocketFactory, this.d, this.e);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return sSLSocketFactory;
        }
    }

    protected final void engineInit(KeyManager[] km, TrustManager[] tm, SecureRandom sr) {
        a(km, tm, sr);
    }

    public final boolean equals(Object o) {
        SSLContextSpi sSLContextSpi = this.c;
        return this.c.equals(o);
    }

    public final int hashCode() {
        SSLContextSpi sSLContextSpi = this.c;
        return this.c.hashCode();
    }

    public final String toString() {
        SSLContextSpi sSLContextSpi = this.c;
        return this.c.toString();
    }
}
