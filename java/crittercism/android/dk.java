package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import org.json.JSONException;
import org.json.JSONObject;

public final class dk extends di {
    private ax a;
    private final boolean b;
    private Context c;

    public dk(Context context, ax axVar, boolean z) {
        this.a = axVar;
        this.b = z;
        this.c = context;
    }

    public final void a() {
        new StringBuilder("Setting opt out status to ").append(this.b).append(".  This will take effect in the next user session.");
        dx.b();
        boolean z = this.b;
        ax axVar = this.a;
        String a = cq.OPT_OUT_STATUS_SETTING.a();
        String b = cq.OPT_OUT_STATUS_SETTING.b();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("optOutStatus", z).put("optOutStatusSet", true);
        } catch (JSONException e) {
        }
        axVar.a(a, b, jSONObject.toString());
        if (this.b) {
            Editor edit = this.c.getSharedPreferences("com.crittercism.optmz.config", 0).edit();
            edit.clear();
            edit.commit();
            h.b(this.c);
        }
    }
}
