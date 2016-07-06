package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.unity3d.player.UnityPlayer;
import java.lang.ref.WeakReference;
import java.util.List;
import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.Console.Options;
import spacemadness.com.lunarconsole.console.ConsoleEntryDispatcher.OnDispatchListener;
import spacemadness.com.lunarconsole.console.ConsoleView.Listener;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.debug.Tags;
import spacemadness.com.lunarconsole.ui.GestureRecognizer;
import spacemadness.com.lunarconsole.ui.GestureRecognizer.OnGestureListener;
import spacemadness.com.lunarconsole.ui.SwipeGestureRecognizer;
import spacemadness.com.lunarconsole.ui.SwipeGestureRecognizer.SwipeDirection;
import spacemadness.com.lunarconsole.utils.ThreadUtils;
import spacemadness.com.lunarconsole.utils.UIUtils;

public class ConsolePlugin implements Listener, WarningView.Listener, Destroyable {
    private static final ConsoleEntryDispatcher entryDispatcher = new ConsoleEntryDispatcher(new OnDispatchListener() {
        public void onDispatchEntries(List<ConsoleEntry> entries) {
            if (ConsolePlugin.instance != null) {
                ConsolePlugin.instance.logEntries(entries);
            } else {
                Log.e("Can't log message: plugin instance is not initialized", new Object[0]);
            }
        }
    });
    private static ConsolePlugin instance;
    private final WeakReference<Activity> activityRef;
    private final Console console;
    private ConsoleView consoleView;
    private final GestureRecognizer gestureDetector;
    private final ConsolePluginImp pluginImp;
    private final String version;
    private WarningView warningView;

    public static void init(String version, int capacity, int trim) {
        Activity activity = UnityPlayer.currentActivity;
        init(activity, version, capacity, trim, new UnityPluginImp(activity));
    }

    public static void init(Activity activity, String version, int capacity, int trim) {
        init(activity, version, capacity, trim, new DefaultPluginImp(activity));
    }

    private static void init(Activity activity, String version, int capacity, int trim, ConsolePluginImp pluginImp) {
        if (ThreadUtils.isRunningOnMainThread()) {
            init0(activity, version, capacity, trim, pluginImp);
            return;
        }
        Log.d(Tags.PLUGIN, "Tried to initialize plugin on the secondary thread. Scheduling on UI-thread...", new Object[0]);
        final Activity activity2 = activity;
        final String str = version;
        final int i = capacity;
        final int i2 = trim;
        final ConsolePluginImp consolePluginImp = pluginImp;
        ThreadUtils.runOnUIThread(new Runnable() {
            public void run() {
                ConsolePlugin.init0(activity2, str, i, i2, consolePluginImp);
            }
        });
    }

    private static void init0(Activity activity, String version, int capacity, int trim, ConsolePluginImp pluginImp) {
        try {
            if (instance == null) {
                Log.d(Tags.PLUGIN, "Initializing plugin instance (%s): %d", version, Integer.valueOf(capacity));
                instance = new ConsolePlugin(activity, version, capacity, trim, pluginImp);
                instance.enableGestureRecognition();
                return;
            }
            Log.w("Plugin instance already initialized", new Object[0]);
        } catch (Throwable e) {
            Log.e(e, "Can't initialize plugin instance", new Object[0]);
        }
    }

    public static void shutdown() {
        if (ThreadUtils.isRunningOnMainThread()) {
            shutdown0();
            return;
        }
        Log.d(Tags.PLUGIN, "Tried to shutdown plugin on the secondary thread. Scheduling on UI-thread...", new Object[0]);
        ThreadUtils.runOnUIThread(new Runnable() {
            public void run() {
                ConsolePlugin.shutdown0();
            }
        });
    }

    private static void shutdown0() {
        try {
            if (instance != null) {
                instance.destroy();
                instance = null;
            }
        } catch (Throwable e) {
            Log.e(e, "Error while shutting down the plugin", new Object[0]);
        }
    }

    public static void logMessage(String message, String stackTrace, int logType) {
        entryDispatcher.add(new ConsoleEntry((byte) logType, message, stackTrace));
    }

    public static void show() {
        if (ThreadUtils.isRunningOnMainThread()) {
            show0();
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                public void run() {
                    ConsolePlugin.show0();
                }
            });
        }
    }

    private static void show0() {
        if (instance != null) {
            instance.showConsole();
        } else {
            Log.w("Can't show console: instance is not initialized", new Object[0]);
        }
    }

    public static void hide() {
        if (ThreadUtils.isRunningOnMainThread()) {
            hide0();
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                public void run() {
                    ConsolePlugin.hide0();
                }
            });
        }
    }

    private static void hide0() {
        if (instance != null) {
            instance.hideConsole();
        } else {
            Log.w("Can't hide console: instance is not initialized", new Object[0]);
        }
    }

    public static void clear() {
        if (ThreadUtils.isRunningOnMainThread()) {
            clear0();
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                public void run() {
                    ConsolePlugin.clear0();
                }
            });
        }
    }

    private static void clear0() {
        if (instance != null) {
            instance.clearConsole();
        }
    }

    private ConsolePlugin(Activity activity, String version, int capacity, int trim, ConsolePluginImp pluginImp) {
        if (activity == null) {
            throw new NullPointerException("Context is null");
        } else if (version == null) {
            throw new NullPointerException("Version is null");
        } else {
            this.version = version;
            this.pluginImp = pluginImp;
            Options options = new Options(capacity);
            options.setTrimCount(trim);
            this.console = new Console(options);
            this.activityRef = new WeakReference(activity);
            this.gestureDetector = new SwipeGestureRecognizer(SwipeDirection.Down, UIUtils.dpToPx(activity, 100.0f));
            this.gestureDetector.setListener(new OnGestureListener() {
                public void onGesture(GestureRecognizer gestureRecognizer) {
                    ConsolePlugin.this.showConsole();
                }
            });
        }
    }

    public void destroy() {
        disableGestureRecognition();
        this.console.destroy();
        entryDispatcher.cancelAll();
        Log.d(Tags.PLUGIN, "Plugin destroyed", new Object[0]);
    }

    private void logEntries(List<ConsoleEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            ConsoleEntry entry = (ConsoleEntry) entries.get(i);
            this.console.logMessage(entry);
            if (ConsoleLogType.isErrorType(entry.type) && !isConsoleShown()) {
                showWarning(entry.message);
            }
        }
    }

    private boolean showConsole() {
        try {
            if (this.consoleView == null) {
                Log.d(Tags.CONSOLE, "Show console", new Object[0]);
                Activity activity = getActivity();
                if (activity == null) {
                    Log.e("Can't show console: activity reference is lost", new Object[0]);
                    return false;
                }
                FrameLayout rootLayout = UIUtils.getRootLayout(activity);
                this.consoleView = new ConsoleView(activity, this.console);
                this.consoleView.setListener(this);
                this.consoleView.requestFocus();
                rootLayout.addView(this.consoleView, new LayoutParams(-1, -1));
                this.consoleView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.lunar_console_slide_in_top));
                disableGestureRecognition();
                return true;
            }
            Log.w("Console is show already", new Object[0]);
            return false;
        } catch (Throwable e) {
            Log.e(e, "Can't show console", new Object[0]);
            return false;
        }
    }

    private boolean hideConsole() {
        try {
            if (this.consoleView == null) {
                return false;
            }
            Log.d(Tags.CONSOLE, "Hide console", new Object[0]);
            Activity activity = getActivity();
            if (activity != null) {
                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.lunar_console_slide_out_top);
                animation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        ConsolePlugin.this.removeConsoleView();
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                this.consoleView.startAnimation(animation);
            } else {
                removeConsoleView();
            }
            return true;
        } catch (Throwable e) {
            Log.e(e, "Can't hide console", new Object[0]);
            return false;
        }
    }

    private void removeConsoleView() {
        if (this.consoleView != null) {
            ViewParent parent = this.consoleView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.consoleView);
            } else {
                Log.e("Can't remove console view: unexpected parent " + parent, new Object[0]);
            }
            this.consoleView.destroy();
            this.consoleView = null;
            enableGestureRecognition();
        }
    }

    private void clearConsole() {
        try {
            this.console.clear();
        } catch (Throwable e) {
            Log.e(e, "Can't clear console", new Object[0]);
        }
    }

    private void showWarning(String message) {
        try {
            if (this.warningView == null) {
                Log.d(Tags.WARNING_VIEW, "Show warning", new Object[0]);
                Activity activity = getActivity();
                if (activity == null) {
                    Log.e("Can't show warning: activity reference is lost", new Object[0]);
                    return;
                }
                FrameLayout rootLayout = UIUtils.getRootLayout(activity);
                this.warningView = new WarningView(activity);
                this.warningView.setListener(this);
                rootLayout.addView(this.warningView);
            }
            this.warningView.setMessage(message);
        } catch (Throwable e) {
            Log.e(e, "Can't show warning", new Object[0]);
        }
    }

    private void hideWarning() {
        if (this.warningView != null) {
            Log.d(Tags.WARNING_VIEW, "Hide warning", new Object[0]);
            ViewParent parent = this.warningView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.warningView);
            } else {
                Log.e("Can't hide warning view: unexpected parent view " + parent, new Object[0]);
            }
            this.warningView.destroy();
            this.warningView = null;
        }
    }

    public void onClose(ConsoleView view) {
        hideConsole();
    }

    public void onDismissClick(WarningView view) {
        hideWarning();
    }

    public void onDetailsClick(WarningView view) {
        hideWarning();
        showConsole();
    }

    public void enableGestureRecognition() {
        Log.d(Tags.GESTURES, "Enable gesture recognition", new Object[0]);
        View view = this.pluginImp.getTouchRecepientView();
        if (view == null) {
            Log.w("Can't enable gesture recognition: touch view is null", new Object[0]);
        } else {
            view.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    ConsolePlugin.this.gestureDetector.onTouchEvent(event);
                    return false;
                }
            });
        }
    }

    public void disableGestureRecognition() {
        Log.d(Tags.GESTURES, "Disable gesture recognition", new Object[0]);
        View view = this.pluginImp.getTouchRecepientView();
        if (view != null) {
            view.setOnTouchListener(null);
        } else {
            Log.w("Can't disable gesture recognition: touch view is null", new Object[0]);
        }
    }

    private Activity getActivity() {
        return (Activity) this.activityRef.get();
    }

    public boolean isConsoleShown() {
        return this.consoleView != null;
    }

    public static String getVersion() {
        return instance != null ? instance.version : "?.?.?";
    }
}
