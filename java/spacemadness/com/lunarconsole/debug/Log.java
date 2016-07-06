package spacemadness.com.lunarconsole.debug;

import spacemadness.com.lunarconsole.utils.StringUtils;

public class Log {
    private static final String TAG = "LunarConsole";
    private static final LogLevel logLevel = LogLevel.Info;

    public enum LogLevel {
        Crit(6),
        Error(6),
        Warn(5),
        Info(4),
        Debug(3),
        None(-1);
        
        private int androidLogPriority;

        private LogLevel(int priority) {
            this.androidLogPriority = priority;
        }

        public int getAndroidLogPriority() {
            return this.androidLogPriority;
        }
    }

    public static void i(String format, Object... args) {
        i(null, format, args);
    }

    public static void i(Tag tag, String format, Object... args) {
        log(LogLevel.Info, tag, format, args);
    }

    public static void w(String format, Object... args) {
        w(null, format, args);
    }

    public static void w(Tag tag, String format, Object... args) {
        log(LogLevel.Warn, tag, format, args);
    }

    public static void d(String format, Object... args) {
        d(null, format, args);
    }

    public static void d(Tag tag, String format, Object... args) {
        log(LogLevel.Debug, tag, format, args);
    }

    public static void e(Tag tag, String format, Object... args) {
        log(LogLevel.Error, tag, format, args);
    }

    public static void e(String format, Object... args) {
        e((Tag) null, format, args);
    }

    public static void e(Throwable t, String format, Object... args) {
        e(format, args);
        if (t != null) {
            t.printStackTrace();
        }
    }

    private static void log(LogLevel level, Tag tag, String format, Object... args) {
        if (!shouldLogLevel(level) || !shouldLogTag(tag)) {
            return;
        }
        if (format != null) {
            logHelper(level, format, args);
        } else {
            logHelper(level, "null", new Object[0]);
        }
    }

    private static void logHelper(LogLevel level, String format, Object... args) {
        android.util.Log.println(level.getAndroidLogPriority(), "LunarConsole/" + Thread.currentThread().getName(), StringUtils.TryFormat(format, args));
    }

    private static boolean shouldLogLevel(LogLevel level) {
        return level.ordinal() <= logLevel.ordinal();
    }

    private static boolean shouldLogTag(Tag tag) {
        return tag == null || tag.enabled;
    }
}
