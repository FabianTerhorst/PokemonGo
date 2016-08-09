package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.VideoView;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.mraid.MRaidDrawables;
import com.voxelbusters.nativeplugins.defines.Keys;

public class MRaidVideoActivity extends Activity implements OnCompletionListener, OnErrorListener, OnPreparedListener {
    private static final float CLOSE_REGION_HEIGHT_OFFSET = 0.01f;
    private static final float CLOSE_REGION_SIZE = 50.0f;
    private static final float CLOSE_REGION_WIDTH_OFFSET = 0.9f;
    private static final String TAG = "MRAIDVideoActivity";
    private long closeButtonDelay;
    private ImageView closeRegion;
    private float deviceHeight;
    private float deviceWidth;
    private FrameLayout layout;
    private boolean shouldReturnToInterstitial;
    private LayoutParams skipParams;
    private VideoView videoView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.layout = new FrameLayout(this);
        LayoutParams params = new LayoutParams(-1, -1);
        params.gravity = 17;
        this.layout.setLayoutParams(params);
        String url = getIntent().getStringExtra(Keys.URL);
        this.closeButtonDelay = getIntent().getLongExtra("cb_ms", 0);
        this.shouldReturnToInterstitial = getIntent().getBooleanExtra("rti", false);
        this.videoView = new VideoView(this);
        this.videoView.setOnCompletionListener(this);
        this.videoView.setOnErrorListener(this);
        this.videoView.setOnPreparedListener(this);
        this.videoView.setVideoPath(url);
        this.videoView.setLayoutParams(params);
        this.layout.addView(this.videoView);
        addCloseRegion();
        setCloseRegionPositionAndSize();
        setContentView(this.layout);
    }

    private void addCloseRegion() {
        this.closeRegion = new ImageButton(this);
        this.closeRegion.setBackgroundColor(0);
        this.closeRegion.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("rti", MRaidVideoActivity.this.shouldReturnToInterstitial);
                MRaidVideoActivity.this.setResult(0, i);
                MRaidVideoActivity.this.finish();
            }
        });
        this.closeRegion.setVisibility(4);
        if (this.closeButtonDelay != -1) {
            this.closeRegion.postDelayed(new Runnable() {
                public void run() {
                    MRaidVideoActivity.this.closeRegion.setVisibility(0);
                }
            }, this.closeButtonDelay);
        }
        Drawable closeButtonNormalDrawable = MRaidDrawables.getDrawableForImage(this, "/assets/drawable/close_button_normal.png", "close_button_normal", -16777216);
        Drawable closeButtonPressedDrawable = MRaidDrawables.getDrawableForImage(this, "/assets/drawable/close_button_pressed.png", "close_button_pressed", -16777216);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-16842919}, closeButtonNormalDrawable);
        states.addState(new int[]{16842919}, closeButtonPressedDrawable);
        this.closeRegion.setImageDrawable(states);
        this.closeRegion.setScaleType(ScaleType.CENTER_CROP);
    }

    private void getScreenDimensions() {
        this.deviceHeight = (float) getResources().getDisplayMetrics().heightPixels;
        this.deviceWidth = (float) getResources().getDisplayMetrics().widthPixels;
    }

    private void setCloseRegionPosition() {
        getScreenDimensions();
        int width = (int) (this.deviceWidth * CLOSE_REGION_WIDTH_OFFSET);
        this.skipParams.topMargin = (int) (this.deviceHeight * CLOSE_REGION_HEIGHT_OFFSET);
        this.skipParams.leftMargin = width;
        this.closeRegion.setLayoutParams(this.skipParams);
    }

    private void setCloseRegionPositionAndSize() {
        int size = (int) TypedValue.applyDimension(1, CLOSE_REGION_SIZE, getResources().getDisplayMetrics());
        this.skipParams = new LayoutParams(size, size);
        setCloseRegionPosition();
        this.layout.addView(this.closeRegion);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setCloseRegionPosition();
    }

    protected void onStop() {
        super.onStop();
        this.videoView.stopPlayback();
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent i = new Intent();
        i.putExtra("rti", this.shouldReturnToInterstitial);
        setResult(-1, i);
        finish();
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        FuseLog.w(TAG, "ERROR LOADING VIDEO!");
        Intent intent = new Intent();
        intent.putExtra("rti", this.shouldReturnToInterstitial);
        setResult(0, intent);
        finish();
        return false;
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        this.videoView.start();
    }

    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("rti", this.shouldReturnToInterstitial);
        setResult(0, i);
        finish();
    }
}
