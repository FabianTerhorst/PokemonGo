package com.upsight.mediation.vast.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.upsight.mediation.vast.model.VASTModel;
import com.upsight.mediation.vast.util.Assets;
import spacemadness.com.lunarconsole.BuildConfig;

public class PlayerControls extends RelativeLayout {
    private static final int BUTTON_HEIGHT = 38;
    private static final int BUTTON_WIDTH = 128;
    public static final float DOWN_STATE = 0.75f;
    private static final int MARGIN = 8;
    private static final int PROGRESS_RING_RADIUS = 10;
    private static final int PROGRESS_RING_VIEW_HEIGHT = 25;
    private static final int PROGRESS_RING_VIEW_WIDTH = 25;
    private static final int PROGRESS_RING_WIDTH = 25;
    public static final int TEXT_FONT_SIZE = 20;
    private static final int TIME_FONT_SIZE = 10;
    private CircleDrawable circleDrawable;
    private long elapsedTime = 0;
    private Context mContext;
    private TextView mLearnText;
    private long mRemainder;
    private LinearLayout mSkipButton;
    private long mSkipOffset;
    private TextView mSkipText;
    private boolean mSkippable;
    private TextView mTimeText;
    private FrameLayout mTimerRing;
    private VASTModel mVastModel;
    private ImageView progressCircle;
    private OnTouchListener skipListener;

    public PlayerControls(Context context) {
        super(context);
        this.mContext = context;
    }

    public PlayerControls(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public PlayerControls(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void init(boolean skippable, boolean postroll) {
        if (!(postroll || skippable) || skippable) {
            setUpLearn(this.mContext);
        }
        setUpSkipButton(this.mContext, skippable);
    }

    private void setUpLearn(Context c) {
        String clickThroughUrl = this.mVastModel.getVideoClicks().getClickThrough();
        if (clickThroughUrl != null && clickThroughUrl.length() > 0) {
            LayoutParams params = new LayoutParams(Assets.convertToDps(128.0f), Assets.convertToDps(38.0f));
            params.addRule(9);
            int margin = Assets.convertToDps(8.0f);
            params.setMargins(margin, margin, 0, 0);
            this.mLearnText = new TextView(c);
            this.mLearnText.setText("LEARN MORE");
            this.mLearnText.setTypeface(Typeface.create("sans-serif-light", 0));
            this.mLearnText.setTextSize(20.0f);
            this.mLearnText.setMaxLines(1);
            this.mLearnText.setLayoutParams(params);
            this.mLearnText.setGravity(17);
            Assets.setImage(this.mLearnText, Assets.getPlayerUIButton(getResources()));
            this.mLearnText.setEnabled(true);
            this.mLearnText.setVisibility(0);
            addView(this.mLearnText);
        }
    }

    private void setUpSkipButton(Context c, boolean skippable) {
        this.mSkippable = skippable;
        this.mSkipButton = new LinearLayout(c);
        this.mSkipButton.setOrientation(0);
        this.mSkipButton.setGravity(17);
        Assets.setImage(this.mSkipButton, Assets.getPlayerUIButton(getResources()));
        int width = Assets.convertToDps(128.0f);
        int height = Assets.convertToDps(38.0f);
        int margin = Assets.convertToDps(8.0f);
        LayoutParams params = new LayoutParams(width, height);
        params.addRule(11);
        params.setMargins(0, margin, margin, 0);
        this.mSkipButton.setLayoutParams(params);
        setUpTimerRing(c);
        setUpSkipText(c);
        Assets.setAlpha(this.mSkipButton, DOWN_STATE);
        addView(this.mSkipButton);
    }

    private void setUpSkipText(Context c) {
        this.mSkipText = new TextView(c);
        if (this.mSkippable) {
            this.mSkipText.setText("SKIP");
        } else {
            this.mSkipText.setText("sec");
        }
        this.mSkipText.setTypeface(Typeface.create("sans-serif-light", 0));
        this.mSkipText.setTextSize(20.0f);
        this.mSkipButton.addView(this.mSkipText);
    }

    private void setUpTimerRing(Context c) {
        this.mTimerRing = new FrameLayout(c);
        this.mTimerRing.setPadding(0, 0, Assets.convertToDps(7.0f), 0);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = 17;
        this.circleDrawable = new CircleDrawable((float) Assets.convertToDps(25.0f), (float) Assets.convertToDps(10.0f));
        this.progressCircle = new ImageView(c);
        this.progressCircle.setMinimumWidth(Assets.convertToDps(25.0f));
        this.progressCircle.setMinimumHeight(Assets.convertToDps(25.0f));
        this.progressCircle.setImageDrawable(this.circleDrawable);
        this.progressCircle.setLayoutParams(params);
        this.mTimeText = new TextView(c);
        this.mTimeText.setTypeface(Typeface.create("sans-serif-light", 0));
        this.mTimeText.setTextSize(10.0f);
        this.mTimeText.setLayoutParams(params);
        this.mTimerRing.addView(this.progressCircle);
        this.mTimerRing.addView(this.mTimeText);
        this.mSkipButton.addView(this.mTimerRing);
    }

    public TextView getLearnText() {
        return this.mLearnText;
    }

    public void setSkipButtonListener(OnTouchListener listener) {
        this.skipListener = listener;
    }

    public void setTimes(long duration, long skipOffset) {
        this.mSkipOffset = skipOffset;
        this.mRemainder = duration;
        this.circleDrawable.setTimer(duration);
    }

    public void update(long timeSinceLastUpdate) {
        this.elapsedTime += timeSinceLastUpdate;
        this.mTimeText.setText(Math.abs((this.elapsedTime - this.mRemainder) / 1000) + BuildConfig.FLAVOR);
        if (this.elapsedTime > this.mRemainder) {
            this.circleDrawable.setSweepAngle(0.0f);
            this.progressCircle.invalidate();
            return;
        }
        if (this.elapsedTime > this.mSkipOffset && this.mSkippable) {
            this.mSkipOffset = this.mRemainder;
            this.mSkipButton.setOnTouchListener(this.skipListener);
            Assets.setAlpha(this.mSkipButton, 1.0f);
        }
        this.circleDrawable.update(this.elapsedTime);
        this.progressCircle.invalidate();
    }

    public void setVastModel(VASTModel mVastModel) {
        this.mVastModel = mVastModel;
    }
}
