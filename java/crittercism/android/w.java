package crittercism.android;

import java.io.OutputStream;

public final class w extends OutputStream implements al {
    private ae a;
    private OutputStream b;
    private c c;
    private af d;

    public w(ae aeVar, OutputStream outputStream) {
        if (aeVar == null) {
            throw new NullPointerException("socket was null");
        } else if (outputStream == null) {
            throw new NullPointerException("output stream was null");
        } else {
            this.a = aeVar;
            this.b = outputStream;
            this.d = b();
            if (this.d == null) {
                throw new NullPointerException("parser was null");
            }
        }
    }

    public final void flush() {
        this.b.flush();
    }

    public final void close() {
        this.b.close();
    }

    public final void write(int oneByte) {
        this.b.write(oneByte);
        try {
            this.d.a(oneByte);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            this.d = as.d;
        }
    }

    public final void write(byte[] buffer) {
        this.b.write(buffer);
        if (buffer != null) {
            a(buffer, 0, buffer.length);
        }
    }

    public final void write(byte[] buffer, int offset, int byteCount) {
        this.b.write(buffer, offset, byteCount);
        if (buffer != null) {
            a(buffer, offset, byteCount);
        }
    }

    private void a(byte[] bArr, int i, int i2) {
        try {
            this.d.a(bArr, i, i2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            this.d = as.d;
        }
    }

    public final void a(String str, String str2) {
        c d = d();
        d.b();
        d.f = str;
        d.i = null;
        k kVar = d.h;
        if (str2 != null) {
            kVar.c = str2;
        }
        this.a.a(d);
    }

    public final void a(int i) {
    }

    public final void a(af afVar) {
        this.d = afVar;
    }

    public final af a() {
        return this.d;
    }

    public final void b(int i) {
        c cVar = this.c;
        this.c = null;
        if (cVar != null) {
            cVar.d((long) i);
        }
    }

    private c d() {
        if (this.c == null) {
            this.c = this.a.a();
        }
        c cVar = this.c;
        return this.c;
    }

    public final af b() {
        return new an(this);
    }

    public final String c() {
        c d = d();
        if (d != null) {
            return d.f;
        }
        return null;
    }

    public final void a(String str) {
        c d = d();
        if (d != null) {
            d.b(str);
        }
    }

    public final boolean a(OutputStream outputStream) {
        return this.b == outputStream;
    }
}
