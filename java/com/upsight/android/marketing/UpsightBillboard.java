package com.upsight.android.marketing;

import android.text.TextUtils;
import android.view.ViewGroup;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.internal.billboard.Billboard;
import java.util.List;
import java.util.Set;

public abstract class UpsightBillboard {

    public static class Dimensions {
        public final int height;
        public final LayoutOrientation layout;
        public final int width;

        public enum LayoutOrientation {
            Portrait,
            Landscape
        }

        public Dimensions(LayoutOrientation layout, int width, int height) {
            this.layout = layout;
            this.width = width;
            this.height = height;
        }
    }

    public interface Handler {
        ViewGroup onAttach(String str, PresentationStyle presentationStyle, Set<Dimensions> set);

        void onDetach();

        void onNextView();

        void onPurchases(List<UpsightPurchase> list);

        void onRewards(List<UpsightReward> list);
    }

    public enum PresentationStyle {
        None,
        Dialog,
        Fullscreen
    }

    public abstract void destroy();

    protected abstract UpsightBillboard setUp(UpsightContext upsightContext) throws IllegalStateException;

    public static UpsightBillboard create(UpsightContext upsight, String scope, Handler handler) throws IllegalArgumentException, IllegalStateException {
        if (!TextUtils.isEmpty(scope) || handler == null) {
            UpsightBillboard billboard = new Billboard(scope, handler);
            billboard.setUp(upsight);
            return billboard;
        }
        throw new IllegalArgumentException("The billboard scope and handler must be non-null and non-empty.");
    }
}
