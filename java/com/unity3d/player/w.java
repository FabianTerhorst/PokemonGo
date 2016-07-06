package com.unity3d.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import java.io.FileInputStream;
import java.io.IOException;

public final class w extends FrameLayout implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, Callback, MediaPlayerControl {
    private static boolean a = false;
    private final UnityPlayer b;
    private final Context c;
    private final SurfaceView d;
    private final SurfaceHolder e;
    private final String f;
    private final int g;
    private final int h;
    private final boolean i;
    private final long j;
    private final long k;
    private final FrameLayout l;
    private final Display m;
    private int n;
    private int o;
    private int p;
    private int q;
    private MediaPlayer r;
    private MediaController s;
    private boolean t = false;
    private boolean u = false;
    private int v = 0;
    private boolean w = false;
    private int x = 0;
    private boolean y;

    protected w(UnityPlayer unityPlayer, Context context, String str, int i, int i2, int i3, boolean z, long j, long j2) {
        super(context);
        this.b = unityPlayer;
        this.c = context;
        this.l = this;
        this.d = new SurfaceView(context);
        this.e = this.d.getHolder();
        this.e.addCallback(this);
        this.e.setType(3);
        this.l.setBackgroundColor(i);
        this.l.addView(this.d);
        this.m = ((WindowManager) this.c.getSystemService("window")).getDefaultDisplay();
        this.f = str;
        this.g = i2;
        this.h = i3;
        this.i = z;
        this.j = j;
        this.k = j2;
        if (a) {
            a("fileName: " + this.f);
        }
        if (a) {
            a("backgroundColor: " + i);
        }
        if (a) {
            a("controlMode: " + this.g);
        }
        if (a) {
            a("scalingMode: " + this.h);
        }
        if (a) {
            a("isURL: " + this.i);
        }
        if (a) {
            a("videoOffset: " + this.j);
        }
        if (a) {
            a("videoLength: " + this.k);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.y = true;
    }

    private void a() {
        doCleanUp();
        try {
            this.r = new MediaPlayer();
            if (this.i) {
                this.r.setDataSource(this.c, Uri.parse(this.f));
            } else if (this.k != 0) {
                FileInputStream fileInputStream = new FileInputStream(this.f);
                this.r.setDataSource(fileInputStream.getFD(), this.j, this.k);
                fileInputStream.close();
            } else {
                try {
                    AssetFileDescriptor openFd = getResources().getAssets().openFd(this.f);
                    this.r.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                    openFd.close();
                } catch (IOException e) {
                    FileInputStream fileInputStream2 = new FileInputStream(this.f);
                    this.r.setDataSource(fileInputStream2.getFD());
                    fileInputStream2.close();
                }
            }
            this.r.setDisplay(this.e);
            this.r.setScreenOnWhilePlaying(true);
            this.r.setOnBufferingUpdateListener(this);
            this.r.setOnCompletionListener(this);
            this.r.setOnPreparedListener(this);
            this.r.setOnVideoSizeChangedListener(this);
            this.r.setAudioStreamType(3);
            this.r.prepare();
            if (this.g == 0 || this.g == 1) {
                this.s = new MediaController(this.c);
                this.s.setMediaPlayer(this);
                this.s.setAnchorView(this);
                this.s.setEnabled(true);
                this.s.show();
            }
        } catch (Exception e2) {
            if (a) {
                a("error: " + e2.getMessage() + e2);
            }
            onDestroy();
        }
    }

    private static void a(String str) {
        Log.v("Video", "VideoPlayer: " + str);
    }

    private void b() {
        if (!isPlaying()) {
            if (a) {
                a("startVideoPlayback");
            }
            updateVideoLayout();
            if (!this.w) {
                start();
            }
        }
    }

    public final boolean canPause() {
        return true;
    }

    public final boolean canSeekBackward() {
        return true;
    }

    public final boolean canSeekForward() {
        return true;
    }

    protected final void doCleanUp() {
        if (this.r != null) {
            this.r.release();
            this.r = null;
        }
        this.p = 0;
        this.q = 0;
        this.u = false;
        this.t = false;
    }

    public final int getBufferPercentage() {
        return this.i ? this.v : 100;
    }

    public final int getCurrentPosition() {
        return this.r == null ? 0 : this.r.getCurrentPosition();
    }

    public final int getDuration() {
        return this.r == null ? 0 : this.r.getDuration();
    }

    public final boolean isPlaying() {
        boolean z = this.u && this.t;
        return this.r == null ? !z : this.r.isPlaying() || !z;
    }

    public final void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        if (a) {
            a("onBufferingUpdate percent:" + i);
        }
        this.v = i;
    }

    public final void onCompletion(MediaPlayer mediaPlayer) {
        if (a) {
            a("onCompletion called");
        }
        onDestroy();
    }

    public final void onControllerHide() {
    }

    protected final void onDestroy() {
        onPause();
        doCleanUp();
        UnityPlayer.a(new Runnable(this) {
            final /* synthetic */ w a;

            {
                this.a = r1;
            }

            public final void run() {
                this.a.b.hideVideoPlayer();
            }
        });
    }

    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4 && (this.g != 2 || i == 0 || keyEvent.isSystem())) {
            return this.s != null ? this.s.onKeyDown(i, keyEvent) : super.onKeyDown(i, keyEvent);
        } else {
            onDestroy();
            return true;
        }
    }

    protected final void onPause() {
        if (a) {
            a("onPause called");
        }
        if (!this.w) {
            pause();
            this.w = false;
        }
        if (this.r != null) {
            this.x = this.r.getCurrentPosition();
        }
        this.y = false;
    }

    public final void onPrepared(MediaPlayer mediaPlayer) {
        if (a) {
            a("onPrepared called");
        }
        this.u = true;
        if (this.u && this.t) {
            b();
        }
    }

    protected final void onResume() {
        if (a) {
            a("onResume called");
        }
        if (!(this.y || this.w)) {
            start();
        }
        this.y = true;
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (this.g != 2 || action != 0) {
            return this.s != null ? this.s.onTouchEvent(motionEvent) : super.onTouchEvent(motionEvent);
        } else {
            onDestroy();
            return true;
        }
    }

    public final void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        if (a) {
            a("onVideoSizeChanged called " + i + "x" + i2);
        }
        if (i != 0 && i2 != 0) {
            this.t = true;
            this.p = i;
            this.q = i2;
            if (this.u && this.t) {
                b();
            }
        } else if (a) {
            a("invalid video width(" + i + ") or height(" + i2 + ")");
        }
    }

    public final void pause() {
        if (this.r != null) {
            this.r.pause();
            this.w = true;
        }
    }

    public final void seekTo(int i) {
        if (this.r != null) {
            this.r.seekTo(i);
        }
    }

    public final void start() {
        if (this.r != null) {
            this.r.start();
            this.w = false;
        }
    }

    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (a) {
            a("surfaceChanged called " + i + " " + i2 + "x" + i3);
        }
        if (this.n != i2 || this.o != i3) {
            this.n = i2;
            this.o = i3;
            updateVideoLayout();
        }
    }

    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (a) {
            a("surfaceCreated called");
        }
        a();
        seekTo(this.x);
    }

    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (a) {
            a("surfaceDestroyed called");
        }
        doCleanUp();
    }

    protected final void updateVideoLayout() {
        if (a) {
            a("updateVideoLayout");
        }
        if (this.n == 0 || this.o == 0) {
            WindowManager windowManager = (WindowManager) this.c.getSystemService("window");
            this.n = windowManager.getDefaultDisplay().getWidth();
            this.o = windowManager.getDefaultDisplay().getHeight();
        }
        int i = this.n;
        int i2 = this.o;
        float f = ((float) this.p) / ((float) this.q);
        float f2 = ((float) this.n) / ((float) this.o);
        if (this.h == 1) {
            if (f2 <= f) {
                i2 = (int) (((float) this.n) / f);
            } else {
                i = (int) (((float) this.o) * f);
            }
        } else if (this.h == 2) {
            if (f2 >= f) {
                i2 = (int) (((float) this.n) / f);
            } else {
                i = (int) (((float) this.o) * f);
            }
        } else if (this.h == 0) {
            i = this.p;
            i2 = this.q;
        }
        if (a) {
            a("frameWidth = " + i + "; frameHeight = " + i2);
        }
        this.l.updateViewLayout(this.d, new LayoutParams(i, i2, 17));
    }
}
