package crittercism.android;

import android.location.Location;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import org.json.JSONArray;
import spacemadness.com.lunarconsole.BuildConfig;

public final class c extends bp {
    public long a = Long.MAX_VALUE;
    public boolean b = false;
    a c = a.NOT_LOGGED_YET;
    public long d = 0;
    public int e = 0;
    public String f = BuildConfig.FLAVOR;
    public cn g = new cn(null);
    public k h = new k();
    public String i;
    public b j = b.MOBILE;
    private long k = Long.MAX_VALUE;
    private boolean l = false;
    private boolean m = false;
    private String n = cg.a.a();
    private long o = 0;
    private boolean p = false;
    private boolean q = false;
    private double[] r;

    public enum a {
        NOT_LOGGED_YET("Not logged"),
        INPUT_STREAM_READ("InputStream.read()"),
        INPUT_STREAM_CLOSE("InputStream.close()"),
        SOCKET_CLOSE("Socket.close()"),
        LEGACY_JAVANET("Legacy java.net"),
        HTTP_CONTENT_LENGTH_PARSER("parse()"),
        INPUT_STREAM_FINISHED("finishedMessage()"),
        PARSING_INPUT_STREAM_LOG_ERROR("logError()"),
        SOCKET_IMPL_CONNECT("MonitoredSocketImpl.connect()"),
        SSL_SOCKET_START_HANDSHAKE("MonitoredSSLSocket.startHandshake"),
        UNIT_TEST("Unit test"),
        LOG_ENDPOINT("logEndpoint");
        
        private String m;

        private a(String str) {
            this.m = str;
        }

        public final String toString() {
            return this.m;
        }
    }

    public c(String str) {
        if (str != null) {
            this.i = str;
        }
    }

    public c(URL url) {
        if (url != null) {
            this.i = url.toExternalForm();
        }
    }

    public final void a(long j) {
        if (!this.p) {
            this.o += j;
        }
    }

    public final void b(long j) {
        this.p = true;
        this.o = j;
    }

    public final void c(long j) {
        if (!this.q) {
            this.d += j;
        }
    }

    public final void d(long j) {
        this.q = true;
        this.d = j;
    }

    public final String a() {
        boolean z = true;
        String str = this.i;
        if (str == null) {
            k kVar = this.h;
            str = kVar.b != null ? kVar.b : kVar.a != null ? kVar.a.getHostName() : "unknown-host";
            String stringBuilder;
            if (kVar.f) {
                int i = kVar.e;
                if (i > 0) {
                    stringBuilder = new StringBuilder(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR).append(i).toString();
                    if (!str.endsWith(stringBuilder)) {
                        str = str + stringBuilder;
                    }
                }
            } else {
                stringBuilder = kVar.c;
                String str2 = BuildConfig.FLAVOR;
                if (stringBuilder == null || !(stringBuilder.regionMatches(true, 0, "http:", 0, 5) || stringBuilder.regionMatches(true, 0, "https:", 0, 6))) {
                    z = false;
                }
                if (z) {
                    str = stringBuilder;
                } else {
                    String str3 = kVar.d != null ? str2 + kVar.d.c + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR : str2;
                    if (stringBuilder.startsWith("//")) {
                        str = str3 + stringBuilder;
                    } else {
                        String str4 = str3 + "//";
                        if (stringBuilder.startsWith(str)) {
                            str = str4 + stringBuilder;
                        } else {
                            str3 = BuildConfig.FLAVOR;
                            if (kVar.e > 0 && (kVar.d == null || kVar.d.d != kVar.e)) {
                                String stringBuilder2 = new StringBuilder(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR).append(kVar.e).toString();
                                if (!str.endsWith(stringBuilder2)) {
                                    str3 = stringBuilder2;
                                }
                            }
                            str = str4 + str + str3 + stringBuilder;
                        }
                    }
                }
            }
            this.i = str;
        }
        return str;
    }

    public final void a(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.i = str;
    }

    private long g() {
        if (this.a == Long.MAX_VALUE || this.k == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        return this.k - this.a;
    }

    public final void e(long j) {
        this.a = j;
        this.l = true;
    }

    public final void b() {
        if (!this.l && this.a == Long.MAX_VALUE) {
            this.a = System.currentTimeMillis();
        }
    }

    public final void f(long j) {
        this.k = j;
        this.m = true;
    }

    public final void c() {
        if (!this.m && this.k == Long.MAX_VALUE) {
            this.k = System.currentTimeMillis();
        }
    }

    public final void a(Location location) {
        this.r = new double[]{location.getLatitude(), location.getLongitude()};
    }

    public final String toString() {
        String str = (((((((((((((((BuildConfig.FLAVOR + "URI            : " + this.i + "\n") + "URI Builder    : " + this.h.toString() + "\n") + "\n") + "Logged by      : " + this.c.toString() + "\n") + "Error type:         : " + this.g.a + "\n") + "Error code:         : " + this.g.b + "\n") + "\n") + "Response time  : " + g() + "\n") + "Start time     : " + this.a + "\n") + "End time       : " + this.k + "\n") + "\n") + "Bytes out    : " + this.d + "\n") + "Bytes in     : " + this.o + "\n") + "\n") + "Response code  : " + this.e + "\n") + "Request method : " + this.f + "\n";
        if (this.r != null) {
            return str + "Location       : " + Arrays.toString(this.r) + "\n";
        }
        return str;
    }

    public final JSONArray d() {
        JSONArray jSONArray = new JSONArray();
        try {
            jSONArray.put(this.f);
            jSONArray.put(a());
            jSONArray.put(ed.a.a(new Date(this.a)));
            jSONArray.put(g());
            jSONArray.put(this.j.a());
            jSONArray.put(this.o);
            jSONArray.put(this.d);
            jSONArray.put(this.e);
            jSONArray.put(this.g.a);
            jSONArray.put(this.g.b);
            if (this.r == null) {
                return jSONArray;
            }
            JSONArray jSONArray2 = new JSONArray();
            jSONArray2.put(this.r[0]);
            jSONArray2.put(this.r[1]);
            jSONArray.put(jSONArray2);
            return jSONArray;
        } catch (Exception e) {
            Exception exception = e;
            System.out.println("Failed to create statsArray");
            exception.printStackTrace();
            return null;
        }
    }

    public final void a(Throwable th) {
        this.g = new cn(th);
    }

    public final void a(InetAddress inetAddress) {
        this.i = null;
        this.h.a = inetAddress;
    }

    public final void b(String str) {
        this.i = null;
        this.h.b = str;
    }

    public final void a(crittercism.android.k.a aVar) {
        this.h.d = aVar;
    }

    public final void a(int i) {
        k kVar = this.h;
        if (i > 0) {
            kVar.e = i;
        }
    }

    public final void a(OutputStream outputStream) {
        outputStream.write(d().toString().getBytes());
    }

    public final String e() {
        return this.n;
    }

    public final void f() {
        this.h.f = true;
    }
}
