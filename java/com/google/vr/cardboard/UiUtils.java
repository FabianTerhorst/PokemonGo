package com.google.vr.cardboard;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class UiUtils {
    private static final String CARDBOARD_CONFIGURE_ACTION = "com.google.vrtoolkit.cardboard.CONFIGURE";
    private static final String CARDBOARD_WEBSITE = "http://google.com/cardboard/cfg";
    private static final String INTENT_KEY = "intent";

    public static class ConfigureSettingsDialogFragment extends DialogFragment {
        private Intent intent;
        private final OnClickListener listener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    ConfigureSettingsDialogFragment.this.getActivity().startActivity(ConfigureSettingsDialogFragment.this.intent);
                } catch (ActivityNotFoundException e) {
                    UiUtils.showInstallDialog(ConfigureSettingsDialogFragment.this.getActivity());
                }
            }
        };

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.intent = (Intent) getArguments().getParcelable(UiUtils.INTENT_KEY);
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Builder builder = new Builder(getActivity());
            builder.setTitle(Strings.getString(Strings.DIALOG_TITLE)).setMessage(Strings.getString(Strings.DIALOG_MESSAGE_SETUP)).setPositiveButton(Strings.getString(Strings.SETUP_BUTTON), this.listener).setNegativeButton(Strings.getString(Strings.CANCEL_BUTTON), null);
            return builder.create();
        }
    }

    public static class InstallSettingsDialogFragment extends DialogFragment {
        private final OnClickListener listener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    InstallSettingsDialogFragment.this.getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(UiUtils.CARDBOARD_WEBSITE)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(InstallSettingsDialogFragment.this.getActivity().getApplicationContext(), Strings.getString(Strings.NO_BROWSER_TEXT), 1).show();
                }
            }
        };

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Builder builder = new Builder(getActivity());
            builder.setTitle(Strings.getString(Strings.DIALOG_TITLE)).setMessage(Strings.getString(Strings.DIALOG_MESSAGE_NO_CARDBOARD)).setPositiveButton(Strings.getString(Strings.GO_TO_PLAYSTORE_BUTTON), this.listener).setNegativeButton(Strings.getString(Strings.CANCEL_BUTTON), null);
            return builder.create();
        }
    }

    public static void launchOrInstallCardboard(Context context, boolean confirm) {
        PackageManager pm = context.getPackageManager();
        Intent settingsIntent = new Intent();
        settingsIntent.setAction(CARDBOARD_CONFIGURE_ACTION);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(settingsIntent, 0);
        List<Intent> intentsToGoogleCardboard = new ArrayList();
        for (ResolveInfo info : resolveInfos) {
            String pkgName = info.activityInfo.packageName;
            if (pkgName.startsWith("com.google.")) {
                Intent intent = new Intent(settingsIntent);
                intent.setClassName(pkgName, info.activityInfo.name);
                intentsToGoogleCardboard.add(intent);
            }
        }
        if (intentsToGoogleCardboard.isEmpty()) {
            showInstallDialog(context);
            return;
        }
        if (intentsToGoogleCardboard.size() == 1) {
            intent = (Intent) intentsToGoogleCardboard.get(0);
        } else {
            intent = settingsIntent;
        }
        if (confirm) {
            showConfigureDialog(context, intent);
        } else {
            context.startActivity(intent);
        }
    }

    static void launchOrInstallCardboard(Context context) {
        launchOrInstallCardboard(context, true);
    }

    private static void showInstallDialog(Context context) {
        new InstallSettingsDialogFragment().show(((Activity) context).getFragmentManager(), "InstallCardboardDialog");
    }

    private static void showConfigureDialog(Context context, Intent intent) {
        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        DialogFragment dialog = new ConfigureSettingsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(INTENT_KEY, intent);
        dialog.setArguments(bundle);
        dialog.show(fragmentManager, "ConfigureCardboardDialog");
    }
}
