package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.UpsightException;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import com.upsight.android.managedvariables.internal.type.UxmSchema.BaseSchema;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import com.upsight.android.managedvariables.type.UpsightManagedVariable.Listener;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import com.upsight.android.persistence.annotation.Removed;
import com.upsight.android.persistence.annotation.Updated;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;

public class ManagedVariableManager {
    private static final Map<Class<? extends ManagedVariable>, Class<? extends ManagedVariableModel>> sModelMap = new HashMap<Class<? extends ManagedVariable>, Class<? extends ManagedVariableModel>>() {
        {
            put(UpsightManagedString.class, Model.class);
            put(UpsightManagedBoolean.class, Model.class);
            put(UpsightManagedInt.class, Model.class);
            put(UpsightManagedFloat.class, Model.class);
        }
    };
    private final Map<String, ManagedVariable> mCache = new HashMap();
    private Scheduler mCallbackScheduler;
    private UpsightDataStore mDataStore;
    private UxmSchema mUxmSchema;

    ManagedVariableManager(Scheduler callbackScheduler, UpsightDataStore dataStore, UxmSchema uxmSchema) {
        this.mCallbackScheduler = callbackScheduler;
        this.mDataStore = dataStore;
        this.mUxmSchema = uxmSchema;
        dataStore.subscribe(this);
    }

    public <T extends ManagedVariable> T fetch(Class<T> clazz, String tag) {
        T managedVariable;
        synchronized (this.mCache) {
            T cachedVariable = (ManagedVariable) this.mCache.get(tag);
            if (cachedVariable != null) {
                managedVariable = cachedVariable;
            } else {
                managedVariable = fromModel(clazz, tag, (ManagedVariableModel) fetchDataStoreObservable(clazz, tag).toBlocking().first());
                if (managedVariable != null) {
                    this.mCache.put(tag, managedVariable);
                }
            }
        }
        return managedVariable;
    }

    public <T extends ManagedVariable> UpsightSubscription fetch(final Class<T> clazz, final String tag, final Listener<T> listener) {
        UpsightSubscription subscription;
        synchronized (this.mCache) {
            ManagedVariable cachedVariable = (ManagedVariable) this.mCache.get(tag);
            if (cachedVariable != null) {
                subscription = Subscriptions.from(Observable.just(cachedVariable).observeOn(this.mCallbackScheduler).subscribe(new Action1<T>() {
                    public void call(T managedVariable) {
                        listener.onSuccess(managedVariable);
                    }
                }));
            } else {
                subscription = Subscriptions.from(fetchDataStoreObservable(clazz, tag).subscribe(new Action1<ManagedVariableModel>() {
                    public void call(ManagedVariableModel model) {
                        synchronized (ManagedVariableManager.this.mCache) {
                            ManagedVariable cachedVariable = (ManagedVariable) ManagedVariableManager.this.mCache.get(tag);
                            if (cachedVariable != null) {
                                listener.onSuccess(cachedVariable);
                            } else {
                                T managedVariable = ManagedVariableManager.this.fromModel(clazz, tag, model);
                                if (managedVariable != null) {
                                    ManagedVariableManager.this.mCache.put(tag, managedVariable);
                                    listener.onSuccess(managedVariable);
                                } else {
                                    listener.onFailure(new UpsightException("Invalid managed variable tag", new Object[0]));
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    public void call(Throwable throwable) {
                        listener.onFailure(new UpsightException(throwable));
                    }
                }));
            }
        }
        return subscription;
    }

    @Updated
    @Created
    public void handleManagedVariableUpdate(Model model) {
        updateValue(UpsightManagedString.class, model.getTag(), model.getValue());
    }

    @Updated
    @Created
    public void handleManagedVariableUpdate(Model model) {
        updateValue(UpsightManagedBoolean.class, model.getTag(), model.getValue());
    }

    @Updated
    @Created
    public void handleManagedVariableUpdate(Model model) {
        updateValue(UpsightManagedInt.class, model.getTag(), model.getValue());
    }

    @Updated
    @Created
    public void handleManagedVariableUpdate(Model model) {
        updateValue(UpsightManagedFloat.class, model.getTag(), model.getValue());
    }

    @Removed
    public void handleManagedVariableRemoval(Model model) {
        resetValue(UpsightManagedString.class, model.getTag());
    }

    @Removed
    public void handleManagedVariableRemoval(Model model) {
        resetValue(UpsightManagedBoolean.class, model.getTag());
    }

    @Removed
    public void handleManagedVariableRemoval(Model model) {
        resetValue(UpsightManagedInt.class, model.getTag());
    }

    @Removed
    public void handleManagedVariableRemoval(Model model) {
        resetValue(UpsightManagedFloat.class, model.getTag());
    }

    private <T extends ManagedVariable> Observable<? extends ManagedVariableModel> fetchDataStoreObservable(final Class<T> clazz, final String tag) {
        return this.mDataStore.fetchObservable((Class) sModelMap.get(clazz)).filter(new Func1<ManagedVariableModel, Boolean>() {
            public Boolean call(ManagedVariableModel model) {
                return Boolean.valueOf(ManagedVariableManager.this.mUxmSchema.get(clazz, tag).tag.equals(model.getTag()));
            }
        }).defaultIfEmpty(null);
    }

    private <T extends ManagedVariable> T fromModel(Class<T> clazz, String tag, ManagedVariableModel model) {
        T t = null;
        T managedVariable = null;
        BaseSchema schemaObject = this.mUxmSchema.get(clazz, tag);
        if (schemaObject == null) {
            return null;
        }
        if (UpsightManagedString.class.equals(clazz)) {
            String defaultValue = schemaObject.defaultValue;
            if (model != null) {
                t = model.getValue();
            }
            managedVariable = new ManagedString(tag, defaultValue, (String) t);
        } else if (UpsightManagedBoolean.class.equals(clazz)) {
            Boolean defaultValue2 = schemaObject.defaultValue;
            if (model != null) {
                t = model.getValue();
            }
            managedVariable = new ManagedBoolean(tag, defaultValue2, (Boolean) t);
        } else if (UpsightManagedInt.class.equals(clazz)) {
            Integer defaultValue3 = schemaObject.defaultValue;
            if (model != null) {
                t = model.getValue();
            }
            managedVariable = new ManagedInt(tag, defaultValue3, (Integer) t);
        } else if (UpsightManagedFloat.class.equals(clazz)) {
            Float defaultValue4 = schemaObject.defaultValue;
            if (model != null) {
                t = model.getValue();
            }
            managedVariable = new ManagedFloat(tag, defaultValue4, (Float) t);
        }
        return managedVariable;
    }

    private <T extends ManagedVariable> void updateValue(Class<T> clazz, String tag, Object value) {
        synchronized (this.mCache) {
            ManagedVariable managedVariable = (ManagedVariable) this.mCache.get(tag);
            if (managedVariable != null && clazz.isInstance(managedVariable)) {
                managedVariable.set(value);
            }
        }
    }

    private <T extends ManagedVariable> void resetValue(Class<T> clazz, String tag) {
        synchronized (this.mCache) {
            ManagedVariable managedVariable = (ManagedVariable) this.mCache.get(tag);
            if (managedVariable != null && clazz.isInstance(managedVariable)) {
                managedVariable.set(null);
            }
        }
    }
}
