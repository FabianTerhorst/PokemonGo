package org.fmod;

import android.media.AudioTrack;
import android.util.Log;
import java.nio.ByteBuffer;

public class FMODAudioDevice implements Runnable {
    private static int h = 0;
    private static int i = 1;
    private static int j = 2;
    private static int k = 3;
    private volatile Thread a = null;
    private volatile boolean b = false;
    private AudioTrack c = null;
    private boolean d = false;
    private ByteBuffer e = null;
    private byte[] f = null;
    private volatile a g;

    private native int fmodGetInfo(int i);

    private native int fmodProcess(ByteBuffer byteBuffer);

    private void releaseAudioTrack() {
        if (this.c != null) {
            if (this.c.getState() == 1) {
                this.c.stop();
            }
            this.c.release();
            this.c = null;
        }
        this.e = null;
        this.f = null;
        this.d = false;
    }

    public synchronized void close() {
        stop();
    }

    native int fmodProcessMicData(ByteBuffer byteBuffer, int i);

    public boolean isRunning() {
        return this.a != null && this.a.isAlive();
    }

    public void run() {
        int i = 3;
        while (this.b) {
            int i2;
            if (this.d || i <= 0) {
                i2 = i;
            } else {
                releaseAudioTrack();
                int fmodGetInfo = fmodGetInfo(h);
                int round = Math.round(((float) AudioTrack.getMinBufferSize(fmodGetInfo, 3, 2)) * 1.1f) & -4;
                int fmodGetInfo2 = fmodGetInfo(i);
                i2 = fmodGetInfo(j);
                if ((fmodGetInfo2 * i2) * 4 > round) {
                    round = (i2 * fmodGetInfo2) * 4;
                }
                this.c = new AudioTrack(3, fmodGetInfo, 3, 2, round, 1);
                this.d = this.c.getState() == 1;
                if (this.d) {
                    this.e = ByteBuffer.allocateDirect((fmodGetInfo2 * 2) * 2);
                    this.f = new byte[this.e.capacity()];
                    this.c.play();
                    i2 = 3;
                } else {
                    Log.e("FMOD", "AudioTrack failed to initialize (status " + this.c.getState() + ")");
                    releaseAudioTrack();
                    i2 = i - 1;
                }
            }
            if (!this.d) {
                i = i2;
            } else if (fmodGetInfo(k) == 1) {
                fmodProcess(this.e);
                this.e.get(this.f, 0, this.e.capacity());
                this.c.write(this.f, 0, this.e.capacity());
                this.e.position(0);
                i = i2;
            } else {
                releaseAudioTrack();
                i = i2;
            }
        }
        releaseAudioTrack();
    }

    public synchronized void start() {
        if (this.a != null) {
            stop();
        }
        this.a = new Thread(this, "FMODAudioDevice");
        this.a.setPriority(10);
        this.b = true;
        this.a.start();
        if (this.g != null) {
            this.g.b();
        }
    }

    public synchronized int startAudioRecord(int i, int i2, int i3) {
        if (this.g == null) {
            this.g = new a(this, i, i2);
            this.g.b();
        }
        return this.g.a();
    }

    public synchronized void stop() {
        while (this.a != null) {
            this.b = false;
            try {
                this.a.join();
                this.a = null;
            } catch (InterruptedException e) {
            }
        }
        if (this.g != null) {
            this.g.c();
        }
    }

    public synchronized void stopAudioRecord() {
        if (this.g != null) {
            this.g.c();
            this.g = null;
        }
    }
}
