package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

final class a implements Runnable {
    private final FMODAudioDevice a;
    private final ByteBuffer b;
    private final int c;
    private final int d;
    private final int e = 2;
    private volatile Thread f;
    private volatile boolean g;
    private AudioRecord h;
    private boolean i;

    a(FMODAudioDevice fMODAudioDevice, int i, int i2) {
        this.a = fMODAudioDevice;
        this.c = i;
        this.d = i2;
        this.b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(i, i2, 2));
    }

    private void d() {
        if (this.h != null) {
            if (this.h.getState() == 1) {
                this.h.stop();
            }
            this.h.release();
            this.h = null;
        }
        this.b.position(0);
        this.i = false;
    }

    public final int a() {
        return this.b.capacity();
    }

    public final void b() {
        if (this.f != null) {
            c();
        }
        this.g = true;
        this.f = new Thread(this);
        this.f.start();
    }

    public final void c() {
        while (this.f != null) {
            this.g = false;
            try {
                this.f.join();
                this.f = null;
            } catch (InterruptedException e) {
            }
        }
    }

    public final void run() {
        int i = 3;
        while (this.g) {
            int i2;
            if (!this.i && i > 0) {
                d();
                this.h = new AudioRecord(1, this.c, this.d, this.e, this.b.capacity());
                this.i = this.h.getState() == 1;
                if (this.i) {
                    this.b.position(0);
                    this.h.startRecording();
                    i2 = 3;
                    if (this.i || this.h.getRecordingState() != 3) {
                        i = i2;
                    } else {
                        this.a.fmodProcessMicData(this.b, this.h.read(this.b, this.b.capacity()));
                        this.b.position(0);
                        i = i2;
                    }
                } else {
                    Log.e("FMOD", "AudioRecord failed to initialize (status " + this.h.getState() + ")");
                    i--;
                    d();
                }
            }
            i2 = i;
            if (this.i) {
            }
            i = i2;
        }
        d();
    }
}
