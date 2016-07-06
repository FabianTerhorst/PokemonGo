package crittercism.android;

import crittercism.android.c.a;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public final class aa extends SSLSocket implements ae {
    private SSLSocket a;
    private e b;
    private d c;
    private final Queue d = new LinkedList();
    private w e;
    private x f;

    public aa(SSLSocket sSLSocket, e eVar, d dVar) {
        if (sSLSocket == null) {
            throw new NullPointerException("delegate was null");
        } else if (eVar == null) {
            throw new NullPointerException("dispatch was null");
        } else {
            this.a = sSLSocket;
            this.b = eVar;
            this.c = dVar;
        }
    }

    public final void addHandshakeCompletedListener(HandshakeCompletedListener listener) {
        this.a.addHandshakeCompletedListener(listener);
    }

    public final boolean getEnableSessionCreation() {
        return this.a.getEnableSessionCreation();
    }

    public final String[] getEnabledCipherSuites() {
        return this.a.getEnabledCipherSuites();
    }

    public final String[] getEnabledProtocols() {
        return this.a.getEnabledProtocols();
    }

    public final boolean getNeedClientAuth() {
        return this.a.getNeedClientAuth();
    }

    public final SSLSession getSession() {
        return this.a.getSession();
    }

    public final String[] getSupportedCipherSuites() {
        return this.a.getSupportedCipherSuites();
    }

    public final String[] getSupportedProtocols() {
        return this.a.getSupportedProtocols();
    }

    public final boolean getUseClientMode() {
        return this.a.getUseClientMode();
    }

    public final boolean getWantClientAuth() {
        return this.a.getWantClientAuth();
    }

    public final void removeHandshakeCompletedListener(HandshakeCompletedListener listener) {
        this.a.removeHandshakeCompletedListener(listener);
    }

    public final void setEnableSessionCreation(boolean flag) {
        this.a.setEnableSessionCreation(flag);
    }

    public final void setEnabledCipherSuites(String[] suites) {
        this.a.setEnabledCipherSuites(suites);
    }

    public final void setEnabledProtocols(String[] protocols) {
        this.a.setEnabledProtocols(protocols);
    }

    public final void setNeedClientAuth(boolean need) {
        this.a.setNeedClientAuth(need);
    }

    public final void setUseClientMode(boolean mode) {
        this.a.setUseClientMode(mode);
    }

    public final void setWantClientAuth(boolean want) {
        this.a.setWantClientAuth(want);
    }

    public final void startHandshake() {
        try {
            this.a.startHandshake();
        } catch (Throwable e) {
            try {
                c a = a(true);
                a.b();
                a.c();
                a.f();
                a.a(e);
                this.b.a(a, a.SSL_SOCKET_START_HANDSHAKE);
            } catch (ThreadDeath e2) {
                throw e2;
            } catch (Throwable th) {
                dx.a(th);
            }
            throw e;
        }
    }

    public final void bind(SocketAddress localAddr) {
        this.a.bind(localAddr);
    }

    public final void close() {
        this.a.close();
        try {
            if (this.f != null) {
                this.f.d();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public final void connect(SocketAddress remoteAddr, int timeout) {
        this.a.connect(remoteAddr, timeout);
    }

    public final void connect(SocketAddress remoteAddr) {
        this.a.connect(remoteAddr);
    }

    public final SocketChannel getChannel() {
        return this.a.getChannel();
    }

    public final InetAddress getInetAddress() {
        return this.a.getInetAddress();
    }

    public final InputStream getInputStream() {
        InputStream inputStream = this.a.getInputStream();
        if (inputStream == null) {
            return inputStream;
        }
        try {
            if (this.f != null && this.f.a(inputStream)) {
                return this.f;
            }
            this.f = new x(this, inputStream, this.b);
            return this.f;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return inputStream;
        }
    }

    public final boolean getKeepAlive() {
        return this.a.getKeepAlive();
    }

    public final InetAddress getLocalAddress() {
        return this.a.getLocalAddress();
    }

    public final int getLocalPort() {
        return this.a.getLocalPort();
    }

    public final SocketAddress getLocalSocketAddress() {
        return this.a.getLocalSocketAddress();
    }

    public final boolean getOOBInline() {
        return this.a.getOOBInline();
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = this.a.getOutputStream();
        if (outputStream == null) {
            return outputStream;
        }
        try {
            if (this.e != null && this.e.a(outputStream)) {
                return this.e;
            }
            w wVar = this.e;
            this.e = new w(this, outputStream);
            return this.e;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
            return outputStream;
        }
    }

    public final int getPort() {
        return this.a.getPort();
    }

    public final int getReceiveBufferSize() {
        return this.a.getReceiveBufferSize();
    }

    public final SocketAddress getRemoteSocketAddress() {
        return this.a.getRemoteSocketAddress();
    }

    public final boolean getReuseAddress() {
        return this.a.getReuseAddress();
    }

    public final int getSendBufferSize() {
        return this.a.getSendBufferSize();
    }

    public final int getSoLinger() {
        return this.a.getSoLinger();
    }

    public final int getSoTimeout() {
        return this.a.getSoTimeout();
    }

    public final boolean getTcpNoDelay() {
        return this.a.getTcpNoDelay();
    }

    public final int getTrafficClass() {
        return this.a.getTrafficClass();
    }

    public final boolean isBound() {
        return this.a.isBound();
    }

    public final boolean isClosed() {
        return this.a.isClosed();
    }

    public final boolean isConnected() {
        return this.a.isConnected();
    }

    public final boolean isInputShutdown() {
        return this.a.isInputShutdown();
    }

    public final boolean isOutputShutdown() {
        return this.a.isOutputShutdown();
    }

    public final void sendUrgentData(int value) {
        this.a.sendUrgentData(value);
    }

    public final void setKeepAlive(boolean keepAlive) {
        this.a.setKeepAlive(keepAlive);
    }

    public final void setOOBInline(boolean oobinline) {
        this.a.setOOBInline(oobinline);
    }

    public final void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        this.a.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    public final void setReceiveBufferSize(int size) {
        this.a.setReceiveBufferSize(size);
    }

    public final void setReuseAddress(boolean reuse) {
        this.a.setReuseAddress(reuse);
    }

    public final void setSendBufferSize(int size) {
        this.a.setSendBufferSize(size);
    }

    public final void setSoLinger(boolean on, int timeout) {
        this.a.setSoLinger(on, timeout);
    }

    public final void setSoTimeout(int timeout) {
        this.a.setSoTimeout(timeout);
    }

    public final void setTcpNoDelay(boolean on) {
        this.a.setTcpNoDelay(on);
    }

    public final void setTrafficClass(int value) {
        this.a.setTrafficClass(value);
    }

    public final void shutdownInput() {
        this.a.shutdownInput();
    }

    public final void shutdownOutput() {
        this.a.shutdownOutput();
    }

    public final String toString() {
        return this.a.toString();
    }

    public final boolean equals(Object o) {
        return this.a.equals(o);
    }

    public final int hashCode() {
        return this.a.hashCode();
    }

    public final c a() {
        return a(false);
    }

    private c a(boolean z) {
        c cVar = new c();
        InetAddress inetAddress = this.a.getInetAddress();
        if (inetAddress != null) {
            cVar.a(inetAddress);
        }
        if (z) {
            cVar.a(getPort());
        }
        cVar.a(k.a.HTTPS);
        if (this.c != null) {
            cVar.j = this.c.a();
        }
        if (bc.b()) {
            cVar.a(bc.a());
        }
        return cVar;
    }

    public final void a(c cVar) {
        if (cVar != null) {
            synchronized (this.d) {
                this.d.add(cVar);
            }
        }
    }

    public final c b() {
        c cVar;
        synchronized (this.d) {
            cVar = (c) this.d.poll();
        }
        return cVar;
    }
}
