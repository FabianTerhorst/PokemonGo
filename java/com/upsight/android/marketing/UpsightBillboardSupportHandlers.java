package com.upsight.android.marketing;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import com.upsight.android.marketing.UpsightBillboard.Dimensions;
import com.upsight.android.marketing.UpsightBillboard.Handler;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import com.upsight.android.marketing.internal.billboard.BillboardSupportFragment;
import java.util.List;
import java.util.Set;
import spacemadness.com.lunarconsole.R;

public final class UpsightBillboardSupportHandlers {
    private static final int STYLE_DIALOG = R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_UpsightDialog;
    private static final int STYLE_FULLSCREEN = 16974122;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$marketing$UpsightBillboard$PresentationStyle = new int[PresentationStyle.values().length];

        static {
            try {
                $SwitchMap$com$upsight$android$marketing$UpsightBillboard$PresentationStyle[PresentationStyle.Dialog.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$marketing$UpsightBillboard$PresentationStyle[PresentationStyle.Fullscreen.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private static abstract class SimpleHandler implements Handler {
        protected FragmentActivity mActivity;
        protected BillboardSupportFragment mFragment = null;

        protected SimpleHandler(FragmentActivity activity) {
            this.mActivity = activity;
        }

        public void onNextView() {
        }

        public void onDetach() {
            DialogFragment fragment = this.mFragment;
            if (fragment != null) {
                fragment.dismissAllowingStateLoss();
                this.mFragment = null;
            }
        }

        public void onPurchases(List<UpsightPurchase> list) {
        }

        public void onRewards(List<UpsightReward> list) {
        }
    }

    public static class DefaultHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List x0) {
            super.onPurchases(x0);
        }

        public /* bridge */ /* synthetic */ void onRewards(List x0) {
            super.onRewards(x0);
        }

        public DefaultHandler(FragmentActivity activity) {
            super(activity);
        }

        public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> dimensions) {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            int style;
            Set<Dimensions> fragmentDimensions = null;
            switch (AnonymousClass1.$SwitchMap$com$upsight$android$marketing$UpsightBillboard$PresentationStyle[presentation.ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    fragmentDimensions = dimensions;
                    style = UpsightBillboardSupportHandlers.STYLE_DIALOG;
                    break;
                default:
                    style = UpsightBillboardSupportHandlers.STYLE_FULLSCREEN;
                    break;
            }
            this.mFragment = BillboardSupportFragment.newInstance(this.mActivity, fragmentDimensions);
            this.mFragment.setStyle(1, style);
            this.mFragment.setCancelable(false);
            this.mFragment.show(this.mActivity.getSupportFragmentManager(), null);
            return this.mFragment.getContentViewContainer();
        }
    }

    public static class DialogHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List x0) {
            super.onPurchases(x0);
        }

        public /* bridge */ /* synthetic */ void onRewards(List x0) {
            super.onRewards(x0);
        }

        public DialogHandler(FragmentActivity activity) {
            super(activity);
        }

        public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> dimensions) {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            this.mFragment = BillboardSupportFragment.newInstance(this.mActivity, dimensions);
            this.mFragment.setStyle(1, UpsightBillboardSupportHandlers.STYLE_DIALOG);
            this.mFragment.setCancelable(false);
            this.mFragment.show(this.mActivity.getSupportFragmentManager(), null);
            return this.mFragment.getContentViewContainer();
        }
    }

    private static class EmbeddedHandler extends SimpleHandler {
        protected int mContainerViewId;

        public EmbeddedHandler(FragmentActivity activity, int containerViewId) {
            super(activity);
            this.mContainerViewId = containerViewId;
        }

        public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> dimensions) {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            this.mFragment = BillboardSupportFragment.newInstance(this.mActivity, dimensions);
            this.mActivity.getSupportFragmentManager().beginTransaction().add(this.mContainerViewId, this.mFragment).commit();
            return this.mFragment.getContentViewContainer();
        }
    }

    public static class FullscreenHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List x0) {
            super.onPurchases(x0);
        }

        public /* bridge */ /* synthetic */ void onRewards(List x0) {
            super.onRewards(x0);
        }

        public FullscreenHandler(FragmentActivity activity) {
            super(activity);
        }

        public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> set) {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            this.mFragment = BillboardSupportFragment.newInstance(this.mActivity, null);
            this.mFragment.setStyle(1, UpsightBillboardSupportHandlers.STYLE_FULLSCREEN);
            this.mFragment.setCancelable(false);
            this.mFragment.show(this.mActivity.getSupportFragmentManager(), null);
            return this.mFragment.getContentViewContainer();
        }
    }

    private static Handler forEmbedded(FragmentActivity activity, int containerViewId) {
        return new EmbeddedHandler(activity, containerViewId);
    }

    public static Handler forDialog(FragmentActivity activity) {
        return new DialogHandler(activity);
    }

    public static Handler forFullscreen(FragmentActivity activity) {
        return new FullscreenHandler(activity);
    }

    public static Handler forDefault(FragmentActivity activity) {
        return new DefaultHandler(activity);
    }
}
