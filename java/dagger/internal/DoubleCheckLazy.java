package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class DoubleCheckLazy<T> implements Lazy<T> {
    static final /* synthetic */ boolean $assertionsDisabled = (!DoubleCheckLazy.class.desiredAssertionStatus());
    private static final Object UNINITIALIZED = new Object();
    private volatile Object instance = UNINITIALIZED;
    private final Provider<T> provider;

    private DoubleCheckLazy(Provider<T> provider) {
        if ($assertionsDisabled || provider != null) {
            this.provider = provider;
            return;
        }
        throw new AssertionError();
    }

    public T get() {
        Object result = this.instance;
        if (result == UNINITIALIZED) {
            synchronized (this) {
                result = this.instance;
                if (result == UNINITIALIZED) {
                    result = this.provider.get();
                    this.instance = result;
                }
            }
        }
        return result;
    }

    public static <T> Lazy<T> create(Provider<T> provider) {
        if (provider != null) {
            return new DoubleCheckLazy(provider);
        }
        throw new NullPointerException();
    }
}
