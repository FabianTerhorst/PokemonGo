package crittercism.android;

public final class ao extends ak {
    private int g;

    public ao(af afVar, int i) {
        super(afVar);
        this.g = i;
    }

    protected final af g() {
        Object obj = (this.a.c().equals("HEAD") || ((this.g >= 100 && this.g <= 199) || this.g == 204 || this.g == 304)) ? 1 : null;
        if (obj != null) {
            this.a.b(a());
            return this.a.b();
        } else if (this.f) {
            return new ai(this);
        } else {
            if (this.d) {
                if (this.e > 0) {
                    return new ag(this, this.e);
                }
                this.a.b(a());
                return this.a.b();
            } else if (!this.a.c().equals("CONNECT")) {
                return new aj(this);
            } else {
                this.a.b(a());
                return this.a.b();
            }
        }
    }
}
