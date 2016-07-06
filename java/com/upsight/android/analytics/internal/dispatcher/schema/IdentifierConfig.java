package com.upsight.android.analytics.internal.dispatcher.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IdentifierConfig {
    private Map<String, String> mIdentifierFilters;
    private Map<String, Set<String>> mIdentifiers;

    public static class Builder {
        private Map<String, String> identifierFilters = new HashMap();
        private Map<String, Set<String>> identifiers = new HashMap();

        public Builder addIdentifiers(String name, Set<String> ids) {
            if (this.identifiers.containsKey(name)) {
                throw new IllegalArgumentException("Identifiers name " + name + " already exists");
            }
            this.identifiers.put(name, ids);
            return this;
        }

        public Builder addIdentifierFilter(String filter, String identifiers) {
            if (this.identifierFilters.containsKey(filter)) {
                throw new IllegalArgumentException("Identifier filter " + filter + " already exists");
            }
            this.identifierFilters.put(filter, identifiers);
            return this;
        }

        public IdentifierConfig build() {
            return new IdentifierConfig();
        }
    }

    private IdentifierConfig(Builder builder) {
        this.mIdentifiers = builder.identifiers;
        this.mIdentifierFilters = builder.identifierFilters;
    }

    public Map<String, Set<String>> getIdentifiers() {
        return Collections.unmodifiableMap(this.mIdentifiers);
    }

    public Map<String, String> getIdentifierFilters() {
        return Collections.unmodifiableMap(this.mIdentifierFilters);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentifierConfig that = (IdentifierConfig) o;
        if (this.mIdentifierFilters == null ? that.mIdentifierFilters != null : !this.mIdentifierFilters.equals(that.mIdentifierFilters)) {
            return false;
        }
        if (this.mIdentifiers != null) {
            if (this.mIdentifiers.equals(that.mIdentifiers)) {
                return true;
            }
        } else if (that.mIdentifiers == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.mIdentifiers != null) {
            result = this.mIdentifiers.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.mIdentifierFilters != null) {
            i = this.mIdentifierFilters.hashCode();
        }
        return i2 + i;
    }
}
