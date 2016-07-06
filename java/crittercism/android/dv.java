package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.voxelbusters.nativeplugins.defines.Keys.Ui;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class dv {
    private SharedPreferences a;

    public dv(Context context, String str) {
        this.a = context.getSharedPreferences("com.crittercism." + str + ".usermetadata", 0);
        if (!this.a.contains(ModelColumns.DATA)) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.putOpt(Ui.USER_NAME, "anonymous");
                a(jSONObject);
            } catch (JSONException e) {
            }
        }
    }

    public final void a(JSONObject jSONObject) {
        JSONObject a = a();
        if (a.length() != 0) {
            Iterator keys = jSONObject.keys();
            boolean z = false;
            while (keys.hasNext()) {
                String str = (String) keys.next();
                Object opt = jSONObject.opt(str);
                Object opt2 = a.opt(str);
                boolean z2 = opt2 == null;
                if (opt2 != null) {
                    if ((opt instanceof JSONObject) || (opt instanceof JSONArray)) {
                        z2 = !opt2.toString().equals(opt.toString());
                    } else {
                        z2 = !opt2.equals(opt);
                    }
                }
                if (z2) {
                    try {
                        a.put(str, opt);
                        z = true;
                    } catch (JSONException e) {
                    }
                }
            }
            if (z) {
                b(a);
                a(true);
            }
        } else if (jSONObject.length() > 0) {
            b(jSONObject);
            a(true);
        }
    }

    private void b(JSONObject jSONObject) {
        Editor edit = this.a.edit();
        edit.putString(ModelColumns.DATA, jSONObject.toString());
        edit.commit();
    }

    public final JSONObject a() {
        try {
            return new JSONObject(this.a.getString(ModelColumns.DATA, "{}"));
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public final boolean b() {
        return this.a.getBoolean("dirty", false);
    }

    public final void a(boolean z) {
        this.a.edit().putBoolean("dirty", z).commit();
    }
}
