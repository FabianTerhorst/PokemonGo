package com.upsight.android.analytics.internal.dispatcher;

import com.upsight.android.UpsightException;
import com.upsight.android.analytics.configuration.UpsightConfiguration;
import com.upsight.android.analytics.dispatcher.AnalyticsEventDeliveryStatus;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.internal.AnalyticsContext;
import com.upsight.android.analytics.internal.DataStoreRecord;
import com.upsight.android.analytics.internal.DataStoreRecord.Action;
import com.upsight.android.analytics.internal.dispatcher.routing.Router;
import com.upsight.android.analytics.internal.dispatcher.routing.RouterBuilder;
import com.upsight.android.analytics.internal.dispatcher.routing.RoutingListener;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaSelectorBuilder;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Dispatcher implements RoutingListener {
    public static final String CONFIGURATION_SUBTYPE = "upsight.configuration.dispatcher";
    static final int DISPATCHER_CONFIGURATION_MAX_SESSIONS = 2;
    private static final String LOG_TAG = "Dispatcher";
    private ConfigParser mConfigParser;
    private AnalyticsContext mContext;
    private Config mCurrentConfig;
    private volatile Router mCurrentRouter;
    private UpsightSubscription mDataStoreSubscription;
    private Collection<Router> mExpiredRouters;
    private boolean mIsLaunched = false;
    private UpsightLogger mLogger;
    private Set<DataStoreRecord> mPendingRecords;
    private RouterBuilder mRouterBuilder;
    private SchemaSelectorBuilder mSchemaSelectorBuilder;
    private SessionManager mSessionManager;
    private Queue<DataStoreRecord> mUnroutedRecords;
    private UpsightDataStore mUpsightDataStore;

    Dispatcher(AnalyticsContext context, SessionManager sessionManager, UpsightDataStore dataStore, ConfigParser configParser, RouterBuilder routerBuilder, SchemaSelectorBuilder schemaSelectorBuilder, UpsightLogger logger) {
        this.mContext = context;
        this.mSessionManager = sessionManager;
        this.mUpsightDataStore = dataStore;
        this.mConfigParser = configParser;
        this.mRouterBuilder = routerBuilder;
        this.mSchemaSelectorBuilder = schemaSelectorBuilder;
        this.mLogger = logger;
    }

    public void launch() {
        if (!this.mIsLaunched) {
            this.mIsLaunched = true;
            this.mCurrentRouter = null;
            this.mExpiredRouters = new HashSet();
            this.mUnroutedRecords = new ConcurrentLinkedQueue();
            this.mPendingRecords = Collections.synchronizedSet(new HashSet());
            this.mCurrentConfig = null;
            this.mDataStoreSubscription = this.mUpsightDataStore.subscribe(this);
            fetchCurrentConfig();
        }
    }

    private void fetchCreatedRecords() {
        this.mUpsightDataStore.fetch(DataStoreRecord.class, new UpsightDataStoreListener<Set<DataStoreRecord>>() {
            public void onSuccess(Set<DataStoreRecord> result) {
                for (DataStoreRecord record : result) {
                    Dispatcher.this.routeRecords(record);
                }
            }

            public void onFailure(UpsightException exception) {
                Dispatcher.this.mLogger.e(Dispatcher.LOG_TAG, "Could not fetch records from store.", exception);
            }
        });
    }

    @Created
    public void onDataStoreRecordCreated(DataStoreRecord storedRecord) {
        routeRecords(storedRecord);
    }

    private void routeRecords(DataStoreRecord storedRecord) {
        if (Action.Created.equals(storedRecord.getAction())) {
            Router currentRouter = this.mCurrentRouter;
            Set<DataStoreRecord> pendingRecords = this.mPendingRecords;
            if (currentRouter == null) {
                Queue<DataStoreRecord> unroutedRecords = this.mUnroutedRecords;
                if (!unroutedRecords.contains(storedRecord)) {
                    unroutedRecords.add(storedRecord);
                    return;
                }
                return;
            } else if (pendingRecords != null && !pendingRecords.contains(storedRecord) && currentRouter.routeEvent(storedRecord)) {
                pendingRecords.add(storedRecord);
                return;
            } else {
                return;
            }
        }
        this.mUpsightDataStore.remove(storedRecord);
    }

    private void fetchCurrentConfig() {
        this.mUpsightDataStore.fetch(UpsightConfiguration.class, new UpsightDataStoreListener<Set<UpsightConfiguration>>() {
            public void onSuccess(Set<UpsightConfiguration> result) {
                if (Dispatcher.this.mCurrentConfig == null) {
                    boolean hasApplied = false;
                    for (UpsightConfiguration config : result) {
                        if (Dispatcher.CONFIGURATION_SUBTYPE.equals(config.getScope()) && Dispatcher.this.isUpsightConfigurationValid(config)) {
                            hasApplied = Dispatcher.this.applyConfiguration(config.getConfiguration());
                        }
                    }
                    if (!hasApplied) {
                        Dispatcher.this.applyDefaultConfiguration();
                    }
                }
            }

            public void onFailure(UpsightException exception) {
                Dispatcher.this.mLogger.e(Dispatcher.LOG_TAG, "Could not fetch config from store.", exception);
                if (Dispatcher.this.mCurrentConfig == null) {
                    Dispatcher.this.applyDefaultConfiguration();
                }
            }
        });
    }

    @Created
    public void onConfigurationCreated(UpsightConfiguration config) {
        if (CONFIGURATION_SUBTYPE.equals(config.getScope()) && isUpsightConfigurationValid(config)) {
            applyConfiguration(config.getConfiguration());
        }
    }

    private boolean isUpsightConfigurationValid(UpsightConfiguration configuration) {
        return this.mSessionManager.getCurrentSession().getSessionNumber() - configuration.getSessionNumberCreated() <= DISPATCHER_CONFIGURATION_MAX_SESSIONS;
    }

    private void applyDefaultConfiguration() {
        applyConfiguration(this.mContext.getDefaultDispatcherConfiguration());
    }

    private boolean applyConfiguration(String jsonConfig) {
        Config config = parseConfiguration(jsonConfig);
        if (config == null) {
            return false;
        }
        if (!config.isValid()) {
            this.mLogger.w(LOG_TAG, "Incoming configuration is not valid", new Object[0]);
            return false;
        } else if (config.equals(this.mCurrentConfig)) {
            return true;
        } else {
            this.mCurrentConfig = config;
            Collection<Router> expiredRouters = this.mExpiredRouters;
            Router currentRouter = this.mCurrentRouter;
            if (!(expiredRouters == null || currentRouter == null)) {
                expiredRouters.add(currentRouter);
                currentRouter.finishRouting();
            }
            this.mCurrentRouter = this.mRouterBuilder.build(config.getRoutingConfig(), this.mSchemaSelectorBuilder.buildSelectorByName(config.getIdentifierConfig()), this.mSchemaSelectorBuilder.buildSelectorByType(config.getIdentifierConfig()), this);
            Queue<DataStoreRecord> unroutedRecords = this.mUnroutedRecords;
            if (!(unroutedRecords == null || this.mCurrentRouter == null)) {
                while (!unroutedRecords.isEmpty()) {
                    routeRecords((DataStoreRecord) unroutedRecords.poll());
                }
            }
            fetchCreatedRecords();
            return true;
        }
    }

    private Config parseConfiguration(String jsonConfig) {
        Config config = null;
        try {
            config = this.mConfigParser.parseConfig(jsonConfig);
        } catch (IOException e) {
            this.mLogger.e(LOG_TAG, "Could not apply incoming config", e);
        }
        return config;
    }

    public void onRoutingFinished(Router rm) {
        Collection<Router> expiredRouters = this.mExpiredRouters;
        if (expiredRouters != null) {
            expiredRouters.remove(rm);
        }
    }

    public void onDelivery(DataStoreRecord record, boolean isSuccessful, boolean isTrashed, String reason) {
        AnalyticsEventDeliveryStatus status;
        if (isSuccessful) {
            status = AnalyticsEventDeliveryStatus.fromSuccess(record.getID());
        } else {
            status = AnalyticsEventDeliveryStatus.fromFailure(record.getID(), reason);
        }
        this.mUpsightDataStore.store(status, new UpsightDataStoreListener<AnalyticsEventDeliveryStatus>() {
            public void onSuccess(AnalyticsEventDeliveryStatus result) {
                Dispatcher.this.mUpsightDataStore.remove(result);
            }

            public void onFailure(UpsightException exception) {
                Dispatcher.this.mLogger.e(Dispatcher.LOG_TAG, exception, "Could not store DeliveryStatus.", new Object[0]);
            }
        });
        if (isSuccessful || isTrashed) {
            this.mUpsightDataStore.remove(record);
        }
        Set<DataStoreRecord> pendingRecords = this.mPendingRecords;
        if (pendingRecords != null) {
            pendingRecords.remove(record);
        }
    }

    public void onResponse(EndpointResponse response) {
        this.mUpsightDataStore.store(response, new UpsightDataStoreListener<EndpointResponse>() {
            public void onSuccess(EndpointResponse result) {
                Dispatcher.this.mUpsightDataStore.remove(result);
            }

            public void onFailure(UpsightException exception) {
                Dispatcher.this.mLogger.e(Dispatcher.LOG_TAG, exception, "Could not store EndpointResponse.", new Object[0]);
            }
        });
    }

    public boolean hasPendingRecords() {
        Set<DataStoreRecord> pendingRecords = this.mPendingRecords;
        return pendingRecords == null || !pendingRecords.isEmpty();
    }

    public void terminate() {
        if (this.mCurrentRouter != null) {
            this.mCurrentRouter.finishRouting();
            this.mCurrentRouter = null;
        }
        if (this.mDataStoreSubscription != null) {
            this.mDataStoreSubscription.unsubscribe();
            this.mDataStoreSubscription = null;
        }
        this.mCurrentConfig = null;
        this.mPendingRecords = null;
        this.mUnroutedRecords = null;
        this.mExpiredRouters = null;
        this.mCurrentRouter = null;
        this.mIsLaunched = false;
    }
}
