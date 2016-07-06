package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

public final class q extends m {
    private static final String[] f = new String[]{"libcore.net.http.HttpsURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnection"};

    public q(e eVar, d dVar) {
        super(eVar, dVar, f);
    }

    protected final URLConnection openConnection(URL u) {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.openConnection(u);
        try {
            return new s(httpsURLConnection, this.c, this.d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return httpsURLConnection;
        }
    }

    protected final URLConnection openConnection(URL u, Proxy proxy) {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.openConnection(u, proxy);
        try {
            return new s(httpsURLConnection, this.c, this.d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return httpsURLConnection;
        }
    }

    protected final int getDefaultPort() {
        return 443;
    }

    protected final String a() {
        return Scheme.HTTPS;
    }
}
