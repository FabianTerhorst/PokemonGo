package crittercism.android;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public final class v implements URLStreamHandlerFactory {
    private static final Object a = new Object();
    private static v b;
    private LinkedList c = new LinkedList();
    private boolean d = false;
    private boolean e = false;

    public enum a {
        HTTP_ONLY,
        HTTPS_ONLY,
        ALL
    }

    public static v a() {
        return b;
    }

    public v(a aVar, e eVar, d dVar) {
        if (aVar == a.ALL || aVar == a.HTTP_ONLY) {
            this.c.add(new o(eVar, dVar));
        }
        if (aVar == a.ALL || aVar == a.HTTPS_ONLY) {
            this.c.add(new q(eVar, dVar));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean b() {
        /*
        r3 = this;
        r0 = 1;
        r1 = a;
        monitor-enter(r1);
        r2 = b;	 Catch:{ all -> 0x0024 }
        if (r2 == 0) goto L_0x0010;
    L_0x0008:
        r2 = b;	 Catch:{ all -> 0x0024 }
        if (r2 != r3) goto L_0x000e;
    L_0x000c:
        monitor-exit(r1);	 Catch:{ all -> 0x0024 }
    L_0x000d:
        return r0;
    L_0x000e:
        r0 = 0;
        goto L_0x000c;
    L_0x0010:
        r0 = r3.d;	 Catch:{ all -> 0x0024 }
        if (r0 != 0) goto L_0x0020;
    L_0x0014:
        r0 = r3.e;	 Catch:{ all -> 0x0024 }
        if (r0 != 0) goto L_0x0020;
    L_0x0018:
        java.net.URL.setURLStreamHandlerFactory(r3);	 Catch:{ Throwable -> 0x0027 }
        r0 = 1;
        r3.d = r0;	 Catch:{ Throwable -> 0x0027 }
        b = r3;	 Catch:{ Throwable -> 0x0027 }
    L_0x0020:
        monitor-exit(r1);	 Catch:{ all -> 0x0024 }
        r0 = r3.d;
        goto L_0x000d;
    L_0x0024:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
    L_0x0027:
        r0 = move-exception;
        goto L_0x0020;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.v.b():boolean");
    }

    private synchronized boolean d() {
        boolean z = false;
        synchronized (this) {
            synchronized (a) {
                if (b != this) {
                    boolean z2 = this.d;
                } else {
                    if (this.d && e()) {
                        this.d = false;
                        b = null;
                    }
                    z = this.d;
                }
            }
        }
        return z;
    }

    public final URLStreamHandler createURLStreamHandler(String protocol) {
        try {
            if (!this.e) {
                Iterator it = this.c.iterator();
                while (it.hasNext()) {
                    m mVar = (m) it.next();
                    if (mVar.a().equals(protocol)) {
                        return mVar;
                    }
                }
            }
            return null;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            this.e = true;
            dx.a(th);
            return null;
        }
    }

    private static boolean e() {
        Field[] declaredFields = URL.class.getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            Field field = declaredFields[i];
            if (URLStreamHandlerFactory.class.isAssignableFrom(field.getType())) {
                try {
                    ea eaVar = ea.STREAM_HANDLER_FACTORY_ANNUL_REFLECTION_FAULT;
                    field.setAccessible(true);
                    field.set(null, null);
                    field.setAccessible(false);
                    URL.setURLStreamHandlerFactory(null);
                    return true;
                } catch (IllegalAccessException e) {
                    dx.c();
                } catch (SecurityException e2) {
                    dx.c();
                } catch (Throwable th) {
                    dx.c();
                }
            } else {
                i++;
            }
        }
        return false;
    }

    private static boolean f() {
        for (Field field : URL.class.getDeclaredFields()) {
            if (Hashtable.class.isAssignableFrom(field.getType())) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Class cls = (Class) parameterizedType.getActualTypeArguments()[0];
                Class cls2 = (Class) parameterizedType.getActualTypeArguments()[1];
                if (String.class.isAssignableFrom(cls) && URLStreamHandler.class.isAssignableFrom(cls2)) {
                    try {
                        ea eaVar = ea.STREAM_HANDLER_FACTORY_CLEAR_STREAM_HANDLERS_FAULT;
                        field.setAccessible(true);
                        Hashtable hashtable = (Hashtable) field.get(null);
                        if (hashtable != null) {
                            hashtable.clear();
                        }
                        field.setAccessible(false);
                        return true;
                    } catch (IllegalArgumentException e) {
                        dx.c();
                    } catch (SecurityException e2) {
                        dx.c();
                    } catch (IllegalAccessException e3) {
                        dx.c();
                    }
                }
            }
        }
        return false;
    }

    public final synchronized boolean c() {
        boolean z = false;
        synchronized (this) {
            d();
            boolean f;
            if (this.d) {
                this.e = true;
                f = f();
            } else {
                f = false;
            }
            if (!this.d || r2) {
                z = true;
            }
        }
        return z;
    }
}
