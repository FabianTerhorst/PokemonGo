package crittercism.android;

import java.util.Locale;

public final class cg {
    public static final cg a = new cg();
    private volatile int b = 1;
    private final long c = System.currentTimeMillis();

    private cg() {
    }

    private synchronized int b() {
        int i;
        i = this.b;
        this.b = i + 1;
        return i;
    }

    public final String a() {
        return String.format(Locale.US, "%d.%d.%09d", new Object[]{Integer.valueOf(1), Long.valueOf(this.c), Integer.valueOf(b())});
    }
}
