package crittercism.android;

import android.content.Context;
import org.json.JSONObject;

public class da implements cy {
    private bs a;
    private bs b;

    public static class a implements cz {
        public final /* synthetic */ cy a(bs bsVar, bs bsVar2, String str, Context context, au auVar) {
            return new da(bsVar, bsVar2);
        }
    }

    public da(bs bsVar, bs bsVar2) {
        this.a = bsVar;
        this.b = bsVar2;
    }

    public void a(boolean z, int i, JSONObject jSONObject) {
        Object obj = (z || (i >= 200 && i < 300)) ? 1 : null;
        if (obj != null) {
            this.a.a();
        } else {
            this.a.a(this.b);
        }
    }
}
