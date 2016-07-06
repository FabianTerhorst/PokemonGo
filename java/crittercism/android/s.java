package crittercism.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.Permission;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public final class s extends HttpsURLConnection {
    private e a = null;
    private HttpsURLConnection b = null;
    private c c = null;
    private d d = null;
    private boolean e = false;
    private boolean f = false;

    public s(HttpsURLConnection httpsURLConnection, e eVar, d dVar) {
        super(httpsURLConnection.getURL());
        this.a = eVar;
        this.b = httpsURLConnection;
        this.d = dVar;
        this.c = new c(httpsURLConnection.getURL());
        SSLSocketFactory sSLSocketFactory = this.b.getSSLSocketFactory();
        if (sSLSocketFactory instanceof ab) {
            this.b.setSSLSocketFactory(((ab) sSLSocketFactory).a());
        }
    }

    private void a() {
        try {
            if (!this.f) {
                this.f = true;
                this.c.f = this.b.getRequestMethod();
                this.c.b();
                this.c.j = this.d.a();
                if (bc.b()) {
                    this.c.a(bc.a());
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private void a(Throwable th) {
        try {
            if (!this.e) {
                this.e = true;
                this.c.c();
                this.c.a(th);
                this.a.a(this.c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            dx.a(th2);
        }
    }

    private void b() {
        Object obj = null;
        try {
            if (!this.e) {
                this.e = true;
                this.c.c();
                if (this.b.getHeaderFields() != null) {
                    p pVar = new p(this.b.getHeaderFields());
                    int b = pVar.b("Content-Length");
                    if (b != -1) {
                        this.c.b((long) b);
                        obj = 1;
                    }
                    long a = pVar.a("X-Android-Sent-Millis");
                    long a2 = pVar.a("X-Android-Received-Millis");
                    if (!(a == Long.MAX_VALUE || a2 == Long.MAX_VALUE)) {
                        this.c.e(a);
                        this.c.f(a2);
                    }
                }
                try {
                    this.c.e = this.b.getResponseCode();
                } catch (IOException e) {
                }
                if (obj != null) {
                    this.a.a(this.c);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public final String getCipherSuite() {
        return this.b.getCipherSuite();
    }

    public final HostnameVerifier getHostnameVerifier() {
        return this.b.getHostnameVerifier();
    }

    public final Certificate[] getLocalCertificates() {
        return this.b.getLocalCertificates();
    }

    public final Principal getLocalPrincipal() {
        return this.b.getLocalPrincipal();
    }

    public final Principal getPeerPrincipal() {
        return this.b.getPeerPrincipal();
    }

    public final SSLSocketFactory getSSLSocketFactory() {
        return this.b.getSSLSocketFactory();
    }

    public final Certificate[] getServerCertificates() {
        return this.b.getServerCertificates();
    }

    public final void setHostnameVerifier(HostnameVerifier v) {
        this.b.setHostnameVerifier(v);
    }

    public final void setSSLSocketFactory(SSLSocketFactory sf) {
        try {
            if (sf instanceof ab) {
                sf = ((ab) sf).a();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
        this.b.setSSLSocketFactory(sf);
    }

    public final void disconnect() {
        this.b.disconnect();
        try {
            if (this.e && !this.c.b) {
                this.a.a(this.c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public final InputStream getErrorStream() {
        a();
        InputStream errorStream = this.b.getErrorStream();
        b();
        if (errorStream != null) {
            try {
                return new t(errorStream, this.a, this.c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.a(th);
            }
        }
        return errorStream;
    }

    public final long getHeaderFieldDate(String field, long defaultValue) {
        a();
        long headerFieldDate = this.b.getHeaderFieldDate(field, defaultValue);
        b();
        return headerFieldDate;
    }

    public final boolean getInstanceFollowRedirects() {
        return this.b.getInstanceFollowRedirects();
    }

    public final Permission getPermission() {
        return this.b.getPermission();
    }

    public final String getRequestMethod() {
        return this.b.getRequestMethod();
    }

    public final int getResponseCode() {
        a();
        try {
            int responseCode = this.b.getResponseCode();
            b();
            return responseCode;
        } catch (Throwable e) {
            a(e);
            throw e;
        }
    }

    public final String getResponseMessage() {
        a();
        try {
            String responseMessage = this.b.getResponseMessage();
            b();
            return responseMessage;
        } catch (Throwable e) {
            a(e);
            throw e;
        }
    }

    public final void setChunkedStreamingMode(int chunkLength) {
        this.b.setChunkedStreamingMode(chunkLength);
    }

    public final void setFixedLengthStreamingMode(int contentLength) {
        this.b.setFixedLengthStreamingMode(contentLength);
    }

    public final void setInstanceFollowRedirects(boolean followRedirects) {
        this.b.setInstanceFollowRedirects(followRedirects);
    }

    public final void setRequestMethod(String method) {
        this.b.setRequestMethod(method);
    }

    public final boolean usingProxy() {
        return this.b.usingProxy();
    }

    public final void addRequestProperty(String field, String newValue) {
        this.b.addRequestProperty(field, newValue);
    }

    public final void connect() {
        this.b.connect();
    }

    public final boolean getAllowUserInteraction() {
        return this.b.getAllowUserInteraction();
    }

    public final int getConnectTimeout() {
        return this.b.getConnectTimeout();
    }

    public final Object getContent() {
        a();
        try {
            Object content = this.b.getContent();
            b();
            return content;
        } catch (Throwable e) {
            a(e);
            throw e;
        }
    }

    public final Object getContent(Class[] types) {
        a();
        try {
            Object content = this.b.getContent(types);
            b();
            return content;
        } catch (Throwable e) {
            a(e);
            throw e;
        }
    }

    public final String getContentEncoding() {
        a();
        String contentEncoding = this.b.getContentEncoding();
        b();
        return contentEncoding;
    }

    public final int getContentLength() {
        return this.b.getContentLength();
    }

    public final String getContentType() {
        a();
        String contentType = this.b.getContentType();
        b();
        return contentType;
    }

    public final long getDate() {
        return this.b.getDate();
    }

    public final boolean getDefaultUseCaches() {
        return this.b.getDefaultUseCaches();
    }

    public final boolean getDoInput() {
        return this.b.getDoInput();
    }

    public final boolean getDoOutput() {
        return this.b.getDoOutput();
    }

    public final long getExpiration() {
        return this.b.getExpiration();
    }

    public final String getHeaderField(int pos) {
        a();
        String headerField = this.b.getHeaderField(pos);
        b();
        return headerField;
    }

    public final String getHeaderField(String key) {
        a();
        String headerField = this.b.getHeaderField(key);
        b();
        return headerField;
    }

    public final int getHeaderFieldInt(String field, int defaultValue) {
        a();
        int headerFieldInt = this.b.getHeaderFieldInt(field, defaultValue);
        b();
        return headerFieldInt;
    }

    public final String getHeaderFieldKey(int posn) {
        a();
        String headerFieldKey = this.b.getHeaderFieldKey(posn);
        b();
        return headerFieldKey;
    }

    public final Map getHeaderFields() {
        a();
        Map headerFields = this.b.getHeaderFields();
        b();
        return headerFields;
    }

    public final long getIfModifiedSince() {
        return this.b.getIfModifiedSince();
    }

    public final InputStream getInputStream() {
        a();
        try {
            InputStream inputStream = this.b.getInputStream();
            b();
            if (inputStream != null) {
                try {
                    return new t(inputStream, this.a, this.c);
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Throwable th) {
                    dx.a(th);
                }
            }
            return inputStream;
        } catch (Throwable th2) {
            a(th2);
            throw th2;
        }
    }

    public final long getLastModified() {
        return this.b.getLastModified();
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = this.b.getOutputStream();
        if (outputStream != null) {
            try {
                return new u(outputStream, this.c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.a(th);
            }
        }
        return outputStream;
    }

    public final int getReadTimeout() {
        return this.b.getReadTimeout();
    }

    public final Map getRequestProperties() {
        return this.b.getRequestProperties();
    }

    public final String getRequestProperty(String field) {
        return this.b.getRequestProperty(field);
    }

    public final URL getURL() {
        return this.b.getURL();
    }

    public final boolean getUseCaches() {
        return this.b.getUseCaches();
    }

    public final void setAllowUserInteraction(boolean newValue) {
        this.b.setAllowUserInteraction(newValue);
    }

    public final void setConnectTimeout(int timeoutMillis) {
        this.b.setConnectTimeout(timeoutMillis);
    }

    public final void setDefaultUseCaches(boolean newValue) {
        this.b.setDefaultUseCaches(newValue);
    }

    public final void setDoInput(boolean newValue) {
        this.b.setDoInput(newValue);
    }

    public final void setDoOutput(boolean newValue) {
        this.b.setDoOutput(newValue);
    }

    public final void setIfModifiedSince(long newValue) {
        this.b.setIfModifiedSince(newValue);
    }

    public final void setReadTimeout(int timeoutMillis) {
        this.b.setReadTimeout(timeoutMillis);
    }

    public final void setRequestProperty(String field, String newValue) {
        this.b.setRequestProperty(field, newValue);
    }

    public final void setUseCaches(boolean newValue) {
        this.b.setUseCaches(newValue);
    }

    public final String toString() {
        return this.b.toString();
    }

    public final boolean equals(Object o) {
        return this.b.equals(o);
    }

    public final int hashCode() {
        return this.b.hashCode();
    }
}
