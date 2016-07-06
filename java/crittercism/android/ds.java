package crittercism.android;

import org.json.JSONException;
import org.json.JSONObject;

public final class ds {
    private boolean a;
    private boolean b = true;

    public static class a {
        public static ds a(ax axVar) {
            JSONObject jSONObject;
            boolean optBoolean;
            JSONObject jSONObject2 = null;
            String a = axVar.a(cq.OPT_OUT_STATUS_SETTING.a(), cq.OPT_OUT_STATUS_SETTING.b());
            if (a != null) {
                try {
                    jSONObject = new JSONObject(a);
                } catch (JSONException e) {
                    dx.b();
                }
            } else {
                jSONObject = null;
            }
            jSONObject2 = jSONObject;
            if (jSONObject2 != null) {
                optBoolean = jSONObject2.optBoolean("optOutStatusSet", false);
            } else {
                optBoolean = false;
            }
            if (optBoolean) {
                optBoolean = jSONObject2.optBoolean("optOutStatus", false);
            } else {
                optBoolean = axVar.c(cq.OLD_OPT_OUT_STATUS_SETTING.a(), cq.OLD_OPT_OUT_STATUS_SETTING.b());
            }
            return new ds(optBoolean);
        }
    }

    public ds(boolean z) {
        this.a = z;
    }

    public final synchronized boolean a() {
        return this.a;
    }
}
