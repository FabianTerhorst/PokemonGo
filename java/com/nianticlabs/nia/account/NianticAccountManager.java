package com.nianticlabs.nia.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nianticlabs.nia.contextservice.ContextService;
import java.io.IOException;
import java.lang.ref.WeakReference;
import spacemadness.com.lunarconsole.BuildConfig;

public class NianticAccountManager extends ContextService {
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static final String TAG = "NianticAccountManager";
    private static WeakReference<NianticAccountManager> instance = null;
    private final SharedPreferences prefs;

    public enum Status {
        UNDEFINED(0),
        OK(1),
        NON_RECOVERABLE_ERROR(2),
        SIGNING_OUT(3),
        USER_CANCELED_LOGIN(4);
        
        public final int id;

        private Status(int id) {
            this.id = id;
        }
    }

    private native void nativeAuthTokenCallback(int i, String str, String str2);

    public static WeakReference<NianticAccountManager> getInstance() {
        return instance;
    }

    public NianticAccountManager(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        instance = new WeakReference(this);
        this.prefs = context.getSharedPreferences(context.getPackageName() + ".PREFS", 0);
    }

    public void getAccount(final String clientId) {
        String accountName;
        boolean useAccountsActivity = false;
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.context);
        if (resultCode != 0) {
            Log.e(TAG, "Google Play Services not available. Error code: " + resultCode);
            setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR, getAccountName());
            return;
        }
        try {
            accountName = getAccountName();
            if (accountName == null || accountName.isEmpty()) {
                useAccountsActivity = true;
            } else {
                Log.d(TAG, "Authenticating with account: " + accountName);
                String scope = "audience:server:client_id:" + clientId;
                Log.d(TAG, "scope: " + scope);
                setAuthToken(Status.OK, GoogleAuthUtil.getToken(this.context, accountName, scope), accountName);
            }
        } catch (UserRecoverableAuthException e) {
            Log.d(TAG, "Use account activity");
            useAccountsActivity = true;
        } catch (IOException transientEx) {
            accountName = getAccountName();
            Log.e(TAG, "Unable to get authToken at this time.", transientEx);
            setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR, accountName);
        } catch (GoogleAuthException authEx) {
            accountName = getAccountName();
            Log.e(TAG, "User cannot be authenticated.", authEx);
            setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR, accountName);
        }
        if (useAccountsActivity) {
            ContextService.runOnUiThread(new Runnable() {
                public void run() {
                    Intent intent = new Intent(NianticAccountManager.this.context, AccountsActivity.class);
                    intent.putExtra(AccountsActivity.EXTRA_OAUTH_CLIENT_ID, clientId);
                    NianticAccountManager.this.context.startActivity(intent);
                }
            });
        }
    }

    private synchronized void clearAccount() {
        this.prefs.edit().remove(KEY_ACCOUNT_NAME).apply();
    }

    public synchronized String getAccountName() {
        return this.prefs.getString(KEY_ACCOUNT_NAME, BuildConfig.FLAVOR);
    }

    public synchronized void setAccountName(String accountName) {
        this.prefs.edit().putString(KEY_ACCOUNT_NAME, accountName).apply();
    }

    public synchronized void setAuthToken(Status status, String authToken, String accountName) {
        synchronized (this.callbackLock) {
            Log.d(TAG, "setAuthToken: " + accountName + " - " + authToken);
            nativeAuthTokenCallback(status.ordinal(), authToken, accountName);
        }
    }
}
