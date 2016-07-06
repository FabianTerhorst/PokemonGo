package dagger.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;

public final class SetFactory<T> implements Factory<Set<T>> {
    static final /* synthetic */ boolean $assertionsDisabled = (!SetFactory.class.desiredAssertionStatus() ? true : $assertionsDisabled);
    private static final String ARGUMENTS_MUST_BE_NON_NULL = "SetFactory.create() requires its arguments to be non-null";
    private final List<Provider<Set<T>>> contributingProviders;

    public static <T> Factory<Set<T>> create(Factory<Set<T>> factory) {
        if ($assertionsDisabled || factory != null) {
            return factory;
        }
        throw new AssertionError(ARGUMENTS_MUST_BE_NON_NULL);
    }

    public static <T> Factory<Set<T>> create(Provider<Set<T>>... providers) {
        if ($assertionsDisabled || providers != null) {
            List<Provider<Set<T>>> contributingProviders = Arrays.asList(providers);
            if (!$assertionsDisabled && contributingProviders.contains(null)) {
                throw new AssertionError("Codegen error?  Null within provider list.");
            } else if ($assertionsDisabled || !hasDuplicates(contributingProviders)) {
                return new SetFactory(contributingProviders);
            } else {
                throw new AssertionError("Codegen error?  Duplicates in the provider list");
            }
        }
        throw new AssertionError(ARGUMENTS_MUST_BE_NON_NULL);
    }

    private static boolean hasDuplicates(List<? extends Object> original) {
        return original.size() != new HashSet(original).size() ? true : $assertionsDisabled;
    }

    private SetFactory(List<Provider<Set<T>>> contributingProviders) {
        this.contributingProviders = contributingProviders;
    }

    public Set<T> get() {
        int i;
        int size = 0;
        List<Set<T>> providedSets = new ArrayList(this.contributingProviders.size());
        int c = this.contributingProviders.size();
        for (i = 0; i < c; i++) {
            Provider<Set<T>> provider = (Provider) this.contributingProviders.get(i);
            Set<T> providedSet = (Set) provider.get();
            if (providedSet == null) {
                throw new NullPointerException(provider + " returned null");
            }
            providedSets.add(providedSet);
            size += providedSet.size();
        }
        Set<T> result = Collections.newLinkedHashSetWithExpectedSize(size);
        c = providedSets.size();
        for (i = 0; i < c; i++) {
            for (T element : (Set) providedSets.get(i)) {
                if (element == null) {
                    throw new NullPointerException("a null element was provided");
                }
                result.add(element);
            }
        }
        return Collections.unmodifiableSet(result);
    }
}
