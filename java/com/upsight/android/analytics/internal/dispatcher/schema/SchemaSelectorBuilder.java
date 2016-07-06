package com.upsight.android.analytics.internal.dispatcher.schema;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema.Default;
import com.upsight.android.analytics.internal.dispatcher.util.ByFilterSelector;
import com.upsight.android.analytics.internal.dispatcher.util.ByNameSelector;
import com.upsight.android.analytics.internal.dispatcher.util.Selector;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SchemaSelectorBuilder {
    private final Map<String, UpsightDataProvider> mDataProviders = new ConcurrentHashMap();
    private final Schema mDefaultSchema;

    SchemaSelectorBuilder(UpsightContext upsight) {
        registerDefaultDataProviders(upsight);
        this.mDefaultSchema = new Default(this.mDataProviders);
    }

    private void registerDefaultDataProviders(UpsightContext upsight) {
        registerDataProvider(new AppBlockProvider(upsight));
        registerDataProvider(new DeviceBlockProvider(upsight));
        registerDataProvider(new AndroidIDBlockProvider(upsight));
        registerDataProvider(new ScreenBlockProvider(upsight));
        registerDataProvider(new SdkBlockProvider(upsight));
        registerDataProvider(new SidProvider(upsight));
        registerDataProvider(new LocationBlockProvider(upsight));
    }

    public void registerDataProvider(UpsightDataProvider provider) {
        for (String key : provider.availableKeys()) {
            if (((UpsightDataProvider) this.mDataProviders.put(key, provider)) != null) {
                throw new IllegalStateException(String.format("Both %s and %s provide values for key.", new Object[]{provider.getClass().getName(), ((UpsightDataProvider) this.mDataProviders.put(key, provider)).getClass().getName()}));
            }
        }
    }

    public Selector<Schema> buildSelectorByName(IdentifierConfig config) {
        Map<String, Set<String>> identifiers = config.getIdentifiers();
        Map<String, Schema> res = new HashMap(identifiers.size());
        for (String name : identifiers.keySet()) {
            Set<String> attributes = (Set) config.getIdentifiers().get(name);
            if (attributes != null) {
                res.put(name, Schema.from(name, this.mDefaultSchema, attributes));
            }
        }
        return new ByNameSelector(res);
    }

    public Selector<Schema> buildSelectorByType(IdentifierConfig config) {
        Map<String, Schema> res = new HashMap(config.getIdentifiers().size());
        for (Entry<String, String> filter : config.getIdentifierFilters().entrySet()) {
            String key = (String) filter.getKey();
            String value = (String) filter.getValue();
            Set<String> attributes = (Set) config.getIdentifiers().get(value);
            if (attributes != null) {
                res.put(key, Schema.from(value, this.mDefaultSchema, attributes));
            }
        }
        return new ByFilterSelector(res, this.mDefaultSchema);
    }
}
