package com.unity3d.player;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;

public final class d implements f {
    private static final SurfaceTexture a = new SurfaceTexture(-1);
    private static final int b = (q.f ? 5894 : 1);
    private volatile boolean c;

    private void a(final View view, int i) {
        Handler handler = view.getHandler();
        if (handler == null) {
            a(view, this.c);
        } else {
            handler.postDelayed(new Runnable(this) {
                final /* synthetic */ d b;

                public final void run() {
                    this.b.a(view, this.b.c);
                }
            }, 1000);
        }
    }

    public final void a(final View view) {
        if (!q.g) {
            view.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener(this) {
                final /* synthetic */ d b;

                public final void onSystemUiVisibilityChange(int i) {
                    this.b.a(view, 1000);
                }
            });
        }
    }

    public final void a(View view, boolean z) {
        this.c = z;
        view.setSystemUiVisibility(this.c ? view.getSystemUiVisibility() | b : view.getSystemUiVisibility() & (b ^ -1));
    }

    public final boolean a(Camera camera) {
        try {
            camera.setPreviewTexture(a);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final void b(View view) {
        if (!q.f && this.c) {
            a(view, false);
            this.c = true;
        }
        a(view, 1000);
    }
}
