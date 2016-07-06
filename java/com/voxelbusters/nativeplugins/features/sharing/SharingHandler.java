package com.voxelbusters.nativeplugins.features.sharing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import com.voxelbusters.nativeplugins.NativePluginHelper;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import com.voxelbusters.nativeplugins.defines.Enums.eShareOptions;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Sharing;
import com.voxelbusters.nativeplugins.utilities.FileUtility;
import com.voxelbusters.nativeplugins.utilities.StringUtility;
import spacemadness.com.lunarconsole.BuildConfig;

public class SharingHandler {
    private static SharingHandler INSTANCE;

    public enum eShareCategeories {
        UNDEFINED,
        TEXT
    }

    public static SharingHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharingHandler();
        }
        return INSTANCE;
    }

    private SharingHandler() {
    }

    public boolean isServiceAvailable(int serviceTypeInt) {
        return SharingHelper.isServiceAvailable(getContext(), eShareOptions.values()[serviceTypeInt]);
    }

    public void share(String message, String urlString, byte[] imageByteArray, int byteArrayLength, String excludedShareOptionsJson) {
        String[] excludedShareOptions = StringUtility.convertJsonStringToStringArray(excludedShareOptionsJson);
        Bundle bundle = new Bundle();
        bundle.putString(Keys.TYPE, BuildConfig.FLAVOR);
        bundle.putString(Keys.MESSAGE, message);
        bundle.putString(Keys.URL, urlString);
        Uri imageUri = FileUtility.createSharingFileUri(getContext(), imageByteArray, byteArrayLength, CommonDefines.SHARING_DIR, System.currentTimeMillis() + ".png");
        if (imageUri != null) {
            bundle.putString(Keys.IMAGE_PATH, imageUri.toString());
        }
        bundle.putStringArray(Keys.EXCLUDE_LIST, excludedShareOptions);
        startActivity(bundle);
    }

    public void sendSms(String messageBody, String recipientsListJson) {
        Bundle bundle = new Bundle();
        bundle.putString(Keys.TYPE, Sharing.SMS);
        bundle.putString(Keys.MESSAGE, messageBody);
        bundle.putStringArray(Sharing.TO_RECIPIENT_LIST, StringUtility.convertJsonStringToStringArray(recipientsListJson));
        startActivity(bundle);
    }

    public void startActivity(Bundle bundleInfo) {
        Intent intent = new Intent(NativePluginHelper.getCurrentContext(), SharingActivity.class);
        intent.putExtras(bundleInfo);
        NativePluginHelper.startActivityOnUiThread(intent);
    }

    public void sendMail(String subject, String body, boolean isHtmlBody, byte[] attachmentByteArray, int attachmentByteArrayLength, String mimeType, String attachmentFileNameWithExtn, String toRecipientsJson, String ccRecipientsJson, String bccRecipientsJson) {
        Bundle bundle = new Bundle();
        bundle.putString(Keys.TYPE, Sharing.MAIL);
        bundle.putString(Keys.SUBJECT, subject);
        if (StringUtility.isNullOrEmpty(body)) {
            body = BuildConfig.FLAVOR;
        }
        CharSequence messageBody = body;
        if (isHtmlBody) {
            messageBody = Html.fromHtml(body);
        }
        bundle.putCharSequence(Keys.BODY, messageBody);
        if (!(attachmentByteArrayLength == 0 || FileUtility.createSharingFileUri(getContext(), attachmentByteArray, attachmentByteArrayLength, CommonDefines.SHARING_DIR, attachmentFileNameWithExtn) == null)) {
            bundle.putStringArray(Keys.ATTACHMENT, new String[]{FileUtility.createSharingFileUri(getContext(), attachmentByteArray, attachmentByteArrayLength, CommonDefines.SHARING_DIR, attachmentFileNameWithExtn).toString()});
        }
        String[] toRecipients = StringUtility.convertJsonStringToStringArray(toRecipientsJson);
        String[] ccRecipients = StringUtility.convertJsonStringToStringArray(ccRecipientsJson);
        String[] bccRecipients = StringUtility.convertJsonStringToStringArray(bccRecipientsJson);
        bundle.putStringArray(Sharing.TO_RECIPIENT_LIST, toRecipients);
        bundle.putStringArray(Sharing.CC_RECIPIENT_LIST, ccRecipients);
        bundle.putStringArray(Sharing.BCC_RECIPIENT_LIST, bccRecipients);
        startActivity(bundle);
    }

    public void shareOnWhatsApp(String message, byte[] imageByteArray, int imageArrayLength) {
        Uri imageUri = null;
        if (imageArrayLength != 0) {
            imageUri = FileUtility.createSharingFileUri(getContext(), imageByteArray, imageArrayLength, CommonDefines.SHARING_DIR, System.currentTimeMillis() + ".png");
        }
        Bundle bundle = new Bundle();
        bundle.putString(Keys.TYPE, Sharing.WHATS_APP);
        bundle.putString(Keys.MESSAGE, message);
        if (imageUri != null) {
            bundle.putString(Keys.IMAGE_PATH, imageUri.toString());
        }
        startActivity(bundle);
    }

    Context getContext() {
        return NativePluginHelper.getCurrentContext();
    }
}
