package crittercism.android;

import java.io.InputStream;

public final class t extends InputStream {
    private final InputStream a;
    private final e b;
    private final c c;

    public t(InputStream inputStream, e eVar, c cVar) {
        if (inputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (eVar == null) {
            throw new NullPointerException("dispatch was null");
        } else if (cVar == null) {
            throw new NullPointerException("stats were null");
        } else {
            this.a = inputStream;
            this.b = eVar;
            this.c = cVar;
        }
    }

    public final int available() {
        return this.a.available();
    }

    public final void close() {
        this.a.close();
    }

    public final void mark(int readlimit) {
        this.a.mark(readlimit);
    }

    public final boolean markSupported() {
        return this.a.markSupported();
    }

    private void a(int i, int i2) {
        try {
            if (this.c == null) {
                return;
            }
            if (i == -1) {
                this.b.a(this.c);
            } else {
                this.c.a((long) i2);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    private void a(Exception exception) {
        try {
            this.c.a((Throwable) exception);
            this.b.a(this.c);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
    }

    public final int read() {
        try {
            int read = this.a.read();
            a(read, 1);
            return read;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public final int read(byte[] buffer) {
        try {
            int read = this.a.read(buffer);
            a(read, read);
            return read;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public final int read(byte[] buffer, int offset, int length) {
        try {
            int read = this.a.read(buffer, offset, length);
            a(read, read);
            return read;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public final synchronized void reset() {
        this.a.reset();
    }

    public final long skip(long byteCount) {
        long skip = this.a.skip(byteCount);
        try {
            if (this.c != null) {
                this.c.a(skip);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
        return skip;
    }
}
