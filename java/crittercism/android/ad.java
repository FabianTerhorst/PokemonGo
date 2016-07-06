package crittercism.android;

import java.net.SocketImpl;
import java.net.SocketImplFactory;

public final class ad implements SocketImplFactory {
    private Class a;
    private SocketImplFactory b;
    private e c;
    private d d;

    public ad(Class cls, e eVar, d dVar) {
        this.c = eVar;
        this.d = dVar;
        this.a = cls;
        Class cls2 = this.a;
        if (cls2 == null) {
            throw new cl("Class was null");
        }
        try {
            cls2.newInstance();
        } catch (Throwable th) {
            cl clVar = new cl("Unable to create new instance", th);
        }
    }

    public ad(SocketImplFactory socketImplFactory, e eVar, d dVar) {
        this.c = eVar;
        this.d = dVar;
        this.b = socketImplFactory;
        SocketImplFactory socketImplFactory2 = this.b;
        if (socketImplFactory2 == null) {
            throw new cl("Factory was null");
        }
        try {
            if (socketImplFactory2.createSocketImpl() == null) {
                throw new cl("Factory does not work");
            }
        } catch (Throwable th) {
            cl clVar = new cl("Factory does not work", th);
        }
    }

    public final SocketImpl createSocketImpl() {
        SocketImpl socketImpl = null;
        if (this.b != null) {
            socketImpl = this.b.createSocketImpl();
        } else {
            Class cls = this.a;
            try {
                socketImpl = (SocketImpl) this.a.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e2) {
                e2.printStackTrace();
            }
        }
        if (socketImpl != null) {
            return new ac(this.c, this.d, socketImpl);
        }
        return socketImpl;
    }
}
