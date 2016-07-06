package com.unity3d.player;

import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

abstract class b implements Callback {
    private final Activity a = ((Activity) t.a.a());
    private final int b = 3;
    private SurfaceView c;

    b(int i) {
    }

    final void a() {
        this.a.runOnUiThread(new Runnable(this) {
            final /* synthetic */ b a;

            {
                this.a = r1;
            }

            public final void run() {
                if (this.a.c == null) {
                    this.a.c = new SurfaceView(t.a.a());
                    this.a.c.getHolder().setType(this.a.b);
                    this.a.c.getHolder().addCallback(this.a);
                    t.a.a(this.a.c);
                    this.a.c.setVisibility(0);
                }
            }
        });
    }

    final void b() {
        this.a.runOnUiThread(new Runnable(this) {
            final /* synthetic */ b a;

            {
                this.a = r1;
            }

            public final void run() {
                if (this.a.c != null) {
                    t.a.b(this.a.c);
                }
                this.a.c = null;
            }
        });
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
