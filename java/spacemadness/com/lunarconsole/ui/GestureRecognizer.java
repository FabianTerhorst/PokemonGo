package spacemadness.com.lunarconsole.ui;

import android.view.MotionEvent;
import spacemadness.com.lunarconsole.debug.Log;

public abstract class GestureRecognizer<T extends GestureRecognizer> {
    private OnGestureListener<T> listener;

    public interface OnGestureListener<T extends GestureRecognizer> {
        void onGesture(T t);
    }

    public abstract boolean onTouchEvent(MotionEvent motionEvent);

    protected void notifyGestureRecognizer() {
        if (this.listener != null) {
            try {
                this.listener.onGesture(this);
            } catch (Throwable e) {
                Log.e(e, "Error while notifying gesture listener", new Object[0]);
            }
        }
    }

    public OnGestureListener<T> getListener() {
        return this.listener;
    }

    public void setListener(OnGestureListener<T> listener) {
        this.listener = listener;
    }
}
