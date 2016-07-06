package spacemadness.com.lunarconsole.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {
    private final Handler handler;

    private static class Holder {
        private static final ThreadUtils INSTANCE = new ThreadUtils();

        private Holder() {
        }
    }

    private ThreadUtils() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    private void postRunnable(Runnable runnable) {
        this.handler.post(runnable);
    }

    private void postRunnable(Runnable runnable, long delay) {
        this.handler.postDelayed(runnable, delay);
    }

    public static void runOnUIThread(Runnable runnable) {
        Holder.INSTANCE.postRunnable(runnable);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        Holder.INSTANCE.postRunnable(runnable, delay);
    }

    public static void cancel(Runnable runnable) {
        Holder.INSTANCE.cancelRunnable(runnable);
    }

    private void cancelRunnable(Runnable runnable) {
        this.handler.removeCallbacks(runnable);
    }

    public static void cancelAll() {
        Holder.INSTANCE.cancelRunnables();
    }

    private void cancelRunnables() {
        this.handler.removeCallbacks(null);
    }

    public static boolean isRunningOnMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
