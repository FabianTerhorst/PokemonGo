package crittercism.android;

import java.net.MalformedURLException;
import java.net.URL;

public final class db {
    private String a;
    private String b;

    public db(String str, String str2) {
        str.endsWith("/");
        str2.startsWith("/");
        this.a = str;
        this.b = str2;
    }

    public final URL a() {
        try {
            return new URL(this.a + this.b);
        } catch (MalformedURLException e) {
            new StringBuilder("Invalid url: ").append(this.a).append(this.b);
            URL url = null;
            dx.b();
            return url;
        }
    }
}
