package com.upsight.android.analytics.internal.dispatcher.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import spacemadness.com.lunarconsole.BuildConfig;

public class ByFilterSelector<D> implements Selector<D> {
    private Map<String, D> mData;
    private D mDefaultValue;
    private Set<Filter> mFilters;

    static class Filter {
        private static final String SEPARATOR = ".";
        private final String mFilter;
        private final boolean mIsWildcard;
        private final String mMatcher;

        public Filter(String filterString) {
            this.mFilter = filterString;
            this.mIsWildcard = filterString.endsWith("*");
            this.mMatcher = filterString.replaceAll("\\*", BuildConfig.FLAVOR);
        }

        public Filter getFilterIfBetter(String eventType, Filter previousMatch) {
            if (!this.mMatcher.equals(eventType)) {
                if (!eventType.startsWith(this.mMatcher)) {
                    return previousMatch;
                }
                if (!this.mMatcher.endsWith(SEPARATOR) && !this.mIsWildcard && !this.mMatcher.isEmpty()) {
                    return previousMatch;
                }
                if (previousMatch != null && previousMatch.getMatcher().length() >= this.mMatcher.length()) {
                    return previousMatch;
                }
            }
            return this;
        }

        public String getMatcher() {
            return this.mMatcher;
        }

        public String getFilter() {
            return this.mFilter;
        }
    }

    public ByFilterSelector(Map<String, D> data, D defaultValue) {
        this(data);
        this.mDefaultValue = defaultValue;
    }

    public ByFilterSelector(Map<String, D> data) {
        this.mDefaultValue = null;
        this.mData = data;
        this.mFilters = new HashSet();
        for (String filterString : this.mData.keySet()) {
            this.mFilters.add(new Filter(filterString));
        }
    }

    public D select(String eventType) {
        String typeFilter = getFilterFor(eventType);
        return typeFilter == null ? this.mDefaultValue : this.mData.get(typeFilter);
    }

    private String getFilterFor(String eventType) {
        Filter selectedFilter = null;
        for (Filter entry : this.mFilters) {
            selectedFilter = entry.getFilterIfBetter(eventType, selectedFilter);
        }
        if (selectedFilter == null) {
            return null;
        }
        return selectedFilter.getFilter();
    }
}
