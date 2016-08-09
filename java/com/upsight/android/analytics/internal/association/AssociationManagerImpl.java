package com.upsight.android.analytics.internal.association;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    public synchronized void associate(String eventType, JsonObject eventNode) {
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

    synchronized void associateInner(String eventType, JsonObject eventNode) {
        Set<Association> associations = (Set) this.mAssociations.get(eventType);
        if (associations != null) {
            boolean isMatched = false;
            Iterator<Association> itr = associations.iterator();
            while (itr.hasNext()) {
                Association association = (Association) itr.next();
                if (this.mClock.currentTimeMillis() - association.getTimestampMs() > ASSOCIATION_EXPIRY) {
                    itr.remove();
                    if (!TextUtils.isEmpty(association.getId())) {
                        this.mDataStore.removeObservable(Association.class, association.getId()).subscribe();
                    }
                } else if (isMatched) {
                    continue;
                } else {
                    UpsightDataFilter filter = association.getUpsightDataFilter();
                    JsonElement associationNode = eventNode.get(KEY_UPSIGHT_DATA);
                    if (associationNode != null && associationNode.isJsonObject()) {
                        JsonObject eventUpsightData = associationNode.getAsJsonObject();
                        JsonElement eventMatchValue = eventUpsightData.get(filter.matchKey);
                        if (eventMatchValue != null && eventMatchValue.isJsonPrimitive()) {
                            Iterator it = filter.matchValues.iterator();
                            while (it.hasNext()) {
                                if (eventMatchValue.equals((JsonElement) it.next())) {
                                    for (Entry<String, JsonElement> field : association.getUpsightData().entrySet()) {
                                        eventUpsightData.add((String) field.getKey(), (JsonElement) field.getValue());
                                    }
                                    itr.remove();
                                    if (!TextUtils.isEmpty(association.getId())) {
                                        this.mDataStore.removeObservable(Association.class, association.getId()).subscribe();
                                    }
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
