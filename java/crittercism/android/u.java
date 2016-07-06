package crittercism.android;

import java.io.OutputStream;

public final class u extends OutputStream {
    private final OutputStream a;
    private final c b;

    public u(OutputStream outputStream, c cVar) {
        if (outputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (cVar == null) {
            throw new NullPointerException("stats were null");
        } else {
            this.a = outputStream;
            this.b = cVar;
        }
    }

    public final void flush() {
        this.a.flush();
    }

    public final void close() {
        this.a.close();
    }

    public final void write(int oneByte) {
        try {
            if (this.b != null) {
                this.b.b();
                this.b.c(1);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
        this.a.write(oneByte);
    }

    public final void write(byte[] buffer) {
        if (this.b != null) {
            this.b.b();
            if (buffer != null) {
                this.b.c((long) buffer.length);
            }
        }
        this.a.write(buffer);
    }

    public final void write(byte[] buffer, int offset, int byteCount) {
        if (this.b != null) {
            this.b.b();
            if (buffer != null) {
                this.b.c((long) byteCount);
            }
        }
        this.a.write(buffer, offset, byteCount);
    }
}
