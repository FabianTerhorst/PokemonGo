package dagger.internal;

import javax.inject.Provider;

public final class DelegateFactory<T> implements Factory<T> {
    private Provider<T> delegate;

    public T get() {
        if (this.delegate != null) {
            return this.delegate.get();
        }
        throw new IllegalStateException();
    }

    public void setDelegatedProvider(Provider<T> delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException();
        } else if (this.delegate != null) {
            throw new IllegalStateException();
        } else {
            this.delegate = delegate;
        }
    }
}
