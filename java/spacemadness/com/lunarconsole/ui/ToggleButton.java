package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ToggleButton extends Button {
    private boolean on;
    private OnStateChangeListener stateChangeListener;

    public interface OnStateChangeListener {
        void onStateChanged(ToggleButton toggleButton);
    }

    public ToggleButton(Context context) {
        super(context);
        init();
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ToggleButton.this.setOn(!ToggleButton.this.on);
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
        if (this.on != flag) {
            this.on = flag;
            notifyStateChanged();
        }
    }

    public OnStateChangeListener getOnStateChangeListener() {
        return this.stateChangeListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.stateChangeListener = onStateChangeListener;
    }
}
