package com.nianticlabs.nia.contextservice;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public abstract class ContextService {
    private static final Handler handler = new Handler(handlerThread.getLooper());
    private static final HandlerThread handlerThread = new HandlerThread("ContextService");
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    protected final long NULL_POINTER = 0;
    protected final Object callbackLock = new Object();
    protected final Context context;
    protected long nativeClassPointer;
    private Runnable runOnPause = new Runnable() {
        public void run() {
            ContextService.this.onPause();
        }
    };
    private Runnable runOnResume = new Runnable() {
        public void run() {
            ContextService.this.onResume();
        }
    };
    private Runnable runOnStart = new Runnable() {
        public void run() {
            ContextService.this.onStart();
        }
    };
    private Runnable runOnStop = new Runnable() {
        public void run() {
            ContextService.this.onStop();
        }
    };

    public static native void setActivityProviderClass(String str);

    public ContextService(Context context, long nativeClassPointer) {
        this.context = context;
        this.nativeClassPointer = nativeClassPointer;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public final void resetNativeClassPointer() {
        synchronized (this.callbackLock) {
            this.nativeClassPointer = 0;
        }
    }

    public Context getContext() {
        return this.context;
    }

    public static void runOnUiThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public static void runOnServiceHandler(Runnable runnable) {
        handler.post(runnable);
    }

    public static boolean onUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean onServiceThread() {
        return Looper.myLooper() == handlerThread.getLooper();
    }

    public static void assertOnServiceThread() {
        if (!onServiceThread()) {
            throw new RuntimeException("Must be on the service thread");
        }
    }

    public static Looper getServiceLooper() {
        return handlerThread.getLooper();
    }

    public static Handler getServiceHandler() {
        return handler;
    }

    private void invokeOnStart() {
        runOnServiceHandler(this.runOnStart);
    }

    private void invokeOnStop() {
        runOnServiceHandler(this.runOnStop);
    }

    private void invokeOnPause() {
        runOnServiceHandler(this.runOnPause);
    }

    private void invokeOnResume() {
        runOnServiceHandler(this.runOnResume);
    }

    static {
        handlerThread.start();
    }
}
