package com.upsight.mediation.vast.activity;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.upsight.mediation.vast.util.Assets;

public class CircleDrawable extends Drawable {
    private static final float STROKE_WIDTH = 2.0f;
    private RectF oval;
    private Paint paint = new Paint(1);
    private float startAngle = 270.0f;
    private float sweepAngle = 0.0f;
    private long totalTime;

    public CircleDrawable(float width, float radius) {
        this.oval = new RectF((width / STROKE_WIDTH) - radius, (width / STROKE_WIDTH) - radius, (width / STROKE_WIDTH) + radius, (width / STROKE_WIDTH) + radius);
        this.paint.setStyle(Style.STROKE);
        this.paint.setColor(-1);
        this.paint.setAlpha(255);
        this.paint.setStrokeWidth((float) Assets.convertToDps(STROKE_WIDTH));
    }

    public void setTimer(long totalTime) {
        this.totalTime = totalTime;
    }

    public void setSweepAngle(float angle) {
        this.sweepAngle = angle;
    }

    public void update(long elapsedTime) {
        this.sweepAngle = 360.0f - ((((float) elapsedTime) / ((float) this.totalTime)) * 360.0f);
    }

    public void draw(Canvas c) {
        c.drawArc(this.oval, this.startAngle, this.sweepAngle, false, this.paint);
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public int getOpacity() {
        return 0;
    }
}
