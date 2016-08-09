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
    public android.app.Dialog onCreateDialog(android.os.Bundle r24) {
        /*
        r23 = this;
        r18 = r23.getActivity();
        r17 = com.upsight.android.Upsight.createContext(r18);
        r18 = "com.upsight.extension.marketing";
        r18 = r17.getUpsightExtension(r18);
        r18 = (com.upsight.android.UpsightMarketingExtension) r18;
        r18 = (com.upsight.android.UpsightMarketingExtension) r18;
        r18 = r18.getComponent();
        r18 = (com.upsight.android.marketing.UpsightMarketingComponent) r18;
        r0 = r18;
        r1 = r23;
        r0.inject(r1);
        r2 = r23.getArguments();
        r18 = "id";
        r0 = r18;
        r12 = r2.getString(r0);
        r18 = "title";
        r0 = r18;
        r16 = r2.getString(r0);
        r18 = "message";
        r0 = r18;
        r14 = r2.getString(r0);
        r18 = "buttons";
        r0 = r18;
        r7 = r2.getString(r0);
        r18 = "dialogTheme";
        r0 = r18;
        r18 = r2.containsKey(r0);
        if (r18 == 0) goto L_0x00b1;
    L_0x004d:
        r18 = "dialogTheme";
        r0 = r18;
        r9 = r2.getInt(r0);
        r3 = new android.app.AlertDialog$Builder;
        r18 = r23.getActivity();
        r0 = r18;
        r3.<init>(r0, r9);
    L_0x0060:
        r0 = r16;
        r18 = r3.setTitle(r0);
        r0 = r18;
        r0.setMessage(r14);
        r18 = android.text.TextUtils.isEmpty(r7);
        if (r18 != 0) goto L_0x00ac;
    L_0x0071:
        r0 = r23;
        r0 = r0.mUpsight;
        r18 = r0;
        r18 = r18.getCoreComponent();
        r11 = r18.gson();
        r0 = r23;
        r0 = r0.mUpsight;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r0;
        r18 = r18.getCoreComponent();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r18.jsonParser();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r8 = r0.parse(r7);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r8.isJsonArray();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        if (r18 == 0) goto L_0x0225;
    L_0x0099:
        r18 = r8.getAsJsonArray();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r15 = r18.size();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r8.getAsJsonArray();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r13 = r18.iterator();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        switch(r15) {
            case 1: goto L_0x00bd;
            case 2: goto L_0x0113;
            case 3: goto L_0x0181;
            default: goto L_0x00ac;
        };
    L_0x00ac:
        r18 = r3.create();
        return r18;
    L_0x00b1:
        r3 = new android.app.AlertDialog$Builder;
        r18 = r23.getActivity();
        r0 = r18;
        r3.<init>(r0);
        goto L_0x0060;
    L_0x00bd:
        r18 = r13.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        if (r18 == 0) goto L_0x00ac;
    L_0x00c3:
        r4 = r13.next();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "text";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r5 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "trigger";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r6 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$1;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r1 = r23;
        r0.<init>(r12, r6);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r3.setNeutralButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        goto L_0x00ac;
    L_0x00f4:
        r10 = move-exception;
        r0 = r23;
        r0 = r0.mUpsight;
        r18 = r0;
        r18 = r18.getLogger();
        r19 = LOG_TAG;
        r20 = "Failed to parse button due to malformed JSON";
        r21 = 1;
        r0 = r21;
        r0 = new java.lang.Object[r0];
        r21 = r0;
        r22 = 0;
        r21[r22] = r10;
        r18.e(r19, r20, r21);
        goto L_0x00ac;
    L_0x0113:
        r18 = r13.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        if (r18 == 0) goto L_0x0149;
    L_0x0119:
        r4 = r13.next();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "text";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r5 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "trigger";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r6 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$2;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r1 = r23;
        r0.<init>(r12, r6);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r3.setNegativeButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f4 }
    L_0x0149:
        r18 = r13.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        if (r18 == 0) goto L_0x00ac;
    L_0x014f:
        r4 = r13.next();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "text";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r5 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "trigger";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r6 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$3;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r1 = r23;
        r0.<init>(r12, r6);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r3.setPositiveButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        goto L_0x00ac;
    L_0x0181:
        r18 = r13.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        if (r18 == 0) goto L_0x01b7;
    L_0x0187:
        r4 = r13.next();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "text";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r5 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "trigger";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r6 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$4;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r1 = r23;
        r0.<init>(r12, r6);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r3.setNegativeButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f4 }
    L_0x01b7:
        r18 = r13.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        if (r18 == 0) goto L_0x01ed;
    L_0x01bd:
        r4 = r13.next();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "text";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r5 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "trigger";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r6 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$5;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r1 = r23;
        r0.<init>(r12, r6);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r3.setNeutralButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f4 }
    L_0x01ed:
        r18 = r13.hasNext();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        if (r18 == 0) goto L_0x00ac;
    L_0x01f3:
        r4 = r13.next();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r4 = (com.google.gson.JsonElement) r4;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "text";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r5 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r4.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = "trigger";
        r18 = r18.get(r19);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r6 = r18.getAsString();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$6;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r1 = r23;
        r0.<init>(r12, r6);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r0 = r18;
        r3.setPositiveButton(r5, r0);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        goto L_0x00ac;
    L_0x0225:
        r0 = r23;
        r0 = r0.mUpsight;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r18 = r0;
        r18 = r18.getLogger();	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r19 = LOG_TAG;	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r20 = "Failed to parse buttons because expected buttons array is missing";
        r21 = 0;
        r0 = r21;
        r0 = new java.lang.Object[r0];	 Catch:{ JsonSyntaxException -> 0x00f4 }
        r21 = r0;
        r18.e(r19, r20, r21);	 Catch:{ JsonSyntaxException -> 0x00f4 }
        goto L_0x00ac;
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
