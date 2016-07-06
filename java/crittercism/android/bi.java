package crittercism.android;

import android.content.Context;
import android.os.ConditionVariable;
import crittercism.android.bx.k;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bi extends di implements bt {
    private long a = System.currentTimeMillis();
    private volatile long b = 10000;
    private ConditionVariable c = new ConditionVariable(false);
    private ConditionVariable d = new ConditionVariable(false);
    private au e;
    private bs f;
    private bs g;
    private bs h;
    private bs i;
    private URL j;
    private Context k;
    private volatile boolean l = false;

    public bi(Context context, au auVar, bs bsVar, bs bsVar2, bs bsVar3, bs bsVar4, URL url) {
        this.k = context;
        this.f = bsVar;
        this.g = bsVar2;
        this.h = bsVar3;
        this.i = bsVar4;
        this.e = auVar;
        this.j = url;
        bs bsVar5 = this.f;
        if (this != null) {
            synchronized (bsVar5.c) {
                bsVar5.c.add(this);
            }
        }
    }

    public final void a() {
        while (!this.l) {
            this.c.block();
            this.d.block();
            if (!this.l) {
                long currentTimeMillis = this.b - (System.currentTimeMillis() - this.a);
                if (currentTimeMillis > 0) {
                    try {
                        Thread.sleep(currentTimeMillis);
                    } catch (InterruptedException e) {
                    }
                }
                this.a = System.currentTimeMillis();
                bs a = this.f.a(this.k);
                this.f.a(a);
                JSONArray jSONArray = new bo(a).a;
                eb.a(a.a);
                if (jSONArray.length() > 0 && a(jSONArray) != null) {
                    JSONObject a2 = a(jSONArray);
                    try {
                        HttpURLConnection a3 = new dc(this.j).a();
                        OutputStream outputStream = a3.getOutputStream();
                        outputStream.write(a2.toString().getBytes("UTF8"));
                        outputStream.close();
                        a3.getResponseCode();
                        a3.disconnect();
                    } catch (IOException e2) {
                        new StringBuilder("Request failed for ").append(this.j);
                        dx.a();
                    } catch (Throwable e3) {
                        new StringBuilder("Request failed for ").append(this.j);
                        dx.a();
                        dx.a(e3);
                    }
                }
            } else {
                return;
            }
        }
    }

    public final void b() {
        this.c.open();
    }

    public final void c() {
        bs bsVar = this.f;
        this.d.open();
    }

    public final void d() {
        bs bsVar = this.f;
        this.d.close();
    }

    public final void a(int i, TimeUnit timeUnit) {
        this.b = timeUnit.toMillis((long) i);
    }

    private JSONObject a(JSONArray jSONArray) {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("appID", this.e.a());
            jSONObject2.put("deviceID", this.e.c());
            jSONObject2.put("crPlatform", "android");
            jSONObject2.put("crVersion", this.e.d());
            jSONObject2.put("deviceModel", this.e.j());
            jSONObject2.put("osName", "android");
            jSONObject2.put("osVersion", this.e.k());
            jSONObject2.put("carrier", this.e.f());
            jSONObject2.put("mobileCountryCode", this.e.g());
            jSONObject2.put("mobileNetworkCode", this.e.h());
            jSONObject2.put("appVersion", this.e.b());
            jSONObject2.put("locale", new k().a);
            jSONObject.put("appState", jSONObject2);
            jSONObject.put("transactions", jSONArray);
            if (!b(jSONArray)) {
                return jSONObject;
            }
            jSONObject.put("breadcrumbs", new bo(this.g).a);
            jSONObject.put("endpoints", new bo(this.h).a);
            jSONObject.put("systemBreadcrumbs", new bo(this.i).a);
            return jSONObject;
        } catch (JSONException e) {
            return null;
        }
    }

    private static boolean b(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONArray optJSONArray = jSONArray.optJSONArray(i);
            if (optJSONArray != null) {
                try {
                    a k = new bg(optJSONArray).k();
                    if (!(k == a.SUCCESS || k == a.INTERRUPTED || k == a.ABORTED)) {
                        return true;
                    }
                } catch (Throwable e) {
                    dx.a(e);
                } catch (Throwable e2) {
                    dx.a(e2);
                }
            }
        }
        return false;
    }
}
