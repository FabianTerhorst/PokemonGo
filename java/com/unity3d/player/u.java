package com.unity3d.player;

import android.os.Build;
import java.lang.Thread.UncaughtExceptionHandler;

final class u implements UncaughtExceptionHandler {
    private volatile UncaughtExceptionHandler a;

    u() {
    }

    final synchronized boolean a() {
        boolean z;
        u defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler == this) {
            z = false;
        } else {
            this.a = defaultUncaughtExceptionHandler;
            Thread.setDefaultUncaughtExceptionHandler(this);
            z = true;
        }
        return z;
    }

    public final synchronized void uncaughtException(Thread thread, Throwable th) {
        try {
            Throwable error = new Error(String.format("FATAL EXCEPTION [%s]\n", new Object[]{thread.getName()}) + String.format("Unity version     : %s\n", new Object[]{"5.3.5f1"}) + String.format("Device model      : %s %s\n", new Object[]{Build.MANUFACTURER, Build.MODEL}) + String.format("Device fingerprint: %s\n", new Object[]{Build.FINGERPRINT}));
            error.setStackTrace(new StackTraceElement[0]);
            error.initCause(th);
            this.a.uncaughtException(thread, error);
        } catch (Throwable th2) {
            this.a.uncaughtException(thread, th);
        }
    }
}
