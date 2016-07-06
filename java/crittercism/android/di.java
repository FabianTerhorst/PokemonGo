package crittercism.android;

public abstract class di implements Runnable {
    public abstract void a();

    public final void run() {
        try {
            a();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }
}
