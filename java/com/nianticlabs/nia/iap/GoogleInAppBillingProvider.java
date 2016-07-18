package com.nianticlabs.nia.iap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.android.vending.billing.IInAppBillingService;
import com.android.vending.billing.IInAppBillingService.Stub;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.iap.InAppBillingProvider.Delegate;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GoogleInAppBillingProvider implements InAppBillingProvider {
    private static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    private static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    private static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    private static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    private static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
    private static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    private static final int BILLING_RESPONSE_RESULT_NOT_FOUND = 1000;
    private static final int BILLING_RESPONSE_RESULT_OK = 0;
    private static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    private static final int BILLING_SERVICE_VERSION = 3;
    static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    private static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    private static final String ITEM_TYPE_INAPP = "inapp";
    private static final String PACKAGE_NAME_BASE = "com.niantic";
    private static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    private static final String RESPONSE_CODE = "RESPONSE_CODE";
    private static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    private static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    private static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    private static final String UNKNOWN_CURRENCY_STRING = "UNKNOWN";
    private static WeakReference<GoogleInAppBillingProvider> instance = null;
    private static final Logger log = new Logger(GoogleInAppBillingProvider.class);
    private IInAppBillingService billingService = null;
    private boolean clientConnected = ENABLE_VERBOSE_LOGS;
    private boolean connectionInProgress = ENABLE_VERBOSE_LOGS;
    private final Context context;
    private Map<String, GetSkuDetailsResponseItem> currentPurchasableItems;
    private Delegate delegate;
    private String itemBeingPurchased = null;
    private final String packageName;
    private PendingIntent pendingIntent;
    private boolean purchaseSupported = ENABLE_VERBOSE_LOGS;
    private ServiceConnection serviceConnection = null;
    private int transactionsInProgress = BILLING_RESPONSE_RESULT_OK;

    private class ConsumeItemTask extends AsyncTask<Void, Void, Integer> {
        private final IInAppBillingService billingService;
        private final String purchaseToken;

        public ConsumeItemTask(String purchaseToken) {
            this.purchaseToken = purchaseToken;
            this.billingService = GoogleInAppBillingProvider.this.billingService;
        }

        protected Integer doInBackground(Void... params) {
            Integer num = null;
            if (this.billingService != null) {
                try {
                    num = Integer.valueOf(this.billingService.consumePurchase(GoogleInAppBillingProvider.BILLING_SERVICE_VERSION, GoogleInAppBillingProvider.this.packageName, this.purchaseToken));
                } catch (RemoteException e) {
                }
            }
            return num;
        }

        protected void onPostExecute(Integer result) {
            if (result == null || result.intValue() != 0) {
                GoogleInAppBillingProvider.this.finalizePurchaseResult(PurchaseResult.FAILURE);
            } else {
                GoogleInAppBillingProvider.this.finalizePurchaseResult(PurchaseResult.SUCCESS);
            }
        }
    }

    private class GetSkuDetailsTask extends AsyncTask<Void, Void, Bundle> {
        private final IInAppBillingService billingService;
        private final Bundle requestBundle = new Bundle();

        public GetSkuDetailsTask(ArrayList<String> skuIds) {
            this.billingService = GoogleInAppBillingProvider.this.billingService;
            this.requestBundle.putStringArrayList(GoogleInAppBillingProvider.GET_SKU_DETAILS_ITEM_LIST, skuIds);
        }

        protected Bundle doInBackground(Void... params) {
            Bundle bundle = null;
            if (this.billingService != null) {
                try {
                    bundle = this.billingService.getSkuDetails(GoogleInAppBillingProvider.BILLING_SERVICE_VERSION, GoogleInAppBillingProvider.this.packageName, GoogleInAppBillingProvider.ITEM_TYPE_INAPP, this.requestBundle);
                } catch (RemoteException e) {
                }
            }
            return bundle;
        }

        protected void onPostExecute(Bundle result) {
            ArrayList<PurchasableItemDetails> purchasableItems = new ArrayList();
            GoogleInAppBillingProvider.this.currentPurchasableItems.clear();
            if (result != null && result.containsKey(GoogleInAppBillingProvider.RESPONSE_GET_SKU_DETAILS_LIST)) {
                Iterator it = result.getStringArrayList(GoogleInAppBillingProvider.RESPONSE_GET_SKU_DETAILS_LIST).iterator();
                while (it.hasNext()) {
                    GetSkuDetailsResponseItem jsonItem = GetSkuDetailsResponseItem.fromJson((String) it.next());
                    if (jsonItem != null) {
                        PurchasableItemDetails item = GetSkuDetailsResponseItem.toPurchasableItemDetails(jsonItem);
                        purchasableItems.add(item);
                        GoogleInAppBillingProvider.this.currentPurchasableItems.put(item.getItemId(), jsonItem);
                    }
                }
            }
            GoogleInAppBillingProvider.this.notifyPurchasableItemsResult(purchasableItems);
            new ProcessPurchasedItemsTask().execute(new Void[GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK]);
        }
    }

    static class Logger {
        private final String tag;

        public Logger(Class className) {
            this.tag = className.toString();
        }

        void warning(String format, Object... objects) {
        }

        void error(String format, Object... objects) {
        }

        void severe(String format, Object... objects) {
        }

        void dev(String format, Object... objects) {
        }

        void assertOnServiceThread(String message) {
            if (!ContextService.onServiceThread()) {
                severe(this.tag + ": Must be on the service thread: " + message, new Object[GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK]);
            }
        }
    }

    private class ProcessPurchasedItemsTask extends AsyncTask<Void, Void, Bundle> {
        private final IInAppBillingService billingService;

        public ProcessPurchasedItemsTask() {
            this.billingService = GoogleInAppBillingProvider.this.billingService;
        }

        protected Bundle doInBackground(Void... params) {
            if (this.billingService == null) {
                return null;
            }
            ArrayList<String> accumulatedPurchaseDataList = null;
            ArrayList<String> accumulatedSignatureList = null;
            String continuationToken = null;
            do {
                try {
                    Bundle ownedItems = this.billingService.getPurchases(GoogleInAppBillingProvider.BILLING_SERVICE_VERSION, GoogleInAppBillingProvider.this.packageName, GoogleInAppBillingProvider.ITEM_TYPE_INAPP, continuationToken);
                    int responseCode = GoogleInAppBillingProvider.getResponseCodeFromBundle(ownedItems);
                    ArrayList<String> purchaseDataList = ownedItems.getStringArrayList(GoogleInAppBillingProvider.RESPONSE_INAPP_PURCHASE_DATA_LIST);
                    ArrayList<String> signatureList = ownedItems.getStringArrayList(GoogleInAppBillingProvider.RESPONSE_INAPP_SIGNATURE_LIST);
                    if (responseCode != GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR && responseCode == 0) {
                        if (!ownedItems.containsKey(GoogleInAppBillingProvider.RESPONSE_INAPP_PURCHASE_DATA_LIST) || !ownedItems.containsKey(GoogleInAppBillingProvider.RESPONSE_INAPP_SIGNATURE_LIST) || purchaseDataList.size() != signatureList.size()) {
                            break;
                        }
                        if (accumulatedPurchaseDataList == null) {
                            accumulatedPurchaseDataList = purchaseDataList;
                            accumulatedSignatureList = signatureList;
                        } else {
                            accumulatedPurchaseDataList.addAll(purchaseDataList);
                            accumulatedSignatureList.addAll(signatureList);
                        }
                        continuationToken = ownedItems.getString(GoogleInAppBillingProvider.INAPP_CONTINUATION_TOKEN);
                        if (continuationToken == null) {
                            break;
                        }
                    } else {
                        break;
                    }
                } catch (RemoteException e) {
                }
            } while (continuationToken.length() != 0);
            if (accumulatedPurchaseDataList == null) {
                return null;
            }
            Bundle result = new Bundle();
            result.putStringArrayList(GoogleInAppBillingProvider.RESPONSE_INAPP_PURCHASE_DATA_LIST, accumulatedPurchaseDataList);
            result.putStringArrayList(GoogleInAppBillingProvider.RESPONSE_INAPP_SIGNATURE_LIST, accumulatedSignatureList);
            return result;
        }

        protected void onPostExecute(final Bundle result) {
            if (result != null) {
                ContextService.runOnServiceHandler(new Runnable() {
                    public void run() {
                        ArrayList<String> purchaseDataList = result.getStringArrayList(GoogleInAppBillingProvider.RESPONSE_INAPP_PURCHASE_DATA_LIST);
                        ArrayList<String> signatureList = result.getStringArrayList(GoogleInAppBillingProvider.RESPONSE_INAPP_SIGNATURE_LIST);
                        for (int i = GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK; i < purchaseDataList.size(); i += GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_USER_CANCELED) {
                            GoogleInAppBillingProvider.this.transactionsInProgress = GoogleInAppBillingProvider.this.transactionsInProgress + GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_USER_CANCELED;
                            GoogleInAppBillingProvider.this.processPurchaseResult(-1, GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK, (String) purchaseDataList.get(i), (String) signatureList.get(i));
                        }
                        GoogleInAppBillingProvider.this.finalizeConnectionResult();
                        GoogleInAppBillingProvider.this.maybeDisconnectBillingService();
                    }
                });
                return;
            }
            GoogleInAppBillingProvider.this.finalizeConnectionResult();
            GoogleInAppBillingProvider.this.maybeDisconnectBillingService();
        }
    }

    public static WeakReference<GoogleInAppBillingProvider> getInstance() {
        return instance;
    }

    public GoogleInAppBillingProvider(Context context) {
        String checkedPackageName = context.getPackageName();
        if (checkedPackageName.startsWith(PACKAGE_NAME_BASE)) {
            this.packageName = checkedPackageName;
        } else {
            this.packageName = "ERROR";
        }
        this.context = context;
        this.currentPurchasableItems = new HashMap();
        instance = new WeakReference(this);
        connectToBillingService();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void onResume() {
        this.clientConnected = true;
        connectToBillingService();
    }

    public void onPause() {
        this.clientConnected = ENABLE_VERBOSE_LOGS;
        maybeDisconnectBillingService();
    }

    public boolean isBillingAvailable() {
        return (this.billingService == null || !this.purchaseSupported) ? ENABLE_VERBOSE_LOGS : true;
    }

    public boolean isTransactionInProgress() {
        return this.transactionsInProgress > 0 ? true : ENABLE_VERBOSE_LOGS;
    }

    private void connectToBillingService() {
        if (!this.connectionInProgress) {
            if (this.billingService != null) {
                finalizeConnectionResult();
                return;
            }
            this.connectionInProgress = true;
            this.serviceConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName name, final IBinder service) {
                    ContextService.runOnServiceHandler(new Runnable() {
                        public void run() {
                            if (GoogleInAppBillingProvider.this.serviceConnection == null) {
                                GoogleInAppBillingProvider.this.finalizeConnectionResult();
                                return;
                            }
                            GoogleInAppBillingProvider.this.billingService = Stub.asInterface(service);
                            try {
                                GoogleInAppBillingProvider.this.purchaseSupported = GoogleInAppBillingProvider.this.billingService.isBillingSupported(GoogleInAppBillingProvider.BILLING_SERVICE_VERSION, GoogleInAppBillingProvider.this.packageName, GoogleInAppBillingProvider.ITEM_TYPE_INAPP) == 0 ? true : GoogleInAppBillingProvider.ENABLE_VERBOSE_LOGS;
                            } catch (RemoteException e) {
                                GoogleInAppBillingProvider.this.purchaseSupported = GoogleInAppBillingProvider.ENABLE_VERBOSE_LOGS;
                            }
                            if (GoogleInAppBillingProvider.this.currentPurchasableItems.size() > 0) {
                                new ProcessPurchasedItemsTask().execute(new Void[GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK]);
                            } else {
                                GoogleInAppBillingProvider.this.finalizeConnectionResult();
                            }
                        }
                    });
                }

                public void onServiceDisconnected(ComponentName name) {
                    ContextService.runOnServiceHandler(new Runnable() {
                        public void run() {
                            GoogleInAppBillingProvider.this.billingService = null;
                            GoogleInAppBillingProvider.this.finalizeConnectionResult();
                        }
                    });
                }
            };
            Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.android.vending");
            List<ResolveInfo> providerList = this.context.getPackageManager().queryIntentServices(serviceIntent, BILLING_RESPONSE_RESULT_OK);
            if (providerList == null || providerList.isEmpty()) {
                finalizeConnectionResult();
            }
            this.context.bindService(serviceIntent, this.serviceConnection, BILLING_RESPONSE_RESULT_USER_CANCELED);
        }
    }

    private void maybeDisconnectBillingService() {
        if (this.transactionsInProgress <= 0 && !this.connectionInProgress && !this.clientConnected) {
            if (this.serviceConnection != null) {
                this.context.unbindService(this.serviceConnection);
            }
            this.serviceConnection = null;
            this.billingService = null;
            this.transactionsInProgress = BILLING_RESPONSE_RESULT_OK;
        }
    }

    public void getPurchasableItems(ArrayList<String> itemIds) {
        if (isBillingAvailable()) {
            new GetSkuDetailsTask(itemIds).execute(new Void[BILLING_RESPONSE_RESULT_OK]);
        } else {
            notifyPurchasableItemsResult(Collections.emptyList());
        }
    }

    public void purchaseItem(String itemId, String developerPayload) {
        this.transactionsInProgress += BILLING_RESPONSE_RESULT_USER_CANCELED;
        if (!isBillingAvailable()) {
            finalizePurchaseResult(PurchaseResult.BILLING_UNAVAILABLE);
        } else if (this.currentPurchasableItems.keySet().contains(itemId)) {
            try {
                Bundle buyIntentBundle = this.billingService.getBuyIntent(BILLING_SERVICE_VERSION, this.packageName, itemId, ITEM_TYPE_INAPP, developerPayload);
                PendingIntent pendingIntent = (PendingIntent) buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
                if (!handlePurchaseErrorResult(getResponseCodeFromBundle(buyIntentBundle))) {
                    if (pendingIntent == null) {
                        finalizePurchaseResult(PurchaseResult.FAILURE);
                        return;
                    }
                    if (this.transactionsInProgress == BILLING_RESPONSE_RESULT_USER_CANCELED) {
                        this.itemBeingPurchased = itemId;
                    } else {
                        this.itemBeingPurchased = null;
                    }
                    launchPurchaseActivity(pendingIntent);
                }
            } catch (RemoteException e) {
                finalizePurchaseResult(PurchaseResult.FAILURE);
            }
        } else {
            finalizePurchaseResult(PurchaseResult.SKU_NOT_AVAILABLE);
        }
    }

    private void launchPurchaseActivity(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
        ContextService.runOnUiThread(new Runnable() {
            public void run() {
                GoogleInAppBillingProvider.this.context.startActivity(new Intent(GoogleInAppBillingProvider.this.context, PurchaseActivity.class));
            }
        });
    }

    public void startBuyIntent(Activity activity) {
        try {
            activity.startIntentSenderForResult(this.pendingIntent.getIntentSender(), PurchaseActivity.REQUEST_PURCHASE_RESULT, new Intent(), BILLING_RESPONSE_RESULT_OK, BILLING_RESPONSE_RESULT_OK, BILLING_RESPONSE_RESULT_OK);
        } catch (SendIntentException e) {
            this.itemBeingPurchased = null;
            this.pendingIntent = null;
            finalizePurchaseResult(PurchaseResult.FAILURE);
        }
    }

    public void forwardedOnActivityResult(int resultCode, Intent data) {
        int responseCode;
        String purchaseData;
        String dataSignature;
        if (data != null) {
            responseCode = getResponseCodeFromIntent(data);
            purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
            dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);
        } else {
            responseCode = BILLING_RESPONSE_RESULT_NOT_FOUND;
            purchaseData = null;
            dataSignature = null;
        }
        final int i = resultCode;
        ContextService.runOnServiceHandler(new Runnable() {
            public void run() {
                GoogleInAppBillingProvider.this.processPurchaseResult(i, responseCode, purchaseData, dataSignature);
            }
        });
    }

    private void processPurchaseResult(int activityResultCode, int responseCode, String purchaseData, String dataSignature) {
        String purchasedItem = this.itemBeingPurchased;
        this.itemBeingPurchased = null;
        if (this.billingService != null) {
            if (responseCode != BILLING_RESPONSE_RESULT_NOT_FOUND && handlePurchaseErrorResult(responseCode)) {
                return;
            }
            if (activityResultCode == 0) {
                finalizePurchaseResult(PurchaseResult.USER_CANCELLED);
            } else if (activityResultCode != -1) {
                finalizePurchaseResult(PurchaseResult.FAILURE);
            } else if (responseCode == BILLING_RESPONSE_RESULT_NOT_FOUND || purchaseData == null || dataSignature == null) {
                finalizePurchaseResult(PurchaseResult.FAILURE);
            } else {
                String currency = UNKNOWN_CURRENCY_STRING;
                String productId = null;
                int pricePaidE6 = BILLING_RESPONSE_RESULT_OK;
                if (purchasedItem != null) {
                    GetSkuDetailsResponseItem itemDetails = (GetSkuDetailsResponseItem) this.currentPurchasableItems.get(purchasedItem);
                    if (itemDetails != null) {
                        productId = itemDetails.getProductId();
                        currency = itemDetails.getCurrencyCode();
                        pricePaidE6 = itemDetails.getPriceE6();
                    }
                }
                if (productId == null) {
                    GoogleInAppPurchaseData parsedPurchaseData = GoogleInAppPurchaseData.fromJson(purchaseData);
                    if (parsedPurchaseData != null) {
                        productId = parsedPurchaseData.getProductId();
                    }
                    if (productId == null) {
                        productId = "unknown";
                    }
                }
                this.delegate.ProcessReceipt(purchaseData, dataSignature, currency, pricePaidE6);
            }
        }
    }

    public void onProcessedGoogleBillingTransaction(boolean success, String purchaseToken) {
        if (!success) {
            finalizePurchaseResult(PurchaseResult.FAILURE);
        } else if (this.billingService == null) {
            finalizePurchaseResult(PurchaseResult.FAILURE);
        } else if (purchaseToken == null) {
            finalizePurchaseResult(PurchaseResult.FAILURE);
        } else {
            new ConsumeItemTask(purchaseToken).execute(new Void[BILLING_RESPONSE_RESULT_OK]);
        }
    }

    private boolean handlePurchaseErrorResult(int resultCode) {
        switch (resultCode) {
            case BILLING_RESPONSE_RESULT_OK /*0*/:
                return ENABLE_VERBOSE_LOGS;
            case BILLING_RESPONSE_RESULT_USER_CANCELED /*1*/:
                finalizePurchaseResult(PurchaseResult.USER_CANCELLED);
                break;
            case BILLING_SERVICE_VERSION /*3*/:
                finalizePurchaseResult(PurchaseResult.BILLING_UNAVAILABLE);
                break;
            case BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE /*4*/:
                finalizePurchaseResult(PurchaseResult.SKU_NOT_AVAILABLE);
                break;
            case BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED /*7*/:
                new ProcessPurchasedItemsTask().execute(new Void[BILLING_RESPONSE_RESULT_OK]);
                finalizePurchaseResult(PurchaseResult.FAILURE);
                break;
            default:
                finalizePurchaseResult(PurchaseResult.FAILURE);
                break;
        }
        return true;
    }

    private void finalizeConnectionResult() {
        boolean z = ENABLE_VERBOSE_LOGS;
        this.connectionInProgress = ENABLE_VERBOSE_LOGS;
        if (this.delegate != null) {
            Delegate delegate = this.delegate;
            if (this.billingService != null) {
                z = true;
            }
            delegate.onConnectionStateChanged(z);
        }
    }

    private void notifyPurchasableItemsResult(Collection<PurchasableItemDetails> purchasableItems) {
        if (this.delegate != null) {
            this.delegate.purchasableItemsResult(purchasableItems);
        }
    }

    private void finalizePurchaseResult(PurchaseResult result) {
        this.transactionsInProgress--;
        maybeDisconnectBillingService();
        if (this.delegate != null) {
            this.delegate.purchaseResult(result);
        }
    }

    private static int getResponseCodeFromBundle(Bundle bundle) {
        return getResponseCodeFromObject(bundle.get(RESPONSE_CODE));
    }

    private static int getResponseCodeFromIntent(Intent intent) {
        return getResponseCodeFromObject(intent.getExtras().get(RESPONSE_CODE));
    }

    private static int getResponseCodeFromObject(Object responseObject) {
        if (responseObject == null) {
            return BILLING_RESPONSE_RESULT_OK;
        }
        if (responseObject instanceof Integer) {
            return ((Integer) responseObject).intValue();
        }
        if (responseObject instanceof Long) {
            return (int) ((Long) responseObject).longValue();
        }
        return BILLING_RESPONSE_RESULT_ERROR;
    }
}
