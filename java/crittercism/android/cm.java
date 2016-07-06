package crittercism.android;

import java.util.HashMap;

public enum cm {
    OK(0, null),
    ASSERTION_ERROR(1, "java.lang.AssertionError"),
    BIND_EXCEPTION(2, "java.net.BindException"),
    CLASS_NOT_FOUND_EXCEPTION(3, "java.lang.ClassNotFoundException"),
    ERROR(4, "java.lang.Error"),
    IO_EXCEPTION(5, "java.io.IOException"),
    ILLEGAL_ARGUMENT_EXCEPTION(6, "java.lang.IllegalArgumentException"),
    ILLEGAL_STATE_EXCEPTION(7, "java.lang.IllegalStateException"),
    INDEX_OUT_OF_BOUNDS_EXCEPTION(8, "java.lang.IndexOutOfBoundsException"),
    MALFORMED_URL_EXCEPTION(9, "java.net.MalformedURLException"),
    NO_SUCH_PROVIDER_EXCEPTION(10, "java.security.NoSuchProviderException"),
    NULL_POINTER_EXCEPTION(11, "java.lang.NullPointerException"),
    PROTOCOL_EXCEPTION(12, "java.net.ProtocolException"),
    SECURITY_EXCEPTION(13, "java.lang.SecurityException"),
    SOCKET_EXCEPTION(14, "java.net.SocketException"),
    SOCKET_TIMEOUT_EXCEPTION(15, "java.net.SocketTimeoutException"),
    SSL_PEER_UNVERIFIED_EXCEPTION(16, "javax.net.ssl.SSLPeerUnverifiedException"),
    STRING_INDEX_OUT_OF_BOUNDS_EXCEPTION(17, "java.lang.StringIndexOutOfBoundsException"),
    UNKNOWN_HOST_EXCEPTION(18, "java.net.UnknownHostException"),
    UNKNOWN_SERVICE_EXCEPTION(19, "java.net.UnknownServiceException"),
    UNSUPPORTED_OPERATION_EXCEPTION(20, "java.lang.UnsupportedOperationException"),
    URI_SYNTAX_EXCEPTION(21, "java.net.URISyntaxException"),
    CONNECT_EXCEPTION(22, "java.net.ConnectException"),
    SSL_EXCEPTION(23, "javax.net.ssl.SSLException"),
    SSL_HANDSHAKE_EXCEPTION(24, "javax.net.ssl.SSLHandshakeException"),
    SSL_KEY_EXCEPTION(25, "javax.net.ssl.SSLKeyException"),
    SSL_PROTOCOL_EXCEPTION(26, "javax.net.ssl.SSLProtocolException"),
    UNDEFINED_EXCEPTION(-1, "__UNKNOWN__");
    
    private static HashMap C;
    private int D;
    private String E;

    private cm(int i, String str) {
        this.D = i;
        this.E = str;
    }

    public final int a() {
        return this.D;
    }

    private static synchronized void b() {
        synchronized (cm.class) {
            if (C == null) {
                HashMap hashMap = new HashMap();
                for (cm cmVar : values()) {
                    hashMap.put(cmVar.E, cmVar);
                }
                C = hashMap;
            }
        }
    }

    public static cm a(Throwable th) {
        if (C == null) {
            b();
        }
        Object obj = null;
        if (th != null) {
            obj = th.getClass().getName();
        }
        cm cmVar = (cm) C.get(obj);
        if (cmVar == null) {
            return UNDEFINED_EXCEPTION;
        }
        return cmVar;
    }
}
