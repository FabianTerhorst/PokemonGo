package crittercism.android;

import crittercism.android.bx.k;
import java.io.OutputStream;
import org.json.JSONException;
import org.json.JSONObject;

public final class bm implements ch {
    private JSONObject a;
    private String b = cg.a.a();

    public bm(au auVar) {
        try {
            this.a = new JSONObject().put("appID", auVar.a()).put("deviceID", auVar.c()).put("crPlatform", "android").put("crVersion", auVar.d()).put("deviceModel", auVar.j()).put("osName", "android").put("osVersion", auVar.k()).put("carrier", auVar.f()).put("mobileCountryCode", auVar.g()).put("mobileNetworkCode", auVar.h()).put("appVersion", auVar.b()).put("locale", new k().a);
        } catch (JSONException e) {
        }
    }

    public final void a(OutputStream outputStream) {
        outputStream.write(this.a.toString().getBytes());
    }

    public final String e() {
        return this.b;
    }
}
