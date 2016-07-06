package com.upsight.android.analytics.internal.dispatcher.util;

import java.util.Map;

public class ByNameSelector<D> implements Selector<D> {
    private Map<String, D> mData;
    private D mDefaultValue;

    public ByNameSelector(Map<String, D> data, D defaultValue) {
        this(data);
        this.mDefaultValue = defaultValue;
    }

    public ByNameSelector(Map<String, D> data) {
        this.mDefaultValue = null;
        this.mData = data;
    }

    public D select(String by) {
        D selected = this.mData.get(by);
        return selected != null ? selected : this.mDefaultValue;
    }
}
