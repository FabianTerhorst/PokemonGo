package com.upsight.mediation.log;

import android.os.Build.VERSION;
import android.util.Log;
import android.webkit.WebView;

public class FuseLog {
    private static boolean INTERNAL = false;
    public static boolean LOG = true;
    private static LogBuffer buffer = new LogBuffer(100, 2000);
    public static boolean debug = false;
    private static boolean testingMode = false;
    public static boolean veryDebug = false;

    public static void enableInternalLogging() {
        buffer = new LogBuffer(200, 2000);
        debug = true;
        veryDebug = true;
        INTERNAL = true;
        if (VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public static void clearBuffer() {
        if (INTERNAL) {
            buffer = new LogBuffer(200, 2000);
        }
    }

    public static void public_e(String tag, String s) {
        if (!testingMode) {
            buffer.append("e", tag, s);
            Log.e(tag, s);
        }
    }

    public static void public_e(String tag, String s, Exception e) {
        if (!testingMode) {
            buffer.append("e", tag, s);
            Log.e(tag, s);
        }
    }

    public static void public_w(String tag, String s) {
        if (!testingMode) {
            buffer.append("w", tag, s);
            Log.w(tag, s);
        }
    }

    public static void public_w(String tag, String s, Throwable th) {
        if (!testingMode) {
            buffer.append("w", tag, s);
            Log.w(tag, s, th);
        }
    }

    public static void e(String tag, String s) {
        if (!testingMode) {
            buffer.append("e", tag, s);
            if (debug) {
                Log.e(tag, s);
            }
        }
    }

    public static void e(String tag, String s, Throwable th) {
        if (!testingMode) {
            buffer.append("e", tag, s);
            if (debug) {
                Log.e(tag, s, th);
            }
        }
    }

    public static void w(String tag, String s) {
        if (!testingMode) {
            buffer.append("w", tag, s);
            if (debug) {
                Log.w(tag, s);
            }
        }
    }

    public static void w(String tag, String s, Throwable th) {
        if (!testingMode) {
            buffer.append("w", tag, s);
            if (debug) {
                Log.w(tag, s, th);
            }
        }
    }

    public static void i(String tag, String s) {
        if (!testingMode) {
            buffer.append("i", tag, s);
            if (debug) {
                Log.i(tag, s);
            }
        }
    }

    public static void d(String tag, String s) {
        if (!testingMode) {
            buffer.append("d", tag, s);
            if (veryDebug) {
                Log.d(tag, s);
            }
        }
    }

    public static void v(String tag, String s) {
        if (!testingMode) {
            buffer.append("v", tag, s);
            if (veryDebug) {
                Log.v(tag, s);
            }
        }
    }

    public static void internal(String tag, String s) {
        if (!testingMode) {
            buffer.append("INTERNAL", tag, s);
            if (INTERNAL) {
                Log.i(tag, "INTERNAL | " + s);
            }
        }
    }

    public static String[] getLogHistory() {
        return buffer.getLog();
    }

    public static void TOAST(String text) {
        if (!testingMode) {
        }
    }

    public static void disableForTests() {
        testingMode = true;
    }
}
