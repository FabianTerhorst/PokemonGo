package com.google.vr.cardboard;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.util.Log;
import com.voxelbusters.nativeplugins.defines.Keys.Scheme;

public class NFCUtils {
    private static final String TAG = NFCUtils.class.getSimpleName();
    Context context;
    NfcAdapter nfcAdapter;
    BroadcastReceiver nfcBroadcastReceiver;
    IntentFilter[] nfcIntentFilters;

    public void onCreate(Activity activity) {
        this.context = activity.getApplicationContext();
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this.context);
        this.nfcBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.i(NFCUtils.TAG, "Got an NFC tag!");
                NFCUtils.this.onNFCTagDetected((Tag) intent.getParcelableExtra("android.nfc.extra.TAG"));
            }
        };
        createNfcIntentFilter().addDataScheme("cardboard");
        IntentFilter shortenedIntent = createNfcIntentFilter();
        shortenedIntent.addDataScheme(Scheme.HTTP);
        shortenedIntent.addDataAuthority("goo.gl", null);
        IntentFilter explicitIntent = createNfcIntentFilter();
        explicitIntent.addDataScheme(Scheme.HTTP);
        explicitIntent.addDataAuthority("google.com", null);
        explicitIntent.addDataPath("/cardboard/cfg.*", 2);
        this.nfcIntentFilters = new IntentFilter[]{originalIntent, shortenedIntent, explicitIntent};
    }

    public void onResume(Activity activity) {
        activity.registerReceiver(this.nfcBroadcastReceiver, createNfcIntentFilter());
        Intent intent = new Intent("android.nfc.action.NDEF_DISCOVERED");
        intent.setPackage(activity.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, intent, 0);
        if (isNFCEnabled()) {
            this.nfcAdapter.enableForegroundDispatch(activity, pendingIntent, this.nfcIntentFilters, (String[][]) null);
        }
    }

    public void onPause(Activity activity) {
        if (isNFCEnabled()) {
            this.nfcAdapter.disableForegroundDispatch(activity);
        }
        activity.unregisterReceiver(this.nfcBroadcastReceiver);
    }

    protected boolean isNFCEnabled() {
        return this.nfcAdapter != null && this.nfcAdapter.isEnabled();
    }

    protected void onNFCTagDetected(Tag tag) {
    }

    private IntentFilter createNfcIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.nfc.action.NDEF_DISCOVERED");
        intentFilter.addAction("android.nfc.action.TECH_DISCOVERED");
        intentFilter.addAction("android.nfc.action.TAG_DISCOVERED");
        return intentFilter;
    }
}
