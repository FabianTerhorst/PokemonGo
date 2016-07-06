package com.upsight.android.analytics.internal.action;

import java.util.HashMap;
import java.util.Map;

public interface ActionableStore<T extends Actionable> {

    public static class DefaultImpl<T extends Actionable> implements ActionableStore<T> {
        private Map<String, T> mMap = new HashMap();

        public synchronized boolean put(String id, T content) {
            this.mMap.put(id, content);
            return true;
        }

        public synchronized T get(String id) {
            return (Actionable) this.mMap.get(id);
        }

        public synchronized boolean remove(String id) {
            return this.mMap.remove(id) != null;
        }
    }

    T get(String str);

    boolean put(String str, T t);

    boolean remove(String str);
}
