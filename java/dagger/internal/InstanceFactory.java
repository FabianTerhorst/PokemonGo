package dagger.internal;

public final class InstanceFactory<T> implements Factory<T> {
    private final T instance;

    public static <T> Factory<T> create(T instance) {
        if (instance != null) {
            return new InstanceFactory(instance);
        }
        throw new NullPointerException();
    }

    private InstanceFactory(T instance) {
        this.instance = instance;
    }

    public T get() {
        return this.instance;
    }
}
