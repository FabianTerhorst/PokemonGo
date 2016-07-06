package com.upsight.android.internal.persistence.subscription;

import com.upsight.android.internal.persistence.subscription.DataStoreEvent.Action;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

class SubscriptionHandlerVisitor implements ClassSubscriptionVisitor {
    private final Set<SubscriptionHandler> mHandlers = new HashSet();
    private final Object mTarget;

    SubscriptionHandlerVisitor(Object target) {
        this.mTarget = target;
    }

    public void visitCreatedSubscription(Method method, Class<?> type) {
        UpsightStorableType storableType = (UpsightStorableType) type.getAnnotation(UpsightStorableType.class);
        if (storableType != null) {
            this.mHandlers.add(new SubscriptionHandler(this.mTarget, method, Action.Created, storableType.value()));
        }
    }

    public void visitUpdatedSubscription(Method method, Class<?> type) {
        UpsightStorableType storableType = (UpsightStorableType) type.getAnnotation(UpsightStorableType.class);
        if (storableType != null) {
            this.mHandlers.add(new SubscriptionHandler(this.mTarget, method, Action.Updated, storableType.value()));
        }
    }

    public void visitRemovedSubscription(Method method, Class<?> type) {
        UpsightStorableType storableType = (UpsightStorableType) type.getAnnotation(UpsightStorableType.class);
        if (storableType != null) {
            this.mHandlers.add(new SubscriptionHandler(this.mTarget, method, Action.Removed, storableType.value()));
        }
    }

    public Set<SubscriptionHandler> getHandlers() {
        return new HashSet(this.mHandlers);
    }
}
