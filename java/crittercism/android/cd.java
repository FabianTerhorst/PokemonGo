package crittercism.android;

import crittercism.android.bx.aa;
import crittercism.android.bx.b;
import crittercism.android.bx.c;
import crittercism.android.bx.d;
import crittercism.android.bx.f;
import crittercism.android.bx.h;
import crittercism.android.bx.j;
import crittercism.android.bx.k;
import crittercism.android.bx.m;
import crittercism.android.bx.o;
import crittercism.android.bx.p;
import crittercism.android.bx.r;
import crittercism.android.bx.s;
import crittercism.android.bx.w;
import crittercism.android.bx.x;
import crittercism.android.bx.z;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class cd implements ch {
    private JSONObject a;
    private JSONObject b;
    private JSONArray c;
    private JSONArray d;
    private File e;
    private String f = cg.a.a();

    public cd(File file, bs bsVar, bs bsVar2, bs bsVar3, bs bsVar4) {
        file.exists();
        this.e = file;
        this.a = new bu().a(new c()).a(new b()).a(new d()).a(new f()).a(new o()).a(new p()).a(new j()).a(new h()).a(new z()).a(new aa()).a(new k()).a(new r()).a(new m()).a(new s()).a(new w()).a(new x()).a();
        Map hashMap = new HashMap();
        hashMap.put("crashed_session", new bo(bsVar).a);
        if (bsVar2.b() > 0) {
            hashMap.put("previous_session", new bo(bsVar2).a);
        }
        this.b = new JSONObject(hashMap);
        this.c = new bo(bsVar3).a;
        this.d = new bo(bsVar4).a;
    }

    public final void a(OutputStream outputStream) {
        Map hashMap = new HashMap();
        hashMap.put("app_state", this.a);
        hashMap.put("breadcrumbs", this.b);
        hashMap.put("endpoints", this.c);
        hashMap.put("systemBreadcrumbs", this.d);
        Object obj = new byte[0];
        Object obj2 = new byte[8192];
        InputStream fileInputStream = new FileInputStream(this.e);
        while (true) {
            int read = fileInputStream.read(obj2);
            if (read != -1) {
                Object obj3 = new byte[(obj.length + read)];
                System.arraycopy(obj, 0, obj3, 0, obj.length);
                System.arraycopy(obj2, 0, obj3, obj.length, read);
                obj = obj3;
            } else {
                fileInputStream.close();
                Map hashMap2 = new HashMap();
                hashMap2.put("dmp_name", this.e.getName());
                hashMap2.put("dmp_file", cr.a(obj));
                hashMap.put("ndk_dmp_info", new JSONObject(hashMap2));
                outputStream.write(new JSONObject(hashMap).toString().getBytes());
                return;
            }
        }
    }

    public final String e() {
        return this.f;
    }
}
