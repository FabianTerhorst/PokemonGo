package com.upsight.android.analytics.internal.association;

import android.text.TextUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upsight.android.analytics.internal.association.Association.UpsightDataFilter;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.annotation.Created;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import rx.functions.Action1;

class AssociationManagerImpl implements AssociationManager {
    static final long ASSOCIATION_EXPIRY = 604800000;
    private static final String KEY_UPSIGHT_DATA = "upsight_data";
    private final Map<String, Set<Association>> mAssociations;
    private final Clock mClock;
    private final UpsightDataStore mDataStore;
    private boolean mIsLaunched = false;

    AssociationManagerImpl(UpsightDataStore dataStore, Clock clock) {
        this.mDataStore = dataStore;
        this.mClock = clock;
        this.mAssociations = new HashMap();
    }

    public synchronized void launch() {
        if (!this.mIsLaunched) {
            this.mIsLaunched = true;
            launchInner();
        }
    }

    public synchronized void associate(String eventType, ObjectNode eventNode) {
        associateInner(eventType, eventNode);
    }

    @Created
    public void handleCreate(Association association) {
        addAssociation(association.getWith(), association);
    }

    synchronized void launchInner() {
        this.mDataStore.subscribe(this);
        this.mDataStore.fetchObservable(Association.class).subscribe(new Action1<Association>() {
            public void call(Association association) {
                AssociationManagerImpl.this.addAssociation(association.getWith(), association);
            }
        });
    }

    synchronized void associateInner(String eventType, ObjectNode eventNode) {
        Set<Association> associations = (Set) this.mAssociations.get(eventType);
        if (associations != null) {
            boolean isMatched = false;
            Iterator<Association> itr = associations.iterator();
            while (itr.hasNext()) {
                Association association = (Association) itr.next();
                if (this.mClock.currentTimeMillis() - association.getTimestampMs() > ASSOCIATION_EXPIRY) {
                    itr.remove();
                    this.mDataStore.removeObservable(Association.class, association.getId()).subscribe();
                } else if (isMatched) {
                    continue;
                } else {
                    UpsightDataFilter filter = association.getUpsightDataFilter();
                    JsonNode associationNode = eventNode.path(KEY_UPSIGHT_DATA);
                    if (associationNode.isObject()) {
                        ObjectNode eventUpsightData = (ObjectNode) associationNode;
                        JsonNode eventMatchValue = eventUpsightData.path(filter.matchKey);
                        if (eventMatchValue.isValueNode()) {
                            Iterator i$ = filter.matchValues.iterator();
                            while (i$.hasNext()) {
                                if (eventMatchValue.equals((JsonNode) i$.next())) {
                                    Iterator<Entry<String, JsonNode>> fields = association.getUpsightData().fields();
                                    while (fields.hasNext()) {
                                        Entry<String, JsonNode> field = (Entry) fields.next();
                                        eventUpsightData.put((String) field.getKey(), (JsonNode) field.getValue());
                                    }
                                    itr.remove();
                                    this.mDataStore.removeObservable(Association.class, association.getId()).subscribe();
                                    isMatched = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    synchronized void addAssociation(String type, Association association) {
        if (!(TextUtils.isEmpty(type) || association == null)) {
            Set<Association> associations = (Set) this.mAssociations.get(type);
            if (associations == null) {
                associations = new HashSet();
            }
            associations.add(association);
            this.mAssociations.put(type, associations);
        }
    }
}
