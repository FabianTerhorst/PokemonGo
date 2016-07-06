package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class ag extends af {
    private int d;
    private int e = 0;

    public ag(af afVar, int i) {
        super(afVar);
        this.d = i;
    }

    public final boolean a(int i) {
        if (i == -1) {
            this.a.a(as.d);
            return true;
        }
        this.e++;
        this.c++;
        if (this.e != this.d) {
            return false;
        }
        this.a.b(a());
        this.a.a(this.a.b());
        return true;
    }

    public final int b(byte[] bArr, int i, int i2) {
        if (i2 == -1) {
            this.a.a(as.d);
            return -1;
        } else if (this.e + i2 < this.d) {
            this.e += i2;
            this.c += i2;
            return i2;
        } else {
            i2 = this.d - this.e;
            this.c += i2;
            this.a.b(a());
            this.a.a(this.a.b());
            return i2;
        }
    }

    public final af b() {
        return as.d;
    }

    public final af c() {
        return as.d;
    }

    public final boolean a(CharArrayBuffer charArrayBuffer) {
        return true;
    }

    protected final int d() {
        return 0;
    }

    protected final int e() {
        return 0;
    }

    public final void f() {
        this.a.b(a());
        this.a.a(as.d);
    }
}
