package com.upsight.android.marketing.internal.billboard;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.upsight.android.marketing.UpsightBillboard.Dimensions;
import com.upsight.android.marketing.UpsightBillboard.Dimensions.LayoutOrientation;
import java.util.Set;
import spacemadness.com.lunarconsole.R;

public final class BillboardFragment extends DialogFragment {
    private static final String BUNDLE_KEY_LANDSCAPE_HEIGHT = "landscapeHeight";
    private static final String BUNDLE_KEY_LANDSCAPE_WIDTH = "landscapeWidth";
    private static final String BUNDLE_KEY_PORTRAIT_HEIGHT = "portraitHeight";
    private static final String BUNDLE_KEY_PORTRAIT_WIDTH = "portraitWidth";
    private BackPressHandler mBackPressHandler = null;
    private ViewGroup mContentViewContainer = null;
    private ViewGroup mRootView = null;

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$marketing$UpsightBillboard$Dimensions$LayoutOrientation = new int[LayoutOrientation.values().length];

        static {
            try {
                $SwitchMap$com$upsight$android$marketing$UpsightBillboard$Dimensions$LayoutOrientation[LayoutOrientation.Portrait.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$marketing$UpsightBillboard$Dimensions$LayoutOrientation[LayoutOrientation.Landscape.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public interface BackPressHandler {
        boolean onBackPress();
    }

    public static BillboardFragment newInstance(Context context, Set<Dimensions> dimensions) {
        BillboardFragment fragment = new BillboardFragment();
        Bundle args = new Bundle();
        if (dimensions != null) {
            for (Dimensions dimension : dimensions) {
                if (dimension.width > 0 && dimension.height > 0) {
                    switch (AnonymousClass2.$SwitchMap$com$upsight$android$marketing$UpsightBillboard$Dimensions$LayoutOrientation[dimension.layout.ordinal()]) {
                        case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            args.putInt(BUNDLE_KEY_PORTRAIT_WIDTH, dimension.width);
                            args.putInt(BUNDLE_KEY_PORTRAIT_HEIGHT, dimension.height);
                            break;
                        case R.styleable.LoadingImageView_circleCrop /*2*/:
                            args.putInt(BUNDLE_KEY_LANDSCAPE_WIDTH, dimension.width);
                            args.putInt(BUNDLE_KEY_LANDSCAPE_HEIGHT, dimension.height);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        fragment.mContentViewContainer = new LinearLayout(context.getApplicationContext());
        return fragment;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.mContentViewContainer != null) {
            this.mRootView = (ViewGroup) inflater.inflate(com.upsight.android.marketing.R.layout.upsight_fragment_billboard, container, false);
            this.mRootView.addView(this.mContentViewContainer, new LayoutParams(-1, -1));
        }
        return this.mRootView;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 0) {
                    return false;
                }
                BackPressHandler handler = BillboardFragment.this.mBackPressHandler;
                if (handler == null || !handler.onBackPress()) {
                    return false;
                }
                return true;
            }
        });
        return dialog;
    }

    public void onStart() {
        super.onStart();
        if (this.mContentViewContainer == null) {
            dismiss();
        }
    }

    public void onResume() {
        int orientation = getResources().getConfiguration().orientation;
        Bundle args = getArguments();
        if (orientation == 1 && args.containsKey(BUNDLE_KEY_PORTRAIT_WIDTH)) {
            setDialogSize(args.getInt(BUNDLE_KEY_PORTRAIT_WIDTH), args.getInt(BUNDLE_KEY_PORTRAIT_HEIGHT));
        } else if (orientation == 2 && args.containsKey(BUNDLE_KEY_LANDSCAPE_WIDTH)) {
            setDialogSize(args.getInt(BUNDLE_KEY_LANDSCAPE_WIDTH), args.getInt(BUNDLE_KEY_LANDSCAPE_HEIGHT));
        }
        super.onResume();
    }

    public void onDestroyView() {
        if (this.mContentViewContainer != null) {
            this.mRootView.removeView(this.mContentViewContainer);
        }
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public ViewGroup getContentViewContainer() {
        return this.mContentViewContainer;
    }

    public void setBackPressHandler(BackPressHandler handler) {
        this.mBackPressHandler = handler;
    }

    private void setDialogSize(int width, int height) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = width;
            params.height = height;
            window.setAttributes(params);
        }
    }
}
