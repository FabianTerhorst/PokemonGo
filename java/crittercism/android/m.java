package crittercism.android;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public abstract class m extends URLStreamHandler {
    public static final String[] a = new String[]{"java.net.URL", "int", "java.net.Proxy"};
    public static final String[] b = new String[]{"java.net.URL", "int"};
    e c;
    d d;
    boolean e;
    private Constructor f;
    private Constructor g;

    protected abstract String a();

    protected abstract int getDefaultPort();

    public m(e eVar, d dVar, String[] strArr) {
        this(eVar, dVar, strArr, a, b);
    }

    private m(e eVar, d dVar, String[] strArr, String[] strArr2, String[] strArr3) {
        this.f = null;
        this.g = null;
        this.c = eVar;
        this.d = dVar;
        this.e = true;
        int i = 0;
        while (i < strArr.length) {
            try {
                this.f = l.a(strArr[i], strArr3);
                this.g = l.a(strArr[i], strArr2);
                this.f.setAccessible(true);
                this.g.setAccessible(true);
                break;
            } catch (ClassNotFoundException e) {
                this.f = null;
                this.f = null;
                i++;
            }
        }
        if (this.f == null || this.g == null) {
            throw new ClassNotFoundException("Couldn't find suitable connection implementations");
        } else if (!b()) {
            throw new ClassNotFoundException("Unable to open test connections");
        }
    }

    private boolean b() {
        this.e = false;
        try {
            openConnection(new URL("http://www.google.com"));
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            this.e = true;
        }
    }

    protected URLConnection openConnection(URL u) {
        return a(u, null);
    }

    protected URLConnection openConnection(URL url, Proxy proxy) {
        if (url != null && proxy != null) {
            return a(url, proxy);
        }
        throw new IllegalArgumentException("url == null || proxy == null");
    }

    private URLConnection a(URL url, Proxy proxy) {
        IOException iOException;
        URLConnection uRLConnection = null;
        String str = "Unable to setup network statistics on a " + a() + " connection due to ";
        try {
            ea eaVar = ea.GENERIC_HANDLER_DO_OPEN_CONNECTION_FAULT;
            if (proxy == null) {
                uRLConnection = (URLConnection) this.f.newInstance(new Object[]{url, Integer.valueOf(getDefaultPort())});
                iOException = null;
            } else {
                uRLConnection = (URLConnection) this.g.newInstance(new Object[]{url, Integer.valueOf(getDefaultPort()), proxy});
                iOException = null;
            }
        } catch (IllegalArgumentException e) {
            new StringBuilder().append(str).append("bad arguments");
            dx.b();
            iOException = new IOException(e.getMessage());
        } catch (InstantiationException e2) {
            new StringBuilder().append(str).append("an instantiation problem");
            dx.b();
            iOException = new IOException(e2.getMessage());
        } catch (IllegalAccessException e3) {
            new StringBuilder().append(str).append("security restrictions");
            dx.b();
            iOException = new IOException(e3.getMessage());
        } catch (InvocationTargetException e4) {
            new StringBuilder().append(str).append("an invocation problem");
            dx.b();
            iOException = new IOException(e4.getMessage());
        }
        if (iOException != null) {
            if (this.e) {
                boolean c;
                this.e = false;
                v a = v.a();
                if (a != null) {
                    c = a.c();
                } else {
                    c = false;
                }
                dx.b("Stopping network statistics monitoring");
                if (c) {
                    return new URL(url.toExternalForm()).openConnection();
                }
            }
            throw iOException;
        }
        return uRLConnection;
    }
}
