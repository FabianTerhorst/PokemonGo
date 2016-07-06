package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public abstract class af {
    al a;
    CharArrayBuffer b;
    protected int c;
    private int d;

    public abstract boolean a(CharArrayBuffer charArrayBuffer);

    public abstract af b();

    public abstract af c();

    protected abstract int d();

    protected abstract int e();

    public af(al alVar) {
        a(alVar, 0);
    }

    public af(af afVar) {
        a(afVar.a, afVar.c);
    }

    private void a(al alVar, int i) {
        this.a = alVar;
        this.d = e();
        this.b = new CharArrayBuffer(d());
        this.c = i;
    }

    public boolean a(int i) {
        if (i == -1) {
            g();
            return true;
        }
        af b;
        this.c++;
        char c = (char) i;
        if (c == '\n') {
            if (a(this.b)) {
                b = b();
            } else {
                b = as.d;
            }
        } else if (this.b.length() < this.d) {
            this.b.append(c);
            b = this;
        } else {
            b = c();
        }
        if (b != this) {
            this.a.a(b);
        }
        if (b == this) {
            return false;
        }
        return true;
    }

    public final void a(byte[] bArr, int i, int i2) {
        int b = b(bArr, i, i2);
        while (b > 0 && b < i2) {
            int b2 = this.a.a().b(bArr, i + b, i2 - b);
            if (b2 > 0) {
                b += b2;
            } else {
                return;
            }
        }
    }

    protected int b(byte[] bArr, int i, int i2) {
        boolean z = false;
        int i3 = -1;
        if (i2 == -1) {
            g();
        } else if (!(bArr == null || i2 == 0)) {
            i3 = 0;
            while (!z && i3 < i2) {
                z = a((char) bArr[i + i3]);
                i3++;
            }
        }
        return i3;
    }

    public final int a() {
        return this.c;
    }

    public final void b(int i) {
        this.c = i;
    }

    public final String toString() {
        return this.b.toString();
    }

    private void g() {
        this.a.a(as.d);
    }

    public void f() {
        if (this.a != null) {
            this.a.a(as.d);
        }
    }
}
