package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import java.util.List;

public final class bb extends CrittercismConfig {
    private String b = "524c99a04002057fcd000001";
    private bn c;

    public bb(bn bnVar, CrittercismConfig crittercismConfig) {
        super(crittercismConfig);
        this.c = bnVar;
    }

    public final List a() {
        List a = super.a();
        a.add(this.c.b());
        return a;
    }

    public final String b() {
        return this.c.a();
    }

    public final String c() {
        return this.c.b();
    }

    public final String d() {
        return this.c.d();
    }

    public final String e() {
        return this.c.c();
    }

    public final String f() {
        return this.b;
    }

    public final String g() {
        return this.a;
    }

    public final boolean equals(Object o) {
        if (!(o instanceof bb)) {
            return false;
        }
        bb bbVar = (bb) o;
        return super.equals(o) && CrittercismConfig.a(this.c.a(), bbVar.c.a()) && CrittercismConfig.a(this.c.b(), bbVar.c.b()) && CrittercismConfig.a(this.c.d(), bbVar.c.d()) && CrittercismConfig.a(this.c.c(), bbVar.c.c()) && CrittercismConfig.a(this.b, bbVar.b);
    }

    public final int hashCode() {
        return (((((((((super.hashCode() * 31) + this.c.a().hashCode()) * 31) + this.c.b().hashCode()) * 31) + this.c.d().hashCode()) * 31) + this.c.c().hashCode()) * 31) + this.b.hashCode();
    }
}
