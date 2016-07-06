package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class ah extends af {
    private ai d;
    private int e;
    private int f = 0;

    public ah(ai aiVar, int i) {
        super((af) aiVar);
        this.d = aiVar;
        this.e = i;
    }

    public final boolean a(int i) {
        if (this.f >= this.e + 2) {
            return false;
        }
        if (i == -1) {
            this.a.b(a());
            this.a.a(as.d);
            return true;
        }
        this.c++;
        char c = (char) i;
        this.f++;
        if (this.f <= this.e) {
            return false;
        }
        if (c == '\n') {
            this.d.b(a());
            this.a.a(this.d);
            return true;
        } else if (this.f != this.e + 2 || c == '\n') {
            return false;
        } else {
            this.a.a(as.d);
            return true;
        }
    }

    public final af b() {
        return this.d;
    }

    public final af c() {
        return null;
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
