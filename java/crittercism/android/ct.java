package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public final class ct extends da {
    private au a;
    private Context b;
    private String c;
    private JSONObject d;
    private JSONObject e;
    private boolean f;

    public static class a implements cz {
        public final /* synthetic */ cy a(bs bsVar, bs bsVar2, String str, Context context, au auVar) {
            return new ct(bsVar, bsVar2, str, context, auVar);
        }
    }

    public ct(bs bsVar, bs bsVar2, String str, Context context, au auVar) {
        super(bsVar, bsVar2);
        this.c = str;
        this.b = context;
        this.a = auVar;
    }

    public final void a(boolean z, int i, JSONObject jSONObject) {
        super.a(z, i, jSONObject);
        if (jSONObject != null) {
            Editor edit;
            if (jSONObject.optBoolean("internalExceptionReporting", false)) {
                dx.a = crittercism.android.dx.a.ON;
                i.d();
            } else {
                dx.a = crittercism.android.dx.a.OFF;
            }
            dt m = this.a.m();
            if (m != null) {
                JSONObject optJSONObject = jSONObject.optJSONObject("rateMyApp");
                if (optJSONObject == null) {
                    m.a(false);
                } else {
                    try {
                        int i2 = optJSONObject.getInt("rateAfterLoadNum");
                        if (i2 < 0) {
                            i2 = 0;
                        }
                        m.a.edit().putInt("rateAfterNumLoads", i2).commit();
                        i2 = optJSONObject.getInt("remindAfterLoadNum");
                        if (i2 <= 0) {
                            i2 = 1;
                        }
                        m.a.edit().putInt("remindAfterNumLoads", i2).commit();
                        m.a.edit().putString("rateAppMessage", optJSONObject.getString(Keys.MESSAGE)).commit();
                        m.a.edit().putString("rateAppTitle", optJSONObject.getString(Keys.TITLE)).commit();
                        m.a(true);
                    } catch (JSONException e) {
                        m.a(false);
                    }
                }
            }
            if (jSONObject.optInt("needPkg", 0) == 1) {
                try {
                    new dj(new cu(this.a).a("device_name", this.a.i()).a("pkg", this.b.getPackageName()), new dc(new db(this.c, "/android_v2/update_package_name").a()), null).run();
                } catch (IOException e2) {
                    new StringBuilder("IOException in handleResponse(): ").append(e2.getMessage());
                    dx.b();
                    dx.c();
                }
                this.f = true;
            }
            this.d = jSONObject.optJSONObject("apm");
            if (this.d != null) {
                h hVar = new h(this.d);
                Context context = this.b;
                if (hVar.c) {
                    h.b(context);
                } else {
                    File a = h.a(context);
                    if (!a.delete() && a.exists()) {
                        dx.b("Unable to reenable OPTMZ instrumentation");
                    }
                }
                edit = context.getSharedPreferences("com.crittercism.optmz.config", 0).edit();
                if (hVar.b) {
                    edit.putBoolean("enabled", hVar.a);
                    edit.putBoolean("kill", hVar.c);
                    edit.putBoolean("persist", hVar.b);
                    edit.putInt("interval", hVar.d);
                } else {
                    edit.clear();
                }
                edit.commit();
                az.A().a(hVar);
            }
            this.e = jSONObject.optJSONObject("txnConfig");
            if (this.e != null) {
                bh bhVar = new bh(this.e);
                edit = this.b.getSharedPreferences("com.crittercism.txn.config", 0).edit();
                edit.putBoolean("enabled", bhVar.a);
                edit.putInt("interval", bhVar.b);
                edit.putInt("defaultTimeout", bhVar.c);
                edit.putString("transactions", bhVar.d.toString());
                edit.commit();
                az.A().a(bhVar);
            }
        }
    }
}
