package crittercism.android;

import crittercism.android.c.a;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketImpl;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;

public final class ac extends SocketImpl implements ae {
    private static Field a;
    private static Field b;
    private static Field c;
    private static Field d;
    private static Method[] e = new Method[20];
    private static boolean f;
    private static Throwable g;
    private final Queue h = new LinkedList();
    private e i;
    private d j;
    private SocketImpl k;
    private w l;
    private x m;

    static {
        f = false;
        g = null;
        try {
            Class cls = SocketImpl.class;
            a = cls.getDeclaredField("address");
            b = cls.getDeclaredField("fd");
            c = cls.getDeclaredField("localport");
            d = cls.getDeclaredField("port");
            AccessibleObject accessibleObject = a;
            AccessibleObject[] accessibleObjectArr = new AccessibleObject[]{b, c, d};
            if (accessibleObject != null) {
                accessibleObject.setAccessible(true);
            }
            if (accessibleObjectArr.length > 0) {
                j.a(accessibleObjectArr);
            }
            e[0] = cls.getDeclaredMethod("accept", new Class[]{SocketImpl.class});
            e[1] = cls.getDeclaredMethod("available", new Class[0]);
            e[2] = cls.getDeclaredMethod("bind", new Class[]{InetAddress.class, Integer.TYPE});
            e[3] = cls.getDeclaredMethod("close", new Class[0]);
            e[4] = cls.getDeclaredMethod("connect", new Class[]{InetAddress.class, Integer.TYPE});
            e[5] = cls.getDeclaredMethod("connect", new Class[]{SocketAddress.class, Integer.TYPE});
            e[6] = cls.getDeclaredMethod("connect", new Class[]{String.class, Integer.TYPE});
            e[7] = cls.getDeclaredMethod("create", new Class[]{Boolean.TYPE});
            e[8] = cls.getDeclaredMethod("getFileDescriptor", new Class[0]);
            e[9] = cls.getDeclaredMethod("getInetAddress", new Class[0]);
            e[10] = cls.getDeclaredMethod("getInputStream", new Class[0]);
            e[11] = cls.getDeclaredMethod("getLocalPort", new Class[0]);
            e[12] = cls.getDeclaredMethod("getOutputStream", new Class[0]);
            e[13] = cls.getDeclaredMethod("getPort", new Class[0]);
            e[14] = cls.getDeclaredMethod("listen", new Class[]{Integer.TYPE});
            e[15] = cls.getDeclaredMethod("sendUrgentData", new Class[]{Integer.TYPE});
            e[16] = cls.getDeclaredMethod("setPerformancePreferences", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
            e[17] = cls.getDeclaredMethod("shutdownInput", new Class[0]);
            e[18] = cls.getDeclaredMethod("shutdownOutput", new Class[0]);
            e[19] = cls.getDeclaredMethod("supportsUrgentData", new Class[0]);
            j.a(e);
            f = true;
        } catch (Throwable e) {
            f = false;
            g = e;
        } catch (Throwable e2) {
            Throwable th = e2;
            f = false;
            int i = 0;
            while (i < 20) {
                if (e[i] == null) {
                    break;
                }
                i++;
            }
            i = 20;
            g = new ck("Bad method: " + i, th);
        } catch (Throwable e22) {
            Throwable th2 = e22;
            f = false;
            String str = "unknown";
            if (a == null) {
                str = "address";
            } else if (b == null) {
                str = "fd";
            } else if (c == null) {
                str = "localport";
            } else if (d == null) {
                str = "port";
            }
            g = new ck("No such field: " + str, th2);
        } catch (Throwable e222) {
            f = false;
            g = e222;
        }
    }

    public ac(e eVar, d dVar, SocketImpl socketImpl) {
        if (eVar == null) {
            throw new NullPointerException("dispatch was null");
        } else if (socketImpl == null) {
            throw new NullPointerException("delegate was null");
        } else {
            this.i = eVar;
            this.j = dVar;
            this.k = socketImpl;
            f();
        }
    }

    public static boolean c() {
        return f;
    }

    public static Throwable d() {
        return g;
    }

    private void f() {
        try {
            this.address = (InetAddress) a.get(this.k);
            this.fd = (FileDescriptor) b.get(this.k);
            this.localport = c.getInt(this.k);
            this.port = d.getInt(this.k);
        } catch (Throwable e) {
            throw new ck(e);
        } catch (Throwable e2) {
            throw new ck(e2);
        }
    }

    private Object a(int i, Object... objArr) {
        Throwable e;
        try {
            a.set(this.k, this.address);
            b.set(this.k, this.fd);
            c.setInt(this.k, this.localport);
            d.setInt(this.k, this.port);
            try {
                Object invoke = e[i].invoke(this.k, objArr);
                f();
                return invoke;
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
                    throw new ck(e222);
                }
            } catch (Throwable e2222) {
                throw new ck(e2222);
            } catch (Throwable e22222) {
                throw new ck(e22222);
            } catch (Throwable th2) {
                f();
            }
        } catch (Throwable e222222) {
            throw new ck(e222222);
        } catch (Throwable e2222222) {
            throw new ck(e2222222);
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

    private Object c(int i, Object... objArr) {
        try {
            return a(i, objArr);
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new ck(e3);
        }
    }

    public final InputStream getInputStream() {
        InputStream inputStream = (InputStream) c(10, new Object[0]);
        if (inputStream == null) {
            return inputStream;
        }
        try {
            if (this.m != null && this.m.a(inputStream)) {
                return this.m;
            }
            this.m = new x(this, inputStream, this.i);
            return this.m;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return inputStream;
        }
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = (OutputStream) c(12, new Object[0]);
        if (outputStream == null) {
            return outputStream;
        }
        try {
            if (this.l != null && this.l.a(outputStream)) {
                return this.l;
            }
            this.l = new w(this, outputStream);
            return this.l;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return outputStream;
        }
    }

    public final void create(boolean stream) {
        c(7, Boolean.valueOf(stream));
    }

    public final void connect(String host, int port) {
        try {
            c(6, host, Integer.valueOf(port));
        } catch (Throwable e) {
            if (host != null) {
                try {
                    c a = a(false);
                    a.b();
                    a.c();
                    a.f();
                    a.b(host);
                    a.a(port);
                    a.a(e);
                    this.i.a(a, a.SOCKET_IMPL_CONNECT);
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.a(th);
                }
            }
            throw e;
        }
    }

    public final void connect(InetAddress address, int port) {
        try {
            c(4, address, Integer.valueOf(port));
        } catch (Throwable e) {
            if (address != null) {
                try {
                    c a = a(false);
                    a.b();
                    a.c();
                    a.f();
                    a.a(address);
                    a.a(port);
                    a.a(e);
                    this.i.a(a, a.SOCKET_IMPL_CONNECT);
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.a(th);
                }
            }
            throw e;
        }
    }

    public final void connect(SocketAddress address, int timeout) {
        try {
            c(5, address, Integer.valueOf(timeout));
        } catch (Throwable e) {
            if (address != null) {
                try {
                    if (address instanceof InetSocketAddress) {
                        c a = a(false);
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
                        a.b();
                        a.c();
                        a.f();
                        a.a(inetSocketAddress.getAddress());
                        a.a(inetSocketAddress.getPort());
                        a.a(e);
                        this.i.a(a, a.SOCKET_IMPL_CONNECT);
                    }
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.a(th);
                }
            }
            throw e;
        }
    }

    public final void bind(InetAddress host, int port) {
        c(2, host, Integer.valueOf(port));
    }

    public final void listen(int backlog) {
        c(14, Integer.valueOf(backlog));
    }

    public final void accept(SocketImpl s) {
        c(0, s);
    }

    public final int available() {
        Integer num = (Integer) c(1, new Object[0]);
        if (num != null) {
            return num.intValue();
        }
        throw new ck("Received a null Integer");
    }

    public final void close() {
        c(3, new Object[0]);
        try {
            if (this.m != null) {
                this.m.d();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public final void shutdownInput() {
        c(17, new Object[0]);
    }

    public final void shutdownOutput() {
        c(18, new Object[0]);
    }

    public final FileDescriptor getFileDescriptor() {
        return (FileDescriptor) b(8, new Object[0]);
    }

    public final InetAddress getInetAddress() {
        return (InetAddress) b(9, new Object[0]);
    }

    public final int getPort() {
        return ((Integer) b(13, new Object[0])).intValue();
    }

    public final boolean supportsUrgentData() {
        return ((Boolean) b(19, new Object[0])).booleanValue();
    }

    public final void sendUrgentData(int data) {
        c(15, Integer.valueOf(data));
    }

    public final int getLocalPort() {
        return ((Integer) b(11, new Object[0])).intValue();
    }

    public final String toString() {
        return this.k.toString();
    }

    public final void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        b(16, Integer.valueOf(connectionTime), Integer.valueOf(latency), Integer.valueOf(bandwidth));
    }

    public final void setOption(int optID, Object value) {
        this.k.setOption(optID, value);
    }

    public final Object getOption(int optID) {
        return this.k.getOption(optID);
    }

    private c a(boolean z) {
        c cVar = new c();
        InetAddress inetAddress = getInetAddress();
        if (inetAddress != null) {
            cVar.a(inetAddress);
        }
        int port = getPort();
        if (port > 0) {
            cVar.a(port);
        }
        if (z) {
            cVar.a(k.a.HTTP);
        }
        if (this.j != null) {
            cVar.j = this.j.a();
        }
        if (bc.b()) {
            cVar.a(bc.a());
        }
        return cVar;
    }

    public final c a() {
        return a(true);
    }

    public final void a(c cVar) {
        synchronized (this.h) {
            this.h.add(cVar);
        }
    }

    public final c b() {
        c cVar;
        synchronized (this.h) {
            cVar = (c) this.h.poll();
        }
        return cVar;
    }

    public static void e() {
        if (f) {
            SocketImpl acVar = new ac(new e(new Executor() {
                public final void execute(Runnable runnable) {
                }
            }), null, new SocketImpl() {
                public final void setOption(int i, Object obj) {
                }

                public final Object getOption(int i) {
                    return null;
                }

                protected final void sendUrgentData(int i) {
                }

                protected final void listen(int i) {
                }

                protected final OutputStream getOutputStream() {
                    return null;
                }

                protected final InputStream getInputStream() {
                    return null;
                }

                protected final void create(boolean z) {
                }

                protected final void connect(SocketAddress socketAddress, int i) {
                }

                protected final void connect(InetAddress inetAddress, int i) {
                }

                protected final void connect(String str, int i) {
                }

                protected final void close() {
                }

                protected final void bind(InetAddress inetAddress, int i) {
                }

                protected final int available() {
                    return 0;
                }

                protected final void accept(SocketImpl socketImpl) {
                }

                protected final FileDescriptor getFileDescriptor() {
                    return null;
                }

                protected final InetAddress getInetAddress() {
                    return null;
                }

                protected final int getLocalPort() {
                    return 0;
                }

                protected final int getPort() {
                    return 0;
                }

                protected final void setPerformancePreferences(int i, int i2, int i3) {
                }

                protected final void shutdownInput() {
                }

                protected final void shutdownOutput() {
                }

                protected final boolean supportsUrgentData() {
                    return false;
                }

                public final String toString() {
                    return null;
                }
            });
            try {
                acVar.setOption(0, new Object());
                acVar.getOption(0);
                acVar.sendUrgentData(0);
                acVar.listen(0);
                acVar.getOutputStream();
                acVar.getInputStream();
                acVar.create(false);
                acVar.connect(null, 0);
                acVar.connect(null, 0);
                acVar.connect(null, 0);
                acVar.close();
                acVar.bind(null, 0);
                acVar.available();
                acVar.accept(acVar);
                acVar.getFileDescriptor();
                acVar.getInetAddress();
                acVar.getLocalPort();
                acVar.getPort();
                acVar.setPerformancePreferences(0, 0, 0);
                acVar.shutdownInput();
                acVar.shutdownOutput();
                acVar.supportsUrgentData();
            } catch (IOException e) {
            } catch (ck e2) {
                throw e2;
            } catch (Throwable th) {
                ck ckVar = new ck(th);
            }
        } else {
            throw new ck(g);
        }
    }
}
