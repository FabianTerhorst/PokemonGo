package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class ce extends ci {
    private String a;
    private String b;
    private a c;
    private String d;
    private String e;
    private String f;

    public enum a {
        INTERNET_UP,
        INTERNET_DOWN,
        CONN_TYPE_GAINED,
        CONN_TYPE_LOST,
        CONN_TYPE_SWITCHED
    }

    public ce(a aVar) {
        if (aVar != a.INTERNET_UP) {
            a aVar2 = a.INTERNET_DOWN;
        }
        this.a = cg.a.a();
        this.b = ed.a.a();
        this.c = aVar;
    }

    public ce(a aVar, String str) {
        if (aVar != a.CONN_TYPE_GAINED) {
            a aVar2 = a.CONN_TYPE_LOST;
        }
        this.a = cg.a.a();
        this.b = ed.a.a();
        this.c = aVar;
        this.d = str;
    }

    public ce(a aVar, String str, String str2) {
        a aVar2 = a.CONN_TYPE_SWITCHED;
        this.a = cg.a.a();
        this.b = ed.a.a();
        this.c = aVar;
        this.e = str;
        this.f = str2;
    }

    public final String e() {
        return this.a;
    }

    public final JSONArray a() {
        Map hashMap = new HashMap();
        hashMap.put("change", Integer.valueOf(this.c.ordinal()));
        if (this.c == a.CONN_TYPE_GAINED || this.c == a.CONN_TYPE_LOST) {
            hashMap.put(Keys.TYPE, this.d);
        } else if (this.c == a.CONN_TYPE_SWITCHED) {
            hashMap.put("oldType", this.e);
            hashMap.put("newType", this.f);
        }
        return new JSONArray().put(this.b).put(4).put(new JSONObject(hashMap));
    }
}
