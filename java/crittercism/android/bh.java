package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

public final class bh {
    public boolean a = false;
    public int b = 10;
    public int c = 3600000;
    public JSONObject d = new JSONObject();

    public static bh a(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.crittercism.txn.config", 0);
        bh bhVar = new bh();
        bhVar.a = sharedPreferences.getBoolean("enabled", false);
        bhVar.b = sharedPreferences.getInt("interval", 10);
        bhVar.c = sharedPreferences.getInt("defaultTimeout", 3600000);
        String string = sharedPreferences.getString("transactions", null);
        bhVar.d = new JSONObject();
        if (string != null) {
            try {
                bhVar.d = new JSONObject(string);
            } catch (JSONException e) {
            }
        }
        return bhVar;
    }

    bh() {
    }

    public bh(JSONObject jSONObject) {
        this.a = jSONObject.optBoolean("enabled", false);
        this.b = jSONObject.optInt("interval", 10);
        this.c = jSONObject.optInt("defaultTimeout", 3600000);
        this.d = jSONObject.optJSONObject("transactions");
        if (this.d == null) {
            this.d = new JSONObject();
        }
    }

    public final long a(String str) {
        JSONObject optJSONObject = this.d.optJSONObject(str);
        if (optJSONObject != null) {
            return optJSONObject.optLong("timeout", (long) this.c);
        }
        return (long) this.c;
    }
}
