package dagger.internal;

import dagger.MembersInjector;

public final class MembersInjectors {

    private enum NoOpMembersInjector implements MembersInjector<Object> {
        INSTANCE;

        public void injectMembers(Object instance) {
            Preconditions.checkNotNull(instance);
        }
    }

    public static <T> T injectMembers(MembersInjector<T> membersInjector, T instance) {
        membersInjector.injectMembers(instance);
        return instance;
    }

    public static <T> MembersInjector<T> noOp() {
        return NoOpMembersInjector.INSTANCE;
    }

    public static <T> MembersInjector<T> delegatingTo(MembersInjector<? super T> delegate) {
        return (MembersInjector) Preconditions.checkNotNull(delegate);
    }

    private MembersInjectors() {
    }
}
