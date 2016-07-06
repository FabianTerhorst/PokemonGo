package com.unity3d.player;

import android.view.MotionEvent;
import android.view.View;

public final class c implements e {
    public final boolean a(View view, MotionEvent motionEvent) {
        return view.dispatchGenericMotionEvent(motionEvent);
    }
}
