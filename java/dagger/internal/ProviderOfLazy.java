package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class ProviderOfLazy<T> implements Provider<Lazy<T>> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ProviderOfLazy.class.desiredAssertionStatus());
    private final Provider<T> provider;

    private ProviderOfLazy(Provider<T> provider) {
        if ($assertionsDisabled || provider != null) {
            this.provider = provider;
            return;
        }
        throw new AssertionError();
    }

    public Lazy<T> get() {
        return DoubleCheck.lazy(this.provider);
    }

    public static <T> Provider<Lazy<T>> create(Provider<T> provider) {
        return new ProviderOfLazy((Provider) Preconditions.checkNotNull(provider));
    }
}
