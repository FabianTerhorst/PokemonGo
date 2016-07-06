package com.unity3d.player;

import android.util.Log;

final class m {
    protected static boolean a = false;

    protected static void Log(int i, String str) {
        if (!a) {
            if (i == 6) {
                Log.e("Unity", str);
            }
            if (i == 5) {
                Log.w("Unity", str);
            }
        }
    }
}
