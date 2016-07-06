package crittercism.android;

import org.json.JSONArray;

public final class bo {
    public JSONArray a = new JSONArray();

    public bo(bs bsVar) {
        for (bq a : bsVar.c()) {
            Object a2 = a.a();
            if (a2 != null) {
                this.a.put(a2);
            }
        }
    }
}
