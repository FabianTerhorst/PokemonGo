package crittercism.android;

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

public final class ca extends bq {

    public static class a extends cj {
        public final bq a(File file) {
            return new ca(file);
        }
    }

    private ca(File file) {
        super(file);
    }

    public final Object a() {
        try {
            return new JSONObject((String) super.a());
        } catch (JSONException e) {
            return null;
        }
    }
}
