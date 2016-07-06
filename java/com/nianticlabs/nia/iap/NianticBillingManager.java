package com.nianticlabs.nia.iap;

import android.content.Context;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.iap.InAppBillingProvider.Delegate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NianticBillingManager extends ContextService implements Delegate {
    private InAppBillingProvider inAppBillingProvider;
    private boolean initializing;

    private native void nativeInitializeCallback();

    private native void nativeOnConnectionStateChanged(boolean z);

    private native void nativeProcessReceipt(String str, String str2, String str3, int i);

    private native void nativePurchasableItemsResult(PurchasableItemDetails[] purchasableItemDetailsArr);

    private native void nativePurchaseResult(int i);

    private native void nativeRecordPurchase(boolean z, String str, int i, float f, String str2, String str3);

    public NianticBillingManager(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.inAppBillingProvider = new GoogleInAppBillingProvider(context);
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onPause() {
        this.inAppBillingProvider.onPause();
    }

    public void onResume() {
        this.inAppBillingProvider.onResume();
    }

    public boolean isBillingAvailable() {
        return this.inAppBillingProvider.isBillingAvailable();
    }

    public void initialize() {
        this.initializing = true;
        this.inAppBillingProvider.setDelegate(this);
        ContextService.runOnServiceHandler(new Runnable() {
            public void run() {
                NianticBillingManager.this.nativeInitializeCallback();
            }
        });
    }

    public boolean isTransactionInProgress() {
        return this.inAppBillingProvider.isTransactionInProgress();
    }

    public void getPurchasableItems(String[] purchasableItems) {
        final ArrayList<String> items = new ArrayList(Arrays.asList(purchasableItems));
        ContextService.runOnServiceHandler(new Runnable() {
            public void run() {
                NianticBillingManager.this.inAppBillingProvider.getPurchasableItems(items);
            }
        });
    }

    public void purchaseVendorItem(final String item, final String userIdToken) {
        ContextService.runOnServiceHandler(new Runnable() {
            public void run() {
                NianticBillingManager.this.inAppBillingProvider.purchaseItem(item, userIdToken);
            }
        });
    }

    public void redeemReceiptResult(final boolean success, final String transactionToken) {
        ContextService.runOnServiceHandler(new Runnable() {
            public void run() {
                NianticBillingManager.this.inAppBillingProvider.onProcessedGoogleBillingTransaction(success, transactionToken);
            }
        });
    }

    public void onConnectionStateChanged(final boolean connected) {
        ContextService.runOnServiceHandler(new Runnable() {
            public void run() {
                synchronized (NianticBillingManager.this.callbackLock) {
                    NianticBillingManager.this.nativeOnConnectionStateChanged(connected);
                }
            }
        });
    }

    public void purchasableItemsResult(Collection<PurchasableItemDetails> items) {
        PurchasableItemDetails[] item_array = new PurchasableItemDetails[items.size()];
        int index = 0;
        for (PurchasableItemDetails item : items) {
            int index2 = index + 1;
            item_array[index] = item;
            index = index2;
        }
        synchronized (this.callbackLock) {
            nativePurchasableItemsResult(item_array);
        }
    }

    public void purchaseResult(PurchaseResult result) {
        synchronized (this.callbackLock) {
            nativePurchaseResult(result.ordinal());
        }
    }

    public void ProcessReceipt(String receiptBase64, String receiptSignature, String currency, int priceE6) {
        synchronized (this.callbackLock) {
            nativeProcessReceipt(receiptBase64, receiptSignature, currency, priceE6);
        }
    }
}
