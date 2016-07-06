package com.voxelbusters.nativeplugins.features.sharing;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import com.voxelbusters.nativeplugins.NativePluginHelper;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import com.voxelbusters.nativeplugins.defines.Enums.eShareOptions;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Mime;
import com.voxelbusters.nativeplugins.defines.Keys.Package;
import com.voxelbusters.nativeplugins.defines.Keys.Sharing;
import com.voxelbusters.nativeplugins.defines.UnityDefines;
import com.voxelbusters.nativeplugins.utilities.Debug;
import com.voxelbusters.nativeplugins.utilities.StringUtility;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import spacemadness.com.lunarconsole.BuildConfig;

public class SharingActivity extends Activity {
    final int SEND_MAIL_REQUEST_CODE = 2;
    final int SEND_SMS_REQUEST_CODE = 3;
    final int SHARE_ON_WHATS_APP_REQUEST_CODE = 4;
    final int SHARING_REQUEST_CODE = 1;
    Bundle bundleInfo;
    File currentImageFileShared = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.bundleInfo == null) {
            this.bundleInfo = getIntent().getExtras();
        }
        String type = this.bundleInfo.getString(Keys.TYPE);
        if (StringUtility.isNullOrEmpty(type)) {
            shareItem(this.bundleInfo.getString(Keys.MESSAGE), this.bundleInfo.getString(Keys.URL), this.bundleInfo.getString(Keys.IMAGE_PATH), type, this.bundleInfo.getStringArray(Keys.EXCLUDE_LIST));
        } else if (type.equals(Sharing.SMS)) {
            shareWithSMS(this.bundleInfo);
        } else if (type.equals(Sharing.MAIL)) {
            shareWithEmail(this.bundleInfo);
        } else if (type.equals(Sharing.WHATS_APP)) {
            shareOnWhatsApp(this.bundleInfo);
        } else {
            Debug.log(CommonDefines.SHARING_TAG, "Sharing not implemented for this type " + type);
        }
    }

    private void shareItem(String message, String urlString, String imagePath, String type, String[] excludedShareOptions) {
        Context context = NativePluginHelper.getCurrentContext();
        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType(getMimeType(type, !StringUtility.isNullOrEmpty(imagePath)));
        if (StringUtility.isNullOrEmpty(urlString)) {
            shareIntent.putExtra("android.intent.extra.TEXT", message);
        } else {
            shareIntent.putExtra("android.intent.extra.TEXT", new StringBuilder(String.valueOf(message)).append("\n").append(urlString).toString());
        }
        if (!StringUtility.isNullOrEmpty(imagePath)) {
            shareIntent.putExtra("android.intent.extra.STREAM", Uri.parse(imagePath));
        }
        shareIntent.addCategory("android.intent.category.DEFAULT");
        boolean failedSharing = false;
        boolean needsOnlySocialNetworkingServices = false;
        if (excludedShareOptions != null && StringUtility.contains(eShareOptions.MESSAGE.ordinal(), excludedShareOptions) && StringUtility.contains(eShareOptions.MAIL.ordinal(), excludedShareOptions) && StringUtility.contains(eShareOptions.WHATSAPP.ordinal(), excludedShareOptions)) {
            needsOnlySocialNetworkingServices = true;
        }
        List<ResolveInfo> availableActivitiesResInfo = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        if (availableActivitiesResInfo.isEmpty()) {
            failedSharing = true;
        } else {
            List<Intent> targetedIntents = new ArrayList();
            for (ResolveInfo resolveInfo : availableActivitiesResInfo) {
                String packageName = resolveInfo.activityInfo != null ? resolveInfo.activityInfo.packageName : null;
                if (!(packageName == null || SharingHelper.checkIfPackageMatchesShareOptions(packageName, excludedShareOptions))) {
                    if (!needsOnlySocialNetworkingServices || SharingHelper.isSocialNetwork(packageName)) {
                        Intent intent = new Intent(shareIntent);
                        intent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
                        intent.setPackage(packageName);
                        targetedIntents.add(intent);
                    }
                }
            }
            if (targetedIntents.isEmpty()) {
                failedSharing = true;
            } else {
                Intent chooserIntent = Intent.createChooser((Intent) targetedIntents.remove(0), "Share via");
                chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) targetedIntents.toArray(new Parcelable[0]));
                chooserIntent.addFlags(1);
                startActivityForResult(chooserIntent, 1);
            }
        }
        if (failedSharing) {
            Builder alertDialogBuilder = new Builder(this).setTitle("Share").setMessage("No services found!").setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    NativePluginHelper.sendMessage(UnityDefines.Sharing.FINISHED, Sharing.FAILED);
                    SharingActivity.this.finish();
                }
            });
            alertDialogBuilder.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == 4) {
                        NativePluginHelper.sendMessage(UnityDefines.Sharing.FINISHED, Sharing.FAILED);
                        SharingActivity.this.finish();
                    }
                    return true;
                }
            });
            alertDialogBuilder.create().show();
        }
    }

    void shareWithEmail(Bundle info) {
        int i = 0;
        CharSequence body = info.getCharSequence(Keys.BODY);
        String subject = info.getString(Keys.SUBJECT);
        String[] toRecipients = info.getStringArray(Sharing.TO_RECIPIENT_LIST);
        String[] ccRecipients = info.getStringArray(Sharing.CC_RECIPIENT_LIST);
        String[] bccRecipients = info.getStringArray(Sharing.BCC_RECIPIENT_LIST);
        String[] attachmentPaths = info.getStringArray(Keys.ATTACHMENT);
        String mimeType = getMimeType(Sharing.MAIL, false);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(mimeType);
        intent.putExtra("android.intent.extra.TEXT", body);
        intent.putExtra("android.intent.extra.SUBJECT", subject);
        intent.putExtra("android.intent.extra.EMAIL", toRecipients);
        intent.putExtra("android.intent.extra.CC", ccRecipients);
        intent.putExtra("android.intent.extra.BCC", bccRecipients);
        if (attachmentPaths != null) {
            intent.setAction("android.intent.action.SEND_MULTIPLE");
            ArrayList<Uri> uris = new ArrayList();
            int length = attachmentPaths.length;
            while (i < length) {
                uris.add(Uri.parse(attachmentPaths[i]));
                i++;
            }
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", uris);
        }
        startActivityForResult(intent, 2);
    }

    void shareWithSMS(Bundle info) {
        String message = info.getString(Keys.MESSAGE);
        String[] recipientsList = info.getStringArray(Sharing.TO_RECIPIENT_LIST);
        String recipientsListStr = BuildConfig.FLAVOR;
        if (recipientsList != null) {
            for (String each : recipientsList) {
                recipientsListStr = new StringBuilder(String.valueOf(recipientsListStr)).append(each).append(";").toString();
            }
        }
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse(new StringBuilder(Keys.Intent.SCHEME_SEND_TO).append(recipientsListStr).toString()));
        intent.putExtra(Keys.Intent.SMS_BODY, message);
        startActivityForResult(intent, 3);
    }

    void shareOnWhatsApp(Bundle info) {
        String message = info.getString(Keys.MESSAGE);
        String imagePath = info.getString(Keys.IMAGE_PATH);
        String mimeType = getMimeType(Sharing.WHATS_APP, !StringUtility.isNullOrEmpty(imagePath));
        Intent intent = new Intent("android.intent.action.SEND");
        if (message != null) {
            intent.putExtra("android.intent.extra.TEXT", message);
        }
        if (imagePath != null) {
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(imagePath));
        }
        intent.setType(mimeType);
        intent.setPackage(Package.WHATS_APP);
        startActivityForResult(intent, 4);
    }

    String getMimeType(String type, boolean hasImage) {
        String mimeType = Mime.PLAIN_TEXT;
        if (hasImage) {
            mimeType = Mime.IMAGE_ALL;
        }
        if (Sharing.MAIL.equals(type)) {
            return Mime.EMAIL;
        }
        return mimeType;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            NativePluginHelper.sendMessage(UnityDefines.Sharing.FINISHED, Sharing.CLOSED);
        } else if (requestCode == 2) {
            NativePluginHelper.sendMessage(UnityDefines.Sharing.SENT_MAIL, Sharing.CLOSED);
        } else if (requestCode == 3) {
            NativePluginHelper.sendMessage(UnityDefines.Sharing.SENT_SMS, Sharing.CLOSED);
        } else if (requestCode == 4) {
            NativePluginHelper.sendMessage(UnityDefines.Sharing.WHATSAPP_SHARE_FINISHED, Sharing.CLOSED);
        }
        finish();
    }
}
