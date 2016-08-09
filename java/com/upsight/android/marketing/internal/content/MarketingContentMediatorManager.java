package com.upsight.android.marketing.internal.content;

import com.upsight.android.marketing.UpsightContentMediator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MarketingContentMediatorManager {
    private DefaultContentMediator mDefaultContentMediator;
    private Map<String, UpsightContentMediator> mMediators = new HashMap();

    MarketingContentMediatorManager(DefaultContentMediator defaultContentMediator) {
        this.mDefaultContentMediator = defaultContentMediator;
    }

    public void register(UpsightContentMediator contentMediator) {
        this.mMediators.put(contentMediator.getContentProvider(), contentMediator);
    }

    Set<String> getContentProviders() {
        return new HashSet(this.mMediators.keySet());
    }

    UpsightContentMediator getContentMediator(String contentProvider) {
        return (UpsightContentMediator) this.mMediators.get(contentProvider);
    }

    UpsightContentMediator getDefaultContentMediator() {
        return this.mDefaultContentMediator;
    }
}
