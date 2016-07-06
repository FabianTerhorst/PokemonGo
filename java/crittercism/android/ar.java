package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class ar extends af {
    private af d;

    public ar(af afVar) {
        super(afVar);
        this.d = afVar;
    }

    public final boolean a(int i) {
        if (i == -1) {
            this.a.a(as.d);
            return true;
        }
        this.c++;
        if (((char) i) != '\n') {
            return false;
        }
        this.d.b(a());
        this.a.a(this.d);
        return true;
    }

    public final af b() {
        return this;
    }

    public final af c() {
        return this;
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
}
