package com.upsight.mediation.mraid;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.upsight.mediation.log.FuseLog;
import java.io.IOException;
import java.io.InputStream;

public class MRaidDrawables {
    private static final String TAG = "MRaidDrawables";

    public static Drawable getDrawableForImage(Context context, String assetPath, String idName, int fallbackColor) {
        InputStream stream = context.getClass().getResourceAsStream(assetPath);
        if (stream != null) {
            Drawable drawable = new BitmapDrawable(context.getResources(), stream);
            try {
                stream.close();
                return drawable;
            } catch (IOException e) {
                return drawable;
            }
        }
        int id = context.getResources().getIdentifier(idName, "drawable", context.getPackageName());
        if (id != 0) {
            return context.getResources().getDrawable(id);
        }
        FuseLog.w(TAG, "Could not load button image: " + idName);
        return new ColorDrawable(fallbackColor);
    }
}
