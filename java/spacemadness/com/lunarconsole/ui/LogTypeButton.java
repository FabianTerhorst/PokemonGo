package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;

public class LogTypeButton extends ToggleButton {
    private static final int MAX_COUNT = 999;
    private int count;
    private float offAlpha;

    @TargetApi(21)
    public LogTypeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public LogTypeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LogTypeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LogTypeButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.count = Integer.MAX_VALUE;
        this.offAlpha = 0.25f;
    }

    public void setOn(boolean flag) {
        super.setOn(flag);
        if (VERSION.SDK_INT >= 11) {
            setAlpha(flag ? 1.0f : this.offAlpha);
        }
    }

    public void setCount(int count) {
        if (this.count != count) {
            if (count < MAX_COUNT) {
                setText(Integer.toString(count));
            } else if (this.count < MAX_COUNT) {
                setText("999+");
            }
            this.count = count;
        }
    }

    public void setOffAlpha(float offAlpha) {
        this.offAlpha = offAlpha;
    }

    public float getOffAlpha() {
        return this.offAlpha;
    }
}
