package crittercism.android;

import java.io.OutputStream;
import org.json.JSONArray;

public final class cf extends bp {
    public static final cf a = new cf("session_start", a.NORMAL);
    private String b;
    private String c;
    private String d;
    private a e;

    public enum a {
        NORMAL,
        URGENT
    }

    public cf(String str, a aVar) {
        this(str, ed.a.a(), aVar);
    }

    private cf(String str, String str2, a aVar) {
        this.d = cg.a.a();
        if (str.length() > 140) {
            str = str.substring(0, 140);
        }
        this.b = str;
        this.c = str2;
        this.e = aVar;
    }

    public final void a(OutputStream outputStream) {
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(this.b);
        jSONArray.put(this.c);
        String jSONArray2 = jSONArray.toString();
        new StringBuilder("BREADCRUMB WRITING ").append(jSONArray2);
        dx.b();
        outputStream.write(jSONArray2.getBytes());
    }

    public final String e() {
        return this.d;
    }
}
