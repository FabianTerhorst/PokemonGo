package crittercism.android;

public final class cv {
    private long a = 0;
    private long b;

    public cv(long j) {
        this.b = j;
    }

    public final synchronized boolean a() {
        return System.nanoTime() - this.a > this.b;
    }

    public final synchronized void b() {
        this.a = System.nanoTime();
    }
}
