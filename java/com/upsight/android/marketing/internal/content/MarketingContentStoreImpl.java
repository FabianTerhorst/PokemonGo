package com.upsight.android.marketing.internal.content;

import android.text.TextUtils;
import com.squareup.otto.Bus;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContent.ScopedAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContent.ScopelessAvailabilityEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class MarketingContentStoreImpl extends UpsightMarketingContentStore implements MarketingContentStore {
    public static final long DEFAULT_TIME_TO_LIVE_MS = 600000;
    private Bus mBus;
    private Clock mClock;
    private final Map<String, MarketingContent> mContentMap = new HashMap();
    private final Map<String, String> mParentEligibilityMap = new HashMap();
    private final Map<String, Set<String>> mScopeEligibilityMap = new HashMap();
    private final Map<String, Long> mTimestamps = new HashMap();

    public MarketingContentStoreImpl(Bus bus, Clock clock) {
        this.mBus = bus;
        this.mClock = clock;
    }

    public synchronized boolean put(String id, MarketingContent content) {
        boolean isAdded;
        isAdded = false;
        if (!(TextUtils.isEmpty(id) || content == null)) {
            this.mContentMap.put(id, content);
            this.mTimestamps.put(id, Long.valueOf(this.mClock.currentTimeMillis()));
            isAdded = true;
        }
        return isAdded;
    }

    public synchronized MarketingContent get(String id) {
        MarketingContent marketingContent;
        Long timestamp = (Long) this.mTimestamps.get(id);
        if (timestamp == null || this.mClock.currentTimeMillis() > timestamp.longValue() + DEFAULT_TIME_TO_LIVE_MS) {
            remove(id);
            marketingContent = null;
        } else {
            marketingContent = (MarketingContent) this.mContentMap.get(id);
        }
        return marketingContent;
    }

    public synchronized boolean remove(String id) {
        boolean isRemoved;
        isRemoved = false;
        if (!TextUtils.isEmpty(id)) {
            isRemoved = this.mContentMap.remove(id) != null;
            if (isRemoved) {
                Iterator<String> eligibleIdsItr = this.mScopeEligibilityMap.keySet().iterator();
                while (eligibleIdsItr.hasNext()) {
                    Set<String> ids = (Set) this.mScopeEligibilityMap.get((String) eligibleIdsItr.next());
                    if (ids != null && ids.contains(id)) {
                        eligibleIdsItr.remove();
                    }
                }
                Iterator<String> childIdMapItr = this.mParentEligibilityMap.keySet().iterator();
                while (childIdMapItr.hasNext()) {
                    String parentId = (String) childIdMapItr.next();
                    String childId = (String) this.mParentEligibilityMap.get(parentId);
                    if (id.equals(parentId) || id.equals(childId)) {
                        childIdMapItr.remove();
                    }
                }
                this.mTimestamps.remove(id);
            }
        }
        return isRemoved;
    }

    public synchronized Set<String> getIdsForScope(String scope) {
        Set<String> ids;
        ids = (Set) this.mScopeEligibilityMap.get(scope);
        if (ids == null) {
            ids = new HashSet();
        }
        return ids;
    }

    public synchronized boolean presentScopedContent(String id, String[] scopes) {
        boolean z;
        MarketingContent content = (MarketingContent) this.mContentMap.get(id);
        if (content == null || scopes == null || scopes.length <= 0) {
            z = false;
        } else {
            for (String scope : scopes) {
                Set<String> ids = (Set) this.mScopeEligibilityMap.get(scope);
                if (ids != null) {
                    ids.add(id);
                } else {
                    ids = new HashSet();
                    ids.add(id);
                    this.mScopeEligibilityMap.put(scope, ids);
                }
            }
            content.markPresentable(new ScopedAvailabilityEvent(id, scopes), this.mBus);
            z = true;
        }
        return z;
    }

    public synchronized boolean presentScopelessContent(String id, String parentId) {
        boolean z;
        MarketingContent content = (MarketingContent) this.mContentMap.get(id);
        if (content == null || TextUtils.isEmpty(parentId)) {
            z = false;
        } else {
            this.mParentEligibilityMap.put(parentId, id);
            content.markPresentable(new ScopelessAvailabilityEvent(id, parentId), this.mBus);
            z = true;
        }
        return z;
    }

    public synchronized boolean isContentReady(String scope) {
        return !getIdsForScope(scope).isEmpty();
    }
}
