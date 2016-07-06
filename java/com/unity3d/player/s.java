package com.unity3d.player;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public final class s extends Dialog implements TextWatcher, OnClickListener {
    private static int c = 1627389952;
    private static int d = -1;
    private Context a = null;
    private UnityPlayer b = null;

    public s(Context context, UnityPlayer unityPlayer, String str, int i, boolean z, boolean z2, boolean z3, String str2) {
        super(context);
        this.a = context;
        this.b = unityPlayer;
        getWindow().setGravity(80);
        getWindow().requestFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(createSoftInputView());
        getWindow().setLayout(-1, -2);
        getWindow().clearFlags(2);
        EditText editText = (EditText) findViewById(1057292289);
        Button button = (Button) findViewById(1057292290);
        a(editText, str, i, z, z2, z3, str2);
        button.setOnClickListener(this);
        editText.setOnFocusChangeListener(new OnFocusChangeListener(this) {
            final /* synthetic */ s a;

            {
                this.a = r1;
            }

            public final void onFocusChange(View view, boolean z) {
                if (z) {
                    this.a.getWindow().setSoftInputMode(5);
                }
            }
        });
    }

    private static int a(int i, boolean z, boolean z2, boolean z3) {
        int i2 = 0;
        int i3 = (z2 ? 131072 : 0) | (z ? 32768 : 0);
        if (z3) {
            i2 = 128;
        }
        i2 |= i3;
        return (i < 0 || i > 7) ? i2 : i2 | new int[]{1, 16385, 12290, 17, 2, 3, 97, 33}[i];
    }

    private String a() {
        EditText editText = (EditText) findViewById(1057292289);
        return editText == null ? null : editText.getText().toString().trim();
    }

    private void a(EditText editText, String str, int i, boolean z, boolean z2, boolean z3, String str2) {
        editText.setImeOptions(6);
        editText.setText(str);
        editText.setHint(str2);
        editText.setHintTextColor(c);
        editText.setInputType(a(i, z, z2, z3));
        editText.addTextChangedListener(this);
        editText.setClickable(true);
        if (!z2) {
            editText.selectAll();
        }
    }

    private void a(String str, boolean z) {
        Selection.removeSelection(((EditText) findViewById(1057292289)).getEditableText());
        this.b.reportSoftInputStr(str, 1, z);
    }

    public final void a(String str) {
        EditText editText = (EditText) findViewById(1057292289);
        if (editText != null) {
            editText.setText(str);
            editText.setSelection(str.length());
        }
    }

    public final void afterTextChanged(Editable editable) {
        this.b.reportSoftInputStr(editable.toString(), 0, false);
    }

    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    protected final View createSoftInputView() {
        View relativeLayout = new RelativeLayout(this.a);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -1));
        relativeLayout.setBackgroundColor(d);
        View anonymousClass2 = new EditText(this, this.a) {
            final /* synthetic */ s a;

            public final boolean onKeyPreIme(int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return i != 84 ? super.onKeyPreIme(i, keyEvent) : true;
                } else {
                    this.a.a(this.a.a(), true);
                    return true;
                }
            }

            public final void onWindowFocusChanged(boolean z) {
                super.onWindowFocusChanged(z);
                if (z) {
                    ((InputMethodManager) this.a.a.getSystemService("input_method")).showSoftInput(this, 0);
                }
            }
        };
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(15);
        layoutParams.addRule(0, 1057292290);
        anonymousClass2.setLayoutParams(layoutParams);
        anonymousClass2.setId(1057292289);
        relativeLayout.addView(anonymousClass2);
        anonymousClass2 = new Button(this.a);
        anonymousClass2.setText(this.a.getResources().getIdentifier("ok", "string", "android"));
        layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(15);
        layoutParams.addRule(11);
        anonymousClass2.setLayoutParams(layoutParams);
        anonymousClass2.setId(1057292290);
        anonymousClass2.setBackgroundColor(0);
        relativeLayout.addView(anonymousClass2);
        ((EditText) relativeLayout.findViewById(1057292289)).setOnEditorActionListener(new OnEditorActionListener(this) {
            final /* synthetic */ s a;

            {
                this.a = r1;
            }

            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 6) {
                    this.a.a(this.a.a(), false);
                }
                return false;
            }
        });
        relativeLayout.setPadding(16, 16, 16, 16);
        return relativeLayout;
    }

    public final void onBackPressed() {
        a(a(), true);
    }

    public final void onClick(View view) {
        a(a(), false);
    }

    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
