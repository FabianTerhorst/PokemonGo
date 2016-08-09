package com.upsight.android.marketing.internal.vast;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.gson.JsonObject;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityTrackEvent;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.internal.billboard.BillboardFragment;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import com.upsight.mediation.ads.adapters.NetworkWrapper;
import com.upsight.mediation.ads.adapters.NetworkWrapper.Listener;
import com.upsight.mediation.ads.adapters.VastAdAdapter;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.data.Offer;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import spacemadness.com.lunarconsole.R;

public final class VastContentMediator extends UpsightContentMediator<VastContentModel> {
    private static final String CONTENT_PROVIDER = "vast";
    public static final String LOG_TAG = VastContentMediator.class.getSimpleName();
    private VastAdAdapter mAdapter = null;
    private Bus mBus;
    private WeakReference<Activity> mCurrentActivity = null;
    private UpsightLogger mLogger;

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState = new int[ActivityState.values().length];

        static {
            try {
                $SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[ActivityState.CREATED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[ActivityState.RESUMED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[ActivityState.STARTED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[ActivityState.PAUSED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public VastContentMediator(UpsightLogger logger, Bus bus) {
        this.mLogger = logger;
        this.mBus = bus;
        this.mBus.register(this);
    }

    @Subscribe
    public void handleActivityTrackEvent(ActivityTrackEvent event) {
        switch (AnonymousClass2.$SwitchMap$com$upsight$android$analytics$UpsightLifeCycleTracker$ActivityState[event.mActivityState.ordinal()]) {
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
            case R.styleable.LoadingImageView_circleCrop /*2*/:
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                this.mCurrentActivity = new WeakReference(event.mActivity);
                return;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                if (this.mCurrentActivity != null) {
                    this.mCurrentActivity.clear();
                    this.mCurrentActivity = null;
                    return;
                }
                return;
            default:
                return;
        }
    }

    public String getContentProvider() {
        return CONTENT_PROVIDER;
    }

    public VastContentModel buildContentModel(MarketingContent<VastContentModel> marketingContent, MarketingContentActionContext actionContext, JsonObject model) {
        VastContentModel modelObject = null;
        try {
            modelObject = VastContentModel.from(model, actionContext.mGson);
        } catch (IOException e) {
            this.mLogger.e(LOG_TAG, "Failed to parse content model", e);
        }
        return modelObject;
    }

    public View buildContentView(final MarketingContent<VastContentModel> content, MarketingContentActionContext actionContext) {
        if (this.mCurrentActivity != null) {
            Activity activity = (Activity) this.mCurrentActivity.get();
            if (activity != null) {
                final VastContentModel model = (VastContentModel) content.getContentModel();
                this.mAdapter = new VastAdAdapter();
                this.mAdapter.init();
                HashMap<String, String> settings = ((VastContentModel) content.getContentModel()).getSettings();
                settings.put(NetworkWrapper.MAX_FILE_SIZE, model.getMaxVastFileSize());
                settings.put(NetworkWrapper.IS_REWARDED, Boolean.toString(model.isRewarded().booleanValue()));
                settings.put(NetworkWrapper.SHOULD_VALIDATE_SCHEMA, Boolean.toString(model.shouldValidateSchema().booleanValue()));
                this.mAdapter.setListener(new Listener() {
                    public void onAdLoaded() {
                        VastContentMediator.this.mLogger.i(VastContentMediator.LOG_TAG, "onAdLoaded", new Object[0]);
                        content.markLoaded(VastContentMediator.this.mBus);
                    }

                    public void onAdFailedToLoad(AdapterLoadError adapterLoadError) {
                        VastContentMediator.this.mLogger.w(VastContentMediator.LOG_TAG, "Failed to load VAST content", new Object[0]);
                    }

                    public void onAdDisplayed() {
                        VastContentMediator.this.mLogger.i(VastContentMediator.LOG_TAG, "onAdDisplayed", new Object[0]);
                        content.executeActions(MarketingContent.TRIGGER_CONTENT_DISPLAYED);
                    }

                    public void onAdFailedToDisplay() {
                        VastContentMediator.this.mLogger.w(VastContentMediator.LOG_TAG, "Failed to display VAST content", new Object[0]);
                    }

                    public void onAdSkipped() {
                    }

                    public void onAdClicked() {
                    }

                    public void onAdCompleted() {
                    }

                    public void onRewardedVideoCompleted() {
                        VastContentMediator.this.mLogger.i(VastContentMediator.LOG_TAG, "onRewardedVideoCompleted", new Object[0]);
                        content.markRewardGranted();
                    }

                    public void onAdClosed() {
                        VastContentMediator.this.mLogger.i(VastContentMediator.LOG_TAG, "onAdClosed", new Object[0]);
                        content.executeActions(content.isRewardGranted() ? MarketingContent.TRIGGER_CONTENT_DISMISSED_WITH_REWARD : MarketingContent.TRIGGER_CONTENT_DISMISSED);
                    }

                    public int getID() {
                        return model.getAdapterId().intValue();
                    }

                    public void onVastError(int i) {
                        VastContentMediator.this.mLogger.w(VastContentMediator.LOG_TAG, "onVastError i=" + i, new Object[0]);
                    }

                    public void onVastReplay() {
                    }

                    public void onVastSkip() {
                    }

                    public void onVastProgress(int i) {
                    }

                    public void onOfferDisplayed(Offer offer) {
                        VastContentMediator.this.mLogger.i(VastContentMediator.LOG_TAG, "onOfferDisplayed", new Object[0]);
                        content.executeActions(MarketingContent.TRIGGER_CONTENT_DISPLAYED);
                    }

                    public void onOfferRejected() {
                    }

                    public void onOfferAccepted() {
                    }

                    public void onOpenMRaidUrl(@NonNull String s) {
                    }

                    public void sendRequestToBeacon(String s) {
                    }
                });
                this.mAdapter.loadAd(activity, settings);
            }
        }
        return null;
    }

    public void displayContent(MarketingContent<VastContentModel> marketingContent, FragmentManager fragmentManager, BillboardFragment fragment) {
        this.mAdapter.displayAd();
    }

    public void hideContent(MarketingContent<VastContentModel> marketingContent, FragmentManager fragmentManager, BillboardFragment fragment) {
    }
}
