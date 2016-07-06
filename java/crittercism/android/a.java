package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class a {
    JSONObject a = new JSONObject();

    public static a a(au auVar, List list) {
        try {
            return new a(auVar, list);
        } catch (JSONException e) {
            dx.b("Unable to generate APM request's JSON: " + e);
            return null;
        }
    }

    private a(au auVar, List list) {
        list.size();
        JSONArray jSONArray = new JSONArray();
        JSONArray jSONArray2 = new JSONArray();
        jSONArray2.put(auVar.a());
        jSONArray2.put(auVar.b());
        jSONArray2.put(auVar.c());
        jSONArray2.put(CrittercismConfig.API_VERSION);
        jSONArray2.put(auVar.e());
        jSONArray.put(jSONArray2);
        jSONArray2 = new JSONArray();
        jSONArray2.put(ed.a.a());
        jSONArray2.put(auVar.f());
        jSONArray2.put(auVar.j());
        jSONArray2.put(auVar.i());
        jSONArray2.put(auVar.k());
        jSONArray2.put(auVar.g());
        jSONArray2.put(auVar.h());
        jSONArray.put(jSONArray2);
        JSONArray jSONArray3 = new JSONArray();
        for (c d : list) {
            jSONArray3.put(d.d());
        }
        jSONArray.put(jSONArray3);
        this.a.put("d", jSONArray);
    }
}
