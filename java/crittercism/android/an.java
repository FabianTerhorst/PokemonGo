package crittercism.android;

import org.apache.http.ParseException;
import org.apache.http.RequestLine;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

public final class an extends af {
    public an(al alVar) {
        super(alVar);
    }

    public final af b() {
        return new am(this);
    }

    public final af c() {
        return as.d;
    }

    public final boolean a(CharArrayBuffer charArrayBuffer) {
        try {
            RequestLine parseRequestLine = BasicLineParser.DEFAULT.parseRequestLine(charArrayBuffer, new ParserCursor(0, charArrayBuffer.length()));
            this.a.a(parseRequestLine.getMethod(), parseRequestLine.getUri());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    protected final int d() {
        return 64;
    }

    protected final int e() {
        return 2048;
    }
}
