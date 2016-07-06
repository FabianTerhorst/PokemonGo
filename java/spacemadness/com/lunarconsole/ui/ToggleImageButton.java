package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ToggleImageButton extends ImageButton {
    private Drawable offDrawable;
    private boolean on;
    private Drawable onDrawable;
    private OnStateChangeListener stateChangeListener;

    public interface OnStateChangeListener {
        void onStateChanged(ToggleImageButton toggleImageButton);
    }

    public ToggleImageButton(Context context) {
        super(context);
        init();
    }

    public ToggleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ToggleImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ToggleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ToggleImageButton.this.setOn(!ToggleImageButton.this.on);
            }
        });
    }

    private void notifyStateChanged() {
        if (this.stateChangeListener != null) {
            this.stateChangeListener.onStateChanged(this);
        }
    }

    public boolean isOn() {
        return this.on;
    }

    public void setOn(boolean flag) {
        boolean oldFlag = this.on;
        this.on = flag;
        Drawable stateDrawable = this.on ? this.onDrawable : this.offDrawable;
        if (stateDrawable != null) {
            setImageDrawable(stateDrawable);
        }
        if (oldFlag != flag) {
            notifyStateChanged();
        }
    }

    public OnStateChangeListener getOnStateChangeListener() {
        return this.stateChangeListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.stateChangeListener = onStateChangeListener;
    }

    public Drawable getOnDrawable() {
        return this.onDrawable;
    }

    public void setOnDrawable(Drawable onDrawable) {
        this.onDrawable = onDrawable;
    }

    public Drawable getOffDrawable() {
        return this.offDrawable;
    }

    public void setOffDrawable(Drawable offDrawable) {
        this.offDrawable = offDrawable;
    }
}
