package dagger.internal;

import dagger.MembersInjector;

public final class MembersInjectors {

    private enum NoOpMembersInjector implements MembersInjector<Object> {
        INSTANCE;

        public void injectMembers(Object instance) {
            if (instance == null) {
                throw new NullPointerException();
            }
        }
    }

    public static <T> MembersInjector<T> noOp() {
        return NoOpMembersInjector.INSTANCE;
    }

    public static <T> MembersInjector<T> delegatingTo(MembersInjector<? super T> delegate) {
        return delegate;
    }

    private MembersInjectors() {
    }
}
