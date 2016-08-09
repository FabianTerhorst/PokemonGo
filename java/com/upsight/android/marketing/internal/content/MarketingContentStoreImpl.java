package com.upsight.android.marketing.internal.content;

import android.text.TextUtils;
import com.squareup.otto.Bus;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
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
    private static final String LOG_TAG = MarketingContentStore.class.getSimpleName();
    private Bus mBus;
    private Clock mClock;
    private final Map<String, MarketingContent> mContentMap = new HashMap();
    private UpsightLogger mLogger;
    private final Map<String, String> mParentEligibilityMap = new HashMap();
    private final Map<String, Set<String>> mScopeEligibilityMap = new HashMap();
    private final Map<String, Long> mTimestamps = new HashMap();

    public MarketingContentStoreImpl(Bus bus, Clock clock, UpsightLogger logger) {
        this.mBus = bus;
        this.mClock = clock;
        this.mLogger = logger;
    }

    public synchronized boolean put(String id, MarketingContent content) {
        boolean isAdded;
        isAdded = false;
        if (!(TextUtils.isEmpty(id) || content == null)) {
            this.mContentMap.put(id, content);
            this.mTimestamps.put(id, Long.valueOf(this.mClock.currentTimeMillis()));
            isAdded = true;
        }
        this.mLogger.d(LOG_TAG, "put id=" + id + " isAdded=" + isAdded, new Object[0]);
        return isAdded;
    }

    public synchronized MarketingContent get(String id) {
        MarketingContent marketingContent;
        Long timestamp = (Long) this.mTimestamps.get(id);
        if (timestamp == null || this.mClock.currentTimeMillis() > timestamp.longValue() + DEFAULT_TIME_TO_LIVE_MS) {
            remove(id);
            marketingContent = null;
        } else {
            this.mLogger.d(LOG_TAG, "get id=" + id, new Object[0]);
            marketingContent = (MarketingContent) this.mContentMap.get(id);
        }
        return marketingContent;
    }

    public synchronized boolean remove(String id) {
        boolean isRemoved;
        isRemoved = false;
        if (!TextUtils.isEmpty(id)) {
            if (this.mContentMap.remove(id) != null) {
                isRemoved = true;
            } else {
                isRemoved = false;
            }
            if (isRemoved) {
                for (String scope : this.mScopeEligibilityMap.keySet()) {
                    Set<String> ids = (Set) this.mScopeEligibilityMap.get(scope);
                    if (ids != null && ids.contains(id)) {
                        ids.remove(id);
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
        this.mLogger.d(LOG_TAG, "remove id=" + id + " isRemoved=" + isRemoved, new Object[0]);
        return isRemoved;
    }

    public synchronized Set<String> getIdsForScope(String scope) {
        Set<String> ids;
        ids = (Set) this.mScopeEligibilityMap.get(scope);
        if (ids == null) {
            ids = new HashSet();
        } else {
            ids = new HashSet(ids);
        }
        StringBuilder sb = new StringBuilder();
        for (String id : ids) {
            sb.append(id).append(" ");
        }
        this.mLogger.d(LOG_TAG, "getIdsForScope scope=" + scope + " ids=[ " + sb + " ]", new Object[0]);
        return ids;
    }

    public synchronized boolean presentScopedContent(String id, String[] scopes) {
        boolean z = false;
        synchronized (this) {
            MarketingContent content = (MarketingContent) this.mContentMap.get(id);
            if (!(content == null || scopes == null || scopes.length <= 0)) {
                int length = scopes.length;
                int i;
                while (i < length) {
                    String scope = scopes[i];
                    Set<String> ids = (Set) this.mScopeEligibilityMap.get(scope);
                    if (ids != null) {
                        ids.add(id);
                    } else {
                        ids = new HashSet();
                        ids.add(id);
                        this.mScopeEligibilityMap.put(scope, ids);
                    }
                    i++;
                }
                content.markPresentable(new ScopedAvailabilityEvent(id, scopes), this.mBus);
                this.mLogger.d(LOG_TAG, "presentScopedContent id=" + id, new Object[0]);
                z = true;
            }
        }
        return z;
    }

    public synchronized boolean presentScopelessContent(String id, String parentId) {
        boolean z = false;
        synchronized (this) {
            MarketingContent content = (MarketingContent) this.mContentMap.get(id);
            if (!(content == null || TextUtils.isEmpty(parentId))) {
                this.mParentEligibilityMap.put(parentId, id);
                content.markPresentable(new ScopelessAvailabilityEvent(id, parentId), this.mBus);
                this.mLogger.d(LOG_TAG, "presentScopelessContent id=" + id + " parentId=" + parentId, new Object[0]);
                z = true;
            }
        }
        return z;
    }

    public synchronized boolean isContentReady(String scope) {
        return !getIdsForScope(scope).isEmpty();
    }
}
