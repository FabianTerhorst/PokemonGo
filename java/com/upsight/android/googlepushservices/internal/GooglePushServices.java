package com.upsight.android.googlepushservices.internal;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.event.comm.UpsightCommRegisterEvent;
import com.upsight.android.analytics.event.comm.UpsightCommUnregisterEvent;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnRegisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnUnregisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.UpsightBillboard;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.HandlerScheduler;

@Singleton
public class GooglePushServices implements UpsightGooglePushServicesApi {
    private static final String KEY_GCM = "com.upsight.gcm";
    private static final String LOG_TAG = GooglePushServices.class.getName();
    private static final String PREFERENCES_NAME = "com.upsight.android.googleadvertisingid.internal.registration";
    private static final String PROPERTY_APP_VERSION = "gcmApplicationVersion";
    private static final String PROPERTY_REG_ID = "gcmRegistrationId";
    static final String PUSH_SCOPE = "com_upsight_push_scope";
    private final Scheduler mComputationScheduler;
    private UpsightLogger mLogger;
    private final Set<OnRegisterListener> mPendingRegisterListeners;
    private final Set<OnUnregisterListener> mPendingUnregisterListeners;
    private SharedPreferences mPrefs;
    private UpsightBillboard mPushBillboard;
    private boolean mRegistrationIsInProgress;
    private final Handler mUiThreadHandler;
    private boolean mUnregistrationIsInProgress;
    private UpsightContext mUpsight;

    @Inject
    GooglePushServices(UpsightContext upsight) {
        this.mUpsight = upsight;
        this.mLogger = upsight.getLogger();
        if (Looper.myLooper() != null) {
            this.mUiThreadHandler = new Handler(Looper.myLooper());
        } else {
            this.mUiThreadHandler = new Handler(Looper.getMainLooper());
        }
        this.mComputationScheduler = upsight.getCoreComponent().subscribeOnScheduler();
        this.mRegistrationIsInProgress = false;
        this.mUnregistrationIsInProgress = false;
        this.mPendingRegisterListeners = new HashSet();
        this.mPendingUnregisterListeners = new HashSet();
        this.mPrefs = this.mUpsight.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    public synchronized void register(OnRegisterListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener could not be null");
        } else if (!hasPlayServices()) {
            listener.onFailure(new UpsightException("Google Play Services are not available", new Object[0]));
        } else if (this.mUnregistrationIsInProgress) {
            listener.onFailure(new UpsightException("Unregistration is in progress, try later", new Object[0]));
        } else {
            String gcmAuthority = null;
            String gcmSenderId = null;
            try {
                Bundle bundle = this.mUpsight.getPackageManager().getApplicationInfo(this.mUpsight.getPackageName(), 128).metaData;
                if (bundle != null) {
                    String gcm = bundle.getString(KEY_GCM);
                    if (!TextUtils.isEmpty(gcm)) {
                        gcmAuthority = gcm.substring(0, gcm.lastIndexOf(46));
                        gcmSenderId = gcm.substring(gcm.lastIndexOf(46) + 1);
                    }
                }
            } catch (NameNotFoundException e) {
                this.mLogger.e(Upsight.LOG_TAG, "Unexpected error: Package name missing!?", e);
            }
            if (!this.mUpsight.getPackageName().equals(gcmAuthority) || TextUtils.isEmpty(gcmSenderId)) {
                this.mLogger.e(LOG_TAG, "Registration aborted, wrong or no value for com.upsight.gcm was defined", new Object[0]);
                if (!this.mUpsight.getPackageName().equals(gcmAuthority)) {
                    this.mLogger.e(LOG_TAG, "Check that the package name of your application is specified correctly", new Object[0]);
                }
                if (TextUtils.isEmpty(gcmSenderId)) {
                    this.mLogger.e(LOG_TAG, "Check that your GCM sender id is specified correctly", new Object[0]);
                }
                listener.onFailure(new UpsightException("GCM properties must be set in the Android Manifest with <meta-data android:name=\"com.upsight.gcm\" android:value=\"" + this.mUpsight.getPackageName() + ".GCM_SENDER_ID\" />", new Object[0]));
            } else {
                this.mPendingRegisterListeners.add(listener);
                if (!this.mRegistrationIsInProgress) {
                    registerInBackground(gcmSenderId);
                }
            }
        }
    }

    private void registerInBackground(final String projectId) {
        this.mRegistrationIsInProgress = true;
        Observable.create(new OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                try {
                    subscriber.onNext(GoogleCloudMessaging.getInstance(GooglePushServices.this.mUpsight).register(new String[]{projectId}));
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(this.mComputationScheduler).observeOn(HandlerScheduler.from(this.mUiThreadHandler)).subscribe(new Observer<String>() {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
                synchronized (GooglePushServices.this) {
                    Set<OnRegisterListener> pendingListeners = new HashSet(GooglePushServices.this.mPendingRegisterListeners);
                    GooglePushServices.this.mPendingRegisterListeners.clear();
                    GooglePushServices.this.mRegistrationIsInProgress = false;
                }
                for (OnRegisterListener lst : pendingListeners) {
                    lst.onFailure(new UpsightException(e));
                }
            }

            public void onNext(String registrationId) {
                synchronized (GooglePushServices.this) {
                    GooglePushServices.this.storeRegistrationId(registrationId);
                    UpsightCommRegisterEvent.createBuilder().setToken(registrationId).record(GooglePushServices.this.mUpsight);
                    Set<OnRegisterListener> pendingListeners = new HashSet(GooglePushServices.this.mPendingRegisterListeners);
                    GooglePushServices.this.mPendingRegisterListeners.clear();
                    GooglePushServices.this.mRegistrationIsInProgress = false;
                }
                for (OnRegisterListener lst : pendingListeners) {
                    lst.onSuccess(registrationId);
                }
            }
        });
    }

    public synchronized void unregister(OnUnregisterListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener could not be null");
        } else if (!isRegistered()) {
            listener.onFailure(new UpsightException("Application is not registered to pushes yet", new Object[0]));
        } else if (this.mRegistrationIsInProgress) {
            listener.onFailure(new UpsightException("Registration is in progress, try later", new Object[0]));
        } else {
            this.mPendingUnregisterListeners.add(listener);
            if (!this.mUnregistrationIsInProgress) {
                unregisterInBackground();
            }
        }
    }

    private void unregisterInBackground() {
        this.mUnregistrationIsInProgress = true;
        Observable.create(new OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                try {
                    GoogleCloudMessaging.getInstance(GooglePushServices.this.mUpsight).unregister();
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(this.mComputationScheduler).observeOn(HandlerScheduler.from(this.mUiThreadHandler)).subscribe(new Observer<String>() {
            public void onCompleted() {
                synchronized (GooglePushServices.this) {
                    UpsightCommUnregisterEvent.createBuilder().record(GooglePushServices.this.mUpsight);
                    Set<OnUnregisterListener> pendingListeners = new HashSet(GooglePushServices.this.mPendingUnregisterListeners);
                    GooglePushServices.this.mPendingUnregisterListeners.clear();
                    GooglePushServices.this.mUnregistrationIsInProgress = false;
                }
                for (OnUnregisterListener lst : pendingListeners) {
                    lst.onSuccess();
                }
            }

            public void onError(Throwable e) {
                synchronized (GooglePushServices.this) {
                    Set<OnUnregisterListener> pendingListeners = new HashSet(GooglePushServices.this.mPendingUnregisterListeners);
                    GooglePushServices.this.mPendingUnregisterListeners.clear();
                    GooglePushServices.this.mUnregistrationIsInProgress = false;
                }
                for (OnUnregisterListener lst : pendingListeners) {
                    lst.onFailure(new UpsightException(e));
                }
            }

            public void onNext(String args) {
            }
        });
    }

    private boolean hasPlayServices() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mUpsight) == 0) {
            return true;
        }
        this.mLogger.e(LOG_TAG, "Google play service is not available: ", GooglePlayServicesUtil.getErrorString(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mUpsight)));
        return false;
    }

    private boolean isRegistered() {
        return getRegistrationId() != null;
    }

    private String getRegistrationId() {
        String registrationId = this.mPrefs.getString(PROPERTY_REG_ID, null);
        if (TextUtils.isEmpty(registrationId)) {
            return null;
        }
        if (this.mPrefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE) != getAppVersion()) {
            return null;
        }
        return registrationId;
    }

    private void storeRegistrationId(String registrationId) {
        int appVersion = getAppVersion();
        Editor editor = this.mPrefs.edit();
        editor.putString(PROPERTY_REG_ID, registrationId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    private int getAppVersion() {
        try {
            return this.mUpsight.getPackageManager().getPackageInfo(this.mUpsight.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public synchronized UpsightBillboard createPushBillboard(UpsightContext upsight, UpsightBillboard.Handler handler) throws IllegalArgumentException, IllegalStateException {
        if (this.mPushBillboard != null) {
            this.mPushBillboard.destroy();
            this.mPushBillboard = null;
        }
        this.mPushBillboard = UpsightBillboard.create(upsight, PUSH_SCOPE, handler);
        return this.mPushBillboard;
    }
}
