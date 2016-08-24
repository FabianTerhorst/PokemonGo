package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.squareup.otto.Bus;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.UpsightManagedVariablesExtension;
import com.upsight.android.analytics.event.uxm.UpsightUxmEnumerateEvent;
import com.upsight.android.analytics.internal.action.Action;
import com.upsight.android.analytics.internal.action.ActionContext;
import com.upsight.android.analytics.internal.action.ActionFactory;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.UpsightManagedVariablesComponent;
import com.upsight.android.persistence.UpsightDataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import rx.Observable;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public final class UxmContentActions {
    private static final Map<String, InternalFactory> FACTORY_MAP = new HashMap<String, InternalFactory>() {
        {
            put("action_uxm_enumerate", new InternalFactory() {
                public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new UxmEnumerate(actionContext, actionType, actionParams);
                }
            });
            put("action_set_bundle_id", new InternalFactory() {
                public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new SetBundleId(actionContext, actionType, actionParams);
                }
            });
            put("action_modify_value", new InternalFactory() {
                public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new ModifyValue(actionContext, actionType, actionParams);
                }
            });
            put("action_notify_uxm_values_synchronized", new InternalFactory() {
                public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new NotifyUxmValuesSynchronized(actionContext, actionType, actionParams);
                }
            });
            put("action_destroy", new InternalFactory() {
                public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new Destroy(actionContext, actionType, actionParams);
                }
            });
        }
    };

    private interface InternalFactory {
        Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject);
    }

    static class Destroy extends Action<UxmContent, UxmContentActionContext> {
        private Destroy(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            Bus bus = ((UxmContentActionContext) getActionContext()).mBus;
            content.signalActionCompleted(bus);
            content.signalActionMapCompleted(bus);
        }
    }

    static class ModifyValue extends Action<UxmContent, UxmContentActionContext> {
        private static final String MATCH = "match";
        private static final String OPERATOR = "operator";
        private static final String OPERATOR_SET = "set";
        private static final String PROPERTY_NAME = "property_name";
        private static final String PROPERTY_VALUE = "property_value";
        private static final String TYPE = "type";
        private static final String VALUES = "values";

        private ModifyValue(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            boolean isSync = true;
            ActionContext actionContext = getActionContext();
            if (content.shouldApplyBundle()) {
                String type = optParamString(TYPE);
                JsonArray matchers = optParamJsonArray(MATCH);
                JsonArray values = optParamJsonArray(VALUES);
                if (!(TextUtils.isEmpty(type) || matchers == null || values == null)) {
                    Class<?> clazz = null;
                    if ("com.upsight.uxm.string".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.boolean".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.integer".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.float".equals(type)) {
                        clazz = Model.class;
                    }
                    if (clazz != null) {
                        modifyValue(content, clazz, matchers, values);
                        isSync = false;
                    } else {
                        actionContext.mLogger.e(Upsight.LOG_TAG, "Failed to execute action_modify_value due to unknown managed variable type " + type, new Object[0]);
                    }
                }
            }
            if (isSync) {
                content.signalActionCompleted(actionContext.mBus);
            }
        }

        private <T> void modifyValue(UxmContent content, Class<T> clazz, JsonArray matchers, JsonArray values) {
            final UxmContentActionContext actionContext = (UxmContentActionContext) getActionContext();
            final Gson gson = actionContext.mGson;
            final UpsightLogger logger = actionContext.mUpsight.getLogger();
            final UpsightDataStore dataStore = actionContext.mUpsight.getDataStore();
            Observable<JsonObject> fetchObservable = dataStore.fetchObservable(clazz).map(new Func1<T, JsonElement>() {
                public JsonElement call(T model) {
                    return gson.toJsonTree(model);
                }
            }).cast(JsonObject.class);
            JsonObject seedNode = new JsonObject();
            Iterator it = matchers.iterator();
            while (it.hasNext()) {
                JsonElement matcher = (JsonElement) it.next();
                String propertyName = matcher.getAsJsonObject().get(PROPERTY_NAME).getAsString();
                JsonElement propertyValue = matcher.getAsJsonObject().get(PROPERTY_VALUE);
                final String str = propertyName;
                final JsonElement jsonElement = propertyValue;
                fetchObservable = fetchObservable.filter(new Func1<JsonObject, Boolean>() {
                    public Boolean call(JsonObject model) {
                        return Boolean.valueOf(model.getAsJsonObject().get(str).equals(jsonElement));
                    }
                });
                seedNode.add(propertyName, propertyValue);
            }
            fetchObservable = fetchObservable.defaultIfEmpty(seedNode);
            it = values.iterator();
            while (it.hasNext()) {
                JsonElement value = (JsonElement) it.next();
                String operator = value.getAsJsonObject().get(OPERATOR).getAsString();
                propertyName = value.getAsJsonObject().get(PROPERTY_NAME).getAsString();
                propertyValue = value.getAsJsonObject().get(PROPERTY_VALUE);
                if (OPERATOR_SET.equals(operator)) {
                    str = propertyName;
                    jsonElement = propertyValue;
                    fetchObservable = fetchObservable.map(new Func1<JsonObject, JsonObject>() {
                        public JsonObject call(JsonObject model) {
                            model.add(str, jsonElement);
                            return model;
                        }
                    });
                }
            }
            final Class<T> cls = clazz;
            final UxmContent uxmContent = content;
            final UpsightLogger upsightLogger = logger;
            final Class<T> cls2 = clazz;
            final UxmContent uxmContent2 = content;
            final UxmContentActionContext uxmContentActionContext = actionContext;
            fetchObservable.subscribeOn(actionContext.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(actionContext.mUpsight.getCoreComponent().observeOnScheduler()).subscribe(new Action1<JsonObject>() {
                public void call(final JsonObject modelNode) {
                    try {
                        dataStore.storeObservable(gson.fromJson(modelNode, cls)).subscribeOn(actionContext.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(actionContext.mUpsight.getCoreComponent().observeOnScheduler()).subscribe(new Action1<T>() {
                            public void call(T t) {
                                logger.d(Upsight.LOG_TAG, "Modified managed variable of class " + cls + " with value " + modelNode, new Object[0]);
                            }
                        }, new Action1<Throwable>() {
                            public void call(Throwable t) {
                                logger.e(Upsight.LOG_TAG, t, "Failed to modify managed variable of class " + cls, new Object[0]);
                            }
                        }, new Action0() {
                            public void call() {
                                uxmContent.signalActionCompleted(actionContext.mBus);
                            }
                        });
                    } catch (JsonSyntaxException e) {
                        logger.e(Upsight.LOG_TAG, e, "Failed to parse managed variable of class " + cls, new Object[0]);
                        uxmContent.signalActionCompleted(actionContext.mBus);
                    }
                }
            }, new Action1<Throwable>() {
                public void call(Throwable throwable) {
                    upsightLogger.e(Upsight.LOG_TAG, throwable, "Failed to fetch managed variable of class " + cls2, new Object[0]);
                    uxmContent2.signalActionCompleted(uxmContentActionContext.mBus);
                }
            });
        }
    }

    static class NotifyUxmValuesSynchronized extends Action<UxmContent, UxmContentActionContext> {
        private static final String TAGS = "tags";

        private NotifyUxmValuesSynchronized(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            List<String> synchronizedTags = new ArrayList();
            JsonArray tagNodes = optParamJsonArray(TAGS);
            if (content.shouldApplyBundle() && tagNodes != null) {
                Iterator it = tagNodes.iterator();
                while (it.hasNext()) {
                    JsonElement tagNode = (JsonElement) it.next();
                    if (tagNode.isJsonPrimitive() && tagNode.getAsJsonPrimitive().isString()) {
                        synchronizedTags.add(tagNode.getAsString());
                    }
                }
            }
            Bus bus = ((UxmContentActionContext) getActionContext()).mBus;
            bus.post(new ScheduleSyncNotificationEvent(content.getId(), synchronizedTags));
            content.signalActionCompleted(bus);
        }
    }

    public static class ScheduleSyncNotificationEvent {
        public final String mId;
        public final List<String> mTags;

        private ScheduleSyncNotificationEvent(String id, List<String> tags) {
            this.mId = id;
            this.mTags = tags;
        }
    }

    static class SetBundleId extends Action<UxmContent, UxmContentActionContext> {
        private static final String BUNDLE_ID = "bundle.id";

        private SetBundleId(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            if (content.shouldApplyBundle()) {
                PreferencesHelper.putString(((UxmContentActionContext) getActionContext()).mUpsight, UxmContent.PREFERENCES_KEY_UXM_BUNDLE_ID, optParamString(BUNDLE_ID));
            }
            content.signalActionCompleted(((UxmContentActionContext) getActionContext()).mBus);
        }
    }

    public static class UxmContentActionContext extends ActionContext {
        public UxmContentActionContext(UpsightContext upsight, Bus bus, Gson gson, Clock clock, Worker mainWorker, UpsightLogger logger) {
            super(upsight, bus, gson, clock, mainWorker, logger);
        }
    }

    public static class UxmContentActionFactory implements ActionFactory<UxmContent, UxmContentActionContext> {
        public static final String TYPE = "datastore_factory";

        public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, JsonObject actionJSON) throws UpsightException {
            if (actionJSON == null) {
                throw new UpsightException("Failed to create Action. JSON is null.", new Object[0]);
            }
            String actionType = actionJSON.get(ActionFactory.KEY_ACTION_TYPE).getAsString();
            JsonObject actionParams = actionJSON.getAsJsonObject(ActionFactory.KEY_ACTION_PARAMS);
            InternalFactory factory = (InternalFactory) UxmContentActions.FACTORY_MAP.get(actionType);
            if (factory != null) {
                return factory.create(actionContext, actionType, actionParams);
            }
            throw new UpsightException("Failed to create Action. Unknown action type.", new Object[0]);
        }
    }

    static class UxmEnumerate extends Action<UxmContent, UxmContentActionContext> {
        private UxmEnumerate(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            ActionContext actionContext = getActionContext();
            UpsightManagedVariablesExtension extension = (UpsightManagedVariablesExtension) actionContext.mUpsight.getUpsightExtension(UpsightManagedVariablesExtension.EXTENSION_NAME);
            if (extension != null) {
                try {
                    UpsightUxmEnumerateEvent.createBuilder(new JSONArray(((UpsightManagedVariablesComponent) extension.getComponent()).uxmSchema().mSchemaJsonString)).record(actionContext.mUpsight);
                } catch (JSONException e) {
                    actionContext.mUpsight.getLogger().e(Upsight.LOG_TAG, e, "Failed to send UXM enumerate event", new Object[0]);
                }
            }
            content.signalActionCompleted(actionContext.mBus);
        }
    }

    private UxmContentActions() {
    }
}
