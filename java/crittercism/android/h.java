package crittercism.android;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public final class h {
    public boolean a = false;
    public boolean b = false;
    public boolean c = false;
    public int d = 10;

    public h(Context context) {
        if (a(context).exists()) {
            this.c = true;
        }
    }

    public h(JSONObject jSONObject) {
        if (jSONObject.has("net")) {
            try {
                JSONObject jSONObject2 = jSONObject.getJSONObject("net");
                this.a = jSONObject2.optBoolean("enabled", false);
                this.b = jSONObject2.optBoolean("persist", false);
                this.c = jSONObject2.optBoolean("kill", false);
                this.d = jSONObject2.optInt("interval", 10);
            } catch (JSONException e) {
            }
        }
    }

    public static File a(Context context) {
        return new File(context.getFilesDir().getAbsolutePath() + "/.crittercism.apm.disabled.");
    }

    public static void b(Context context) {
        try {
            a(context).createNewFile();
        } catch (IOException e) {
            dx.b("Unable to kill APM: " + e.getMessage());
        }
    }

    public final int hashCode() {
        int i;
        int i2 = 1231;
        int i3 = ((this.c ? 1231 : 1237) + 31) * 31;
        if (this.a) {
            i = 1231;
        } else {
            i = 1237;
        }
        i = (i + i3) * 31;
        if (!this.b) {
            i2 = 1237;
        }
        return ((i + i2) * 31) + this.d;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof h)) {
            return false;
        }
        h hVar = (h) obj;
        if (this.c != hVar.c) {
            return false;
        }
        if (this.a != hVar.a) {
            return false;
        }
        if (this.b != hVar.b) {
            return false;
        }
        if (this.d != hVar.d) {
            return false;
        }
        return true;
    }

    public final String toString() {
        return "OptmzConfiguration [\nisSendTaskEnabled=" + this.a + "\n, shouldPersist=" + this.b + "\n, isKilled=" + this.c + "\n, statisticsSendInterval=" + this.d + "]";
    }
}
