package crittercism.android;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class cs implements cw {
    private Map a = new HashMap();

    static class a {
        boolean a;
        int b;

        public a() {
            this((byte) 0);
        }

        private a(byte b) {
            this.a = false;
            this.b = 0;
            this.a = false;
            this.b = 0;
        }
    }

    public static class b implements cx {
        public final /* synthetic */ cw a(au auVar) {
            return new cs();
        }
    }

    public final /* synthetic */ cw a(bs bsVar) {
        Object obj = null;
        for (bq bqVar : bsVar.c()) {
            Object obj2;
            Object obj3;
            if (bqVar instanceof ca) {
                JSONObject jSONObject = (JSONObject) bqVar.a();
                if (jSONObject == null) {
                    obj2 = null;
                } else {
                    Map hashMap = new HashMap(jSONObject.length());
                    Iterator keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        String str = (String) keys.next();
                        hashMap.put(str, jSONObject.opt(str));
                    }
                    Map map = hashMap;
                }
            } else {
                obj2 = null;
            }
            if (obj2 != null) {
                a aVar = (a) this.a.get(obj2);
                if (aVar == null) {
                    aVar = new a();
                    this.a.put(obj2, aVar);
                }
                r0.b++;
                obj3 = obj2;
            } else {
                obj3 = obj;
            }
            obj = obj3;
        }
        if (obj != null) {
            ((a) this.a.get(obj)).a = true;
        }
        return this;
    }

    private JSONArray a() {
        JSONArray jSONArray = new JSONArray();
        for (Entry entry : this.a.entrySet()) {
            JSONObject jSONObject = new JSONObject((Map) entry.getKey());
            a aVar = (a) entry.getValue();
            try {
                jSONArray.put(new JSONObject().put("appLoads", jSONObject).put("count", aVar.b).put("current", aVar.a));
            } catch (JSONException e) {
            }
        }
        return jSONArray;
    }

    public final void a(OutputStream outputStream) {
        outputStream.write(a().toString().getBytes("UTF8"));
    }

    public final String toString() {
        String str = null;
        try {
            str = a().toString(4);
        } catch (JSONException e) {
            dx.a();
        }
        return str;
    }
}
