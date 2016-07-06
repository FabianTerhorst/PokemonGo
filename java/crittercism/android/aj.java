package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class aj extends af {
    public aj(af afVar) {
        super(afVar);
    }

    public final boolean a(int i) {
        if (i == -1) {
            this.a.b(a());
            this.a.a(as.d);
            return true;
        }
        this.c++;
        return false;
    }

    public final int b(byte[] bArr, int i, int i2) {
        if (i2 == -1) {
            this.a.b(a());
            this.a.a(as.d);
            return -1;
        }
        this.c += i2;
        return i2;
    }

    public final void f() {
        this.a.b(a());
        this.a.a(as.d);
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
        return Integer.MAX_VALUE;
    }
}
