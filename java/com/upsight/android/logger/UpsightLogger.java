package com.upsight.android.logger;

import java.util.EnumSet;

public interface UpsightLogger {
    public static final int MAX_LENGTH = 4000;

    public enum Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    void d(String str, String str2, Object... objArr);

    void d(String str, Throwable th, String str2, Object... objArr);

    void e(String str, String str2, Object... objArr);

    void e(String str, Throwable th, String str2, Object... objArr);

    void i(String str, String str2, Object... objArr);

    void i(String str, Throwable th, String str2, Object... objArr);

    void setLogLevel(String str, EnumSet<Level> enumSet);

    void v(String str, String str2, Object... objArr);

    void v(String str, Throwable th, String str2, Object... objArr);

    void w(String str, String str2, Object... objArr);

    void w(String str, Throwable th, String str2, Object... objArr);
}
