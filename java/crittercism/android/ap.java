package crittercism.android;

import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

public final class ap extends af {
    private int d = -1;

    public ap(al alVar) {
        super(alVar);
    }

    public final af b() {
        return new ao(this, this.d);
    }

    public final af c() {
        return as.d;
    }

    public final boolean a(CharArrayBuffer charArrayBuffer) {
        try {
            StatusLine parseStatusLine = BasicLineParser.DEFAULT.parseStatusLine(charArrayBuffer, new ParserCursor(0, charArrayBuffer.length()));
            this.d = parseStatusLine.getStatusCode();
            this.a.a(parseStatusLine.getStatusCode());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    protected final int d() {
        return 20;
    }

    protected final int e() {
        return 64;
    }
}
