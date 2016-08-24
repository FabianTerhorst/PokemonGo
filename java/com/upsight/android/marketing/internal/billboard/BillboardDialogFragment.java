package com.upsight.android.marketing.internal.billboard;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContent.PendingDialog;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import javax.inject.Inject;

public final class BillboardDialogFragment extends DialogFragment {
    private static final String FRAGMENT_BUNDLE_KEY_BUTTONS = "buttons";
    private static final String FRAGMENT_BUNDLE_KEY_DIALOG_THEME = "dialogTheme";
    private static final String FRAGMENT_BUNDLE_KEY_DISMISS_TRIGGER = "dismissTrigger";
    private static final String FRAGMENT_BUNDLE_KEY_ID = "id";
    private static final String FRAGMENT_BUNDLE_KEY_MESSAGE = "message";
    private static final String FRAGMENT_BUNDLE_KEY_TITLE = "title";
    private static final String LOG_TAG = BillboardDialogFragment.class.getSimpleName();
    @Inject
    MarketingContentStore mContentStore;
    @Inject
    UpsightContext mUpsight;

    public static DialogFragment newInstance(PendingDialog pendingDialog) {
        DialogFragment fragment = new BillboardDialogFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_BUNDLE_KEY_ID, pendingDialog.mId);
        args.putString(FRAGMENT_BUNDLE_KEY_TITLE, pendingDialog.mTitle);
        args.putString(FRAGMENT_BUNDLE_KEY_MESSAGE, pendingDialog.mMessage);
        args.putString(FRAGMENT_BUNDLE_KEY_BUTTONS, pendingDialog.mButtons);
        args.putString(FRAGMENT_BUNDLE_KEY_DISMISS_TRIGGER, pendingDialog.mDismissTrigger);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogFragment newInstance(PendingDialog pendingDialog, int dialogTheme) {
        DialogFragment fragment = new BillboardDialogFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_BUNDLE_KEY_ID, pendingDialog.mId);
        args.putString(FRAGMENT_BUNDLE_KEY_TITLE, pendingDialog.mTitle);
        args.putString(FRAGMENT_BUNDLE_KEY_MESSAGE, pendingDialog.mMessage);
        args.putString(FRAGMENT_BUNDLE_KEY_BUTTONS, pendingDialog.mButtons);
        args.putString(FRAGMENT_BUNDLE_KEY_DISMISS_TRIGGER, pendingDialog.mDismissTrigger);
        args.putInt(FRAGMENT_BUNDLE_KEY_DIALOG_THEME, dialogTheme);
        fragment.setArguments(args);
        return fragment;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.app.Dialog onCreateDialog(android.os.Bundle r25) {
        /*
        r24 = this;
        r19 = r24.getActivity();
        r18 = com.upsight.android.Upsight.createContext(r19);
        r19 = "com.upsight.extension.marketing";
        r11 = r18.getUpsightExtension(r19);
        r11 = (com.upsight.android.UpsightMarketingExtension) r11;
        if (r11 != 0) goto L_0x0015;
    L_0x0012:
        r19 = 0;
    L_0x0014:
        return r19;
    L_0x0015:
        r19 = r11.getComponent();
        r19 = (com.upsight.android.marketing.UpsightMarketingComponent) r19;
        r0 = r19;
        r1 = r24;
        r0.inject(r1);
        r2 = r24.getArguments();
        r19 = "id";
        r0 = r19;
        r13 = r2.getString(r0);
        r19 = "title";
        r0 = r19;
        r17 = r2.getString(r0);
        r19 = "message";
        r0 = r19;
        r15 = r2.getString(r0);
        r19 = "buttons";
        r0 = r19;
        r7 = r2.getString(r0);
        r19 = "dialogTheme";
        r0 = r19;
        r19 = r2.containsKey(r0);
        if (r19 == 0) goto L_0x00b5;
    L_0x0050:
        r19 = "dialogTheme";
        r0 = r19;
        r9 = r2.getInt(r0);
        r3 = new android.app.AlertDialog$Builder;
        r19 = r24.getActivity();
        r0 = r19;
        r3.<init>(r0, r9);
    L_0x0063:
        r0 = r17;
        r19 = r3.setTitle(r0);
        r0 = r19;
        r0.setMessage(r15);
        r19 = android.text.TextUtils.isEmpty(r7);
        if (r19 != 0) goto L_0x00af;
    L_0x0074:
        r0 = r24;
        r0 = r0.mUpsight;
        r19 = r0;
        r19 = r19.getCoreComponent();
        r12 = r19.gson();
        r0 = r24;
        r0 = r0.mUpsight;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r0;
        r19 = r19.getCoreComponent();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r19.jsonParser();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r8 = r0.parse(r7);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r8.isJsonArray();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        if (r19 == 0) goto L_0x0229;
    L_0x009c:
        r19 = r8.getAsJsonArray();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r16 = r19.size();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r8.getAsJsonArray();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r14 = r19.iterator();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        switch(r16) {
            case 1: goto L_0x00c1;
            case 2: goto L_0x0117;
            case 3: goto L_0x0185;
            default: goto L_0x00af;
        };
    L_0x00af:
        r19 = r3.create();
        goto L_0x0014;
    L_0x00b5:
        r3 = new android.app.AlertDialog$Builder;
        r19 = r24.getActivity();
        r0 = r19;
        r3.<init>(r0);
        goto L_0x0063;
    L_0x00c1:
        r19 = r14.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        if (r19 == 0) goto L_0x00af;
    L_0x00c7:
        r4 = r14.next();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "text";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r5 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "trigger";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r6 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$1;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r1 = r24;
        r0.<init>(r13, r6);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r3.setNeutralButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        goto L_0x00af;
    L_0x00f8:
        r10 = move-exception;
        r0 = r24;
        r0 = r0.mUpsight;
        r19 = r0;
        r19 = r19.getLogger();
        r20 = LOG_TAG;
        r21 = "Failed to parse button due to malformed JSON";
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r22[r23] = r10;
        r19.e(r20, r21, r22);
        goto L_0x00af;
    L_0x0117:
        r19 = r14.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        if (r19 == 0) goto L_0x014d;
    L_0x011d:
        r4 = r14.next();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "text";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r5 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "trigger";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r6 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$2;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r1 = r24;
        r0.<init>(r13, r6);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r3.setNegativeButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f8 }
    L_0x014d:
        r19 = r14.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        if (r19 == 0) goto L_0x00af;
    L_0x0153:
        r4 = r14.next();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "text";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r5 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "trigger";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r6 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$3;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r1 = r24;
        r0.<init>(r13, r6);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r3.setPositiveButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        goto L_0x00af;
    L_0x0185:
        r19 = r14.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        if (r19 == 0) goto L_0x01bb;
    L_0x018b:
        r4 = r14.next();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "text";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r5 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "trigger";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r6 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$4;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r1 = r24;
        r0.<init>(r13, r6);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r3.setNegativeButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f8 }
    L_0x01bb:
        r19 = r14.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        if (r19 == 0) goto L_0x01f1;
    L_0x01c1:
        r4 = r14.next();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "text";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r5 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "trigger";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r6 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$5;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r1 = r24;
        r0.<init>(r13, r6);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r3.setNeutralButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f8 }
    L_0x01f1:
        r19 = r14.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        if (r19 == 0) goto L_0x00af;
    L_0x01f7:
        r4 = r14.next();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "text";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r5 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = "trigger";
        r19 = r19.get(r20);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r6 = r19.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$6;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r1 = r24;
        r0.<init>(r13, r6);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r0 = r19;
        r3.setPositiveButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        goto L_0x00af;
    L_0x0229:
        r0 = r24;
        r0 = r0.mUpsight;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r19 = r0;
        r19 = r19.getLogger();	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r20 = LOG_TAG;	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r21 = "Failed to parse buttons because expected buttons array is missing";
        r22 = 0;
        r0 = r22;
        r0 = new java.lang.Object[r0];	 Catch:{ JsonSyntaxException -> 0x00f8 }
        r22 = r0;
        r19.e(r20, r21, r22);	 Catch:{ JsonSyntaxException -> 0x00f8 }
        goto L_0x00af;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.marketing.internal.billboard.BillboardDialogFragment.onCreateDialog(android.os.Bundle):android.app.Dialog");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Bundle args = getArguments();
        executeActions(args.getString(FRAGMENT_BUNDLE_KEY_ID), args.getString(FRAGMENT_BUNDLE_KEY_DISMISS_TRIGGER));
    }

    private void executeActions(String id, String trigger) {
        MarketingContent content = (MarketingContent) this.mContentStore.get(id);
        if (content != null) {
            content.executeActions(trigger);
        }
    }
}
