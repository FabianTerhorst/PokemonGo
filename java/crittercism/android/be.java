package crittercism.android;

import com.crittercism.app.Transaction;

public final class be extends Transaction {
    public be() {
        dx.c("Creating no-op transaction");
    }

    public final void a() {
        dx.b("No-op transaction. Ignoring Transaction.start() call.", new IllegalStateException("No-op transaction"));
    }

    public final void b() {
        dx.b("No-op transaction. Ignoring Transaction.stop() call.", new IllegalStateException("No-op transaction"));
    }

    public final void c() {
        dx.b("No-op transaction. Ignoring Transaction.fail() call.", new IllegalStateException("No-op transaction"));
    }

    public final void a(int i) {
        dx.b("No-op transaction. Ignoring Transaction.setValue(double) call.", new IllegalStateException("No-op transaction"));
    }

    public final int d() {
        dx.b("No-op transaction. Ignoring Transaction.getValue() call.", new IllegalStateException("No-op transaction"));
        return -1;
    }
}
