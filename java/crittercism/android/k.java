package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.InetAddress;

public final class k {
    InetAddress a;
    String b;
    public String c = "/";
    a d = null;
    int e = -1;
    boolean f = false;

    public enum a {
        HTTP(Scheme.HTTP, 80),
        HTTPS(Scheme.HTTPS, 443);
        
        private String c;
        private int d;

        private a(String str, int i) {
            this.c = str;
            this.d = i;
        }
    }
}
