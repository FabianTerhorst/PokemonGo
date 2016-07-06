package com.unity3d.player;

import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class l implements h {
    private Choreographer a = null;
    private long b = 0;
    private FrameCallback c;
    private Lock d = new ReentrantLock();

    public final void a() {
        this.d.lock();
        if (this.a != null) {
            this.a.removeFrameCallback(this.c);
        }
        this.a = null;
        this.d.unlock();
    }

    public final void a(final UnityPlayer unityPlayer) {
        this.d.lock();
        if (this.a == null) {
            this.a = Choreographer.getInstance();
            if (this.a != null) {
                m.Log(4, "Choreographer available: Enabling VSYNC timing");
                this.c = new FrameCallback(this) {
                    final /* synthetic */ l b;

                    public final void doFrame(long j) {
                        UnityPlayer.lockNativeAccess();
                        if (v.c()) {
                            unityPlayer.nativeAddVSyncTime(j);
                        }
                        UnityPlayer.unlockNativeAccess();
                        this.b.d.lock();
                        if (this.b.a != null) {
                            this.b.a.postFrameCallback(this.b.c);
                        }
                        this.b.d.unlock();
                    }
                };
                this.a.postFrameCallback(this.c);
            }
        }
        this.d.unlock();
    }
}
