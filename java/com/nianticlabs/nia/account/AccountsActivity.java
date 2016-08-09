package com.nianticlabs.nia.account;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nianticlabs.nia.account.NianticAccountManager.Status;
import java.io.IOException;
import java.lang.ref.WeakReference;
import spacemadness.com.lunarconsole.BuildConfig;

public class AccountsActivity extends Activity {
    static final String AUTH_TOKEN_SCOPE_PREFIX = "audience:server:client_id:";
    static String EXTRA_OAUTH_CLIENT_ID = "oauthClientId";
    private static final int REQUEST_CHOOSE_ACCOUNT = 1;
    private static final int REQUEST_GET_AUTH = 2;
    private static String TAG = "AccountsActivity";
    private NianticAccountManager accountManager;
    private boolean authInProgress = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("accounts_activity", "layout", getPackageName()));
        this.accountManager = null;
        WeakReference<NianticAccountManager> weakAccountManager = NianticAccountManager.getInstance();
        if (weakAccountManager != null) {
            this.accountManager = (NianticAccountManager) weakAccountManager.get();
        }
        if (this.accountManager == null) {
            throw new RuntimeException("Unable to locate NianticAccountManager");
        }
    }

    protected void onResume() {
        super.onResume();
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != 0) {
            Log.e(TAG, "Google Play Services not available, need to do something. Error code: " + resultCode);
            this.accountManager.setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR, this.accountManager.getAccountName());
            finish();
        } else if (!this.authInProgress) {
            this.authInProgress = true;
            getAuthOrAccount();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String error = "Unexpected requestCode " + requestCode;
        switch (requestCode) {
            case REQUEST_CHOOSE_ACCOUNT /*1*/:
                if (resultCode == 0) {
                    failAuth(Status.USER_CANCELED_LOGIN, "User decided to cancel account selection.");
                    return;
                } else if (data == null) {
                    failAuth(Status.NON_RECOVERABLE_ERROR, "Attempt to choose null account, resultCode: " + resultCode);
                    return;
                } else {
                    String accountName = data.getStringExtra("authAccount");
                    if (accountName == null || accountName.isEmpty()) {
                        failAuth(Status.NON_RECOVERABLE_ERROR, "Attempt to choose unnamed account, resultCode: " + resultCode);
                        return;
                    }
                    this.accountManager.setAccountName(accountName);
                    getAuth(accountName);
                    return;
                }
            case REQUEST_GET_AUTH /*2*/:
                getAuthOrAccount();
                return;
            default:
                Log.e(TAG, error);
                return;
        }
    }

    private void getAuthOrAccount() {
        String accountName = this.accountManager.getAccountName();
        if (accountName == null || accountName.isEmpty()) {
            String[] strArr = new String[REQUEST_CHOOSE_ACCOUNT];
            strArr[0] = "com.google";
            startActivityForResult(AccountPicker.newChooseAccountIntent(null, null, strArr, false, null, null, null, null), REQUEST_CHOOSE_ACCOUNT);
            return;
        }
        getAuth(accountName);
    }

    private void getAuth(final String accountName) {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                AccountsActivity.getAuthTokenBlocking(AccountsActivity.this, accountName);
                return null;
            }
        }.execute(new Void[0]);
    }

    private static void getAuthTokenBlocking(AccountsActivity activity, String accountName) {
        try {
            Log.d(TAG, "Authenticating with account: " + accountName);
            String clientId = activity.getIntent().getStringExtra(EXTRA_OAUTH_CLIENT_ID);
            Log.i(TAG, "Authenticating with client id: " + clientId);
            String scope = AUTH_TOKEN_SCOPE_PREFIX + clientId;
            Log.i(TAG, "Authenticating with scope: " + scope);
            activity.accountManager.setAuthToken(Status.OK, GoogleAuthUtil.getToken(activity, accountName, scope), accountName);
            activity.postFinish();
        } catch (UserRecoverableAuthException userAuthEx) {
            activity.askUserToRecover(userAuthEx);
        } catch (IOException transientEx) {
            Log.e(TAG, "Unable to get authToken at this time.", transientEx);
            activity.accountManager.setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR, accountName);
            activity.postFinish();
        } catch (GoogleAuthException authEx) {
            Log.e(TAG, "User cannot be authenticated.", authEx);
            activity.accountManager.setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR, accountName);
            activity.postFinish();
        }
    }

    private void failAuth(Status status, String error) {
        Log.e(TAG, error);
        this.accountManager.setAuthToken(status, BuildConfig.FLAVOR, this.accountManager.getAccountName());
        finish();
    }

    private void askUserToRecover(final UserRecoverableAuthException e) {
        runOnUiThread(new Runnable() {
            public void run() {
                AccountsActivity.this.startActivityForResult(e.getIntent(), AccountsActivity.REQUEST_GET_AUTH);
            }
        });
    }

    private void postFinish() {
        runOnUiThread(new Runnable() {
            public void run() {
                AccountsActivity.this.finish();
            }
        });
    }
}
