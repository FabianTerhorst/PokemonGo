package crittercism.android;

import android.os.Build.VERSION;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public final class i {
    public static final crittercism.android.v.a a = crittercism.android.v.a.HTTPS_ONLY;
    public static b b;
    private static final List c = new LinkedList();
    private ad d;
    private ab e;
    private ab f;
    private v g;
    private e h;
    private d i;
    private b j = b;
    private crittercism.android.v.a k = a;

    static class a implements Runnable {
        private boolean a;
        private boolean b = false;
        private i c;

        public a(i iVar) {
            this.c = iVar;
            this.a = true;
        }

        public final boolean a() {
            return this.b;
        }

        public final void run() {
            if (this.a) {
                this.b = this.c.c();
            } else {
                this.c.b();
            }
        }
    }

    public enum b {
        SOCKET_MONITOR,
        STREAM_MONITOR,
        NONE
    }

    static {
        b = b.NONE;
        try {
            if (!((URLStreamHandler) j.a(j.a(URL.class, URLStreamHandler.class), new URL("https://www.google.com"))).getClass().getName().contains("okhttp") || VERSION.SDK_INT < 19) {
                b = b.STREAM_MONITOR;
            } else {
                b = b.SOCKET_MONITOR;
            }
        } catch (Exception e) {
            b = b.NONE;
        }
    }

    public i(e eVar, d dVar) {
        this.h = eVar;
        this.i = dVar;
    }

    public final boolean a() {
        if (ac.c()) {
            try {
                boolean e;
                boolean a;
                ac.e();
                int h = h() | 0;
                if (VERSION.SDK_INT >= 19) {
                    e = h | e();
                } else {
                    e = h | c();
                }
                if (VERSION.SDK_INT >= 17) {
                    a = e | y.a(this.h, this.i);
                } else {
                    a = e;
                }
                if (this.j == b.SOCKET_MONITOR) {
                    SSLSocketFactory defaultSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                    if (defaultSSLSocketFactory instanceof ab) {
                        this.e = (ab) defaultSSLSocketFactory;
                    } else {
                        this.e = new ab(defaultSSLSocketFactory, this.h, this.i);
                        HttpsURLConnection.setDefaultSSLSocketFactory(this.e);
                    }
                    return a | 1;
                } else if (this.j == b.STREAM_MONITOR) {
                    return a | f();
                } else {
                    return a;
                }
            } catch (Throwable e2) {
                dx.a(e2.toString(), e2);
                return false;
            }
        }
        a("Unable to install OPTMZ", ac.d());
        return false;
    }

    private boolean e() {
        Object aVar = new a(this);
        Thread thread = new Thread(aVar);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
        return aVar.a();
    }

    private boolean f() {
        boolean z = false;
        try {
            this.g = new v(this.k, this.h, this.i);
            z = this.g.b();
        } catch (ClassNotFoundException e) {
        }
        return z;
    }

    public final void b() {
        try {
            SSLSocketFactory g = g();
            if (g instanceof ab) {
                a(((ab) g).a());
            }
            this.f = null;
        } catch (Throwable e) {
            a("Unable to install OPTIMZ for SSL HttpClient connections", e);
        } catch (Throwable e2) {
            a("Unable to install OPTIMZ for SSL HttpClient connections", e2);
        } catch (Throwable e22) {
            a("Unable to install OPTIMZ for SSL HttpClient connections", e22);
        }
    }

    public final boolean c() {
        try {
            SSLSocketFactory g = g();
            if (g == null) {
                a("Unable to install OPTIMZ for SSL HttpClient connections", new NullPointerException("Delegate factory was null"));
                return false;
            } else if (g instanceof ab) {
                return false;
            } else {
                SSLSocketFactory abVar = new ab(g, this.h, this.i);
                try {
                    a(abVar);
                    this.f = abVar;
                    return true;
                } catch (Throwable e) {
                    a("Unable to install OPTIMZ for SSL HttpClient connections", e);
                    return false;
                } catch (Throwable e2) {
                    a("Unable to install OPTIMZ for SSL HttpClient connections", e2);
                    return false;
                } catch (Throwable e22) {
                    a("Unable to install OPTIMZ for SSL HttpClient connections", e22);
                    return false;
                }
            }
        } catch (Throwable e222) {
            a("Unable to install OPTIMZ for SSL HttpClient connections", e222);
            return false;
        } catch (Throwable e2222) {
            a("Unable to install OPTIMZ for SSL HttpClient connections", e2222);
            return false;
        } catch (Throwable e22222) {
            a("Unable to install OPTIMZ for SSL HttpClient connections", e22222);
            return false;
        } catch (Throwable e222222) {
            a("Unable to install OPTIMZ for SSL HttpClient connections", e222222);
            return false;
        }
    }

    private static SSLSocketFactory g() {
        return (SSLSocketFactory) j.a(org.apache.http.conn.ssl.SSLSocketFactory.class, SSLSocketFactory.class).get(org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory());
    }

    private static void a(SSLSocketFactory sSLSocketFactory) {
        j.a(org.apache.http.conn.ssl.SSLSocketFactory.class, SSLSocketFactory.class).set(org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory(), sSLSocketFactory);
    }

    private boolean h() {
        Class cls = null;
        try {
            ad adVar;
            SocketImplFactory socketImplFactory = (SocketImplFactory) j.a(j.a(Socket.class, SocketImplFactory.class), null);
            if (socketImplFactory == null) {
                try {
                    SocketImpl socketImpl = (SocketImpl) j.a(j.a(Socket.class, SocketImpl.class), new Socket());
                    if (socketImpl == null) {
                        throw new cl("SocketImpl was null");
                    }
                    cls = socketImpl.getClass();
                } catch (Throwable e) {
                    a("Unable to install OPTIMZ for http connections", e);
                    return false;
                }
            } else if (socketImplFactory instanceof ad) {
                return true;
            }
            if (socketImplFactory != null) {
                try {
                    SocketImplFactory adVar2 = new ad(socketImplFactory, this.h, this.i);
                    a(adVar2);
                    adVar = adVar2;
                } catch (Throwable e2) {
                    a("Unable to install OPTIMZ for http connections", e2);
                    return false;
                } catch (Throwable e22) {
                    a("Unable to install OPTIMZ for http connections", e22);
                    return false;
                }
            } else if (cls != null) {
                adVar = new ad(cls, this.h, this.i);
                Socket.setSocketImplFactory(adVar);
            } else {
                a("Unable to install OPTIMZ for http connections", new NullPointerException("Null SocketImpl"));
                return false;
            }
            this.d = adVar;
            return true;
        } catch (Throwable e222) {
            a("Unable to install OPTIMZ for http connections", e222);
            return false;
        }
    }

    private static boolean a(SocketImplFactory socketImplFactory) {
        try {
            Field a = j.a(Socket.class, SocketImplFactory.class);
            try {
                a.setAccessible(true);
                a.set(null, socketImplFactory);
                return true;
            } catch (Throwable e) {
                a("Unable to install OPTIMZ for http connections", e);
                return true;
            } catch (Throwable e2) {
                a("Unable to install OPTIMZ for http connections", e2);
                return false;
            } catch (Throwable e22) {
                a("Unable to install OPTIMZ for http connections", e22);
                return false;
            }
        } catch (Throwable e222) {
            a("Unable to install OPTIMZ for http connections", e222);
            return false;
        }
    }

    private static void a(String str, Throwable th) {
        synchronized (c) {
            c.add(th);
        }
        dx.c(str);
    }

    public static void d() {
        synchronized (c) {
            for (Throwable a : c) {
                dx.a(a);
            }
            c.clear();
        }
    }
}
