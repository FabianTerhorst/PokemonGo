package com.upsight.mediation.vast.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.upsight.mediation.vast.Postroll.Postroll;
import com.upsight.mediation.vast.Postroll.Postroll.Listener;
import com.upsight.mediation.vast.Postroll.VasButton;
import com.upsight.mediation.vast.util.Assets;

public class DefaultPostroll extends RelativeLayout implements Postroll {
    private static final int BUTTON_HEIGHT = 75;
    private static final int BUTTON_WIDTH = 256;
    private static final int MARGIN = 8;
    private static final float SCALED_DENSITY = VASTActivity.displayMetrics.scaledDensity;
    public static final int TEXT_FONT_SIZE = 28;
    private static final int X_HEIGHT = 29;
    private static final int X_WIDTH = 29;
    private final String mActionButtonText;
    private Context mContext;
    private ImageView mLastFrameView;
    private LinearLayout mLayerOfDarkness;
    private Button mLearnButton;
    private final Listener mListener;
    private final boolean mShouldShowActionButton;
    private ImageView mXButtonView;

    public DefaultPostroll(Context context, Listener listener, boolean shouldShowActionButton, String actionButtonText) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        this.mShouldShowActionButton = shouldShowActionButton;
        this.mActionButtonText = actionButtonText;
    }

    public void init() {
        setLayoutParams(new LayoutParams(-1, -1));
        setUpLastFrameView(this.mContext);
        setUpLayerOfDarkness(this.mContext);
        setUpLearn(this.mContext);
        setUpX(this.mContext);
    }

    public boolean isReady() {
        return true;
    }

    public void show(ViewGroup parent) {
        parent.addView(this);
    }

    public void hide() {
        ((ViewGroup) getParent()).removeView(this);
    }

    private void setUpLayerOfDarkness(Context c) {
        this.mLayerOfDarkness = new LinearLayout(c);
        this.mLayerOfDarkness.setLayoutParams(new LayoutParams(-1, -1));
        this.mLayerOfDarkness.setBackgroundColor(Color.argb(192, 0, 0, 0));
        addView(this.mLayerOfDarkness);
    }

    private void setUpLearn(Context c) {
        if (this.mShouldShowActionButton) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Assets.convertToDps(256.0f), Assets.convertToDps(75.0f));
            params.addRule(13);
            this.mLearnButton = new VasButton(c);
            this.mLearnButton.setTypeface(Typeface.create("sans-serif-light", 0));
            this.mLearnButton.setMaxLines(1);
            this.mLearnButton.setLayoutParams(params);
            this.mLearnButton.setGravity(17);
            Assets.setImage(this.mLearnButton, Assets.getPostrollButton(getResources()));
            this.mLearnButton.setEnabled(true);
            this.mLearnButton.setVisibility(0);
            this.mLearnButton.setTextColor(-1);
            this.mLearnButton.setText(this.mActionButtonText);
            this.mLearnButton.setTextSize((float) (28 - (this.mActionButtonText.length() / 2)));
            this.mLearnButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    DefaultPostroll.this.mListener.infoClicked(true);
                }
            });
            addView(this.mLearnButton);
        }
    }

    private void setUpLastFrameView(Context c) {
        this.mLastFrameView = new ImageView(c);
        this.mLastFrameView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        addView(this.mLastFrameView);
    }

    private void setUpX(Context c) {
        this.mXButtonView = new ImageView(c);
        Assets.setImage(this.mXButtonView, Assets.getXButton(getResources()));
        Assets.setAlpha(this.mXButtonView, 0.8f);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Assets.convertToDps(29.0f), Assets.convertToDps(29.0f));
        params.addRule(11);
        int margin = Assets.convertToDps(8.0f);
        params.setMargins(0, margin, margin, 0);
        this.mXButtonView.setLayoutParams(params);
        this.mXButtonView.setId(16908327);
        this.mXButtonView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DefaultPostroll.this.hide();
                DefaultPostroll.this.mListener.closeClicked();
            }
        });
        addView(this.mXButtonView);
    }
}
