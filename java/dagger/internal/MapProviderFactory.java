package dagger.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Provider;

public final class MapProviderFactory<K, V> implements Factory<Map<K, Provider<V>>> {
    private static final MapProviderFactory<Object, Object> EMPTY = new MapProviderFactory(Collections.emptyMap());
    private final Map<K, Provider<V>> contributingMap;

    public static final class Builder<K, V> {
        private final LinkedHashMap<K, Provider<V>> mapBuilder;

        private Builder(int size) {
            this.mapBuilder = Collections.newLinkedHashMapWithExpectedSize(size);
        }

        public MapProviderFactory<K, V> build() {
            return new MapProviderFactory(this.mapBuilder);
        }

        public Builder<K, V> put(K key, Provider<V> providerOfValue) {
            if (key == null) {
                throw new NullPointerException("The key is null");
            } else if (providerOfValue == null) {
                throw new NullPointerException("The provider of the value is null");
            } else {
                this.mapBuilder.put(key, providerOfValue);
                return this;
            }
        }
    }

    public static <K, V> Builder<K, V> builder(int size) {
        return new Builder(size);
    }

    public static <K, V> MapProviderFactory<K, V> empty() {
        return EMPTY;
    }

    private MapProviderFactory(Map<K, Provider<V>> contributingMap) {
        this.contributingMap = Collections.unmodifiableMap(contributingMap);
    }

    public Map<K, Provider<V>> get() {
        return this.contributingMap;
    }
}
