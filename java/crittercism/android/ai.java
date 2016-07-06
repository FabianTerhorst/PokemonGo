package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class ai extends af {
    private int d = -1;

    public ai(af afVar) {
        super(afVar);
    }

    public final af b() {
        int i = this.d;
        if (this.d == 0) {
            return new aq(this);
        }
        this.b.clear();
        return new ah(this, this.d);
    }

    public final af c() {
        return as.d;
    }

    public final boolean a(CharArrayBuffer charArrayBuffer) {
        int indexOf = charArrayBuffer.indexOf(59);
        int length = charArrayBuffer.length();
        if (indexOf <= 0) {
            indexOf = length;
        }
        try {
            this.d = Integer.parseInt(charArrayBuffer.substringTrimmed(0, indexOf), 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected final int d() {
        return 16;
    }

    protected final int e() {
        return 256;
    }

    public final void f() {
        this.a.b(a());
        this.a.a(as.d);
    }
}
