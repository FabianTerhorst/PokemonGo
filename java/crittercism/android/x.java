package crittercism.android;

import crittercism.android.c.a;
import java.io.InputStream;

public final class x extends InputStream implements al {
    private ae a;
    private c b;
    private InputStream c;
    private e d;
    private af e;

    public x(ae aeVar, InputStream inputStream, e eVar) {
        if (aeVar == null) {
            throw new NullPointerException("socket was null");
        } else if (inputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (eVar == null) {
            throw new NullPointerException("dispatch was null");
        } else {
            this.a = aeVar;
            this.c = inputStream;
            this.d = eVar;
            this.e = b();
            if (this.e == null) {
                throw new NullPointerException("parser was null");
            }
        }
    }

    public final int available() {
        return this.c.available();
    }

    public final void close() {
        try {
            this.e.f();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
        this.c.close();
    }

    public final void mark(int readlimit) {
        this.c.mark(readlimit);
    }

    public final boolean markSupported() {
        return this.c.markSupported();
    }

    private void a(Exception exception) {
        try {
            c e = e();
            e.a((Throwable) exception);
            this.d.a(e, a.PARSING_INPUT_STREAM_LOG_ERROR);
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (IllegalStateException e3) {
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public final int read() {
        try {
            int read = this.c.read();
            try {
                this.e.a(read);
            } catch (ThreadDeath e) {
                throw e;
            } catch (IllegalStateException e2) {
                this.e = as.d;
            } catch (Throwable th) {
                this.e = as.d;
                dx.a(th);
            }
            return read;
        } catch (Exception e3) {
            a(e3);
            throw e3;
        }
    }

    public final int read(byte[] buffer) {
        try {
            int read = this.c.read(buffer);
            a(buffer, 0, read);
            return read;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public final int read(byte[] buffer, int offset, int length) {
        try {
            int read = this.c.read(buffer, offset, length);
            a(buffer, offset, read);
            return read;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    private void a(byte[] bArr, int i, int i2) {
        try {
            this.e.a(bArr, i, i2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (IllegalStateException e2) {
            this.e = as.d;
        } catch (Throwable th) {
            this.e = as.d;
            dx.a(th);
        }
    }

    public final synchronized void reset() {
        this.c.reset();
    }

    public final long skip(long byteCount) {
        return this.c.skip(byteCount);
    }

    public final void a(String str, String str2) {
    }

    public final void a(int i) {
        c e = e();
        e.c();
        e.e = i;
    }

    public final void a(af afVar) {
        this.e = afVar;
    }

    public final af a() {
        return this.e;
    }

    public final void b(int i) {
        c cVar = null;
        c cVar2 = this.b;
        if (this.b != null) {
            int i2 = this.b.e;
            if (i2 >= 100 && i2 < 200) {
                cVar = new c(this.b.a());
                cVar.e(this.b.a);
                cVar.d(this.b.d);
                cVar.f = this.b.f;
            }
            this.b.b((long) i);
            this.d.a(this.b, a.INPUT_STREAM_FINISHED);
        }
        this.b = cVar;
    }

    private c e() {
        if (this.b == null) {
            this.b = this.a.b();
        }
        if (this.b != null) {
            return this.b;
        }
        throw new IllegalStateException("No statistics were queued up.");
    }

    public final af b() {
        return new ap(this);
    }

    public final String c() {
        return e().f;
    }

    public final void a(String str) {
    }

    public final boolean a(InputStream inputStream) {
        return this.c == inputStream;
    }

    public final void d() {
        if (this.b != null) {
            cn cnVar = this.b.g;
            Object obj = (cnVar.a == co.Android.ordinal() && cnVar.b == cm.OK.a()) ? 1 : null;
            if (obj != null && this.e != null) {
                this.e.f();
            }
        }
    }
}
