package com.unity3d.player;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public final class k implements g {
    private Object a = new Object[0];
    private Presentation b;
    private DisplayListener c;

    public final void a(Context context) {
        if (this.c != null) {
            DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
            if (displayManager != null) {
                displayManager.unregisterDisplayListener(this.c);
            }
        }
    }

    public final void a(final UnityPlayer unityPlayer, Context context) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
        if (displayManager != null) {
            displayManager.registerDisplayListener(new DisplayListener(this) {
                final /* synthetic */ k b;

                public final void onDisplayAdded(int i) {
                    unityPlayer.displayChanged(-1, null);
                }

                public final void onDisplayChanged(int i) {
                    unityPlayer.displayChanged(-1, null);
                }

                public final void onDisplayRemoved(int i) {
                    unityPlayer.displayChanged(-1, null);
                }
            }, null);
        }
    }

    public final boolean a(final UnityPlayer unityPlayer, final Context context, int i) {
        synchronized (this.a) {
            Display display;
            if (this.b != null && this.b.isShowing()) {
                display = this.b.getDisplay();
                if (display != null && display.getDisplayId() == i) {
                    return true;
                }
            }
            DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
            if (displayManager == null) {
                return false;
            }
            display = displayManager.getDisplay(i);
            if (display == null) {
                return false;
            }
            unityPlayer.b(new Runnable(this) {
                final /* synthetic */ k d;

                public final void run() {
                    synchronized (this.d.a) {
                        if (this.d.b != null) {
                            this.d.b.dismiss();
                        }
                        this.d.b = new Presentation(this, context, display) {
                            final /* synthetic */ AnonymousClass2 a;

                            protected final void onCreate(Bundle bundle) {
                                View surfaceView = new SurfaceView(context);
                                surfaceView.getHolder().addCallback(new Callback(this) {
                                    final /* synthetic */ AnonymousClass1 a;

                                    {
                                        this.a = r1;
                                    }

                                    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                                        unityPlayer.displayChanged(1, surfaceHolder.getSurface());
                                    }

                                    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
                                        unityPlayer.displayChanged(1, surfaceHolder.getSurface());
                                    }

                                    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                                        unityPlayer.displayChanged(1, null);
                                    }
                                });
                                setContentView(surfaceView);
                            }

                            public final void onDisplayRemoved() {
                                dismiss();
                                synchronized (this.a.d.a) {
                                    this.a.d.b = null;
                                }
                            }
                        };
                        this.d.b.show();
                    }
                }
            });
            return true;
        }
    }
}
