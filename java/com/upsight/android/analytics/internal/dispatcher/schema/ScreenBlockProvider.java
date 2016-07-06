package com.upsight.android.analytics.internal.dispatcher.schema;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.logger.UpsightLogger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ScreenBlockProvider extends UpsightDataProvider {
    private static final float ANDROID_SCREEN_SCALE = 1.0f;
    public static final String DPI_KEY = "screen.dpi";
    public static final String HEIGHT_KEY = "screen.height";
    public static final String SCALE_KEY = "screen.scale";
    public static final String WIDTH_KEY = "screen.width";
    private final UpsightLogger mLogger;

    ScreenBlockProvider(UpsightContext upsight) {
        put(SCALE_KEY, Float.valueOf(ANDROID_SCREEN_SCALE));
        this.mLogger = upsight.getLogger();
        DisplayMetrics metrics = getDefaultDisplayMetrics(upsight);
        if (metrics != null) {
            put(DPI_KEY, Integer.valueOf(metrics.densityDpi));
            put(WIDTH_KEY, Integer.valueOf(metrics.widthPixels));
            put(HEIGHT_KEY, Integer.valueOf(metrics.heightPixels));
        }
    }

    private DisplayMetrics getDefaultDisplayMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService("window");
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            if (display != null) {
                display.getMetrics(metrics);
            }
        } else {
            this.mLogger.e("ScreenBlock", "Could not retrieve screen size, windowManager=null", new Object[0]);
        }
        return metrics;
    }

    public Set<String> availableKeys() {
        return new HashSet(Arrays.asList(new String[]{WIDTH_KEY, HEIGHT_KEY, SCALE_KEY, DPI_KEY}));
    }
}
