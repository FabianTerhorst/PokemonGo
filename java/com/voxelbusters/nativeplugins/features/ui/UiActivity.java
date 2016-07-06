package com.voxelbusters.nativeplugins.features.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.voxelbusters.nativeplugins.NativePluginHelper;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Ui;
import com.voxelbusters.nativeplugins.defines.UnityDefines;
import com.voxelbusters.nativeplugins.utilities.StringUtility;
import java.util.HashMap;

public class UiActivity extends Activity {
    AlertDialog alertDialog;
    Bundle bundleInfo;
    boolean paused;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.bundleInfo == null) {
            this.bundleInfo = getIntent().getExtras();
        }
        eUiType type = eUiType.values()[this.bundleInfo.getInt(Keys.TYPE)];
        if (type == eUiType.ALERT_DIALOG) {
            showAlertDialog(this.bundleInfo);
        } else if (type == eUiType.SINGLE_FIELD_PROMPT) {
            showSinglePrompt(this.bundleInfo);
        } else if (type == eUiType.LOGIN_PROMPT) {
            showLoginPrompt(this.bundleInfo);
        }
    }

    @SuppressLint({"NewApi"})
    protected void onResume() {
        super.onResume();
        if (this.paused) {
            finish();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    UiActivity.this.startActivity(UiActivity.this.getIntent());
                }
            }, 10);
        }
    }

    protected void onPause() {
        super.onPause();
        this.paused = true;
    }

    public void onConfigurationChanged(Configuration new_config) {
        super.onConfigurationChanged(new_config);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.alertDialog != null) {
            this.alertDialog.dismiss();
            this.alertDialog = null;
        }
    }

    private void showAlertDialog(Bundle bundle) {
        int length = 3;
        final String tag = bundle.getString(Keys.TAG);
        String[] buttonsList = bundle.getStringArray(Keys.BUTTON_LIST);
        this.alertDialog = getDialogWithDefaultDetails(bundle);
        if (buttonsList.length <= 3) {
            length = buttonsList.length;
        }
        final String[] tempButtonList = buttonsList;
        for (int eachButtonIndex = 0; eachButtonIndex < length; eachButtonIndex++) {
            final int index = eachButtonIndex;
            this.alertDialog.setButton(-1 - eachButtonIndex, buttonsList[eachButtonIndex], new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String buttonPressed = tempButtonList[index];
                    String callerTag = tag;
                    HashMap dataMap = new HashMap();
                    dataMap.put(Ui.BUTTON_PRESSED, buttonPressed);
                    dataMap.put(Ui.CALLER, callerTag);
                    NativePluginHelper.sendMessage(UnityDefines.Ui.ALERT_DIALOG_CLOSED, dataMap);
                    UiHandler.getInstance().onFinish(callerTag);
                    UiActivity.this.finish();
                }
            });
        }
        this.alertDialog.show();
    }

    private void showSinglePrompt(Bundle bundle) {
        int length = 3;
        String[] buttonsList = bundle.getStringArray(Keys.BUTTON_LIST);
        this.alertDialog = getDialogWithDefaultDetails(bundle);
        final EditText promptField = new EditText(this);
        this.alertDialog.setView(promptField);
        boolean isSecureText = bundle.getBoolean(Keys.IS_SECURE);
        String placeHolderText = bundle.getString(Keys.PLACE_HOLDER_TEXT_1);
        if (isSecureText) {
            promptField.setTransformationMethod(new PasswordTransformationMethod());
        }
        if (placeHolderText != null) {
            promptField.setHint(placeHolderText);
        }
        if (buttonsList.length <= 3) {
            length = buttonsList.length;
        }
        final String[] tempButtonList = buttonsList;
        for (int eachButtonIndex = 0; eachButtonIndex < length; eachButtonIndex++) {
            final int index = eachButtonIndex;
            this.alertDialog.setButton(-1 - eachButtonIndex, buttonsList[eachButtonIndex], new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String promptText = promptField.getText().toString();
                    HashMap dataMap = new HashMap();
                    dataMap.put(Ui.BUTTON_PRESSED, tempButtonList[index]);
                    dataMap.put(Ui.INPUT, promptText);
                    NativePluginHelper.sendMessage(UnityDefines.Ui.SINGLE_FIELD_PROMPT_DIALOG_CLOSED, dataMap);
                    UiActivity.this.finish();
                }
            });
        }
        this.alertDialog.show();
    }

    private void showLoginPrompt(Bundle bundle) {
        String[] buttonsList = bundle.getStringArray(Keys.BUTTON_LIST);
        setContentView(new LinearLayout(this));
        final String[] finalButtonList = buttonsList;
        this.alertDialog = getDialogWithDefaultDetails(bundle);
        final EditText usernameField = new EditText(this);
        final EditText passwordField = new EditText(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        layout.addView(usernameField);
        layout.addView(passwordField);
        this.alertDialog.setView(layout);
        String placeHolder1Text = bundle.getString(Keys.PLACE_HOLDER_TEXT_1);
        String placeHolder2Text = bundle.getString(Keys.PLACE_HOLDER_TEXT_2);
        passwordField.setTransformationMethod(new PasswordTransformationMethod());
        usernameField.setHint(placeHolder1Text);
        passwordField.setHint(placeHolder2Text);
        int length = finalButtonList.length > 3 ? 3 : finalButtonList.length;
        for (int eachButtonIndex = 0; eachButtonIndex < length; eachButtonIndex++) {
            final int index = eachButtonIndex;
            this.alertDialog.setButton(-1 - eachButtonIndex, finalButtonList[eachButtonIndex], new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    HashMap dataMap = new HashMap();
                    dataMap.put(Ui.BUTTON_PRESSED, finalButtonList[index]);
                    dataMap.put(Ui.USER_NAME, usernameField.getText().toString());
                    dataMap.put(Ui.PASSWORD, passwordField.getText().toString());
                    NativePluginHelper.sendMessage(UnityDefines.Ui.LOGIN_PROMPT_DIALOG_CLOSED, dataMap);
                    UiActivity.this.finish();
                }
            });
        }
        this.alertDialog.show();
    }

    AlertDialog getDialogWithDefaultDetails(Bundle bundle) {
        String title = bundle.getString(Keys.TITLE);
        String message = bundle.getString(Keys.MESSAGE);
        Builder builder = new Builder(this);
        if (!StringUtility.isNullOrEmpty(title)) {
            builder.setTitle(title);
        }
        if (!StringUtility.isNullOrEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setCancelable(false);
        return builder.create();
    }
}
