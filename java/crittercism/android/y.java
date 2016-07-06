package crittercism.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;

public final class y extends Service {
    public static final String[] a = new String[]{"Default", "SSL", "TLSv1.1", "TLSv1.2", "SSLv3", "TLSv1", "TLS"};
    private e b;
    private d c;
    private Service d;

    private y(Service service, e eVar, d dVar) {
        super(service.getProvider(), service.getType(), service.getAlgorithm(), service.getClassName(), null, null);
        this.b = eVar;
        this.c = dVar;
        this.d = service;
    }

    private static y a(Service service, e eVar, d dVar) {
        y yVar = new y(service, eVar, dVar);
        try {
            Field[] fields = Service.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                fields[i].set(yVar, fields[i].get(service));
            }
            return yVar;
        } catch (Exception e) {
            return null;
        }
    }

    private static Provider a() {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            if (instance != null) {
                return instance.getProvider();
            }
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean a(e eVar, d dVar) {
        int i = 0;
        if (!z.a()) {
            return false;
        }
        Provider a = a();
        if (a == null) {
            return false;
        }
        boolean z = false;
        while (i < a.length) {
            Service service = a.getService("SSLContext", a[i]);
            if (!(service == null || (service instanceof y))) {
                y a2 = a(service, eVar, dVar);
                if (a2 != null) {
                    z |= a2.b();
                }
            }
            i++;
        }
        return z;
    }

    private boolean b() {
        Provider provider = getProvider();
        if (provider == null) {
            return false;
        }
        try {
            Method declaredMethod = Provider.class.getDeclaredMethod("putService", new Class[]{Service.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(provider, new Object[]{this});
            String str = "SSLContext.DummySSLAlgorithm";
            provider.put(str, getClassName());
            provider.remove(getType() + "." + getAlgorithm());
            provider.remove(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final Object newInstance(Object constructorParameter) {
        Object newInstance = super.newInstance(constructorParameter);
        try {
            if (!(newInstance instanceof SSLContextSpi)) {
                return newInstance;
            }
            z a = z.a((SSLContextSpi) newInstance, this.b, this.c);
            if (a != null) {
                return a;
            }
            return newInstance;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return newInstance;
        }
    }
}
