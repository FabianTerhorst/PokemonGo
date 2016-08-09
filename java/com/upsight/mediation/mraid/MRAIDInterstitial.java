package com.upsight.mediation.mraid;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.upsight.mediation.mraid.internal.MRAIDLog;

public class MRAIDInterstitial implements MRAIDViewListener {
    private static final String TAG = "MRAIDInterstitial";
    private Handler handler;
    public boolean isReady;
    private MRAIDInterstitialListener listener;
    public MRAIDView mraidView;

    public MRAIDInterstitial(Context context, String baseUrl, String data, String[] supportedNativeFeatures, MRAIDInterstitialListener listener, MRAIDNativeFeatureListener nativeFeatureListener) {
        this(context, baseUrl, data, 0, supportedNativeFeatures, listener, nativeFeatureListener);
    }

    public MRAIDInterstitial(Context context, String baseUrl, String data, int backgroundColor, @NonNull String[] supportedNativeFeatures, MRAIDInterstitialListener listener, MRAIDNativeFeatureListener nativeFeatureListener) {
        this.listener = listener;
        this.handler = new Handler(Looper.getMainLooper());
        this.mraidView = new MRAIDView(context, baseUrl, data, backgroundColor, supportedNativeFeatures, this, nativeFeatureListener, true);
    }

    public boolean show() {
        if (this.isReady) {
            this.mraidView.showAsInterstitial();
            this.isReady = false;
            return true;
        }
        MRAIDLog.i(TAG, "interstitial is not ready to show");
        return false;
    }

    public void updateContext(Context context) {
        this.mraidView.updateContext(context);
    }

    public void injectJavaScript(String js) {
        this.mraidView.injectJavaScript(js);
    }

    public void mraidViewLoaded(MRAIDView mraidView) {
        MRAIDLog.v("MRAIDInterstitial-MRAIDViewListener", "mraidViewLoaded");
        this.isReady = true;
        this.handler.postDelayed(new Runnable() {
            public void run() {
                if (!MRAIDInterstitial.this.isReady || MRAIDInterstitial.this.listener == null) {
                    MRAIDLog.i(MRAIDInterstitial.TAG, "No longer ready");
                } else {
                    MRAIDInterstitial.this.listener.mraidInterstitialLoaded(MRAIDInterstitial.this);
                }
            }
        }, 250);
    }

    public void mraidViewExpand(MRAIDView mraidView) {
        MRAIDLog.i("MRAIDInterstitial-MRAIDViewListener", "mraidViewExpand");
        if (this.listener != null) {
            this.listener.mraidInterstitialShow(this);
        }
    }

    public void mraidViewClose(MRAIDView mraidView) {
        MRAIDLog.i("MRAIDInterstitial-MRAIDViewListener", "mraidViewClose");
        this.isReady = false;
        if (this.listener != null) {
            this.listener.mraidInterstitialHide(this);
        }
    }

    public boolean mraidViewResize(MRAIDView mraidView, int width, int height, int offsetX, int offsetY) {
        return true;
    }

    public void mraidViewFailedToLoad(MRAIDView mraidView) {
        this.isReady = false;
        if (this.listener != null) {
            this.listener.mraidInterstitialFailedToLoad(this);
        }
    }

    public void mraidViewAcceptPressed(MRAIDView mraidView) {
        if (this.listener != null) {
            this.listener.mraidInterstitialAcceptPressed(this);
        }
    }

    public void mraidViewRejectPressed(MRAIDView mraidView) {
        if (this.listener != null) {
            this.listener.mraidInterstitialRejectPressed(this);
        }
    }

    public void mraidReplayVideoPressed(MRAIDView mraidView) {
        if (this.listener != null) {
            this.listener.mraidInterstitialReplayVideoPressed(this);
        }
    }

    public void setOrientationConfig(int rotateMode) {
        this.mraidView.setOrientationConfig(rotateMode);
    }
}
