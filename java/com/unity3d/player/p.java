package com.unity3d.player;

import android.app.Activity;
import android.content.ContextWrapper;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import spacemadness.com.lunarconsole.R;

public final class p implements j {
    private final Queue a = new ConcurrentLinkedQueue();
    private final Activity b;
    private Runnable c = new Runnable(this) {
        final /* synthetic */ p a;

        {
            this.a = r1;
        }

        private static void a(View view, MotionEvent motionEvent) {
            if (q.b) {
                q.j.a(view, motionEvent);
            }
        }

        public final void run() {
            while (true) {
                MotionEvent motionEvent = (MotionEvent) this.a.a.poll();
                if (motionEvent != null) {
                    View decorView = this.a.b.getWindow().getDecorView();
                    int source = motionEvent.getSource();
                    if ((source & 2) != 0) {
                        switch (motionEvent.getAction() & 255) {
                            case R.styleable.AdsAttrs_adSize /*0*/:
                            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            case R.styleable.LoadingImageView_circleCrop /*2*/:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                decorView.dispatchTouchEvent(motionEvent);
                                break;
                            default:
                                AnonymousClass1.a(decorView, motionEvent);
                                break;
                        }
                    } else if ((source & 4) != 0) {
                        decorView.dispatchTrackballEvent(motionEvent);
                    } else {
                        AnonymousClass1.a(decorView, motionEvent);
                    }
                } else {
                    return;
                }
            }
        }
    };

    public p(ContextWrapper contextWrapper) {
        this.b = (Activity) contextWrapper;
    }

    private static int a(PointerCoords[] pointerCoordsArr, float[] fArr, int i) {
        for (int i2 = 0; i2 < pointerCoordsArr.length; i2++) {
            PointerCoords pointerCoords = new PointerCoords();
            pointerCoordsArr[i2] = pointerCoords;
            int i3 = i + 1;
            pointerCoords.orientation = fArr[i];
            int i4 = i3 + 1;
            pointerCoords.pressure = fArr[i3];
            i3 = i4 + 1;
            pointerCoords.size = fArr[i4];
            i4 = i3 + 1;
            pointerCoords.toolMajor = fArr[i3];
            i3 = i4 + 1;
            pointerCoords.toolMinor = fArr[i4];
            i4 = i3 + 1;
            pointerCoords.touchMajor = fArr[i3];
            i3 = i4 + 1;
            pointerCoords.touchMinor = fArr[i4];
            i4 = i3 + 1;
            pointerCoords.x = fArr[i3];
            i = i4 + 1;
            pointerCoords.y = fArr[i4];
        }
        return i;
    }

    private static PointerCoords[] a(int i, float[] fArr) {
        PointerCoords[] pointerCoordsArr = new PointerCoords[i];
        a(pointerCoordsArr, fArr, 0);
        return pointerCoordsArr;
    }

    public final void a(long j, long j2, int i, int i2, int[] iArr, float[] fArr, int i3, float f, float f2, int i4, int i5, int i6, int i7, int i8, long[] jArr, float[] fArr2) {
        if (this.b != null) {
            MotionEvent obtain = MotionEvent.obtain(j, j2, i, i2, iArr, a(i2, fArr), i3, f, f2, i4, i5, i6, i7);
            int i9 = 0;
            for (int i10 = 0; i10 < i8; i10++) {
                PointerCoords[] pointerCoordsArr = new PointerCoords[i2];
                i9 = a(pointerCoordsArr, fArr2, i9);
                obtain.addBatch(jArr[i10], pointerCoordsArr, i3);
            }
            this.a.add(obtain);
            this.b.runOnUiThread(this.c);
        }
    }
}
