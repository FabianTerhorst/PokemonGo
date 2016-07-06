package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class aq extends af {
    private boolean d = false;

    public aq(af afVar) {
        super(afVar);
    }

    public final af b() {
        if (this.d) {
            this.a.b(a());
            return this.a.b();
        }
        this.b.clear();
        return this;
    }

    public final af c() {
        this.b.clear();
        return new ar(this);
    }

    public final boolean a(CharArrayBuffer charArrayBuffer) {
        boolean z = false;
        if (charArrayBuffer.substringTrimmed(0, charArrayBuffer.length()).length() == 0) {
            z = true;
        }
        this.d = z;
        return true;
    }

    protected final int d() {
        return 8;
    }

    protected final int e() {
        return 128;
    }
}
