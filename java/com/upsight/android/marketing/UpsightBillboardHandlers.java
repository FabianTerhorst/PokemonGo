package com.upsight.android.marketing;

import android.app.Activity;
import com.upsight.android.marketing.UpsightBillboard.AttachParameters;
import com.upsight.android.marketing.UpsightBillboard.Handler;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import java.lang.ref.WeakReference;
import java.util.List;

public final class UpsightBillboardHandlers {

    private static abstract class SimpleHandler implements Handler {
        private WeakReference<Activity> mActivity;

        protected SimpleHandler(Activity activity) {
            this.mActivity = new WeakReference(activity);
        }

        public void onNextView() {
        }

        public void onDetach() {
        }

        public void onPurchases(List<UpsightPurchase> list) {
        }

        public void onRewards(List<UpsightReward> list) {
        }

        Activity getActivity() {
            return (Activity) this.mActivity.get();
        }
    }

    public static class DefaultHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List list) {
            super.onPurchases(list);
        }

        public /* bridge */ /* synthetic */ void onRewards(List list) {
            super.onRewards(list);
        }

        public DefaultHandler(Activity activity) {
            super(activity);
        }

        public AttachParameters onAttach(String scope) {
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            return new AttachParameters(activity).putPreferredPresentationStyle(PresentationStyle.None);
        }
    }

    public static class DialogHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List list) {
            super.onPurchases(list);
        }

        public /* bridge */ /* synthetic */ void onRewards(List list) {
            super.onRewards(list);
        }

        public DialogHandler(Activity activity) {
            super(activity);
        }

        public AttachParameters onAttach(String scope) {
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            return new AttachParameters(activity).putPreferredPresentationStyle(PresentationStyle.Dialog);
        }
    }

    public static class FullscreenHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List list) {
            super.onPurchases(list);
        }

        public /* bridge */ /* synthetic */ void onRewards(List list) {
            super.onRewards(list);
        }

        public FullscreenHandler(Activity activity) {
            super(activity);
        }

        public AttachParameters onAttach(String scope) {
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            return new AttachParameters(activity).putPreferredPresentationStyle(PresentationStyle.Fullscreen);
        }
    }

    public static Handler forDialog(Activity activity) {
        return new DialogHandler(activity);
    }

    public static Handler forFullscreen(Activity activity) {
        return new FullscreenHandler(activity);
    }

    public static Handler forDefault(Activity activity) {
        return new DefaultHandler(activity);
    }
}
