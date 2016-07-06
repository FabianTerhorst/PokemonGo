package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class cu implements cw {
    public Map a = new HashMap();

    public static class a implements cx {
        public final /* synthetic */ cw a(au auVar) {
            return new cu(auVar);
        }
    }

    public final /* synthetic */ cw a(bs bsVar) {
        String str = bsVar.b;
        this.a.put(bsVar.b, new bo(bsVar).a);
        return this;
    }

    public cu(au auVar) {
        this.a.put("app_id", auVar.a());
        this.a.put("hashed_device_id", auVar.c());
        this.a.put("library_version", CrittercismConfig.API_VERSION);
    }

    public final cu a(String str, String str2) {
        this.a.put(str, str2);
        return this;
    }

    public final cu a(String str, JSONArray jSONArray) {
        this.a.put(str, jSONArray);
        return this;
    }

    public final void a(OutputStream outputStream) {
        dx.b();
        outputStream.write(new JSONObject(this.a).toString().getBytes("UTF8"));
    }

    public final String toString() {
        String str = null;
        try {
            str = new JSONObject(this.a).toString(4);
        } catch (JSONException e) {
            dx.a();
        }
        return str;
    }
}
