package crittercism.android;

import android.util.SparseArray;

public enum b {
    MOBILE(0),
    WIFI(1),
    UNKNOWN(2),
    NOT_CONNECTED(3);
    
    private static SparseArray e;
    private int f;

    static {
        SparseArray sparseArray = new SparseArray();
        e = sparseArray;
        sparseArray.put(0, MOBILE);
        e.put(1, WIFI);
    }

    private b(int i) {
        this.f = i;
    }

    public final int a() {
        return this.f;
    }

    public static b a(int i) {
        b bVar = (b) e.get(i);
        if (bVar == null) {
            return UNKNOWN;
        }
        return bVar;
    }
}
