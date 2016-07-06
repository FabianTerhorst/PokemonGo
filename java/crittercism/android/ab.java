package crittercism.android;

import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public final class ab extends SSLSocketFactory {
    private SSLSocketFactory a;
    private e b;
    private d c;

    public ab(SSLSocketFactory sSLSocketFactory, e eVar, d dVar) {
        this.a = sSLSocketFactory;
        this.b = eVar;
        this.c = dVar;
    }

    public final SSLSocketFactory a() {
        return this.a;
    }

    public final String[] getDefaultCipherSuites() {
        return this.a.getDefaultCipherSuites();
    }

    public final String[] getSupportedCipherSuites() {
        return this.a.getSupportedCipherSuites();
    }

    private Socket a(Socket socket) {
        if (socket == null) {
            return socket;
        }
        try {
            if (socket instanceof SSLSocket) {
                return new aa((SSLSocket) socket, this.b, this.c);
            }
            return socket;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return socket;
        }
    }

    public final Socket createSocket(Socket s, String host, int port, boolean autoClose) {
        return a(this.a.createSocket(s, host, port, autoClose));
    }

    public final Socket createSocket(String host, int port) {
        return a(this.a.createSocket(host, port));
    }

    public final Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        return a(this.a.createSocket(host, port, localHost, localPort));
    }

    public final Socket createSocket(InetAddress host, int port) {
        return a(this.a.createSocket(host, port));
    }

    public final Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        return a(this.a.createSocket(address, port, localAddress, localPort));
    }

    public final Socket createSocket() {
        return a(this.a.createSocket());
    }
}
