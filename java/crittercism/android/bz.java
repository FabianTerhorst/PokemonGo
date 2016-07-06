package crittercism.android;

import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;

public final class bz extends bq {

    public static class a extends cj {
        public final bq a(File file) {
            return new bz(file);
        }
    }

    private bz(File file) {
        super(file);
    }

    public final Object a() {
        try {
            return new JSONArray((String) super.a());
        } catch (JSONException e) {
            return null;
        }
    }
}
