package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class SingleCheck<T> implements Provider<T>, Lazy<T> {
    static final /* synthetic */ boolean $assertionsDisabled = (!SingleCheck.class.desiredAssertionStatus());
    private static final Object UNINITIALIZED = new Object();
    private volatile Factory<T> factory;
    private volatile Object instance = UNINITIALIZED;

    private SingleCheck(Factory<T> factory) {
        if ($assertionsDisabled || factory != null) {
            this.factory = factory;
            return;
        }
        throw new AssertionError();
    }

    public T get() {
        Factory<T> factoryReference = this.factory;
        if (this.instance == UNINITIALIZED) {
            this.instance = factoryReference.get();
            this.factory = null;
        }
        return this.instance;
    }

    public static <T> Provider<T> provider(Factory<T> factory) {
        return new SingleCheck((Factory) Preconditions.checkNotNull(factory));
    }
}
