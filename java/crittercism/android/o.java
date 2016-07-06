package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public final class o extends m {
    private static final String[] f = new String[]{"libcore.net.http.HttpURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnection"};

    public o(e eVar, d dVar) {
        super(eVar, dVar, f);
    }

    protected final URLConnection openConnection(URL u) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) super.openConnection(u);
        try {
            return new r(httpURLConnection, this.c, this.d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return httpURLConnection;
        }
    }

    protected final URLConnection openConnection(URL u, Proxy proxy) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) super.openConnection(u, proxy);
        try {
            return new r(httpURLConnection, this.c, this.d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return httpURLConnection;
        }
    }

    protected final int getDefaultPort() {
        return 80;
    }

    protected final String a() {
        return Scheme.HTTP;
    }
}
